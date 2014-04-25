import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	private TmpString[] tmpWords;
	private TmpString[] tmpCandidates;
	private TmpString[] tmpQuoteMarks;
	private TmpParagraphNum[] tmpPraragrahNums;
	private TmpString[] tmpExpVerb;
	
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
		//TBD
		Iterator<String> it = names.iterator();
		while(it.hasNext())
		{
			String name = it.next();
			nameMap.put(name, new Person(name));
		}
	}
	
	/*
	 * Could take long
	 */
	public void preprocessing() throws IOException
	{
		if(verbose) Util.debug("Annotation starts");
	    StandfordWrapper.annotate(annotation, out);
	    if(verbose) Util.debug("Annotation ends");
	    
		names = StandfordWrapper.getNames(annotation);
	    System.out.println(names);
	    generateNameMap(names);
	    if(verbose) Util.debug(nameMap);
	    
	    String path = new File("dict").getAbsolutePath();
		URL url = new URL("file", null, path);
		dict = new Dictionary(url);
		dict.open();
		
		initAllTempArrays();
	}
	
	private void initAllTempArrays()
	{
		List<TmpString> tmpWords = new ArrayList<TmpString>();
		List<TmpString> tmpCandidates = new ArrayList<TmpString>();
		List<TmpParagraphNum> tmpPnums = new ArrayList<TmpParagraphNum>();
		List<TmpString> tmpQuoteMarks = new ArrayList<TmpString>();
		List<TmpString> tmpExpVerbs = new ArrayList<QuotedSpeechAttribution.TmpString>();
		
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
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
		      for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) 
				{
					
					ArrayCoreMap aToken = (ArrayCoreMap) token;
					String ne = aToken
							.get(CoreAnnotations.NamedEntityTagAnnotation.class);
					String txt = aToken.get(CoreAnnotations.OriginalTextAnnotation.class);
					String lemma = aToken.get(CoreAnnotations.LemmaAnnotation.class);
					int beginOffset = aToken.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
					int endOffset = aToken.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
					
					tmpWords.add(new TmpString(beginOffset, endOffset, txt));
					if (this.isVerbalExpression(lemma))
						tmpExpVerbs.add(new TmpString(beginOffset, endOffset, txt));
					if(txt.equals("\""))
					{
						++totalQuoteMarks;
						tmpQuoteMarks.add(new TmpString(beginOffset, endOffset, txt));
					}
					
				}
		    int quoteMarkCnt = 0;
		    if(totalQuoteMarks%2 == 1)
		    {
		    	if(!sentence.get(CoreAnnotations.TextAnnotation.class).startsWith("\"")) ++quoteMarkCnt;
		    }
			for (CoreMap token : sentence
					.get(CoreAnnotations.TokensAnnotation.class)) 
			{
				ArrayCoreMap aToken = (ArrayCoreMap) token;
				String ne = aToken
						.get(CoreAnnotations.NamedEntityTagAnnotation.class);
				String txt = aToken
						.get(CoreAnnotations.OriginalTextAnnotation.class);
				int beginOffset = aToken
						.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
				int endOffset = aToken
						.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
				if(txt.startsWith("\"")) ++quoteMarkCnt;
				//if(quoteMarkCnt%2 == 1) continue;
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
						tmpCandidates.add(new TmpString(tmpPersonBegin,
								tmpPersonEnd, tmpPerson));
						tmpPerson = null;
					}
				}
			}
		  }
		/*
		System.out.println("para #: " + tmpPnums.size());
		for(int i=0; i<5 && i < tmpPnums.size(); ++i)
		{
			TmpParagraphNum n = tmpPnums.get(i);
			System.out.println(n.num + "(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		System.out.println("tmpWords #: " + tmpWords.size());
		for(int i=0; i<5; ++i)
		{
			TmpString n = tmpWords.get(i);
			System.out.println("word(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		System.out.println("tmpCandiates #: " + tmpCandidates.size());
		for(int i=0; i<5; ++i)
		{
			TmpString n = tmpCandidates.get(i);
			System.out.println("Candiates(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		
		System.out.println("tmpQuoteMarks #: " + tmpQuoteMarks.size());
		for(int i=0; i<5; ++i)
		{
			TmpString n = tmpQuoteMarks.get(i);
			System.out.println("qmark(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}
		
		System.out.println("tmpExpVerbs #: " + tmpExpVerbs.size());
		for(int i=0; i<5; ++i)
		{
			TmpString n = tmpExpVerbs.get(i);
			System.out.println("expVerb(" +  n.beginOffset + "," + n.endOffset + "):" + this.originalText.substring(n.beginOffset, n.endOffset));
		}		
		*/
		this.tmpWords = tmpWords.toArray(new TmpString[tmpWords.size()]);
		this.tmpExpVerb = tmpExpVerbs.toArray(new TmpString[tmpExpVerbs.size()]);
		this.tmpPraragrahNums = tmpPnums.toArray(new TmpParagraphNum[tmpPnums.size()]);
		this.tmpQuoteMarks = tmpQuoteMarks.toArray(new TmpString[tmpQuoteMarks.size()]);
		this.tmpCandidates = tmpCandidates.toArray(new TmpString[tmpCandidates.size()]);
	}
	
	public QuotedSpeechAttribution(Annotation annotation, PrintWriter out, boolean verbose)
	{
		this.annotation = annotation;
		this.originalText = annotation.get(CoreAnnotations.TextAnnotation.class);
		this.out = out;
		this.nameMap = null;
		this.verbose = verbose;
	}
	
	private int updateParagraphNum(int current, ArrayCoreMap sentence)
	{
		int e = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
		if(e+1 >= this.originalText.length() || this.originalText.charAt(e+1) == '\n')
			return current+1;
		return current;
	}
	
	public List<CandidateQuote> getCandidateQuotePairsForJ48()
	{
		if(this.candidateQuotes == null) this.getAllAttributedQuotes();
		return this.candidateQuotes;
	}
	
	public Vector<Quote> getAllAttributedQuotes() 
	{
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		int paragraphNum = 0;
		Vector<Quote> ret = new Vector<Quote>();
		for(int i=0; i<sentences.size(); ++i)
		 {
		      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);
		      
		      paragraphNum = this.updateParagraphNum(paragraphNum, sentence);
		      processSentence(paragraphNum, sentence, ret);
		      
		  }
		//int cqCnt = 0;
		if(this.candidateQuotes == null) return ret;
		out.println("****************************");
		for(CandidateQuote cq:this.candidateQuotes)
		{
			out.println("CandidateQuote(" + cq.candidate.name + "," + this.originalText.substring(cq.quoteBeginOffset, cq.quoteEndOffset) );
			out.println("Distance=" + cq.distance);
		}
		return ret;
	}
	static int debugQuoteCnt = 0;
	static int[] cntQuote = {0,0,0,0, 0};
	static int cntQuoteMore = 0;
	static int cntMiddleQuote = 0;
	
	private void processSentence(int paragraphNum, ArrayCoreMap sentence, Vector<Quote> ret) 
	{		
		//Util.debug(sentence.get(CoreAnnotations.TextAnnotation.class));
		int beginOffsetSen = sentence.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
		int endOffsetSen = sentence.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
		int bQuote = this.binarySearchAfter(beginOffsetSen, this.tmpQuoteMarks);
		int eQuote = this.binarySearchBefore(endOffsetSen, this.tmpQuoteMarks) + 1;
		if(eQuote <= bQuote) return;
		int totalQuoteMark = eQuote - bQuote;
		if(totalQuoteMark == 0 || totalQuoteMark > 4) return;
		//because sentence split are not perfect, we get odd number of quote marks in a sentence often
		if(totalQuoteMark % 2 == 1)
		{
			if(this.originalText.charAt(beginOffsetSen) == '\"') ++eQuote;
			else --bQuote;
			if(bQuote < 0 || eQuote > this.tmpQuoteMarks.length) return;
		}
		Set<Person> speakers = new TreeSet<Person>(new Comparator<Person>(){
			public int compare(Person a, Person b) {
				return a.Id - b.Id;
			}
		});
		int pQuote = bQuote;
		int bOffset = beginOffsetSen;
		String expVerb = null;
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
					speakers.add(this.nameMap.get(name));
				}
			}
			if(pQuote == eQuote) break;
			bOffset = this.tmpQuoteMarks[pQuote + 1].beginOffset + 1;
			pQuote += 2;
		}
		if(expVerb != null && speakers.size() == 1)
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
			int distance = getDistanceInWords(beginOffset, endOffset);
			if(distance > DIS_THRESH_IN_WORDS) break;
			Person p = this.nameMap.get(this.tmpCandidates[pCand].txt);
			if(!candidateDistance.containsKey(p)) candidateDistance.put(p, distance);
		}
		
		for(int pCand = firstCandidateAfterQuote; pCand < this.tmpCandidates.length; ++pCand)
		{
			int beginOffset = this.tmpQuoteMarks[bQuote].endOffset;
			int endOffset = this.tmpCandidates[pCand].beginOffset;
			int distance = getDistanceInWords(beginOffset, endOffset);
			if(distance > DIS_THRESH_IN_WORDS) break;
			Person p = this.nameMap.get(this.tmpCandidates[pCand].txt);
			if(!candidateDistance.containsKey(p) || candidateDistance.get(p) >distance) candidateDistance.put(p, distance);
		}
		for(Person p: candidateDistance.keySet())
		{
			CandidateQuote cq = new CandidateQuote();
			cq.candidate = p;
			cq.quoteBeginOffset = this.tmpQuoteMarks[bQuote].beginOffset;
			cq.quoteEndOffset = this.tmpQuoteMarks[bQuote+1].endOffset;
			cq.distance = candidateDistance.get(p);
			this.candidateQuotes.add(cq);
		}
		
	}

	private int getDistanceInWords(int beginOffset, int endOffset) {
		// TODO Auto-generated method stub
		int bWords = this.binarySearchAfter(beginOffset, this.tmpWords);
		int eWords = this.binarySearchBefore(endOffset, this.tmpWords) + 1;
		if(eWords < bWords) throw new IllegalStateException();
		return eWords - bWords;
	}

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
	public boolean isVerbalExpression(String word)  
	{
		/*
		String [] simple = {"say", "says", "said"};
		for(int i=0; i<simple.length; ++i)
		{
			if(simple[i].equalsIgnoreCase(word)) return true;
		}
		return false;*/
		//if(word == null || word.length() == 0 || word.equals(" ")) return false;
		try{
		IIndexWord idxWord = dict.getIndexWord (word, POS.VERB );
		if(idxWord==null){
			//System.out.println("Not a verb");
			return false;
		}
		IWordID wordID = idxWord . getWordIDs ().get (0) ;
		IWord wd = dict.getWord ( wordID );
		String lf = wd.getSenseKey().getLexicalFile().toString();
		
		return (lf=="verb.communication")||(lf=="verb.cognition")||(lf=="verb.emotion");
		}catch(Exception e)
		{
			System.out.println("WARNING(exception): word is " + word);
			//e.printStackTrace();
			return false;
		}
		
		
	}

}
