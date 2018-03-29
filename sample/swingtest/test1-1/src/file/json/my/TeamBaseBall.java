package file.json.my;

public class TeamBaseBall extends Team
{
	public enum LEAGUE
	{
		CENTRAL,
		PACIFIC
	}
	
	private LEAGUE league = LEAGUE.PACIFIC;
	
	public LEAGUE getLeauge()
	{
		return this.league;
	}

	public void setLeauge( LEAGUE league )
	{
		this.league = league;
	}
}
