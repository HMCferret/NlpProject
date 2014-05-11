import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Demo {
	
	private List<ConversationalNetworkFeature> lcnf = new ArrayList<ConversationalNetworkFeature>();
	private ConversationalNetworkFeature cnf;
	public void Check() throws Exception
	{
		String filePath = new File("data - Copy.txt").getAbsolutePath();
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
		
//		AJ48 result = new AJ48(lcnf,lcnf,200,5);
//		result.Create();
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
}
