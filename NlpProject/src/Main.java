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
			
//		 	PrintWriter out;
//		    if (args.length > 1) {
//		      out = new PrintWriter(new PrintStream(args[1]), true);
//		    } else {
//		      out = new PrintWriter(System.out, true);
//		    }
//		    String text2 = "Yubin send an email to Stanford University. \"Die hard\" Kosgi said \"Live strong.\" Tu said \"Wow if only this works!\" He loves his father";	
//			String file = new File("DataSet/novels/Fiction/fi_fold1/failure1/11005.txt").getAbsolutePath();
//		    String text = readFile(file);
//		    Annotation input = new Annotation(text);
//		    if (args.length > 0) {
//		    	input = new Annotation(IOUtils.slurpFileNoExceptions(args[0]));
//		    }
//		    System.out.println(input);
//		    
//		    QuotedSpeechAttribution qsa = new QuotedSpeechAttribution(input, out, false);
//		    qsa.preprocessing();
//		    Vector<Quote> list = qsa.getAllAttributedQuotes();
//		    Iterator<Quote> it = list.iterator();
//		    int cnt = 0;		  
//		    while(it.hasNext())
//		    {
//		    	Quote q= it.next();
//		    	System.out.println(++cnt + ":" + "[" + q.speaker.name + "]" + "=>" + q.text);
//		    }
//			//Set<String> setName = StandfordWrapper.getNames(input);
//		System.out.println("Start extracting features from network");
//		long startTime = System.currentTimeMillis();
//		SNFeatureExtraction snfe = new SNFeatureExtraction(list, text, qsa.names);
//		ConversationalNetworkFeature cnf = snfe.ConstructNetwork();
//		
//		System.out.println("cnf.numChar: " + cnf.numChar);
//		System.out.println("cnf.numSpeakingChar: " + cnf.numSpeakingChar);
//		
//		System.out.println("cnf.MostFqtChar: " + cnf.MostFqtChar);
//		
//		System.out.println("cnf.numQuote: " + cnf.numQuote);
//		System.out.println("cnf.quoteProportion: " + cnf.quoteProportion);
//		
//		System.out.println("cnf.num3Clique: " + cnf.num3Clique);
//		System.out.println("cnf.num4Clique: " + cnf.num4Clique);
//		
//		System.out.println("cnf.avgDegree: " + cnf.avgDegree);
//		
//		System.out.println("cnf.graphDensity: " + cnf.graphDensity);
//		long endTime = System.currentTimeMillis();
//		System.out.println("Time for features network extraction: " + (endTime - startTime));
//		
		
