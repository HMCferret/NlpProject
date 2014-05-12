
public class CandidateQuote {
	//1
	public int distance;
	
	//2
	public boolean commaPresence;
	public boolean periodPresence;
	public boolean punctuationPresence;
	
	//3
	public int ordinal;
	
	//4
	public double proportion;
	
	//5
	public int numNameCd;
	public int numQuoteCd;
	public int numWordCd;
	
	public int numNameQt;
	public int numQuoteQt;
	public int numWordQt;
	
	public int numNameOtr;
	public int numQuoteOtr;
	public int numWordOtr;
	
	//6
	public int numAppearance;
	
	//7
	public boolean expVerbPresence;
	public boolean personPresence;
	
	
	//8
	public int quoteLength;
	public int quotePosition;
	public boolean otherCharPresence;
	public boolean candidatePresence;
	
	public Person candidate;
	public int quoteBeginOffset;
	public int quoteEndOffset;
	
	@Override
	public String toString()
	{
		return "dist="+this.distance + " comma,period,punct=" + this.commaPresence + "," + this.periodPresence + ","
				+ this.punctuationPresence + " ordinal=" + this.ordinal + " propotion=" + this.proportion
				+ " name,quote,wordCd=" + this.numNameCd + "," + this.numQuoteCd + "," +this.numWordCd 
				+ " name,quote,wordQt=" + this.numNameQt + "," + this.numQuoteQt + "," + this.numWordQt 
				+ " name,quote,wordOtr=" + this.numNameOtr + "," + this.numQuoteOtr + "," + this.numWordOtr 
				+ " numApperance=" + this.numAppearance
				+ " exverb,persenPresence=" + this.expVerbPresence + "," + this.personPresence
				+ " quoteLen,pos,OtherChar,Candidate=" + this.quoteLength + "," + this.quotePosition + "," + this.otherCharPresence
				+ "," + this.candidatePresence;
	}
}
