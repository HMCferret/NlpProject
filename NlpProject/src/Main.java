import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
		 	PrintWriter out;
		    if (args.length > 1) {
		      out = new PrintWriter(new PrintStream(args[1]), true);
		    } else {
		      out = new PrintWriter(System.out, true);
		    }
		    String text2 = "Yubin send an email to Stanford University. \"Die hard\" Kosgi said \"Live strong.\" Tu said \"Wow if only this works!\" He loves his father";	
			String file = new File("DataSet/novels/Fiction/fi_fold1/failure1/11005.txt").getAbsolutePath();
		    String text = readFile(file);
		    Annotation input = new Annotation(text);
		    if (args.length > 0) {
		    	input = new Annotation(IOUtils.slurpFileNoExceptions(args[0]));
		    }
		    System.out.println(input);
		    
		    QuotedSpeechAttribution qsa = new QuotedSpeechAttribution(input, out, false);
		    qsa.preprocessing();
		    Vector<Quote> list = qsa.getAllAttributedQuotes();
		    Iterator<Quote> it = list.iterator();
		    int cnt = 0;		  
		    while(it.hasNext())
		    {
		    	Quote q= it.next();
		    	System.out.println(++cnt + ":" + "[" + q.speaker.name + "]" + "=>" + q.text);
		    }
			Set<String> setName = StandfordWrapper.getNames(input);
			
		SNFeatureExtraction snfe = new SNFeatureExtraction(list, text, qsa.names);
		ConversationalNetworkFeature cnf = snfe.ConstructNetwork();
		
		System.out.println("cnf.numChar: " + cnf.numChar);
		System.out.println("cnf.numSpeakingChar: " + cnf.numSpeakingChar);
		
		System.out.println("cnf.MostFqtChar: " + cnf.MostFqtChar);
		
		System.out.println("cnf.numQuote: " + cnf.numQuote);
		System.out.println("cnf.quoteProportion: " + cnf.quoteProportion);
		
		System.out.println("cnf.num3Clique: " + cnf.num3Clique);
		System.out.println("cnf.num4Clique: " + cnf.num4Clique);
		
		System.out.println("cnf.avgDegree: " + cnf.avgDegree);
		
		System.out.println("cnf.graphDensity: " + cnf.graphDensity);
		
		
		//Demo();
	}
	
	 public static String readFile(String filePath)
		{
			String content = null;
			File file = new File(filePath);
			try {
				FileReader reader = new FileReader(file);
				char[] chars = new char[(int) file.length()];
				reader.read(chars);
				content = new String(chars);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return content;
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
	    
	    //List<CandidateQuote>, Range, RangeLength
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
