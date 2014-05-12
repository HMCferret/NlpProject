import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weka.core.Instances;

//This class store help functions for testing the project
public class Demo {
	
	private List<ConversationalNetworkFeature> lcnf = new ArrayList<ConversationalNetworkFeature>();
	private ConversationalNetworkFeature cnf;
	public void Check() throws Exception
	{
		String filePath = new File("filename.txt").getAbsolutePath();
		readFile(filePath);
		
		for(ConversationalNetworkFeature cnf:lcnf)
		{
			System.out.println("item = new ConversationalNetworkFeature();");
			System.out.println("item.numChar= " + cnf.numChar+";");
			System.out.println("item.numSpeakingChar= " + cnf.numSpeakingChar+";");
			
			System.out.println("item.mostFqtChar= " + cnf.mostFqtChar+";");
			
			System.out.println("item.numQuote= " + cnf.numQuote+";");
			System.out.println("item.quoteProportion= " + cnf.quoteProportion+";");
			
			System.out.println("item.num3Clique= " + cnf.num3Clique+";");
			//System.out.println("cnf.num4Clique: " + cnf.num4Clique);
			
			System.out.println("item.avgDegree= " + cnf.avgDegree/5+";");
			
			System.out.println("item.graphDensity= " + cnf.graphDensity+";");
			
			System.out.println("item.success= " + cnf.success+";");
			System.out.println("cq.add(item);");
			System.out.println();
			
		}
	}
	
	public void readFile(String file) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			cnf = new ConversationalNetworkFeature();
			String[] parts = line.split(" ");
			cnf.numChar = Integer.parseInt(parts[0]);
			cnf.numSpeakingChar = Integer.parseInt(parts[1]);
			cnf.mostFqtChar = Double.parseDouble(parts[2]);
			cnf.numQuote = Integer.parseInt(parts[3]);
			cnf.quoteProportion = Double.parseDouble(parts[4]);
			cnf.num3Clique = Integer.parseInt(parts[5]);
			cnf.avgDegree = Double.parseDouble(parts[6]);
			cnf.graphDensity = Double.parseDouble(parts[7]);
			cnf.success = Boolean.valueOf(parts[8]);
			lcnf.add(cnf);
		}
		br.close();
	}
	
