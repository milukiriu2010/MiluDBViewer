package abc;

public class TeamBaseBall extends Team
{
	public enum LEAGUE
	{
		CENTRAL,
		PACIFIC,
		UNKOWN
	}
	
	private LEAGUE league = LEAGUE.UNKOWN;
	
	/*
	public TeamBaseBall()
	{
	}
	*/
	
	public LEAGUE getLeague()
	{
		return this.league;
	}

	public void setLeague( LEAGUE league )
	{
		this.league = league;
	}
	
	@Override
	public TeamBaseBall clone() throws CloneNotSupportedException
	{
		Team clone = super.clone();
		return (TeamBaseBall)clone;
	}	
}
