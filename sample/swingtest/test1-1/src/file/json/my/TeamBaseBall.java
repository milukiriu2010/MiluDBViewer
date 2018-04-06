package file.json.my;

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
}
