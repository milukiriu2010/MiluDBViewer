package abc;

public class TeamSoccer extends Team 
{
	private int level = -1;
	
	/*
	public TeamSoccer()
	{
	}
	*/

	public int getLevel()
	{
		return this.level;
	}
	
	public void setLevel( int level )
	{
		this.level = level;
	}
	
	
	@Override
	public TeamSoccer clone() throws CloneNotSupportedException
	{
		Team clone = super.clone();
		return (TeamSoccer)clone;
	}
}
