import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class SNFeatureExtraction {
	
	private List<Quote> lq; // list of quote
	private String text;// text in data
	private int countWordQuote;// count word in quote
	private Map<String, Integer> mapHelper;//store speakers's frequency
	private Map<Integer, Quote> mapHelper2;//store speaker's quotePosition
	private Set<String> names;
	private boolean success;
	
	public SNFeatureExtraction(List<Quote> lq, String text, Set<String> names, boolean success)
	{
		this.lq = lq;
		this.text = text;
		this.names= names;
		this.success = success;
	}
	
	public ConversationalNetworkFeature ConstructNetwork()
	{
		countWordQuote = 0;
		mapHelper = new LinkedHashMap<String, Integer>();
		mapHelper2 = new TreeMap<Integer, Quote>();
		
		//loop through list quote to count word quote + check adjacency
		Iterator<Quote> it = lq.iterator();
		while(it.hasNext())
		{
			Quote q = it.next();
			if(!mapHelper.containsKey(q.speaker.name))
			{
				mapHelper.put(q.speaker.name, 1);
			}
			else
			{
				mapHelper.put(q.speaker.name, mapHelper.get(q.speaker.name) + 1);
			}
			countWordQuote += q.text.length();
			
			mapHelper2.put(q.characterBeginOffset,q);
		}
		
		Map<String, Integer> result = BuildMap();

		return ExtractFeature(result);
	}
	
	public ConversationalNetworkFeature ExtractFeature(Map<String, Integer> network)
	{
		ConversationalNetworkFeature cnf = new ConversationalNetworkFeature();
		int t = mapHelper.size();
		int n = (t % 5 == 0)?5:t % 5; //n most frequent speakers
		cnf.mostFqtChar = 0;
		
		//1
		cnf.numChar = names.size();
		cnf.numSpeakingChar = mapHelper.size();
		
		//2
		//Sort map to get n most frequent speakers
		mapHelper = sortByValue(mapHelper);
		int index = 0;
		for (Map.Entry<String,Integer> entry : mapHelper.entrySet()) {
			cnf.mostFqtChar += entry.getValue();
			index++;
			if(index >= n )
				break;
	    	}
		cnf.mostFqtChar /=lq.size();
		cnf.mostFqtChar*=100;
		cnf.mostFqtChar = (cnf.mostFqtChar==Double.NaN)?0.1:(cnf.mostFqtChar-0.01);
		
		//3
		cnf.numQuote = lq.size();
		cnf.quoteProportion = 100.0*countWordQuote/text.length();
		
		//4
		cnf.num3Clique = numClique(3);
		//cnf.num4Clique = numClique(4);
		
		//5
		cnf.avgDegree = network.size()*1.0/t;
		cnf.avgDegree = (cnf.avgDegree==Double.NaN)?0.1:cnf.avgDegree;
		
		//6
		cnf.graphDensity = network.size()*1.0/(t*(t-1));
		cnf.graphDensity = (cnf.graphDensity==Double.NaN)?0.1:cnf.graphDensity;
		
		cnf.success = success;
		
		return cnf;
		
	}
	
	//Helper methods
	
	public static Map<String,Integer> sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2) {
                return (m2.getValue()).compareTo(m1.getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
	
	public Map<String, Integer> BuildMap()
	{
		Map<String, Integer> result = new TreeMap<String,Integer>();
		Set<?> set = mapHelper2.entrySet();
	     Iterator<?> iterator = set.iterator();
	     String name = "";
	     int l = 0;//length
	     int qp = 0;//quotePosition
	     
	     while(iterator.hasNext()) {
	         @SuppressWarnings("rawtypes")
			Map.Entry me = (Map.Entry)iterator.next();
	         if(name!="" && !name.equals(((Quote)me.getValue()).speaker.name) && Distance(qp, (Integer)me.getKey()) < 300)
	         {
	       		 String pair1 = name + " " + ((Quote)me.getValue()).speaker.name;
	       		 String pair2 = ((Quote)me.getValue()).speaker.name + " " + name;
	       		 if(result.containsKey(pair1))
	       		 {
	       			result.put(pair1, result.get(pair1) + l + ((Quote)me.getValue()).text.length());
	       			result.put(pair2, result.get(pair2) + l + ((Quote)me.getValue()).text.length());
	       		 }
	       		 else
	       		 {
	       			 result.put(pair1,l + ((Quote)me.getValue()).text.length());
	       			 result.put(pair2,l + ((Quote)me.getValue()).text.length());
	       		 }
	         }
	         name = ((Quote)me.getValue()).speaker.name;
	         l = ((Quote)me.getValue()).text.length();
	         qp = (Integer)me.getKey();
	     }
	     return result;
	}
	
	@SuppressWarnings("rawtypes")
	public int numClique(int num)
	{
		if(num > mapHelper.size())
			return 0;
		Set<?> set = mapHelper2.entrySet();
	    Iterator<?> iterator = set.iterator();
		int index = 0;
		int count = 0;
		String name = "";
	    List<Integer> helper = new ArrayList<Integer>();
	    
	    try
	    {
		    while(iterator.hasNext()) {
		         Map.Entry me = (Map.Entry)iterator.next();
		         if((name!="" && !name.equals(((Quote)me.getValue()).speaker.name)) || name=="")
		         {
		        	 name = ((Quote)me.getValue()).speaker.name;
			         helper.add((Integer)me.getKey());
			         //index++;
		         }
		    }
	    	
		    for(int i = 0;i<=helper.size() - num;i++)
		    {
		    	int j = i;
		    	while(j < i + num - 1 && Distance(helper.get(j),helper.get(j+1)) < 300)
		    	{
		    		j++;
		    	}
		    	if(j == i + num - 1)
		    		count++;
		    }
		    return count;
	    }
	    catch(Exception ex)
	    {
	    	return 0;
	    }
	}
	
	public int Distance(int begin, int end)
	{
		return text.substring(begin, end).length() - text.replaceAll(" ","").length();
	}
}
