package milu.ctrl.sqlparse;

public class SQLBag 
{
	public enum COMMAND
	{
		QUERY,
		TRANSACTION,
		UNKNOWN_COMMAND
	}
	
	public enum TYPE
	{
		SELECT,
		INSERT,
		UPDATE,
		DELETE,
		UNKNOWN_TYPE
	}
	
	private String sql      = null;
	
	private COMMAND command = COMMAND.UNKNOWN_COMMAND;
	
	private TYPE    type    = TYPE.UNKNOWN_TYPE;
	
	public String getSQL()
	{
		return this.sql;
	}
	
	public void setSQL( String sql )
	{
		this.sql = sql;
	}
	
	public COMMAND getCommand()
	{
		return this.command;
	}
	
	public void setCommand( COMMAND command )
	{
		this.command = command;
	}
	
	public TYPE getType()
	{
		return this.type;
	}
	
	public void setType( TYPE type )
	{
		this.type = type;
	}
}
