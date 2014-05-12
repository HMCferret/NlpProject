import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;













import java.util.Comparator;

public class QuotedSpeechAttribution {
	private static final int DIS_THRESH_IN_WORDS = 300; 
	
	private Map<String, Person> nameMap;
	private Annotation annotation;
	private PrintWriter out;
	private boolean verbose;
	private String originalText;
	public Set<String> names;
	
	private boolean useSingleQuoteMark = false;
	private TmpString[] tmpWords;
	private TmpString[] tmpCandidates;
	private TmpString[] tmpQuoteMarks;
	private TmpParagraphNum[] tmpPraragrahNums;
	private TmpString[] tmpExpVerb;
	private TmpString[] tmpCandidatesInQuote;
	
	List<CandidateQuote> candidateQuotes;
	
	private static class TmpString
	{
		int beginOffset;
		int endOffset;
		String txt;
		public TmpString(int b, int e, String t) {
			this.beginOffset = b;
			this.endOffset = e;
			this.txt = t;
		}
		@Override
		public String toString()
		{
			return "(" + beginOffset + "," + endOffset + "): " + txt ;
		}
	}
	
	private static class TmpParagraphNum extends TmpString
	{
		int num;
		TmpParagraphNum(int n, int b, int e) {
			super(b, e, null);
			this.num = n;
		}
	}
	
	private void generateNameMap(Set<String> names)
	{
		nameMap =new HashMap<String, Person>();
		
		String[] namesSorted = names.toArray(new String[names.size()]);
		Arrays.sort(namesSorted, new Comparator<String>(){
			@Override
			public int compare(String arg0, String arg1) {
				return arg0.length() - arg1.length();
			}
		});
		
		for(int i=0; i<namesSorted.length; ++i)
		{
			boolean found = false;
			for(String s : nameMap.keySet())
			{
				if(isSamePerson(namesSorted[i], s))
				{
					nameMap.put(namesSorted[i], nameMap.get(s));
					found = true;
					break;
				}
			}
			if(!found)
				nameMap.put(namesSorted[i], new Person(namesSorted[i]));

		}
		/*
		Iterator<String> it = names.iterator();
		while(it.hasNext())
		{
			String name = it.next();
			nameMap.put(name, new Person(name));
		}*/
	}
	
	private boolean isSamePerson(String name, String cmp) {
		List<Integer> startIndices = new ArrayList<Integer>();
		char last = ' ';
		for(int i=0; i<name.length(); ++i)
		{
			if(name.charAt(i) != ' ' && last == ' ') startIndices.add(i);
			last = name.charAt(i);
		}
		for(int i=0; i<startIndices.size(); ++i)
		{
			int startIndex = startIndices.get(i);
			int endIndex = startIndex + cmp.length();
			if( endIndex > name.length()) break;
			if((endIndex == name.length() || name.charAt(endIndex) == ' ') 
					&& name.substring(startIndex, endIndex).equals(cmp) )
				return true;
		}
		return false;
	}

	/*
	 * Could take long
	 */
	public void preprocessing() throws IOException
	{
		if(verbose) Util.debug("Annotation starts");
	    StandfordWrapper.annotate(annotation, out);
	    if(verbose) Util.debug("Annotation ends");
	    
		names = getNames(annotation);
	    Util.debug(names);
	    this.out.println(names);
	    generateNameMap(names);
	    Util.debug(nameMap);
	    this.out.println(nameMap);
	    
	   
		if(verbose) Util.debug("initTempArrays start");
		initAllTempArrays();
		if(verbose) Util.debug("initTempArrays end");
	}
	
