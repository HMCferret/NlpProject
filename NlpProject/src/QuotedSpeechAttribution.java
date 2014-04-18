import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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




public class QuotedSpeechAttribution {

	private Map<String, Person> nameMap;
	private Annotation annotation;
	private PrintWriter out;
	
	private boolean verbose;
	private String originalText;
	
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
	    
		Set<String> names = StandfordWrapper.getNames(annotation);
	    System.out.println(names);
	    generateNameMap(names);
	    if(verbose) Util.debug(nameMap);
	    
	    String path = new File("dict").getAbsolutePath();
		URL url = new URL("file", null, path);
		dict = new Dictionary(url);
		dict.open();
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
		return current+1;
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
		
		return ret;
	}
	static int debugQuoteCnt = 0;
	static int[] cntQuote = {0,0,0,0, 0};
	static int cntQuoteMore = 0;
	static int cntMiddleQuote = 0;
	private void processSentence(int paragraphNum, ArrayCoreMap sentence, Vector<Quote> ret) 
	{
		//Util.debug(sentence.get(CoreAnnotations.TextAnnotation.class));
		int q1b = -1;
		int q1e = -1;
		int q2b = -1;
		int q2e = -1;
		String tmpPerson = null;
		Vector<String> speaker = new Vector<String>();
		String verb = null;
		int cntQuoteMark = 0;
		for (CoreMap token : sentence
				.get(CoreAnnotations.TokensAnnotation.class)) 
		{
			
			ArrayCoreMap aToken = (ArrayCoreMap) token;
			String ne = aToken
					.get(CoreAnnotations.NamedEntityTagAnnotation.class);
			String txt = aToken.get(CoreAnnotations.OriginalTextAnnotation.class);
			String lemma = aToken.get(CoreAnnotations.LemmaAnnotation.class);
			int beginOffset = aToken.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
			int endOffset = aToken.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
			//Util.debug("Token[" + txt + "]");
			if (cntQuoteMark == 0 || cntQuoteMark == 2)
			{
				//Util.debug(lemma + " " + this.isVerbalExpression(lemma));
				if (this.isVerbalExpression(lemma))
					verb = txt;
				if (ne.equals("PERSON")) 
				{
					if (tmpPerson == null)
						tmpPerson = txt;
					else
						tmpPerson = tmpPerson + " " + txt;
				} else 
				{
					if (tmpPerson != null)
					{
						speaker.add(tmpPerson);
						tmpPerson = null;
					}
				}
			}
			if (txt.equals("\""))
			{
				if (cntQuoteMark == 0)
					q1b = beginOffset;
				else if (cntQuoteMark == 1)
					q1e = endOffset;
				else if(cntQuoteMark == 2)
					q2b = beginOffset;
				else if(cntQuoteMark == 3)
					q2e = endOffset;
				++cntQuoteMark;
			}
			
		}
		if(cntQuoteMark <=4)
		{
			++cntQuote[cntQuoteMark];
		}else
			++cntQuoteMore;
		/*
		if(q1b != -1)
			Util.debug("quote1" + this.originalText.substring(q1b, q1e));
		if(q2b != -1)
			Util.debug("quote2:" + this.originalText.substring(q2b, q2e));
		Util.debug("Speaker:" + speaker);
		Util.debug("V:" + verb);
		Util.debug("cntQuoteMark:" + cntQuoteMark);
		Util.debug("");*/
		if(cntQuoteMark > 0 && cntQuoteMark != 2 && cntQuoteMark != 4)
		{
			String txtSentence = sentence.get(CoreAnnotations.TextAnnotation.class);
			if(!(txtSentence.startsWith("\"") || txtSentence.endsWith("\""))) 
				{
				++cntMiddleQuote;
				System.out.println(cntQuoteMark + " quotemark" + txtSentence);

				}
		}
		if(q1b != -1 && q1e != -1) 
			{
			//System.out.println(++debugQuoteCnt + ":" + this.originalText.substring(q1b, q1e));
			}
		if(q2b != -1 && q2e != -1)
			{
			//System.out.println(++debugQuoteCnt + ":" + this.originalText.substring(q2b, q2e));
			}
		
		if(speaker.size() == 1)
		{
			Person p = this.nameMap.get(speaker.get(0));
			if(q1b != -1 && q1e != -1) ret.add(new Quote(p, this.originalText.substring(q1b, q1e), verb, q1b, q1e));
			if(q2b != -1 && q2e != -1) ret.add(new Quote(p, this.originalText.substring(q2b, q2e), verb, q2b, q2e));
		}
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
			e.printStackTrace();
			return false;
		}
		
		
	}

}
