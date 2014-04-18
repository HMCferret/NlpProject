
public class Quote {
	Person speaker;
	String text;
	int characterBeginOffset;
	int characterEndOffset;
	String verb;
	
	Quote(Person speaker, String text, String verb, int bOffset, int eOffset)
	{
		this.speaker = speaker;
		this.text = text;
		this.characterBeginOffset = bOffset;
		this.characterEndOffset = eOffset;
		this.verb = verb;
	}
	
	@Override
	public String toString()
	{
		return "{Speaker: " + speaker + ", text=" + text + ", bOffset=" + this.characterBeginOffset + ", eOffset" + this.characterEndOffset  + "}";
	}
}
