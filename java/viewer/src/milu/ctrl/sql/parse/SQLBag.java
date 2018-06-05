package milu.ctrl.sql.parse;

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
		SELECT("SELECT"),
		INSERT("INSERT"),
		UPDATE("UPDATE"),
		DELETE("DELETE"),
		CREATE_TABLE("CREATE TABLE"),
		CREATE_INDEX("CREATE INDEX"),
		CREATE_VIEW("CREATE VIEW"),
		ALTER_VIEW("ALTER VIEW"),
		ALTER("ALTER"),
		DROP("DROP"),
		TRUNCATE("TRUNCATE"),
		EXECUTE("EXECUTE"),
		MERGE("MERGE"),
		UPSERT("UPSERT"),
		REPLACE("REPLACE"),
		UNKNOWN_TYPE("UNKNOWN TYPE")
		;
		
		private String val = null;
		
		private TYPE( String val )
		{
			this.val = val;
		}
		
		public String getVal()
		{
			return this.val;
		}
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
