

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.stanford.nlp.pipeline.Annotation;

public class SVMTrain {
	
	private ConversationalNetworkFeature cnf;
	private List<String> fileTrain;
	private List<String> fileTest;
	private List<ConversationalNetworkFeature> test;
	private List<ConversationalNetworkFeature> train;
	public void Train() throws Exception
	{
		String filePath = new File("DataSet/novels/Love_Stories").getAbsolutePath();
		
		for(int i = 1; i <=1;i++)
		{
			String fold = "fold" + Integer.toString(i);
			fileTrain = new ArrayList<String>();
			fileTest = new ArrayList<String>();
			test = new ArrayList<ConversationalNetworkFeature>();
			train= new ArrayList<ConversationalNetworkFeature>();
			listf(filePath, fileTrain, fold,true);
			listf(filePath, fileTest, fold,false);
			
			for(String item:fileTrain)
			{
//				System.out.println(item);
				train.add(featureExtraction(item, item.contains("success")));
				System.out.println();
			}
			
			for(String item:fileTest)
			{
//				System.out.println(item);
				test.add(featureExtraction(item, item.contains("success")));
				System.out.println();
			}
			
//			AJ48 aj48 = new AJ48(train,test,200,5);
//			aj48.Create();
//			aj48.ShowData();
			
			PrintWriter out = new PrintWriter("filename.txt");
			for(ConversationalNetworkFeature cnf:test)
			{
				System.out.println("cnf.numChar: " + cnf.numChar);
				System.out.println("cnf.numSpeakingChar: " + cnf.numSpeakingChar);
				
				System.out.println("cnf.MostFqtChar: " + cnf.mostFqtChar);
				
				System.out.println("cnf.numQuote: " + cnf.numQuote);
				System.out.println("cnf.quoteProportion: " + cnf.quoteProportion);
				
				System.out.println("cnf.num3Clique: " + cnf.num3Clique);
				//System.out.println("cnf.num4Clique: " + cnf.num4Clique);
				
				System.out.println("cnf.avgDegree: " + cnf.avgDegree);
				
				System.out.println("cnf.graphDensity: " + cnf.graphDensity);
				
				System.out.println("cnf.graphDensity: " + cnf.success);
				
				String str = cnf.numChar + " " + cnf.numSpeakingChar + " " + cnf.mostFqtChar + " " + cnf.numQuote;
				str += " " + cnf.quoteProportion + " " + cnf.num3Clique + " " + cnf.avgDegree + " ";
				str += cnf.graphDensity + " " + cnf.success;
				out.println(str);
				
			}
			
			for(ConversationalNetworkFeature cnf:train)
			{
				System.out.println("cnf.numChar: " + cnf.numChar);
				System.out.println("cnf.numSpeakingChar: " + cnf.numSpeakingChar);
				
				System.out.println("cnf.MostFqtChar: " + cnf.mostFqtChar);
				
				System.out.println("cnf.numQuote: " + cnf.numQuote);
				System.out.println("cnf.quoteProportion: " + cnf.quoteProportion);
				
				System.out.println("cnf.num3Clique: " + cnf.num3Clique);
				//System.out.println("cnf.num4Clique: " + cnf.num4Clique);
				
				System.out.println("cnf.avgDegree: " + cnf.avgDegree);
				
				System.out.println("cnf.graphDensity: " + cnf.graphDensity);
				
				System.out.println("cnf.graphDensity: " + cnf.success);
				
				String str = cnf.numChar + " " + cnf.numSpeakingChar + " " + cnf.mostFqtChar + " " + cnf.numQuote;
				str += " " + cnf.quoteProportion + " " + cnf.num3Clique + " " + cnf.avgDegree + " ";
				str += cnf.graphDensity + " " + cnf.success;
				out.println(str);
			}
			out.close();
		}
	}
	
	private ConversationalNetworkFeature featureExtraction(String fileName, boolean success) throws Exception
	{
	 	PrintWriter out;
	    out = new PrintWriter(System.out, true);	
	    String text = readFile(fileName);
	    Annotation input = new Annotation(text);
	    //System.out.println(input);
	    
	    QuotedSpeechAttribution qsa = new QuotedSpeechAttribution(input, out, false);
	    qsa.preprocessing();
	    Vector<Quote> list = qsa.getAllAttributedQuotes();
	    
		SNFeatureExtraction snfe = new SNFeatureExtraction(list, text, qsa.names, success);
		cnf = snfe.ConstructNetwork();
		return cnf;
	    
//	    Iterator<Quote> it = list.iterator();
//	    int cnt = 0;		  
//	    while(it.hasNext())
//	    {
//	    	Quote q= it.next();
//	    	System.out.println(++cnt + ":" + "[" + q.speaker.name + "]" + "=>" + q.text);
//	    }
	}
	
	public void listf(String directoryName, List<String> files, String fold, boolean train) {	
		//boolean matches = false;
	    File[] fList = new File(directoryName).listFiles();
	    for (File file : fList) {
//	    	System.out.println(directoryName + "\\" + file.getName());
//	    	System.out.println(files.size());
	        if (file.isFile()){
	        	if((train==true)?!directoryName.contains(fold):directoryName.contains(fold))
	        		//if((success==true)?directoryName.contains("success"):directoryName.contains("failure")) 
	        		files.add(directoryName + "\\" + file.getName());
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath(), files, fold, train);
	        }
	    }
	}

	
	public String readFile(String filePath)
	{
		//System.out.println(filePath);
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
}
