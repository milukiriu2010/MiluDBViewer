package milu.ctrl.sql.parse;

public enum MySQLType 
{
	NUMERIC(java.sql.Types.NUMERIC),
	VARCHAR(java.sql.Types.VARCHAR),
	TIMESTAMP(java.sql.Types.TIMESTAMP)
	;
	
	private Integer val = null;
	
	private MySQLType( Integer val )
	{
		this.val = val;
	}
	
	public Integer getVal()
	{
		return this.val;
	}
}
