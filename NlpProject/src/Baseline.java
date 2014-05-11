import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Baseline {
	
	private List<String> filesTestNeg;
	private List<String> filesTestPos;
	private List<String> filesNeg;
	private List<String> filesPos;
	public void Run()
	{
		double proUniGram=0;
		double proBiGram=0;
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("***** Unigram*****");
		
		//run 5 trials for unigram
		for(int i=1;i<=5;i++)
		{
			System.out.println("Round " + i);
			proUniGram+=LaplaceUniGram(i);
		}
		System.out.println("Result: " + proUniGram/5 + "%");
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time: " + totalTime+"ms");
		System.out.println();
		
		startTime = System.currentTimeMillis();
		System.out.println("*****Bigram*****");
		
		//run 5 trials for bigram
		for(int i=1;i<=5;i++)
		{
			System.out.println("Round " + i);
			proBiGram+=LaplaceBiGram(i);
		}
		System.out.println("Result: " + proBiGram/5 + "%");
		endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Time: " + totalTime+"ms");
	}
	
	public double LaplaceUniGram(int a)
	{
		filesTestNeg = new ArrayList<String>();
		filesTestPos = new ArrayList<String>();
		filesNeg = new ArrayList<String>();
		filesPos = new ArrayList<String>();
		
		String filePath = new File("DataSet/novels/Love_Stories").getAbsolutePath();
		String fold = "fold" + Integer.toString(a);
		
		//get list of files for testing in dataset. Neg means list file in neg folder. Pos means list file in pos folder
		listf(filePath, filesTestNeg, fold, false, false);
		listf(filePath,filesTestPos, fold, false, true);
		
		//get list of files for training in dataset
		listf(filePath,filesNeg,fold,true,false);
		listf(filePath,filesPos,fold,true,true);
		
		//count frequency of each word in training set
		Map<String, Integer> mapCountNeg = countWord(filesNeg);
		Map<String, Integer> mapCountPos = countWord(filesPos);
		
		//compute vocabulary size and corpus size of training set
		int vocabSizeNeg = mapCountNeg.size();
		int corpusSizeNeg = getCorpusSize(mapCountNeg);
		
		int vocabSizePos = mapCountPos.size();
		int corpusSizePos = getCorpusSize(mapCountPos);
		System.out.println("Vocabulary size neg: " + vocabSizeNeg);
		System.out.println("Corpus size neg: " + corpusSizeNeg);
		System.out.println("Vocabulary size pos: " + vocabSizePos);
		System.out.println("Corpus size pos: " + corpusSizePos);
		
		//compute unigram
		Map<String, Double> mapUniGramNeg = UniGram(mapCountNeg,vocabSizeNeg,corpusSizeNeg);
		Map<String, Double> mapUniGramPos = UniGram(mapCountPos,vocabSizePos,corpusSizePos);
		
		//unigram for words not existing in training set 
		double unkNeg = 1.0/(vocabSizeNeg+corpusSizeNeg);
		double unkPos = 1.0/(vocabSizePos+corpusSizePos);
		
		//compute perplexity. negNeg means a review from neg class is testing under neg training set. Similar to the others one.
		double[] negNeg = PerplexityUniGram(filesTestNeg,mapUniGramNeg,unkNeg);
		double[] negPos = PerplexityUniGram(filesTestNeg,mapUniGramPos,unkPos);
		double[] posNeg = PerplexityUniGram(filesTestPos,mapUniGramNeg,unkNeg);
		double[] posPos = PerplexityUniGram(filesTestPos,mapUniGramPos,unkPos);
		
		//compute result
		double result = TestingData(negNeg,negPos,posNeg,posPos,a)*100;
		System.out.println("Efficiency: " + result + "%");
		System.out.println();
		return result;
	}
	
	/*	Main method using laplace add-one for bigram
		Input: position of the testing fold
		Output: probability of correct results for each trial*/
	public double LaplaceBiGram(int a)
	{
		filesTestNeg = new ArrayList<String>();
		filesTestPos = new ArrayList<String>();
		filesNeg = new ArrayList<String>();
		filesPos = new ArrayList<String>();
		
		String filePath = new File("DataSet/novels/Love_Stories").getAbsolutePath();
		String fold = "fold" + Integer.toString(a);
		
		//get list of files for testing in dataset. Neg means list file in neg folder. Pos means list file in pos folder
		listf(filePath, filesTestNeg, fold, false, false);
		listf(filePath,filesTestPos, fold, false, true);
		
		//get list of files for training in dataset
		listf(filePath,filesNeg,fold,true,false);
		listf(filePath,filesPos,fold,true,true);
		
		Map<String, Integer> mapCountNeg = countWord(filesNeg);
		Map<String, Integer> mapCountPos = countWord(filesPos);
		
		//count frequency of each 2-word in training set
		Map<String, Integer> mapCountNegBiGram = countWordBiGram(filesNeg);
		Map<String, Integer> mapCountPosBiGram = countWordBiGram(filesPos);
		
		int vocabSizeNeg = mapCountNegBiGram.size();
		int corpusSizeNeg = getCorpusSize(mapCountNegBiGram);
		
		int vocabSizePos = mapCountPosBiGram.size();
		int corpusSizePos = getCorpusSize(mapCountPosBiGram);
		
		System.out.println("Vocabulary size neg: " + vocabSizeNeg);
		System.out.println("Corpus size neg: " + corpusSizeNeg);
		System.out.println("Vocabulary size pos: " + vocabSizePos);
		System.out.println("Corpus size pos: " + corpusSizePos);
		
		Map<String, Double> mapBiGramNeg = BiGram(mapCountNeg,vocabSizeNeg,mapCountNegBiGram);
		Map<String, Double> mapBiGramPos = BiGram(mapCountPos,vocabSizePos,mapCountPosBiGram);
		
		double unkNeg = 1.0/(vocabSizeNeg+corpusSizeNeg);
		double unkPos = 1.0/(vocabSizePos+corpusSizePos);
		
		double[] negNeg = PerplexityBiGram(filesTestNeg,mapBiGramNeg,unkNeg);
		double[] negPos = PerplexityBiGram(filesTestNeg,mapBiGramPos,unkPos);
		double[] posNeg = PerplexityBiGram(filesTestPos,mapBiGramNeg,unkNeg);
		double[] posPos = PerplexityBiGram(filesTestPos,mapBiGramPos,unkPos);
		
		double result = TestingData(negNeg,negPos,posNeg,posPos,a)*100;
		System.out.println("Efficiency: " + result + "%");
		System.out.println();
		
		return result;
	}
	
	/*	Compute perplexity for unigram
	 * Input:
	 * 		- results: list of data files
	 * 		- mapUniGram: value of each feature 
	 * 		- unk: unigram for words not existing in training set
	 * Output:
	 * 		- a double array of perplexity value for unigram*/ 
	public double[] PerplexityUniGram(List<String> results, Map<String, Double> mapUniGram, double unk){
		double[] prob = new double[200];
		int index=0;
		for(String result:results)
		{
			double p=1.0;
			StringBuilder str = new StringBuilder();
			str.append(readFile(result));
			String[] splited = str.toString().split("\\s+");
			for(String item:splited)
			{
				Double f = mapUniGram.get(item);
				if(f==null)
				{
					p*=Math.pow(unk,-1.0/splited.length);
				}
				else
				{
					p*=Math.pow(f,-1.0/splited.length);
				}	
			}
			prob[index++]=p;
		}
		return prob;
	}
	
	/*	Compute perplexity for bigram
	 * Input:
	 * 		- results: list of data files
	 * 		- mapUniGram: value of each feature 
	 * 		- unk: unigram for words not existing in training set
	 * Output:
	 * 		- a double array of perplexity value for unigram*/ 
	public double[] PerplexityBiGram(List<String> results, Map<String, Double> mapBiGram, double unk){
		double[] prob = new double[200];
		int index=0;
		
		for(String result:results)
		{
			double p=1.0;
			StringBuilder str = new StringBuilder();
			str.append(readFile(result));
			String[] splited = str.toString().split("\\s+");
			for(int i =0;i<splited.length-1;i++)
			{
				String item = splited[i] + " " + splited[i+1];
				Double f = mapBiGram.get(item);
				if(f==null)
				{
					p*=Math.pow(unk,-1.0/splited.length);
				}
				else
				{
					p*=Math.pow(f,-1.0/splited.length);
				}	
			}
			prob[index]=p;
			index++;
		}
		return prob;
	}
	
	//Get the corpus size
	public int getCorpusSize(Map<String, Integer> mapCount){
		Iterator<?> it = mapCount.entrySet().iterator();
		int sum = 0;
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        sum +=(Integer)pairs.getValue();
	    }
	    
	    return sum;
	}
	
	//count frequency of each word in training set for unigram
	public Map<String, Integer> countWord(List<String> results){
		
		Map<String, Integer> mapCount = new HashMap<String, Integer>();
		StringBuilder str = new StringBuilder();
		for(String item:results)
		{
			str.append(readFile(item));
		}

		String[] splited = str.toString().split("\\s+");
		for(String item:splited)
		{
			Integer f = mapCount.get(item);
			if(f==null)
			{
				mapCount.put(item,1);
			}
			else
			{
				mapCount.put(item, ++f);
			}
		}
		return mapCount;
	}
	
	//count frequency of each 2-word in training set for bigram
	public Map<String, Integer> countWordBiGram(List<String> results)
	{
		Map<String, Integer> mapCount = new HashMap<String, Integer>();
		StringBuilder str = new StringBuilder();
		for(String item:results)
		{
			str.append(readFile(item));
		}

		String[] splited = str.toString().split("\\s+");

			for(int i =0;i<splited.length-1;i++){
					String item = splited[i] + " " + splited[i+1];
					Integer f = mapCount.get(item);
					if(f==null)
					{
						mapCount.put(item,1);
					}
					else
					{
						mapCount.put(item, ++f);
					}
			}
		return mapCount;
	}
	
	/*Get the list of file path
	 * Input:
	 * 		- filePath: list of absolute path of file directory
	 * 		- a, b: to identify the file name
	 * Output:
	 * 		- return list of file path*/
	public List<String> getListFiles(String filePath,int a, int b)
	{
		File[] files = new File(filePath).listFiles();
		List<String> results = new ArrayList<String>();
		for (File file : files) {
			String fileName = file.getName();
			boolean matches=false;
			for(int i = a;i<=b;i++){
				//matches: regular expression to identify files
				matches |=fileName.startsWith("cv"+Integer.toString(i)); 
			}
		    if (file.isFile() && matches) {
		        results.add(filePath+"\\"+file.getName());
		    }
		}
		return results;
	}
	
	//output string from file
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
	
	//compute a list of unigram p(word) in corpus
	public Map<String, Double> UniGram(Map<String, Integer> mapCount, int vocabSize, int wordNumber)
	{
		Map<String, Double> map = new HashMap<String, Double>();
		Iterator<?> it = mapCount.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry item = (Map.Entry)it.next();			
			double value = ((Integer)item.getValue()+1.0)/(vocabSize+wordNumber);
			map.put(item.getKey().toString(), value);
			//it.remove();//testing
		}
		return map;
	}
	
	public Map<String, Double> BiGram(Map<String, Integer> mapCount, int vocabSize, Map<String, Integer> mapCountBiGram)
	{
		Map<String, Double> map = new HashMap<String, Double>();
		Iterator<?> it = mapCountBiGram.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry item = (Map.Entry)it.next();
			int wordNumber = mapCount.get(item.getKey().toString().split("\\s+")[0]);
			double value = ((Integer)item.getValue()+1.0)/(vocabSize+wordNumber);
			map.put(item.getKey().toString(), value);
		}
		return map;
	}
	
	//print result for test purpose
	public void printMap(Map<?, ?> mp) {
	    Iterator<?> it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	//testing data and return the probability of correct reviews
	public double TestingData(double[] negNeg, double[] negPos, double[] posNeg, double[] posPos,int a)
	{
		int count = 0;
			for(int i=0;i<20;i++)
			{
				if(negNeg[i] < negPos[i])
					count++;
				if(posNeg[i] > posPos[i])
					count++;
			}
			System.out.println("Number of correct reviews: " + count + " over 40");
			return count/40.0;
	}
	
	public Integer Sub(int a, int b)
	{
		return a-b;
	}

	public void listf(String directoryName, List<String> files, String fold, boolean train, boolean success) {	
		//boolean matches = false;
	    File[] fList = new File(directoryName).listFiles();
	    for (File file : fList) {
//	    	System.out.println(directoryName + "\\" + file.getName());
//	    	System.out.println(files.size());
	        if (file.isFile()){
	        	if((train==true)?!directoryName.contains(fold):directoryName.contains(fold))
	        		if((success==true)?directoryName.contains("success"):directoryName.contains("failure")) 
	            files.add(directoryName + "\\" + file.getName());
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath(), files, fold, train, success);
	        }
	    }
	}
}