//	PrintWriter out = new PrintWriter("filename.txt");
//	for(ConversationalNetworkFeature cnf:testData)
//	{
//		System.out.println("cnf.numChar: " + cnf.numChar);
//		System.out.println("cnf.numSpeakingChar: " + cnf.numSpeakingChar);
//		
//		System.out.println("cnf.MostFqtChar: " + cnf.mostFqtChar);
//		
//		System.out.println("cnf.numQuote: " + cnf.numQuote);
//		System.out.println("cnf.quoteProportion: " + cnf.quoteProportion);
//		
//		System.out.println("cnf.num3Clique: " + cnf.num3Clique);
//		//System.out.println("cnf.num4Clique: " + cnf.num4Clique);
//		
//		System.out.println("cnf.avgDegree: " + cnf.avgDegree);
//		
//		System.out.println("cnf.graphDensity: " + cnf.graphDensity);
//		
//		System.out.println("cnf.graphDensity: " + cnf.success);
//		
//		String str = cnf.numChar + " " + cnf.numSpeakingChar + " " + cnf.mostFqtChar + " " + cnf.numQuote;
//		str += " " + cnf.quoteProportion + " " + cnf.num3Clique + " " + cnf.avgDegree + " ";
//		str += cnf.graphDensity + " " + cnf.success;
//		out.println(str);
//		
//	}
//	
//	for(ConversationalNetworkFeature cnf:trainData)
//	{
//		System.out.println("cnf.numChar: " + cnf.numChar);
//		System.out.println("cnf.numSpeakingChar: " + cnf.numSpeakingChar);
//		
//		System.out.println("cnf.MostFqtChar: " + cnf.mostFqtChar);
//		
//		System.out.println("cnf.numQuote: " + cnf.numQuote);
//		System.out.println("cnf.quoteProportion: " + cnf.quoteProportion);
//		
//		System.out.println("cnf.num3Clique: " + cnf.num3Clique);
//		//System.out.println("cnf.num4Clique: " + cnf.num4Clique);
//		
//		System.out.println("cnf.avgDegree: " + cnf.avgDegree);
//		
//		System.out.println("cnf.graphDensity: " + cnf.graphDensity);
//		
//		System.out.println("cnf.graphDensity: " + cnf.success);
//		
//		String str = cnf.numChar + " " + cnf.numSpeakingChar + " " + cnf.mostFqtChar + " " + cnf.numQuote;
//		str += " " + cnf.quoteProportion + " " + cnf.num3Clique + " " + cnf.avgDegree + " ";
//		str += cnf.graphDensity + " " + cnf.success;
//		out.println(str);
//	}
//	out.close();
	
	 public String readFile2(String filePath)
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
		List<ConversationalNetworkFeature> cq2 = new ArrayList<ConversationalNetworkFeature>();
		ConversationalNetworkFeature item = new ConversationalNetworkFeature();
	    
		item = new ConversationalNetworkFeature();
		item.numChar= 42;
		item.numSpeakingChar= 20;
		item.mostFqtChar= 89.48579831932773;
		item.numQuote= 2380;
		item.quoteProportion= 49.08280816341219;
		item.num3Clique= 736;
		item.avgDegree= 1.06;
		item.graphDensity= 0.2789473684210526;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 44;
		item.numSpeakingChar= 23;
		item.mostFqtChar= 53.49404312668463;
		item.numQuote= 742;
		item.quoteProportion= 27.42867762830927;
		item.num3Clique= 297;
		item.avgDegree= 1.0782608695652174;
		item.graphDensity= 0.2450592885375494;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 47;
		item.numSpeakingChar= 32;
		item.mostFqtChar= 53.736770025839796;
		item.numQuote= 774;
		item.quoteProportion= 52.801252926855746;
		item.num3Clique= 246;
		item.avgDegree= 0.70625;
		item.graphDensity= 0.11391129032258064;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 107;
		item.numSpeakingChar= 61;
		item.mostFqtChar= 34.63;
		item.numQuote= 5000;
		item.quoteProportion= 47.368058782028115;
		item.num3Clique= 1036;
		item.avgDegree= 1.062295081967213;
		item.graphDensity= 0.08852459016393442;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 40;
		item.numSpeakingChar= 21;
		item.mostFqtChar= 27.945271565495208;
		item.numQuote= 626;
		item.quoteProportion= 35.44398251785524;
		item.num3Clique= 162;
		item.avgDegree= 0.8952380952380953;
		item.graphDensity= 0.22380952380952382;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 74;
		item.numSpeakingChar= 36;
		item.mostFqtChar= 80.20238938053096;
		item.numQuote= 2825;
		item.quoteProportion= 41.326316694797725;
		item.num3Clique= 223;
		item.avgDegree= 0.5888888888888889;
		item.graphDensity= 0.08412698412698413;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 56;
		item.numSpeakingChar= 35;
		item.mostFqtChar= 85.71216097023152;
		item.numQuote= 1814;
		item.quoteProportion= 41.24502954991258;
		item.num3Clique= 288;
		item.avgDegree= 0.7885714285714286;
		item.graphDensity= 0.11596638655462185;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 24;
		item.numSpeakingChar= 7;
		item.mostFqtChar= 97.69491803278687;
		item.numQuote= 305;
		item.quoteProportion= 28.720287302844575;
		item.num3Clique= 22;
		item.avgDegree= 0.45714285714285713;
		item.graphDensity= 0.38095238095238093;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 44;
		item.numSpeakingChar= 27;
		item.mostFqtChar= 62.18081272084806;
		item.numQuote= 1132;
		item.quoteProportion= 45.31795774020736;
		item.num3Clique= 400;
		item.avgDegree= 1.037037037037037;
		item.graphDensity= 0.19943019943019943;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 57;
		item.numSpeakingChar= 30;
		item.mostFqtChar= 78.65574009728978;
		item.numQuote= 1439;
		item.quoteProportion= 50.203816671705766;
		item.num3Clique= 645;
		item.avgDegree= 1.1866666666666668;
		item.graphDensity= 0.2045977011494253;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 35;
		item.numSpeakingChar= 11;
		item.mostFqtChar= 94.5579012345679;
		item.numQuote= 1215;
		item.quoteProportion= 30.154122784484972;
		item.num3Clique= 54;
		item.avgDegree= 0.36363636363636365;
		item.graphDensity= 0.18181818181818182;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 104;
		item.numSpeakingChar= 45;
		item.mostFqtChar= 94.02556771545828;
		item.numQuote= 3655;
		item.quoteProportion= 42.42592441433352;
		item.num3Clique= 271;
		item.avgDegree= 0.5599999999999999;
		item.graphDensity= 0.06363636363636363;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 180;
		item.numSpeakingChar= 132;
		item.mostFqtChar= 40.331845140032954;
		item.numQuote= 4856;
		item.quoteProportion= 33.840051525425224;
		item.num3Clique= 1493;
		item.avgDegree= 1.0151515151515151;
		item.graphDensity= 0.03874624103631737;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 55;
		item.numSpeakingChar= 10;
		item.mostFqtChar= 94.25229508196722;
		item.numQuote= 122;
		item.quoteProportion= 45.4123973548434;
		item.num3Clique= 25;
		item.avgDegree= 0.52;
		item.graphDensity= 0.28888888888888886;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 130;
		item.numSpeakingChar= 46;
		item.mostFqtChar= 83.24705568268497;
		item.numQuote= 2622;
		item.quoteProportion= 22.672732488813534;
		item.num3Clique= 406;
		item.avgDegree= 0.6347826086956522;
		item.graphDensity= 0.07053140096618357;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 1;
		item.numSpeakingChar= 0;
		item.mostFqtChar= 0.1;
		item.numQuote= 0;
		item.quoteProportion= 0.0;
		item.num3Clique= 0;
		item.avgDegree= 0.02;
		item.graphDensity= 0.1;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 34;
		item.numSpeakingChar= 19;
		item.mostFqtChar= 76.2504208019055;
		item.numQuote= 2519;
		item.quoteProportion= 31.176427982087635;
		item.num3Clique= 365;
		item.avgDegree= 1.1789473684210525;
		item.graphDensity= 0.32748538011695905;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 33;
		item.numSpeakingChar= 23;
		item.mostFqtChar= 63.85898669396111;
		item.numQuote= 977;
		item.quoteProportion= 15.376723662437948;
		item.num3Clique= 187;
		item.avgDegree= 0.9565217391304348;
		item.graphDensity= 0.21739130434782608;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 114;
		item.numSpeakingChar= 41;
		item.mostFqtChar= 64.558345323741;
		item.numQuote= 1668;
		item.quoteProportion= 76.30968415021572;
		item.num3Clique= 527;
		item.avgDegree= 0.7317073170731707;
		item.graphDensity= 0.09146341463414634;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 104;
		item.numSpeakingChar= 40;
		item.mostFqtChar= 93.24640100973674;
		item.numQuote= 2773;
		item.quoteProportion= 38.15680237569722;
		item.num3Clique= 410;
		item.avgDegree= 0.6;
		item.graphDensity= 0.07692307692307693;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 20;
		item.numSpeakingChar= 13;
		item.mostFqtChar= 78.40121495327102;
		item.numQuote= 1070;
		item.quoteProportion= 51.28880266075388;
		item.num3Clique= 140;
		item.avgDegree= 0.5846153846153845;
		item.graphDensity= 0.24358974358974358;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 60;
		item.numSpeakingChar= 35;
		item.mostFqtChar= 81.129896373057;
		item.numQuote= 965;
		item.quoteProportion= 37.29166505091555;
		item.num3Clique= 263;
		item.avgDegree= 0.7428571428571429;
		item.graphDensity= 0.1092436974789916;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 60;
		item.numSpeakingChar= 36;
		item.mostFqtChar= 41.2009375;
		item.numQuote= 1536;
		item.quoteProportion= 35.88642130927305;
		item.num3Clique= 385;
		item.avgDegree= 1.0222222222222221;
		item.graphDensity= 0.14603174603174604;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 29;
		item.numSpeakingChar= 21;
		item.mostFqtChar= 57.46782002534855;
		item.numQuote= 1578;
		item.quoteProportion= 43.037491916271925;
		item.num3Clique= 290;
		item.avgDegree= 0.9142857142857143;
		item.graphDensity= 0.22857142857142856;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 31;
		item.numSpeakingChar= 24;
		item.mostFqtChar= 77.66653758542141;
		item.numQuote= 439;
		item.quoteProportion= 21.092629347947003;
		item.num3Clique= 106;
		item.avgDegree= 0.75;
		item.graphDensity= 0.16304347826086957;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 81;
		item.numSpeakingChar= 35;
		item.mostFqtChar= 95.73559518693597;
		item.numQuote= 2327;
		item.quoteProportion= 35.212323631654286;
		item.num3Clique= 291;
		item.avgDegree= 0.64;
		item.graphDensity= 0.09411764705882353;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 54;
		item.numSpeakingChar= 29;
		item.mostFqtChar= 84.06720144752714;
		item.numQuote= 2487;
		item.quoteProportion= 41.73215133033203;
		item.num3Clique= 516;
		item.avgDegree= 0.8;
		item.graphDensity= 0.14285714285714285;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 41;
		item.numSpeakingChar= 23;
		item.mostFqtChar= 71.83801381692574;
		item.numQuote= 1737;
		item.quoteProportion= 41.69552877041186;
		item.num3Clique= 405;
		item.avgDegree= 1.0782608695652174;
		item.graphDensity= 0.2450592885375494;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 32;
		item.numSpeakingChar= 23;
		item.mostFqtChar= 80.18431988041853;
		item.numQuote= 1338;
		item.quoteProportion= 40.35345939742977;
		item.num3Clique= 256;
		item.avgDegree= 0.8347826086956521;
		item.graphDensity= 0.18972332015810275;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 52;
		item.numSpeakingChar= 32;
		item.mostFqtChar= 57.51930568079351;
		item.numQuote= 1109;
		item.quoteProportion= 43.77322022380536;
		item.num3Clique= 381;
		item.avgDegree= 1.2125;
		item.graphDensity= 0.19556451612903225;
		item.success= false;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 57;
		item.numSpeakingChar= 36;
		item.mostFqtChar= 43.49649350649351;
		item.numQuote= 2772;
		item.quoteProportion= 29.80907489648831;
		item.num3Clique= 422;
		item.avgDegree= 0.8777777777777779;
		item.graphDensity= 0.1253968253968254;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 42;
		item.numSpeakingChar= 27;
		item.mostFqtChar= 78.85297376093294;
		item.numQuote= 2744;
		item.quoteProportion= 32.66323780046831;
		item.num3Clique= 228;
		item.avgDegree= 0.7703703703703704;
		item.graphDensity= 0.14814814814814814;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 36;
		item.numSpeakingChar= 25;
		item.mostFqtChar= 90.87514225500526;
		item.numQuote= 1898;
		item.quoteProportion= 36.7707696661373;
		item.num3Clique= 429;
		item.avgDegree= 0.8960000000000001;
		item.graphDensity= 0.18666666666666668;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 50;
		item.numSpeakingChar= 15;
		item.mostFqtChar= 97.54058572949947;
		item.numQuote= 939;
		item.quoteProportion= 25.9656888104136;
		item.num3Clique= 370;
		item.avgDegree= 0.72;
		item.graphDensity= 0.2571428571428571;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 89;
		item.numSpeakingChar= 50;
		item.mostFqtChar= 71.67897749928795;
		item.numQuote= 3511;
		item.quoteProportion= 42.44635025368805;
		item.num3Clique= 745;
		item.avgDegree= 1.04;
		item.graphDensity= 0.10612244897959183;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 43;
		item.numSpeakingChar= 31;
		item.mostFqtChar= 34.6393265211333;
		item.numQuote= 2153;
		item.quoteProportion= 29.82394902830286;
		item.num3Clique= 412;
		item.avgDegree= 0.8903225806451612;
		item.graphDensity= 0.14838709677419354;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 33;
		item.numSpeakingChar= 24;
		item.mostFqtChar= 89.4996921322691;
		item.numQuote= 1754;
		item.quoteProportion= 32.70661606329219;
		item.num3Clique= 406;
		item.avgDegree= 0.85;
		item.graphDensity= 0.18478260869565216;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 71;
		item.numSpeakingChar= 35;
		item.mostFqtChar= 86.10111111111111;
		item.numQuote= 3348;
		item.quoteProportion= 22.67724419455463;
		item.num3Clique= 667;
		item.avgDegree= 0.9028571428571428;
		item.graphDensity= 0.13277310924369748;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 42;
		item.numSpeakingChar= 32;
		item.mostFqtChar= 77.17282682103232;
		item.numQuote= 2073;
		item.quoteProportion= 38.6980771480947;
		item.num3Clique= 324;
		item.avgDegree= 0.8;
		item.graphDensity= 0.12903225806451613;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 64;
		item.numSpeakingChar= 35;
		item.mostFqtChar= 94.79757483200977;
		item.numQuote= 4911;
		item.quoteProportion= 33.17710902812038;
		item.num3Clique= 790;
		item.avgDegree= 0.8228571428571427;
		item.graphDensity= 0.12100840336134454;
		item.success= true;
		cq2.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 53;
		item.numSpeakingChar= 39;
		item.mostFqtChar= 86.9842196531792;
		item.numQuote= 2422;
		item.quoteProportion= 34.73834376632185;
		item.num3Clique= 588;
		item.avgDegree= 0.9025641025641026;
		item.graphDensity= 0.11875843454790823;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 41;
		item.numSpeakingChar= 27;
		item.mostFqtChar= 68.54739169271996;
		item.numQuote= 2239;
		item.quoteProportion= 41.35422088770591;
		item.num3Clique= 342;
		item.avgDegree= 0.9185185185185185;
		item.graphDensity= 0.17663817663817663;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 21;
		item.numSpeakingChar= 16;
		item.mostFqtChar= 46.6932967032967;
		item.numQuote= 728;
		item.quoteProportion= 31.50076146396189;
		item.num3Clique= 145;
		item.avgDegree= 0.8;
		item.graphDensity= 0.26666666666666666;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 30;
		item.numSpeakingChar= 22;
		item.mostFqtChar= 54.13141414141415;
		item.numQuote= 495;
		item.quoteProportion= 22.441335406025612;
		item.num3Clique= 176;
		item.avgDegree= 1.0363636363636364;
		item.graphDensity= 0.24675324675324675;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 55;
		item.numSpeakingChar= 39;
		item.mostFqtChar= 68.91934279543351;
		item.numQuote= 3241;
		item.quoteProportion= 34.24335108447577;
		item.num3Clique= 607;
		item.avgDegree= 1.2512820512820513;
		item.graphDensity= 0.16464237516869096;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 20;
		item.numSpeakingChar= 16;
		item.mostFqtChar= 53.869310344827596;
		item.numQuote= 232;
		item.quoteProportion= 20.332081157295708;
		item.num3Clique= 76;
		item.avgDegree= 0.675;
		item.graphDensity= 0.225;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 32;
		item.numSpeakingChar= 18;
		item.mostFqtChar= 96.09636277302944;
		item.numQuote= 1053;
		item.quoteProportion= 29.126083699026008;
		item.num3Clique= 98;
		item.avgDegree= 0.4666666666666667;
		item.graphDensity= 0.13725490196078433;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 38;
		item.numSpeakingChar= 17;
		item.mostFqtChar= 95.59853199498117;
		item.numQuote= 797;
		item.quoteProportion= 18.82010385156667;
		item.num3Clique= 43;
		item.avgDegree= 0.47058823529411764;
		item.graphDensity= 0.14705882352941177;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 19;
		item.numSpeakingChar= 9;
		item.mostFqtChar= 98.96119341563785;
		item.numQuote= 486;
		item.quoteProportion= 33.21206827648433;
		item.num3Clique= 22;
		item.avgDegree= 0.3555555555555555;
		item.graphDensity= 0.2222222222222222;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 36;
		item.numSpeakingChar= 28;
		item.mostFqtChar= 44.87330341113106;
		item.numQuote= 1114;
		item.quoteProportion= 35.899179894697696;
		item.num3Clique= 290;
		item.avgDegree= 1.2714285714285714;
		item.graphDensity= 0.23544973544973544;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 31;
		item.numSpeakingChar= 22;
		item.mostFqtChar= 75.8030081300813;
		item.numQuote= 1476;
		item.quoteProportion= 26.416167546952202;
		item.num3Clique= 130;
		item.avgDegree= 0.6545454545454545;
		item.graphDensity= 0.15584415584415584;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 96;
		item.numSpeakingChar= 52;
		item.mostFqtChar= 43.10270125223614;
		item.numQuote= 2795;
		item.quoteProportion= 20.62129077794994;
		item.num3Clique= 597;
		item.avgDegree= 0.976923076923077;
		item.graphDensity= 0.09577677224736049;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 1;
		item.numSpeakingChar= 1;
		item.mostFqtChar= 99.99;
		item.numQuote= 15;
		item.quoteProportion= 0.8033878043097248;
		item.num3Clique= 0;
		item.avgDegree= 0.0;
		item.graphDensity= 0.1;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 47;
		item.numSpeakingChar= 31;
		item.mostFqtChar= 64.08872849663426;
		item.numQuote= 1337;
		item.quoteProportion= 25.24951444941104;
		item.num3Clique= 182;
		item.avgDegree= 0.6193548387096774;
		item.graphDensity= 0.1032258064516129;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 52;
		item.numSpeakingChar= 36;
		item.mostFqtChar= 45.16096580683863;
		item.numQuote= 1667;
		item.quoteProportion= 27.952776413507763;
		item.num3Clique= 325;
		item.avgDegree= 0.8666666666666666;
		item.graphDensity= 0.12380952380952381;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 22;
		item.numSpeakingChar= 6;
		item.mostFqtChar= 81.00153504880213;
		item.numQuote= 1127;
		item.quoteProportion= 23.33186536138372;
		item.num3Clique= 147;
		item.avgDegree= 0.5333333333333333;
		item.graphDensity= 0.5333333333333333;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 16;
		item.numSpeakingChar= 7;
		item.mostFqtChar= 90.13598540145985;
		item.numQuote= 274;
		item.quoteProportion= 24.455970568728976;
		item.num3Clique= 28;
		item.avgDegree= 0.6285714285714286;
		item.graphDensity= 0.5238095238095238;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 47;
		item.numSpeakingChar= 35;
		item.mostFqtChar= 70.65831683168317;
		item.numQuote= 2424;
		item.quoteProportion= 23.263727129710524;
		item.num3Clique= 438;
		item.avgDegree= 1.1199999999999999;
		item.graphDensity= 0.16470588235294117;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 71;
		item.numSpeakingChar= 33;
		item.mostFqtChar= 92.24621414913958;
		item.numQuote= 3138;
		item.quoteProportion= 29.32203623858778;
		item.num3Clique= 304;
		item.avgDegree= 0.5575757575757576;
		item.graphDensity= 0.08712121212121213;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 104;
		item.numSpeakingChar= 54;
		item.mostFqtChar= 81.76056050288108;
		item.numQuote= 1909;
		item.quoteProportion= 28.589151662720848;
		item.num3Clique= 297;
		item.avgDegree= 0.6518518518518518;
		item.graphDensity= 0.061495457721872815;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 70;
		item.numSpeakingChar= 39;
		item.mostFqtChar= 74.22214998005583;
		item.numQuote= 2507;
		item.quoteProportion= 48.45568977073106;
		item.num3Clique= 638;
		item.avgDegree= 1.0666666666666667;
		item.graphDensity= 0.14035087719298245;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 41;
		item.numSpeakingChar= 13;
		item.mostFqtChar= 91.01941176470588;
		item.numQuote= 680;
		item.quoteProportion= 21.879239395857827;
		item.num3Clique= 162;
		item.avgDegree= 0.7692307692307693;
		item.graphDensity= 0.32051282051282054;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 37;
		item.numSpeakingChar= 18;
		item.mostFqtChar= 93.46181008902076;
		item.numQuote= 1348;
		item.quoteProportion= 33.61894653661711;
		item.num3Clique= 129;
		item.avgDegree= 0.5555555555555556;
		item.graphDensity= 0.16339869281045752;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 55;
		item.numSpeakingChar= 35;
		item.mostFqtChar= 80.93713656387665;
		item.numQuote= 908;
		item.quoteProportion= 70.34351048703948;
		item.num3Clique= 223;
		item.avgDegree= 0.6971428571428572;
		item.graphDensity= 0.10252100840336134;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 25;
		item.numSpeakingChar= 18;
		item.mostFqtChar= 78.52164556962025;
		item.numQuote= 1975;
		item.quoteProportion= 36.126942669099776;
		item.num3Clique= 447;
		item.avgDegree= 0.888888888888889;
		item.graphDensity= 0.26143790849673204;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 43;
		item.numSpeakingChar= 29;
		item.mostFqtChar= 69.65731898238747;
		item.numQuote= 2044;
		item.quoteProportion= 46.40655156978731;
		item.num3Clique= 448;
		item.avgDegree= 1.0206896551724138;
		item.graphDensity= 0.18226600985221675;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 96;
		item.numSpeakingChar= 59;
		item.mostFqtChar= 77.16842323651453;
		item.numQuote= 2169;
		item.quoteProportion= 54.61062679265225;
		item.num3Clique= 599;
		item.avgDegree= 0.888135593220339;
		item.graphDensity= 0.07656341320864991;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 26;
		item.numSpeakingChar= 10;
		item.mostFqtChar= 99.15201117318436;
		item.numQuote= 1790;
		item.quoteProportion= 55.183391130437975;
		item.num3Clique= 443;
		item.avgDegree= 0.6799999999999999;
		item.graphDensity= 0.37777777777777777;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 151;
		item.numSpeakingChar= 84;
		item.mostFqtChar= 45.45684709066306;
		item.numQuote= 1478;
		item.quoteProportion= 38.97310910072861;
		item.num3Clique= 438;
		item.avgDegree= 1.0285714285714287;
		item.graphDensity= 0.06196213425129088;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 25;
		item.numSpeakingChar= 17;
		item.mostFqtChar= 54.33298440979956;
		item.numQuote= 449;
		item.quoteProportion= 28.025184031034865;
		item.num3Clique= 70;
		item.avgDegree= 0.6352941176470588;
		item.graphDensity= 0.19852941176470587;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 50;
		item.numSpeakingChar= 27;
		item.mostFqtChar= 59.614413145539906;
		item.numQuote= 1065;
		item.quoteProportion= 36.83591591427019;
		item.num3Clique= 368;
		item.avgDegree= 1.0814814814814815;
		item.graphDensity= 0.20797720797720798;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 35;
		item.numSpeakingChar= 27;
		item.mostFqtChar= 70.03341534008683;
		item.numQuote= 2073;
		item.quoteProportion= 26.06844043983732;
		item.num3Clique= 273;
		item.avgDegree= 0.9185185185185185;
		item.graphDensity= 0.17663817663817663;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 27;
		item.numSpeakingChar= 18;
		item.mostFqtChar= 67.11328767123287;
		item.numQuote= 1022;
		item.quoteProportion= 10.748273430416727;
		item.num3Clique= 191;
		item.avgDegree= 1.1777777777777778;
		item.graphDensity= 0.3464052287581699;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 37;
		item.numSpeakingChar= 23;
		item.mostFqtChar= 61.013821591485055;
		item.numQuote= 1973;
		item.quoteProportion= 29.29771433175763;
		item.num3Clique= 433;
		item.avgDegree= 1.008695652173913;
		item.graphDensity= 0.22924901185770752;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 10;
		item.numSpeakingChar= 7;
		item.mostFqtChar= 90.48459041731066;
		item.numQuote= 1294;
		item.quoteProportion= 30.070850744422142;
		item.num3Clique= 37;
		item.avgDegree= 0.5142857142857143;
		item.graphDensity= 0.42857142857142855;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 49;
		item.numSpeakingChar= 32;
		item.mostFqtChar= 66.21073578595318;
		item.numQuote= 897;
		item.quoteProportion= 25.263571056931347;
		item.num3Clique= 162;
		item.avgDegree= 0.8;
		item.graphDensity= 0.12903225806451613;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 56;
		item.numSpeakingChar= 27;
		item.mostFqtChar= 89.68411764705881;
		item.numQuote= 2125;
		item.quoteProportion= 46.04150562979988;
		item.num3Clique= 227;
		item.avgDegree= 0.5185185185185185;
		item.graphDensity= 0.09971509971509972;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 18;
		item.numSpeakingChar= 5;
		item.mostFqtChar= 99.99;
		item.numQuote= 49;
		item.quoteProportion= 92.91240502811317;
		item.num3Clique= 15;
		item.avgDegree= 0.4;
		item.graphDensity= 0.5;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 131;
		item.numSpeakingChar= 88;
		item.mostFqtChar= 60.28697900665643;
		item.numQuote= 9765;
		item.quoteProportion= 49.86136417650321;
		item.num3Clique= 1748;
		item.avgDegree= 1.1863636363636363;
		item.graphDensity= 0.06818181818181818;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 77;
		item.numSpeakingChar= 57;
		item.mostFqtChar= 70.73182153279066;
		item.numQuote= 6511;
		item.quoteProportion= 38.99241956479743;
		item.num3Clique= 938;
		item.avgDegree= 0.9473684210526315;
		item.graphDensity= 0.08458646616541353;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 48;
		item.numSpeakingChar= 40;
		item.mostFqtChar= 63.877542413960256;
		item.numQuote= 2063;
		item.quoteProportion= 50.223278573111735;
		item.num3Clique= 361;
		item.avgDegree= 1.3599999999999999;
		item.graphDensity= 0.17435897435897435;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 37;
		item.numSpeakingChar= 28;
		item.mostFqtChar= 73.70505861136158;
		item.numQuote= 2218;
		item.quoteProportion= 44.64726714473036;
		item.num3Clique= 510;
		item.avgDegree= 1.3;
		item.graphDensity= 0.24074074074074073;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 46;
		item.numSpeakingChar= 26;
		item.mostFqtChar= 28.922038834951454;
		item.numQuote= 1030;
		item.quoteProportion= 45.26095161119083;
		item.num3Clique= 410;
		item.avgDegree= 1.2461538461538462;
		item.graphDensity= 0.24923076923076923;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 41;
		item.numSpeakingChar= 11;
		item.mostFqtChar= 67.78089376053963;
		item.numQuote= 593;
		item.quoteProportion= 23.134341178491667;
		item.num3Clique= 215;
		item.avgDegree= 0.5454545454545454;
		item.graphDensity= 0.2727272727272727;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 35;
		item.numSpeakingChar= 26;
		item.mostFqtChar= 28.035157456922164;
		item.numQuote= 1683;
		item.quoteProportion= 34.19457770284186;
		item.num3Clique= 446;
		item.avgDegree= 1.169230769230769;
		item.graphDensity= 0.23384615384615384;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 71;
		item.numSpeakingChar= 37;
		item.mostFqtChar= 78.71823618470854;
		item.numQuote= 1321;
		item.quoteProportion= 57.42783691704833;
		item.num3Clique= 275;
		item.avgDegree= 0.6378378378378378;
		item.graphDensity= 0.08858858858858859;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 39;
		item.numSpeakingChar= 22;
		item.mostFqtChar= 82.57823529411765;
		item.numQuote= 850;
		item.quoteProportion= 23.971535107380088;
		item.num3Clique= 197;
		item.avgDegree= 0.7454545454545454;
		item.graphDensity= 0.1774891774891775;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 6;
		item.numSpeakingChar= 4;
		item.mostFqtChar= 99.99;
		item.numQuote= 79;
		item.quoteProportion= 30.484532240029818;
		item.num3Clique= 12;
		item.avgDegree= 0.3;
		item.graphDensity= 0.5;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 54;
		item.numSpeakingChar= 27;
		item.mostFqtChar= 65.83507042253521;
		item.numQuote= 568;
		item.quoteProportion= 23.87962022819031;
		item.num3Clique= 162;
		item.avgDegree= 0.7555555555555555;
		item.graphDensity= 0.1452991452991453;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 79;
		item.numSpeakingChar= 46;
		item.mostFqtChar= 59.14300546448088;
		item.numQuote= 1464;
		item.quoteProportion= 61.46579995315062;
		item.num3Clique= 313;
		item.avgDegree= 0.7652173913043478;
		item.graphDensity= 0.08502415458937199;
		item.success= false;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 139;
		item.numSpeakingChar= 83;
		item.mostFqtChar= 60.87078967350038;
		item.numQuote= 6585;
		item.quoteProportion= 27.51657880888211;
		item.num3Clique= 1277;
		item.avgDegree= 0.925301204819277;
		item.graphDensity= 0.05642080517190714;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 23;
		item.numSpeakingChar= 21;
		item.mostFqtChar= 31.194379562043792;
		item.numQuote= 548;
		item.quoteProportion= 14.277738571316553;
		item.num3Clique= 123;
		item.avgDegree= 0.8571428571428571;
		item.graphDensity= 0.21428571428571427;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 104;
		item.numSpeakingChar= 70;
		item.mostFqtChar= 78.7453991361382;
		item.numQuote= 6251;
		item.quoteProportion= 34.96329402714704;
		item.num3Clique= 1089;
		item.avgDegree= 1.1828571428571428;
		item.graphDensity= 0.08571428571428572;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 10;
		item.numSpeakingChar= 4;
		item.mostFqtChar= 99.99;
		item.numQuote= 46;
		item.quoteProportion= 4.828196058615462;
		item.num3Clique= 6;
		item.avgDegree= 0.4;
		item.graphDensity= 0.6666666666666666;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 60;
		item.numSpeakingChar= 46;
		item.mostFqtChar= 33.02679741205014;
		item.numQuote= 2473;
		item.quoteProportion= 29.042071891000425;
		item.num3Clique= 678;
		item.avgDegree= 1.2260869565217392;
		item.graphDensity= 0.13623188405797101;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 48;
		item.numSpeakingChar= 23;
		item.mostFqtChar= 68.6064229471316;
		item.numQuote= 889;
		item.quoteProportion= 45.42270112643249;
		item.num3Clique= 224;
		item.avgDegree= 1.0434782608695652;
		item.graphDensity= 0.23715415019762845;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 93;
		item.numSpeakingChar= 47;
		item.mostFqtChar= 59.99;
		item.numQuote= 1540;
		item.quoteProportion= 27.19332906407968;
		item.num3Clique= 275;
		item.avgDegree= 0.7489361702127659;
		item.graphDensity= 0.08140610545790934;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 96;
		item.numSpeakingChar= 55;
		item.mostFqtChar= 83.84889898450025;
		item.numQuote= 1871;
		item.quoteProportion= 24.994132233935076;
		item.num3Clique= 460;
		item.avgDegree= 0.8727272727272727;
		item.graphDensity= 0.08080808080808081;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 45;
		item.numSpeakingChar= 31;
		item.mostFqtChar= 72.12647441104792;
		item.numQuote= 2462;
		item.quoteProportion= 51.66804270116332;
		item.num3Clique= 463;
		item.avgDegree= 0.864516129032258;
		item.graphDensity= 0.14408602150537633;
		item.success= true;
		cq.add(item);

		item = new ConversationalNetworkFeature();
		item.numChar= 21;
		item.numSpeakingChar= 13;
		item.mostFqtChar= 72.93964028776978;
		item.numQuote= 695;
		item.quoteProportion= 36.27376725062216;
		item.num3Clique= 92;
		item.avgDegree= 0.7692307692307693;
		item.graphDensity= 0.32051282051282054;
		item.success= true;
		cq.add(item);
	    
	    //List<CandidateQuote>, Range, RangeLength
	    AJ48 att = new AJ48(cq,cq2,10000,10);
	    att.Create();
	    //att.ShowData();
	}
}
