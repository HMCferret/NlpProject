import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import weka.core.Instances;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;


public class Main {
	
	public static void main(String[] args) throws Exception {
		 /*PrintWriter out;
		    if (args.length > 1) {
		      out = new PrintWriter(new PrintStream(args[1]), true);
		    } else {
		      out = new PrintWriter(System.out, true);
		    }
		    		    
		    Annotation input = new Annotation("Kosgi Santosh sent an email to Stanford University. \"Die hard\" Kosgi said \"Live strong.\" He said \"Wow if only this works!\" He loves his father");
		    if (args.length > 0) {
		    	input = new Annotation(IOUtils.slurpFileNoExceptions(args[0]));
		    }
		    System.out.println(input);
		    
		    QuotedSpeechAttribution qsa = new QuotedSpeechAttribution(input, out, false);
		    qsa.preprocessing();
		    Vector<Quote> list = qsa.getAllAttributedQuotes();
		    Iterator<Quote> it = list.iterator();
		    int cnt = 0;
		    System.out.print("cntQuote=");
		    for(int i=0; i<=4; ++i)
		    	System.out.println(QuotedSpeechAttribution.cntQuote[i]);
		    System.out.println("cntQuoteMore=" + QuotedSpeechAttribution.cntQuoteMore);
		    System.out.println("cntMiddleQuote=" + QuotedSpeechAttribution.cntMiddleQuote);
		    while(it.hasNext())
		    {
		    	Quote q= it.next();
		    	System.out.println(++cnt + ":" + "[" + q.speaker.name + "]" + "=>" + q.text);
		    }*/
		    
		   Demo();
	}
	
	public static void Demo() throws Exception
	{
		List<CandidateQuote> cq = new ArrayList<CandidateQuote>();
	    CandidateQuote item = new CandidateQuote();
	    
	    item.distance = 2;
	    item.commaPresence = true;
	    item.periodPresence = false;
	    item.punctuationPresence = true;
	    item.ordinal = 2;
	    item.proportion = 35.0;
	    item.numNameCd = 2;
	    item.numQuoteCd = 3;
	    item.numWordCd = 4;
	    item.numNameQt = 4;
	    item.numQuoteQt = 3;
	    item.numWordQt = 7;
	    item.numNameOtr = 9;
	    item.numQuoteOtr = 8;
	    item.numWordOtr = 35;
	    item.numAppearance = 12;
	    item.expVerbPresence = false;
	    item.personPresence = true;
	    item.quoteLength = 37;
	    item.quotePosition = 22;
	    item.otherCharPresence = true;
	    item.candidatePresence = false;
	    
	    cq.add(item);
	    
	    item = new CandidateQuote();
	    item.distance = 6;
	    item.commaPresence = false;
	    item.periodPresence = true;
	    item.punctuationPresence = false;
	    item.ordinal = 2;
	    item.proportion = 37.0;
	    item.numNameCd = 7;
	    item.numQuoteCd = 18;
	    item.numWordCd = 24;
	    item.numNameQt = 24;
	    item.numQuoteQt = 23;
	    item.numWordQt = 7;
	    item.numNameOtr = 9;
	    item.numQuoteOtr = 8;
	    item.numWordOtr = 35;
	    item.numAppearance = 12;
	    item.expVerbPresence = false;
	    item.personPresence = true;
	    item.quoteLength = 37;
	    item.quotePosition = 22;
	    item.otherCharPresence = true;
	    item.candidatePresence = false;
	    
	    cq.add(item);
	    
	    
	    AttributeJ48 att = new AttributeJ48(cq,200,5);
	    List<CandidateQuoteResult> cqr = att.Create();
	    for(CandidateQuoteResult it:cqr)
	    {
	    	System.out.println(it.candidateIsSpeaker);
	    }
	    
	    Instances data = att.ShowData();
	    System.out.println(data);
	}
}