//		Baseline bl = new Baseline();
//		bl.Run();
		
		SVMTrain svmt = new SVMTrain();
		svmt.Train();
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
	
	public static void Demo2() throws Exception
	{
		List<ConversationalNetworkFeature> cq = new ArrayList<ConversationalNetworkFeature>();
		ConversationalNetworkFeature item = new ConversationalNetworkFeature();
	    
		item.numChar = 20;
		item.numSpeakingChar=55;
		item.mostFqtChar=55.0;
		item.numQuote=10;
		item.quoteProportion=20.0;
		item.num3Clique=10;
		item.avgDegree=2;
		item.graphDensity=0.12;
		item.success=true;
	    
	    cq.add(item);
	    
	    item = new ConversationalNetworkFeature();
		item.numChar = 30;
		item.numSpeakingChar=15;
		item.mostFqtChar=75.0;
		item.numQuote=20;
		item.quoteProportion=50.0;
		item.num3Clique=20;
		item.avgDegree=4.0;
		item.graphDensity=0.1;
		item.success=false;
	    cq.add(item);
	    
	    item = new ConversationalNetworkFeature();
	    item.numChar= 42;
	    item.numSpeakingChar= 12;
	    item.mostFqtChar= 51.09677419354839;
	    item.numQuote= 775;
	    item.quoteProportion= 13.882029067649084;
	    item.num3Clique= 334;
	    item.avgDegree= 3.8333333333333335;
	    item.graphDensity= 0.3484848484848485;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 35;
	    item.numSpeakingChar= 7;
	    item.mostFqtChar= 97.45454545454545;
	    item.numQuote= 275;
	    item.quoteProportion= 4.695607500642178;
	    item.num3Clique= 12;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.3333333333333333;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 104;
	    item.numSpeakingChar= 21;
	    item.mostFqtChar= 85.14056224899599;
	    item.numQuote= 498;
	    item.quoteProportion= 4.502585623262447;
	    item.num3Clique= 76;
	    item.avgDegree= 2.4761904761904763;
	    item.graphDensity= 0.12380952380952381;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 180;
	    item.numSpeakingChar= 68;
	    item.mostFqtChar= 43.56287425149701;
	    item.numQuote= 668;
	    item.quoteProportion= 3.5508722510528883;
	    item.num3Clique= 378;
	    item.avgDegree= 4.294117647058823;
	    item.graphDensity= 0.06409130816505706;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 55;
	    item.numSpeakingChar= 0;
	    item.mostFqtChar= 0.1;
	    item.numQuote= 0;
	    item.quoteProportion= 0.0;
	    item.num3Clique= 0;
	    item.avgDegree= 0.1;
	    item.graphDensity= 0.1;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 130;
	    item.numSpeakingChar= 21;
	    item.mostFqtChar= 86.56215005599104;
	    item.numQuote= 893;
	    item.quoteProportion= 5.633546208158676;
	    item.num3Clique= 120;
	    item.avgDegree= 2.6666666666666665;
	    item.graphDensity= 0.13333333333333333;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 1;
	    item.numSpeakingChar= 0;
	    item.mostFqtChar= 0.1;
	    item.numQuote= 0;
	    item.quoteProportion= 0.0;
	    item.num3Clique= 0;
	    item.avgDegree= 0.1;
	    item.graphDensity= 0.1;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 34;
	    item.numSpeakingChar= 11;
	    item.mostFqtChar= 23.52941176470588;
	    item.numQuote= 68;
	    item.quoteProportion= 0.7370585678925258;
	    item.num3Clique= 25;
	    item.avgDegree= 3.6363636363636362;
	    item.graphDensity= 0.36363636363636365;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 33;
	    item.numSpeakingChar= 12;
	    item.mostFqtChar= 45.16129032258064;
	    item.numQuote= 62;
	    item.quoteProportion= 0.7018201875344733;
	    item.num3Clique= 28;
	    item.avgDegree= 3.5;
	    item.graphDensity= 0.3181818181818182;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 114;
	    item.numSpeakingChar= 36;
	    item.mostFqtChar= 47.368421052631575;
	    item.numQuote= 703;
	    item.quoteProportion= 7.143070930446346;
	    item.num3Clique= 291;
	    item.avgDegree= 4.111111111111111;
	    item.graphDensity= 0.11746031746031746;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 104;
	    item.numSpeakingChar= 6;
	    item.mostFqtChar= 84.90566037735849;
	    item.numQuote= 53;
	    item.quoteProportion= 0.3741059510634577;
	    item.num3Clique= 9;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.4;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 20;
	    item.numSpeakingChar= 4;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 53;
	    item.quoteProportion= 5.10307781649245;
	    item.num3Clique= 3;
	    item.avgDegree= 1.5;
	    item.graphDensity= 0.5;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 60;
	    item.numSpeakingChar= 18;
	    item.mostFqtChar= 70.58823529411765;
	    item.numQuote= 102;
	    item.quoteProportion= 3.1937582888032323;
	    item.num3Clique= 35;
	    item.avgDegree= 2.5555555555555554;
	    item.graphDensity= 0.1503267973856209;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 60;
	    item.numSpeakingChar= 16;
	    item.mostFqtChar= 16.901408450704224;
	    item.numQuote= 71;
	    item.quoteProportion= 1.4003047025122557;
	    item.num3Clique= 40;
	    item.avgDegree= 3.625;
	    item.graphDensity= 0.24166666666666667;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 29;
	    item.numSpeakingChar= 8;
	    item.mostFqtChar= 94.3089430894309;
	    item.numQuote= 123;
	    item.quoteProportion= 2.0445613782963648;
	    item.num3Clique= 25;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.2857142857142857;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 31;
	    item.numSpeakingChar= 5;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 10;
	    item.quoteProportion= 0.2300950239755502;
	    item.num3Clique= 5;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.5;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 81;
	    item.numSpeakingChar= 9;
	    item.mostFqtChar= 97.53086419753086;
	    item.numQuote= 324;
	    item.quoteProportion= 2.809834157390561;
	    item.num3Clique= 44;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.25;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 54;
	    item.numSpeakingChar= 17;
	    item.mostFqtChar= 68.65671641791045;
	    item.numQuote= 201;
	    item.quoteProportion= 2.531816480917827;
	    item.num3Clique= 86;
	    item.avgDegree= 3.176470588235294;
	    item.graphDensity= 0.19852941176470587;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 41;
	    item.numSpeakingChar= 13;
	    item.mostFqtChar= 78.0701754385965;
	    item.numQuote= 114;
	    item.quoteProportion= 2.4456725599539872;
	    item.num3Clique= 44;
	    item.avgDegree= 2.923076923076923;
	    item.graphDensity= 0.24358974358974358;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 32;
	    item.numSpeakingChar= 10;
	    item.mostFqtChar= 92.20779220779221;
	    item.numQuote= 77;
	    item.quoteProportion= 1.6978025502676426;
	    item.num3Clique= 39;
	    item.avgDegree= 3.2;
	    item.graphDensity= 0.35555555555555557;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 52;
	    item.numSpeakingChar= 15;
	    item.mostFqtChar= 81.81818181818183;
	    item.numQuote= 88;
	    item.quoteProportion= 4.079518300460645;
	    item.num3Clique= 52;
	    item.avgDegree= 3.466666666666667;
	    item.graphDensity= 0.24761904761904763;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 57;
	    item.numSpeakingChar= 18;
	    item.mostFqtChar= 70.08928571428571;
	    item.numQuote= 224;
	    item.quoteProportion= 1.6951591806688173;
	    item.num3Clique= 113;
	    item.avgDegree= 4.555555555555555;
	    item.graphDensity= 0.2679738562091503;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 42;
	    item.numSpeakingChar= 12;
	    item.mostFqtChar= 46.34146341463415;
	    item.numQuote= 41;
	    item.quoteProportion= 0.3894140077781655;
	    item.num3Clique= 25;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.2727272727272727;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 36;
	    item.numSpeakingChar= 14;
	    item.mostFqtChar= 89.62025316455696;
	    item.numQuote= 395;
	    item.quoteProportion= 5.4102633350605585;
	    item.num3Clique= 134;
	    item.avgDegree= 4.0;
	    item.graphDensity= 0.3076923076923077;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 50;
	    item.numSpeakingChar= 10;
	    item.mostFqtChar= 97.99196787148594;
	    item.numQuote= 249;
	    item.quoteProportion= 5.297481489175137;
	    item.num3Clique= 121;
	    item.avgDegree= 2.8;
	    item.graphDensity= 0.3111111111111111;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 89;
	    item.numSpeakingChar= 23;
	    item.mostFqtChar= 64.84018264840182;
	    item.numQuote= 219;
	    item.quoteProportion= 3.8852970378304;
	    item.num3Clique= 82;
	    item.avgDegree= 3.652173913043478;
	    item.graphDensity= 0.16600790513833993;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 43;
	    item.numSpeakingChar= 16;
	    item.mostFqtChar= 48.16901408450705;
	    item.numQuote= 355;
	    item.quoteProportion= 4.8272674981686965;
	    item.num3Clique= 151;
	    item.avgDegree= 3.875;
	    item.graphDensity= 0.25833333333333336;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 33;
	    item.numSpeakingChar= 14;
	    item.mostFqtChar= 92.57142857142857;
	    item.numQuote= 350;
	    item.quoteProportion= 3.6636100985833817;
	    item.num3Clique= 115;
	    item.avgDegree= 3.2857142857142856;
	    item.graphDensity= 0.25274725274725274;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 71;
	    item.numSpeakingChar= 18;
	    item.mostFqtChar= 76.20578778135048;
	    item.numQuote= 311;
	    item.quoteProportion= 2.174683251819617;
	    item.num3Clique= 120;
	    item.avgDegree= 3.4444444444444446;
	    item.graphDensity= 0.20261437908496732;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 42;
	    item.numSpeakingChar= 7;
	    item.mostFqtChar= 43.75;
	    item.numQuote= 16;
	    item.quoteProportion= 0.27718189736989224;
	    item.num3Clique= 8;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.3333333333333333;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 64;
	    item.numSpeakingChar= 2;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 15;
	    item.quoteProportion= 0.17518403513354033;
	    item.num3Clique= 0;
	    item.avgDegree= 1.0;
	    item.graphDensity= 1.0;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 53;
	    item.numSpeakingChar= 17;
	    item.mostFqtChar= 80.81632653061224;
	    item.numQuote= 245;
	    item.quoteProportion= 2.93064336329407;
	    item.num3Clique= 82;
	    item.avgDegree= 3.176470588235294;
	    item.graphDensity= 0.19852941176470587;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 41;
	    item.numSpeakingChar= 10;
	    item.mostFqtChar= 87.17948717948718;
	    item.numQuote= 78;
	    item.quoteProportion= 1.6249832364647239;
	    item.num3Clique= 25;
	    item.avgDegree= 3.4;
	    item.graphDensity= 0.37777777777777777;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 21;
	    item.numSpeakingChar= 5;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 32;
	    item.quoteProportion= 1.3142705392031997;
	    item.num3Clique= 16;
	    item.avgDegree= 2.4;
	    item.graphDensity= 0.6;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 30;
	    item.numSpeakingChar= 13;
	    item.mostFqtChar= 62.5;
	    item.numQuote= 56;
	    item.quoteProportion= 1.3398475090680286;
	    item.num3Clique= 38;
	    item.avgDegree= 3.6923076923076925;
	    item.graphDensity= 0.3076923076923077;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 55;
	    item.numSpeakingChar= 16;
	    item.mostFqtChar= 49.137931034482754;
	    item.numQuote= 116;
	    item.quoteProportion= 0.7693024266795013;
	    item.num3Clique= 48;
	    item.avgDegree= 3.25;
	    item.graphDensity= 0.21666666666666667;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 20;
	    item.numSpeakingChar= 7;
	    item.mostFqtChar= 75.0;
	    item.numQuote= 28;
	    item.quoteProportion= 2.500279958057193;
	    item.num3Clique= 12;
	    item.avgDegree= 2.2857142857142856;
	    item.graphDensity= 0.38095238095238093;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 32;
	    item.numSpeakingChar= 8;
	    item.mostFqtChar= 97.47292418772562;
	    item.numQuote= 277;
	    item.quoteProportion= 6.4172375040137;
	    item.num3Clique= 28;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.2857142857142857;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 38;
	    item.numSpeakingChar= 9;
	    item.mostFqtChar= 97.57785467128028;
	    item.numQuote= 289;
	    item.quoteProportion= 3.5015124799818826;
	    item.num3Clique= 17;
	    item.avgDegree= 1.7777777777777777;
	    item.graphDensity= 0.2222222222222222;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 19;
	    item.numSpeakingChar= 3;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 137;
	    item.quoteProportion= 5.3007799142217955;
	    item.num3Clique= 3;
	    item.avgDegree= 1.3333333333333333;
	    item.graphDensity= 0.6666666666666666;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 36;
	    item.numSpeakingChar= 16;
	    item.mostFqtChar= 21.875;
	    item.numQuote= 96;
	    item.quoteProportion= 2.125129670587775;
	    item.num3Clique= 48;
	    item.avgDegree= 3.875;
	    item.graphDensity= 0.25833333333333336;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 31;
	    item.numSpeakingChar= 11;
	    item.mostFqtChar= 28.947368421052634;
	    item.numQuote= 38;
	    item.quoteProportion= 0.3273875833868495;
	    item.num3Clique= 18;
	    item.avgDegree= 2.727272727272727;
	    item.graphDensity= 0.2727272727272727;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 96;
	    item.numSpeakingChar= 29;
	    item.mostFqtChar= 75.91240875912408;
	    item.numQuote= 274;
	    item.quoteProportion= 1.6774767475369303;
	    item.num3Clique= 106;
	    item.avgDegree= 3.7241379310344827;
	    item.graphDensity= 0.1330049261083744;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 1;
	    item.numSpeakingChar= 1;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 7;
	    item.quoteProportion= 0.7152481586918861;
	    item.num3Clique= 0;
	    item.avgDegree= 0.0;
	    item.graphDensity= 0.1;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 47;
	    item.numSpeakingChar= 12;
	    item.mostFqtChar= 50.0;
	    item.numQuote= 28;
	    item.quoteProportion= 1.7470982966509714;
	    item.num3Clique= 18;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.2727272727272727;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 52;
	    item.numSpeakingChar= 10;
	    item.mostFqtChar= 81.48148148148148;
	    item.numQuote= 27;
	    item.quoteProportion= 0.32575047253352385;
	    item.num3Clique= 15;
	    item.avgDegree= 2.2;
	    item.graphDensity= 0.24444444444444444;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 22;
	    item.numSpeakingChar= 2;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 317;
	    item.quoteProportion= 4.802827125428102;
	    item.num3Clique= 0;
	    item.avgDegree= 1.0;
	    item.graphDensity= 1.0;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 16;
	    item.numSpeakingChar= 4;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 10;
	    item.quoteProportion= 0.4365916113739847;
	    item.num3Clique= 2;
	    item.avgDegree= 1.5;
	    item.graphDensity= 0.5;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 47;
	    item.numSpeakingChar= 18;
	    item.mostFqtChar= 37.77777777777778;
	    item.numQuote= 45;
	    item.quoteProportion= 0.2713070173078584;
	    item.num3Clique= 25;
	    item.avgDegree= 2.6666666666666665;
	    item.graphDensity= 0.1568627450980392;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 71;
	    item.numSpeakingChar= 12;
	    item.mostFqtChar= 95.37815126050421;
	    item.numQuote= 476;
	    item.quoteProportion= 3.7183322953358426;
	    item.num3Clique= 45;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.18181818181818182;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 104;
	    item.numSpeakingChar= 15;
	    item.mostFqtChar= 85.13513513513513;
	    item.numQuote= 74;
	    item.quoteProportion= 1.1321414429063703;
	    item.num3Clique= 31;
	    item.avgDegree= 2.8;
	    item.graphDensity= 0.2;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 70;
	    item.numSpeakingChar= 12;
	    item.mostFqtChar= 58.964143426294825;
	    item.numQuote= 251;
	    item.quoteProportion= 5.4167439699277775;
	    item.num3Clique= 76;
	    item.avgDegree= 4.166666666666667;
	    item.graphDensity= 0.3787878787878788;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 41;
	    item.numSpeakingChar= 8;
	    item.mostFqtChar= 92.66666666666666;
	    item.numQuote= 150;
	    item.quoteProportion= 3.8646332639956587;
	    item.num3Clique= 33;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.42857142857142855;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 37;
	    item.numSpeakingChar= 11;
	    item.mostFqtChar= 91.82879377431907;
	    item.numQuote= 257;
	    item.quoteProportion= 4.330422970945442;
	    item.num3Clique= 28;
	    item.avgDegree= 1.8181818181818181;
	    item.graphDensity= 0.18181818181818182;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 55;
	    item.numSpeakingChar= 14;
	    item.mostFqtChar= 55.319148936170215;
	    item.numQuote= 47;
	    item.quoteProportion= 2.1819604411876057;
	    item.num3Clique= 25;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.23076923076923078;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 25;
	    item.numSpeakingChar= 7;
	    item.mostFqtChar= 82.16560509554141;
	    item.numQuote= 157;
	    item.quoteProportion= 1.6388905705414825;
	    item.num3Clique= 64;
	    item.avgDegree= 3.142857142857143;
	    item.graphDensity= 0.5238095238095238;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 43;
	    item.numSpeakingChar= 9;
	    item.mostFqtChar= 74.75728155339806;
	    item.numQuote= 103;
	    item.quoteProportion= 1.5895209537607031;
	    item.num3Clique= 43;
	    item.avgDegree= 4.444444444444445;
	    item.graphDensity= 0.5555555555555556;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 96;
	    item.numSpeakingChar= 24;
	    item.mostFqtChar= 74.63414634146342;
	    item.numQuote= 205;
	    item.quoteProportion= 3.5078652357576585;
	    item.num3Clique= 85;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.13043478260869565;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 26;
	    item.numSpeakingChar= 6;
	    item.mostFqtChar= 56.666666666666664;
	    item.numQuote= 330;
	    item.quoteProportion= 6.419454077295218;
	    item.num3Clique= 136;
	    item.avgDegree= 3.3333333333333335;
	    item.graphDensity= 0.6666666666666666;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 151;
	    item.numSpeakingChar= 40;
	    item.mostFqtChar= 39.285714285714285;
	    item.numQuote= 140;
	    item.quoteProportion= 2.3442739378065114;
	    item.num3Clique= 87;
	    item.avgDegree= 3.7;
	    item.graphDensity= 0.09487179487179487;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 25;
	    item.numSpeakingChar= 10;
	    item.mostFqtChar= 76.19047619047619;
	    item.numQuote= 21;
	    item.quoteProportion= 1.1089559120466794;
	    item.num3Clique= 9;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.2222222222222222;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 50;
	    item.numSpeakingChar= 17;
	    item.mostFqtChar= 55.980861244019145;
	    item.numQuote= 209;
	    item.quoteProportion= 4.9614782136120965;
	    item.num3Clique= 96;
	    item.avgDegree= 3.411764705882353;
	    item.graphDensity= 0.21323529411764705;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 35;
	    item.numSpeakingChar= 12;
	    item.mostFqtChar= 72.8395061728395;
	    item.numQuote= 81;
	    item.quoteProportion= 0.6633771679576655;
	    item.num3Clique= 32;
	    item.avgDegree= 2.6666666666666665;
	    item.graphDensity= 0.24242424242424243;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 27;
	    item.numSpeakingChar= 11;
	    item.mostFqtChar= 56.19834710743802;
	    item.numQuote= 121;
	    item.quoteProportion= 1.356630362999362;
	    item.num3Clique= 45;
	    item.avgDegree= 3.090909090909091;
	    item.graphDensity= 0.3090909090909091;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 37;
	    item.numSpeakingChar= 10;
	    item.mostFqtChar= 82.35294117647058;
	    item.numQuote= 34;
	    item.quoteProportion= 0.2935982488182209;
	    item.num3Clique= 14;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.2222222222222222;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 10;
	    item.numSpeakingChar= 1;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 4;
	    item.quoteProportion= 0.15603043793773924;
	    item.num3Clique= 0;
	    item.avgDegree= 0.0;
	    item.graphDensity= 0.1;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 49;
	    item.numSpeakingChar= 15;
	    item.mostFqtChar= 53.333333333333336;
	    item.numQuote= 30;
	    item.quoteProportion= 0.44301236235733693;
	    item.num3Clique= 21;
	    item.avgDegree= 2.533333333333333;
	    item.graphDensity= 0.18095238095238095;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 56;
	    item.numSpeakingChar= 9;
	    item.mostFqtChar= 97.98206278026906;
	    item.numQuote= 446;
	    item.quoteProportion= 6.48801003988326;
	    item.num3Clique= 53;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.25;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 18;
	    item.numSpeakingChar= 1;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 5;
	    item.quoteProportion= 0.25160416263454866;
	    item.num3Clique= 0;
	    item.avgDegree= 0.0;
	    item.graphDensity= 0.1;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 131;
	    item.numSpeakingChar= 10;
	    item.mostFqtChar= 92.0;
	    item.numQuote= 75;
	    item.quoteProportion= 0.3913491246138002;
	    item.num3Clique= 25;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.3333333333333333;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 77;
	    item.numSpeakingChar= 4;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 27;
	    item.quoteProportion= 0.10222349255879298;
	    item.num3Clique= 6;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.6666666666666666;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 48;
	    item.numSpeakingChar= 19;
	    item.mostFqtChar= 71.42857142857143;
	    item.numQuote= 189;
	    item.quoteProportion= 3.6713688503844466;
	    item.num3Clique= 67;
	    item.avgDegree= 4.0;
	    item.graphDensity= 0.2222222222222222;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 37;
	    item.numSpeakingChar= 16;
	    item.mostFqtChar= 44.285714285714285;
	    item.numQuote= 140;
	    item.quoteProportion= 1.834053311875553;
	    item.num3Clique= 76;
	    item.avgDegree= 4.125;
	    item.graphDensity= 0.275;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 46;
	    item.numSpeakingChar= 13;
	    item.mostFqtChar= 72.97297297297297;
	    item.numQuote= 222;
	    item.quoteProportion= 7.923586162492422;
	    item.num3Clique= 116;
	    item.avgDegree= 5.384615384615385;
	    item.graphDensity= 0.44871794871794873;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 41;
	    item.numSpeakingChar= 6;
	    item.mostFqtChar= 60.0;
	    item.numQuote= 205;
	    item.quoteProportion= 6.587785603848136;
	    item.num3Clique= 86;
	    item.avgDegree= 2.6666666666666665;
	    item.graphDensity= 0.5333333333333333;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 35;
	    item.numSpeakingChar= 13;
	    item.mostFqtChar= 66.66666666666666;
	    item.numQuote= 78;
	    item.quoteProportion= 1.4548430235910843;
	    item.num3Clique= 43;
	    item.avgDegree= 3.230769230769231;
	    item.graphDensity= 0.2692307692307692;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 71;
	    item.numSpeakingChar= 8;
	    item.mostFqtChar= 86.07594936708861;
	    item.numQuote= 79;
	    item.quoteProportion= 2.0560082864309694;
	    item.num3Clique= 28;
	    item.avgDegree= 2.25;
	    item.graphDensity= 0.32142857142857145;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 39;
	    item.numSpeakingChar= 8;
	    item.mostFqtChar= 88.75;
	    item.numQuote= 80;
	    item.quoteProportion= 1.6672439789722482;
	    item.num3Clique= 23;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.42857142857142855;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 6;
	    item.numSpeakingChar= 0;
	    item.mostFqtChar= 0.1;
	    item.numQuote= 0;
	    item.quoteProportion= 0.0;
	    item.num3Clique= 0;
	    item.avgDegree= 0.1;
	    item.graphDensity= 0.1;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 54;
	    item.numSpeakingChar= 15;
	    item.mostFqtChar= 80.0;
	    item.numQuote= 65;
	    item.quoteProportion= 2.0831629043977884;
	    item.num3Clique= 26;
	    item.avgDegree= 2.533333333333333;
	    item.graphDensity= 0.18095238095238095;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 79;
	    item.numSpeakingChar= 16;
	    item.mostFqtChar= 46.42857142857143;
	    item.numQuote= 56;
	    item.quoteProportion= 2.894998828765519;
	    item.num3Clique= 31;
	    item.avgDegree= 2.75;
	    item.graphDensity= 0.18333333333333332;
	    item.success= false;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 139;
	    item.numSpeakingChar= 23;
	    item.mostFqtChar= 59.01639344262295;
	    item.numQuote= 488;
	    item.quoteProportion= 2.204399912525002;
	    item.num3Clique= 181;
	    item.avgDegree= 4.0;
	    item.graphDensity= 0.18181818181818182;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 23;
	    item.numSpeakingChar= 9;
	    item.mostFqtChar= 71.42857142857143;
	    item.numQuote= 28;
	    item.quoteProportion= 0.7580961342429232;
	    item.num3Clique= 17;
	    item.avgDegree= 2.888888888888889;
	    item.graphDensity= 0.3611111111111111;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 104;
	    item.numSpeakingChar= 32;
	    item.mostFqtChar= 41.409691629955944;
	    item.numQuote= 227;
	    item.quoteProportion= 0.635508140548307;
	    item.num3Clique= 115;
	    item.avgDegree= 4.0625;
	    item.graphDensity= 0.1310483870967742;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 10;
	    item.numSpeakingChar= 2;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 6;
	    item.quoteProportion= 0.39287518948964123;
	    item.num3Clique= 0;
	    item.avgDegree= 1.0;
	    item.graphDensity= 1.0;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 60;
	    item.numSpeakingChar= 26;
	    item.mostFqtChar= 30.384615384615383;
	    item.numQuote= 260;
	    item.quoteProportion= 1.848502510408853;
	    item.num3Clique= 132;
	    item.avgDegree= 4.076923076923077;
	    item.graphDensity= 0.16307692307692306;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 48;
	    item.numSpeakingChar= 14;
	    item.mostFqtChar= 78.3132530120482;
	    item.numQuote= 83;
	    item.quoteProportion= 3.8962828616897376;
	    item.num3Clique= 42;
	    item.avgDegree= 3.2857142857142856;
	    item.graphDensity= 0.25274725274725274;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 93;
	    item.numSpeakingChar= 13;
	    item.mostFqtChar= 64.22764227642277;
	    item.numQuote= 123;
	    item.quoteProportion= 2.517678797705503;
	    item.num3Clique= 43;
	    item.avgDegree= 2.923076923076923;
	    item.graphDensity= 0.24358974358974358;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 96;
	    item.numSpeakingChar= 34;
	    item.mostFqtChar= 71.84115523465704;
	    item.numQuote= 277;
	    item.quoteProportion= 3.0393327414543214;
	    item.num3Clique= 140;
	    item.avgDegree= 3.0;
	    item.graphDensity= 0.09090909090909091;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 45;
	    item.numSpeakingChar= 3;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 30;
	    item.quoteProportion= 0.4150910691559967;
	    item.num3Clique= 5;
	    item.avgDegree= 1.3333333333333333;
	    item.graphDensity= 0.6666666666666666;
	    item.success= true;
	    cq.add(item);

	    item = new ConversationalNetworkFeature();
	    item.numChar= 21;
	    item.numSpeakingChar= 5;
	    item.mostFqtChar= 99.99;
	    item.numQuote= 59;
	    item.quoteProportion= 3.216017626865231;
	    item.num3Clique= 6;
	    item.avgDegree= 2.0;
	    item.graphDensity= 0.5;
	    item.success= true;
	    cq.add(item);
	    
	    //List<CandidateQuote>, Range, RangeLength
	    AJ48 att = new AJ48(cq,cq,900,5);
	    att.Create();
	    att.ShowData();
	}
	
	
}
