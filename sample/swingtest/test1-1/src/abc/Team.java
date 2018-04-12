package abc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public abstract class Team implements Cloneable
{
	protected String type = null;
	
	protected String name = null;
	
	protected List<String>  playerLst = new ArrayList<>();
	
	protected Map<Integer,Integer>  yearPosMap = new HashMap<>();
	
	/*
	public Team()
	{
	}
	*/
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType( String type )
	{
		this.type = type;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName( String name )
	{
		this.name = name;
	}
	
	public List<String> getPlayerLst()
	{
		return this.playerLst;
	}
	
	public void addPlayer( String player )
	{
		this.playerLst.add( player );
	}
	
	public Map<Integer,Integer> getYearPosMap()
	{
		return this.yearPosMap;
	}
	
	public void putYearPosMap( Integer year, Integer pos )
	{
		this.yearPosMap.put( year, pos );
	}
	
	@Override
	public Team clone() throws CloneNotSupportedException
	{
		Object clone = super.clone();
		Team team = (Team)clone;
		//team.name = new String( this.name );
		return team;
	}
}
