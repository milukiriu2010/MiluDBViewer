package milu.ctrl.sql.parse;

public class CallObj {
	
	public enum ParamType {
		IN,
		OUT,
		IN_OUT
	}
	
	// IN/OUTパラメータの番号
	private Integer paramNo = 1;
	
	// IN/OUTパラメータのType
	//private ParamType paramType = ParamType.IN;
	private ParamType paramType = null;
	
	// MySQLType(java.sql.Types)
	//private MySQLType sqlType = MySQLType.NUMERIC;
	private MySQLType sqlType = null;
	
	// INパラメータに使う列
	//private String inColName = "A";
	private String inColName = null;
	
	// IN/OUTパラメータの番号
	public void setParamNo( Integer paramNo )
	{
		this.paramNo = paramNo;
	}
	
	public Integer getParamNo()
	{
		return this.paramNo;
	}
	
	
	// IN/OUTパラメータのType
	public void setParamType( ParamType paramType )
	{
		this.paramType = paramType;
	}
	
	public ParamType getParamType()
	{
		return this.paramType;
	}
	
	// MySQLType(java.sql.Types)
	public void setSqlType( MySQLType sqlType )
	{
		this.sqlType = sqlType;
	}
	
	public MySQLType getSqlType()
	{
		return this.sqlType;
	}
	
	// INパラメータに使う列
	public void setInColName( String inColName )
	{
		this.inColName = inColName;
	}
	
	public String getInColName()
	{
		return this.inColName;
	}
}