	private void checkIfSingleQuoteMarkIsUsed(List<CoreMap> sentences)
	{
		int singleCnt = 0;
		int doubleCnt = 0;
		for(int i=0; i<sentences.size(); ++i)
		{
		      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);
		      for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) 
		      {
		    	  ArrayCoreMap aToken = (ArrayCoreMap) token;
		    	  String txt = aToken.get(CoreAnnotations.OriginalTextAnnotation.class);
		    	  if(txt.startsWith("\'"))
		    	  {
		    		  ++singleCnt;
		    	  }else if(txt.startsWith("\""))
		    	  {
		    		  ++doubleCnt;
		    	  }
		      }
		}
		if(singleCnt > doubleCnt) this.useSingleQuoteMark = true;
	}
	private void initAllTempArrays()
	{
		List<TmpString> tmpWords = new ArrayList<TmpString>();
		List<TmpString> tmpCandidates = new ArrayList<TmpString>();
		List<TmpParagraphNum> tmpPnums = new ArrayList<TmpParagraphNum>();
		List<TmpString> tmpQuoteMarks = new ArrayList<TmpString>();
		List<TmpString> tmpExpVerbs = new ArrayList<QuotedSpeechAttribution.TmpString>();
		List<TmpString> tmpCandidatesInQuote = new ArrayList<TmpString>();
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		checkIfSingleQuoteMarkIsUsed(sentences);
		int paragraphNum = 0, prevParapraphOffset = 0;
		for(int i=0; i<sentences.size(); ++i)
		 {
		      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);
		      int prevPNum = paragraphNum;
		      paragraphNum = this.updateParagraphNum(paragraphNum, sentence);		
			  if(paragraphNum != prevPNum) 
			  {
				  //int beginOffsetS = sentence.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
				  int endOffsetS = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
				  tmpPnums.add(new TmpParagraphNum(paragraphNum, prevParapraphOffset, endOffsetS));
				  prevParapraphOffset = endOffsetS;
			  }
			  String tmpPerson = null;
			  int tmpPersonBegin = -1;
			  int tmpPersonEnd = -1;
			  int totalQuoteMarks = 0;
			  int firstQuoteMarkOffset = -1;
		      for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) 
				{
					ArrayCoreMap aToken = (ArrayCoreMap) token;
					String txt = aToken.get(CoreAnnotations.OriginalTextAnnotation.class);
					int beginOffset = aToken.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
					int endOffset = aToken.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
					tmpWords.add(new TmpString(beginOffset, endOffset, txt));
					if(isQuoteMark(txt))
					{
						if(firstQuoteMarkOffset == -1) firstQuoteMarkOffset = beginOffset;
						++totalQuoteMarks;
						tmpQuoteMarks.add(new TmpString(beginOffset, endOffset, txt));
					}
				}
		    int quoteMarkCnt = 0;
		    int[] adjust = this.adjustQuoteIndexs(totalQuoteMarks, sentence, firstQuoteMarkOffset);
		    if(adjust[0] == -1) ++ quoteMarkCnt;
		    
		    
		    List tokens = sentence
					.get(CoreAnnotations.TokensAnnotation.class);
			for (int j=0; j<tokens.size(); ++j) 
			{
			
				ArrayCoreMap aToken = (ArrayCoreMap) tokens.get(j);
				String ne = aToken
						.get(CoreAnnotations.NamedEntityTagAnnotation.class);
				String txt = aToken
						.get(CoreAnnotations.OriginalTextAnnotation.class);
				int beginOffset = aToken
						.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
				int endOffset = aToken
						.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
				String lemma = aToken.get(CoreAnnotations.LemmaAnnotation.class);
				
				if(isQuoteMark(txt)) ++quoteMarkCnt;
				if (this.isVerbalExpression(lemma) && quoteMarkCnt % 2 == 0)
					tmpExpVerbs.add(new TmpString(beginOffset, endOffset, txt));
				if (ne.equals("PERSON")) {
					if (tmpPerson == null) {
						tmpPerson = txt;
						tmpPersonBegin = beginOffset;
						tmpPersonEnd = endOffset;
					} else {
						tmpPerson = tmpPerson + " " + txt;
						tmpPersonEnd = endOffset;
					}
				} else {
					if (tmpPerson != null) {
						if(quoteMarkCnt%2 == 1) 
							tmpCandidatesInQuote.add(new TmpString(tmpPersonBegin,
								tmpPersonEnd, tmpPerson));
						else
							tmpCandidates.add(new TmpString(tmpPersonBegin,
								tmpPersonEnd, tmpPerson));
						tmpPerson = null;
					}else if(this.isPerson(txt))
					{
						if(quoteMarkCnt%2 == 1) 
							tmpCandidatesInQuote.add(new TmpString(beginOffset,
								endOffset, txt));
						else
							tmpCandidates.add(new TmpString(beginOffset,
									endOffset, txt));
					}
				}
			}
		  }
		
		System.out.println("para #: " + tmpPnums.size());
		for(int i=0; i<5 && i < tmpPnums.size(); ++i)
		{
			TmpParagraphNum n = tmpPnums.get(i);
			System.out.println(n.num + "(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		System.out.println("tmpWords #: " + tmpWords.size());
		for(int i=0; i<5 && i< tmpWords.size(); ++i)
		{
			TmpString n = tmpWords.get(i);
			System.out.println("word(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		System.out.println("tmpCandiates #: " + tmpCandidates.size());
		
		for(int i=0; i<5 && i < tmpCandidates.size(); ++i)
		{
			TmpString n = tmpCandidates.get(i);
			System.out.println("Candiates(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		
		System.out.println("tmpCandidatesInQuote #: " + tmpCandidatesInQuote.size());
		
		for(int i=0; i<5 && i < tmpCandidatesInQuote.size(); ++i)
		{
			TmpString n = tmpCandidatesInQuote.get(i);
			System.out.println("CandiatesInQuote(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		
		System.out.println("tmpQuoteMarks #: " + tmpQuoteMarks.size());
		/*for(int i=0; i<5; ++i)
		{
			TmpString n = tmpQuoteMarks.get(i);
			System.out.println("qmark(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}*/
		
		System.out.println("tmpExpVerbs #: " + tmpExpVerbs.size());
		for(int i=0; i<5 && i < tmpExpVerbs.size(); ++i)
		{
			TmpString n = tmpExpVerbs.get(i);
			System.out.println("expVerb(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}		
		
		this.tmpWords = tmpWords.toArray(new TmpString[tmpWords.size()]);
		this.tmpExpVerb = tmpExpVerbs.toArray(new TmpString[tmpExpVerbs.size()]);
		this.tmpPraragrahNums = tmpPnums.toArray(new TmpParagraphNum[tmpPnums.size()]);
		this.tmpQuoteMarks = tmpQuoteMarks.toArray(new TmpString[tmpQuoteMarks.size()]);
		this.tmpCandidates = tmpCandidates.toArray(new TmpString[tmpCandidates.size()]);
		this.tmpCandidatesInQuote = tmpCandidatesInQuote.toArray(new TmpString[tmpCandidatesInQuote.size()]);
	}
	
	public QuotedSpeechAttribution(Annotation annotation, PrintWriter out, boolean verbose) throws IOException
	{
		this.annotation = annotation;
		this.originalText = annotation.get(CoreAnnotations.TextAnnotation.class);
		
		this.out = out;
		this.nameMap = null;
		this.verbose = verbose;
		 String path = new File("dict").getAbsolutePath();
			URL url = new URL("file", null, path);
			dict = new Dictionary(url);
			dict.open();
	}
	
	private int updateParagraphNum(int current, ArrayCoreMap sentence)
	{
		int e = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
		while(e < this.originalText.length() && Character.isSpaceChar(this.originalText.charAt(e)))
		{
			++e;
		}

		if(e >= this.originalText.length() || this.originalText.charAt(e) == '\n') return current + 1;
		return current;
	}
	
	public List<CandidateQuote> getCandidateQuotePairsForJ48()
	{
		if(this.candidateQuotes == null) this.getAllAttributedQuotes();
		return this.candidateQuotes;
	}
	
	public Vector<Quote> getAllAttributedQuotes() 
	{
		if(verbose) Util.debug("in getAllAttributedQuotes");
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		int paragraphNum = 0;
		Vector<Quote> ret = new Vector<Quote>();
		for(int i=0; i<sentences.size(); ++i)
		 {
				//System.out.println("sentence#" + i);
		      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);
		      
		      paragraphNum = this.updateParagraphNum(paragraphNum, sentence);
		      processSentence(paragraphNum, sentence, ret);
		      
		  }
		//int cqCnt = 0;
		if(this.candidateQuotes == null) return ret;
		out.println("****************************");
		int cntUniqueQuote = 0, prevOffset = -1, cntTotal=0;
		for(CandidateQuote cq:this.candidateQuotes)
		{
			++cntTotal;
			if(cq.quoteBeginOffset != prevOffset) ++cntUniqueQuote;
			out.println("CandidateQuote(" + cq.candidate.name + "," + this.originalText.substring(cq.quoteBeginOffset, cq.quoteEndOffset) );
			//out.println("Distance=" + cq.distance + ",Oridinal=" + cq.ordinal);
			out.println(cq);
			prevOffset = cq.quoteBeginOffset;
		}
		System.out.println("cntUniqueQuote:" + cntUniqueQuote + ", cntTotal=" + cntTotal);
		getQuotesFromCandidateQuoteUsingJ48(ret);
		
		Set<Integer> quoteBOffsets = new HashSet<Integer>();
		Vector<Quote> uniqueRet = new Vector<Quote>();
		for(Quote q :ret)
		{
			if(!quoteBOffsets.contains(q.characterBeginOffset))
			{
				quoteBOffsets.add(q.characterBeginOffset);
				uniqueRet.add(q);
			}
		}
		
		Collections.sort(uniqueRet, new Comparator<Quote>(){
			@Override
			public int compare(Quote arg0, Quote arg1) {
				return arg0.characterBeginOffset - arg1.characterBeginOffset;
			}
			
		});
		return uniqueRet;
	}

	private void getQuotesFromCandidateQuoteUsingJ48(Vector<Quote> ret) {
		if(this.candidateQuotes != null)
		{
		for(CandidateQuote cq :this.candidateQuotes)
		{
			if(cq.ordinal == 1)
			{
				Quote q = new Quote(cq.candidate, this.originalText.substring(cq.quoteBeginOffset, cq.quoteEndOffset), null,
						cq.quoteBeginOffset, cq.quoteEndOffset);
				ret.add(q);
			}
		}
		}
	}

	private boolean isSpace(int offset)
	{
		if(offset < 0 || offset >=this.originalText.length()) return true;
		return Character.isSpaceChar(this.originalText.charAt(offset)) || Character.isWhitespace(this.originalText.charAt(offset));
	}
	
	private boolean isQuoteMark(char c)
	{
		if(this.useSingleQuoteMark) return c == '\'';
		else return c == '\"';
	}
	private boolean isQuoteMark(String s)
	{
		return s != null && s.length() == 1 && isQuoteMark(s.charAt(0));
	}
	private int[] adjustQuoteIndexs(int totalQuoteMark, CoreMap sentence, int quoteOffset)
	{
		int beginOffsetSen = sentence.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
		int endOffsetSen = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
		
		int eQuote = 0, bQuote = 0;
		if(totalQuoteMark % 2 == 0)
		{
			int bQuoteOffset = quoteOffset;
			if(this.isSpace(bQuoteOffset+1) && !this.isSpace(bQuoteOffset-1))
				{
				--bQuote;
				++eQuote;
				}
		}
		else{
			if(isQuoteMark(this.originalText.charAt(beginOffsetSen))) ++eQuote;
			else if(isQuoteMark(this.originalText.charAt(endOffsetSen-1))) --bQuote;
			else
			{
				int bQuoteOffset = quoteOffset;
				if(this.isSpace(bQuoteOffset-1) && !this.isSpace(bQuoteOffset+1)) ++eQuote;
				else if(this.isSpace(bQuoteOffset+1) && !this.isSpace(bQuoteOffset-1)) --bQuote;
				else{
					System.out.println("***************WeirdQuoteMarks************:"
							+ sentence
									.get(CoreAnnotations.TextAnnotation.class));
					boolean hasCommuncationWordAfterQuote = false;
					boolean hasSayBeforeQuote = false;
					boolean hasSayAfterQuote = false;
					for (CoreMap token : sentence
							.get(CoreAnnotations.TokensAnnotation.class)) {
						ArrayCoreMap aToken = (ArrayCoreMap) token;
						int bOffset = aToken
								.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
						String lemma = aToken
								.get(CoreAnnotations.LemmaAnnotation.class);
						if (lemma.equals("say")) {
							if (bOffset > quoteOffset)
								hasSayAfterQuote = true;
							else
								hasSayBeforeQuote = true;
						}
						if (this.isCommunicationWord(lemma)) {
							if (bOffset > quoteOffset)
								hasCommuncationWordAfterQuote = true;
						}
					}
					if (!hasCommuncationWordAfterQuote
							|| (hasSayBeforeQuote && !hasSayAfterQuote))
						++eQuote;
					else
						--bQuote;
				}
			}
		}
		return new int[] {bQuote, eQuote};
	}
	private void processSentence(int paragraphNum, ArrayCoreMap sentence, Vector<Quote> ret) 
	{		
		/*if(sentence.get(CoreAnnotations.TextAnnotation.class).contains("he stole it from"))
			System.out.println("SDFSD((((((((())))))))))");*/
		//Util.debug(sentence.get(CoreAnnotations.TextAnnotation.class));
		int beginOffsetSen = sentence.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
		int endOffsetSen = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
		int bQuote = this.binarySearchAfter(beginOffsetSen, this.tmpQuoteMarks);
		int eQuote = this.binarySearchBefore(endOffsetSen, this.tmpQuoteMarks) + 1;
		if(eQuote <= bQuote) return;
		int totalQuoteMark = eQuote - bQuote;
		if(totalQuoteMark == 0 || totalQuoteMark > 4) return;
		//because sentence split are not perfect, we get odd number of quote marks in a sentence often
		int []adjust = this.adjustQuoteIndexs(totalQuoteMark, sentence, this.tmpQuoteMarks[bQuote].beginOffset);
		bQuote += adjust[0];
		eQuote += adjust[1];
		if(bQuote < 0 || eQuote > this.tmpQuoteMarks.length) return;
		Set<Person> speakers = new TreeSet<Person>(new Comparator<Person>(){
			public int compare(Person a, Person b) {
				return a.Id - b.Id;
			}
		});
		int pQuote = bQuote;
		int bOffset = beginOffsetSen;
		String expVerb = null;
		boolean nonNECandidateExists = false;
		while(pQuote <= eQuote)
		{
			int eOffset = pQuote == eQuote ? endOffsetSen: this.tmpQuoteMarks[pQuote].beginOffset;
			if(bOffset < eOffset)
			{
				int bVerb = this.binarySearchAfter(bOffset, this.tmpExpVerb);
				int eVerb = this.binarySearchBefore(eOffset, this.tmpExpVerb) + 1;
				if(bVerb < eVerb) expVerb = this.tmpExpVerb[bVerb].txt;
				
				int bCandidate = this.binarySearchAfter(bOffset, this.tmpCandidates);
				int eCandidate = this.binarySearchBefore(eOffset, this.tmpCandidates) + 1;
				for(int pCandidate = bCandidate; pCandidate < eCandidate; ++ pCandidate)
				{
					String name = this.tmpCandidates[pCandidate].txt;
					Person person = this.nameMap.get(name);
					if(person != null)
						speakers.add(this.nameMap.get(name));
					//else
						//nonNECandidateExists = true;
				}
			}
			if(pQuote == eQuote) break;
			bOffset = this.tmpQuoteMarks[pQuote + 1].beginOffset + 1;
			pQuote += 2;
		}
		if(expVerb != null && speakers.size() == 1 && !nonNECandidateExists)
		{
			pQuote = bQuote;
			while(pQuote < eQuote)
			{
				int bQuoteOffset = this.tmpQuoteMarks[pQuote].beginOffset;
				int eQuoteOffset = this.tmpQuoteMarks[pQuote+1].endOffset;
				ret.add(new Quote(speakers.iterator().next(), this.originalText.substring(bQuoteOffset, eQuoteOffset), expVerb, bQuoteOffset, eQuoteOffset));
				pQuote += 2;
			}
		}else
		{
			pQuote = bQuote;
			while(pQuote < eQuote)
			{
				extractCandidateQuoteForOneQuote(paragraphNum, sentence, pQuote);
				pQuote += 2;
			}
		}
	}
	private void calcuteFeature_typeOfWordBetween(CandidateQuote cq)
	{
		//TBD; use binary search and TmpString, don't scan word by word;
		/*
		int bWord = this.binarySearchAfter(cq.quoteBeginOffset, this.tmpWords);
		if(cq.distance > 0) bWord = this.binarySearchBefore(cq.quoteEndOffset, this.tmpWords);
		int step = cq.distance / Math.abs(cq.distance);
		for(int i=step; Math.abs(i)< Math.abs(cq.distance); i+=step)
		{
			TmpString word = this.tmpWords[bWord+i];
			String txt = word.txt;
			char startChar = txt.charAt(0);
			if(startChar == ',') cq.commaPresence = true;
			else if(startChar == '.') cq.periodPresence = true; 
			else if(!Character.isLetter(startChar)) cq.punctuationPresence = true; 
		
			//if(this.isVerbalExpression(txt)) cq.expVerbPresence = true;
			int bPerson = this.binarySearchAfter(word.beginOffset,tmpCandidates);
			if(bPerson <tmpCandidates.length && tmpCandidates[bPerson].beginOffset == word.beginOffset) 
				cq.personPresence = true;
			
		}*/
		
	}
	private void extractCandidateQuoteForOneQuote(int paragraphNum, ArrayCoreMap sentence, int bQuote)
	{
		if(this.candidateQuotes == null) this.candidateQuotes = new ArrayList<CandidateQuote>();
		Map<Person, Integer> candidateDistance = new TreeMap<Person, Integer>(new Comparator<Person>(){
			public int compare(Person a, Person b) {
				return a.Id - b.Id;
			}
		});
		int lastCandidateBeforeQuote = this.binarySearchBefore(this.tmpQuoteMarks[bQuote].beginOffset,
				this.tmpCandidates);
		int firstCandidateAfterQuote = this.binarySearchAfter(this.tmpQuoteMarks[bQuote+1].endOffset,
				this.tmpCandidates);
		for(int pCand = lastCandidateBeforeQuote; pCand >= 0; --pCand)
		{
			int beginOffset = this.tmpCandidates[pCand].endOffset;
			int endOffset = this.tmpQuoteMarks[bQuote].beginOffset;
			int distance = -getDistanceInWords(beginOffset, endOffset) - 1;
			if(-distance > DIS_THRESH_IN_WORDS) break;
			Person p = this.nameMap.get(this.tmpCandidates[pCand].txt);
			if(p != null && !candidateDistance.containsKey(p)) candidateDistance.put(p, distance);
		}
		
		for(int pCand = firstCandidateAfterQuote; pCand < this.tmpCandidates.length; ++pCand)
		{
			int beginOffset = this.tmpQuoteMarks[bQuote+1].endOffset;
			int endOffset = this.tmpCandidates[pCand].beginOffset;
			int distance = getDistanceInWords(beginOffset, endOffset) + 1;
			if(distance > DIS_THRESH_IN_WORDS) break;
			Person p = this.nameMap.get(this.tmpCandidates[pCand].txt);
			if(p != null && (!candidateDistance.containsKey(p) || Math.abs(candidateDistance.get(p)) >distance)) candidateDistance.put(p, distance);
		}
		CandidateQuote[] sortList = new CandidateQuote[candidateDistance.size()];
		int i=0;
		for(Person p: candidateDistance.keySet())
		{
			CandidateQuote cq = new CandidateQuote();
			cq.candidate = p;
			cq.quoteBeginOffset = this.tmpQuoteMarks[bQuote].beginOffset;
			cq.quoteEndOffset = this.tmpQuoteMarks[bQuote+1].endOffset;
			cq.distance = candidateDistance.get(p);
			sortList[i++] = cq;
			//calcuteFeature_typeOfWordBetween(cq);
			this.candidateQuotes.add(cq);
		}
		//for the feature ordinal number
		Arrays.sort(sortList, new Comparator<CandidateQuote>(){
			@Override
			public int compare(CandidateQuote arg0, CandidateQuote arg1) {
				return Math.abs(arg0.distance) - Math.abs(arg1.distance);
			}
		});
		
		for(i=0; i<sortList.length; ++i)
		{
			sortList[i].ordinal = i+1;
		}
		
	}

	private int getDistanceInWords(int beginOffset, int endOffset) {
		// TODO Auto-generated method stub
		int bWords = this.binarySearchAfter(beginOffset, this.tmpWords);
		int eWords = this.binarySearchBefore(endOffset, this.tmpWords) + 1;
		if(eWords < bWords) throw new IllegalStateException();
		return eWords - bWords;
	}

	/**
	 * @param bOffset
	 * @param array
	 * @return the first TmpString whose beginOffset is equal or greater than bOffset;
	 */
	private int binarySearchAfter(int bOffset, TmpString[] array)
	{
		int l = 0, r = array.length-1;
		while(l <= r)
		{
			int m = (l + r)>>1;
			if(array[m].beginOffset < bOffset) l = m + 1;
			else r = m - 1;
		}
		return l;
	}
	
	/**
	 * @param eOffset
	 * @param array
	 * @return the last TmpString whose endOffset is equal or smaller than eOffset
	 */
	private int binarySearchBefore(int eOffset, TmpString[] array)
	{		
		int l = 0, r = array.length-1;
		while(l <= r)
		{
			int m = (l + r)>>1;
			if(array[m].endOffset > eOffset) r = m - 1;
			else l = m + 1;
		}
		return r;
	}
	
	private IDictionary dict;
	public boolean checkWordNet(String word, POS pos, String[] cmps, boolean checkAll)  
	{
		try {
			IIndexWord idxWord = dict.getIndexWord(word, pos);
			if (idxWord == null) {
				// System.out.println("Not a verb");
				return false;
			}
			for(IWordID wordID: idxWord.getWordIDs())
			{
				IWord wd = dict.getWord(wordID);
				String lf = wd.getSenseKey().getLexicalFile().toString();
				for(String cmp: cmps)
					if (lf.equals(cmp))
						return true;
				if(!checkAll) break;
			}
			
		} catch (Exception e) {
			// System.out.print("WARNING(exception): word is " + word);
			// e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean isVerbalExpression(String word)
	{
		return checkWordNet(word, POS.VERB, new String[]{"verb.communication", "verb.cognition", "verb.emotion"}, true);
	}
	
	public boolean isCommunicationWord(String word)
	{
		return checkWordNet(word, POS.VERB, new String[]{"verb.communication"}, true);
	}
	
	public boolean isPerson(String word)  
	{
		if(word.equalsIgnoreCase("i")) return true;
		return checkWordNet(word, POS.NOUN, new String[]{"noun.person"}, true);
	}
	
	public Set<String> getNames(Annotation annotation)
	{
		 List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		 Set<String> names = new TreeSet<String>();
		 names.add("i");
		 for(int i=0; i<sentences.size(); ++i)
		 {
		      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);    
		      //Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		      String name = null;
		      List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
		      for (int j=0; j<tokens.size(); ++j) {
		    	  CoreMap token = tokens.get(j);
		    	  ArrayCoreMap aToken = (ArrayCoreMap) token;
		    	  String ne = aToken.get(CoreAnnotations.NamedEntityTagAnnotation.class);
		    	  String txt = aToken.get(CoreAnnotations.TextAnnotation.class);
		    	  if(ne.equals("PERSON"))
		    	  {
		    		  //System.out.println();
		    		  if(name != null)
		    			  name = name + " " + txt;
		    		  else 
		    			  name = txt;
		    	  }
		    	  else
		    	  {
		    		  if(name != null)
		    		  {
		    			  names.add(name);
		    			  name = null;
		    		  }
		    		  else
		    		  {
		    			  /*
		    			  if(this.isPerson(txt))
		    				  names.add(txt);*/
		    		  }
		    	  }
		      }
		      
		  }
		 return names;
	}

}
