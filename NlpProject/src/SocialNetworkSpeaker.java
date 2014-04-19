
public class SocialNetworkSpeaker {
	private static int counter = 0;
	public int index;
	public String name;
	//public int quotePosition;
	public int quoteLength;
	
	public SocialNetworkSpeaker(String name, int quoteLength)
	{
		this.index = ++counter;
		this.name=name;
		this.quoteLength=quoteLength;
	}
}
