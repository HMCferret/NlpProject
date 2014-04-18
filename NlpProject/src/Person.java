
public class Person {
	private static int cnt = 0;
	int Id;
	String name;
	Person(String name)
	{
		Id = ++cnt;
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return "{"+Id+", " + name + "}";
	}
}
