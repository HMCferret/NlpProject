import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class SNFeatureExtraction {
	//1
	private int numChar;
	private int numSpeakingChar;
	
	//2
	private double MostFqtChar;

	
	//3
	private int numQuote;
	private double quoteProportion;
	
	//4
	private int num3Cliche;
	public int num4Cliche;
	
	//5
	private double avgDegree;
	
	//6
	private double graphDensity;
	
	
	private List<Quote> lq; // list of quote
	private String text;// text in data
	private int countWordQuote;// count word in quote
	private Map<String, Integer> mapHelper;//store speakers's frequency
	private Map<Integer, SocialNetworkSpeaker> mapHelper2;//store speaker's quotePosition
	
	public SNFeatureExtraction(List<Quote> lq, String text)
	{
		this.lq = lq;
		this.text = text;
	}
	
	public void CreateNetwork()
	{
		countWordQuote = 0;
		int countSpeaker = 0;
		mapHelper = new LinkedHashMap<String, Integer>();
		mapHelper2 = new TreeMap<Integer, SocialNetworkSpeaker>();
		SocialNetworkSpeaker sns;
		
		//loop through list quote to count word quote + check adjacency
		Iterator<Quote> it = lq.iterator();
		while(it.hasNext())
		{
			Quote q = it.next();
			if(!mapHelper.containsKey(q.speaker.name))
			{
					countSpeaker++;
					mapHelper.put(q.speaker.name, 1);
			}
			else
			{
				mapHelper.put(q.speaker.name, mapHelper.get(q.speaker.name) + 1);
			}
			countWordQuote += q.text.length();
			
			sns = new SocialNetworkSpeaker(q.speaker.name,q.text.length());
			mapHelper2.put(q.characterBeginOffset,sns);
		}
		
		Map<String, Integer> result = BuildMap(1);

		Extract(result);
	}
	
	public void Extract(Map<String, Integer> network)
	{
		int t = network.size()/2;
		int n = (t % 5 == 0)?5:t % 5; //n most frequent speakers
		MostFqtChar = 0;
		
		//1
		numSpeakingChar = mapHelper.size();
		
		//2
		//Sort map to get n most frequent speakers
		mapHelper = sortByValue(mapHelper);
		int index = 0;
		for (Map.Entry<String,Integer> entry : mapHelper.entrySet()) {
			MostFqtChar += entry.getValue();
			index++;
			if(index >= n )
				break;
	    	}
		MostFqtChar /=lq.size();
		
		//3
		numQuote = lq.size();
		quoteProportion = 100.0*countWordQuote/text.length();
		
		//4
		
		
		//5
		avgDegree = network.size()*1.0/t;
		
		//6
		graphDensity = network.size()*1.0/(t*(t-1));
		
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
	
	public Map<String, Integer> BuildMap(int cliqueNumber)
	{
		Map<String, Integer> result = new TreeMap<String,Integer>();
		Set<?> set = mapHelper2.entrySet();
	     Iterator<?> iterator = set.iterator();
	     String name = "";
	     int l = 0;//length
	     int qp = 0;//quotePosition
	     
	     while(iterator.hasNext()) {
	         Map.Entry me = (Map.Entry)iterator.next();
	         if(name!="" && !name.equals(((SocialNetworkSpeaker)me.getValue()).name) && Distance(qp, (Integer)me.getKey()) < 300)
	         {
       		 String pair1 = name + " " + ((SocialNetworkSpeaker)me.getValue()).name;
       		 String pair2 = ((SocialNetworkSpeaker)me.getValue()).name + name;
       		 if(result.containsKey(pair1))
       		 {
       			result.put(pair1, result.get(pair1) + l + ((SocialNetworkSpeaker)me.getValue()).quoteLength);
       			result.put(pair2, result.get(pair2) + l + ((SocialNetworkSpeaker)me.getValue()).quoteLength);
       		 }
       		 else
       		 {
       			 result.put(pair1,l + ((SocialNetworkSpeaker)me.getValue()).quoteLength);
       			 result.put(pair2,l + ((SocialNetworkSpeaker)me.getValue()).quoteLength);
       		 }

	         }
	         name = ((SocialNetworkSpeaker)me.getValue()).name;
	         l = ((SocialNetworkSpeaker)me.getValue()).quoteLength;
	         qp = (Integer)me.getKey();
	     }
	     return result;
	}
	
	public int Distance(int begin, int end)
	{
		return text.substring(begin, end).length() - text.replaceAll("\\.","").length();
	}
}
