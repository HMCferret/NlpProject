

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
	private List<String> files;
	private List<ConversationalNetworkFeature> data;
	private List<ConversationalNetworkFeature> testData;
	private List<ConversationalNetworkFeature> trainData;
	public void Train(String link) throws Exception
	{
		PrintWriter out = new PrintWriter(link+".txt");
		long startTime = System.currentTimeMillis();
		double sum = 0;
		String path = "DataSet/novels/"+link;
		System.out.println(path);
		String filePath = new File(path).getAbsolutePath();
		files = new ArrayList<String>();
		listf(filePath, files);
		
		data = new ArrayList<ConversationalNetworkFeature>();
		
		for(String item:files)
		{
			data.add(featureExtraction(item, item.contains("success")));
		}
		
		for(int i = 1; i <=5;i++)
		{
			getData(i);
			
			AJ48 aj48 = new AJ48(trainData,testData,10000,10);
			aj48.Create();
			sum+=aj48.getResult();
			out.println("Round " + i + ": " + aj48.getResult());
			//aj48.ShowData();			

		}
		
		System.out.println("Result: " + String.format("%.2f", sum/5) +"%");
		out.println("Result: " + String.format("%.2f", sum/5) +"%");
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time: " + totalTime+"ms");
		out.println("Time: " + totalTime+"ms");
		System.out.println();
		out.close();
	}
	
	private void getData(int t)
	{
		testData = new ArrayList<ConversationalNetworkFeature>();
		trainData = new ArrayList<ConversationalNetworkFeature>();
		for(int i=0;i<100;i++)
		{
			trainData.add(data.get(i));
		}
		
		for(int i = (t - 1)*20; i < t*20;i++)
		{
			testData.add(data.get(i));
			trainData.remove(data.get(i));
		}
	}
	
	private ConversationalNetworkFeature featureExtraction(String fileName, boolean success) throws Exception
	{
	 	PrintWriter out;
	    out = new PrintWriter(System.out, true);	
	    String text = readFile(fileName);
	    Annotation input = new Annotation(text);
	    
	    QuotedSpeechAttribution qsa = new QuotedSpeechAttribution(input, out, false);
	    qsa.preprocessing();
	    Vector<Quote> list = qsa.getAllAttributedQuotes();
	    
		SNFeatureExtraction snfe = new SNFeatureExtraction(list, text, qsa.names, success);
		cnf = snfe.ConstructNetwork();
		return cnf;
	}
	
	public void listf(String directoryName, List<String> files){
		//boolean matches = false;
	    File[] fList = new File(directoryName).listFiles();
	    for (File file : fList) {
	        if (file.isFile()){
	        		files.add(directoryName + "\\" + file.getName());
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath(), files);
	        }
	    }
	}

	
	public String readFile(String filePath)
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
}
