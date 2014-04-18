import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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
		 PrintWriter out;
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
		    }
	}
}
