package milu.entity.schema;

import java.util.Map;
import java.util.LinkedHashMap;

public class SchemaEntityEachFK extends SchemaEntity 
{
	// schema of Foreign Key
	private String srcFKSchema = null;
	
	// name of Foreign Key
	// -------------------------------------
	// = super.name
	// -------------------------------------
	private String srcConstraintName   = null;
	
	// schema of the table(FK/src)   
	private String srcTableSchema = null;

	// table(FK/src)
	private String srcTableName  = null;
	
	// columns(FK/src)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	private Map<String,String> srcColumnMap = new LinkedHashMap<>();
	
	// constraint name(FK/dst)
	private String dstConstraintName = null;
	
	// schema of the table(FK/dst)
	private String dstTableSchema = null;
	
	// table(FK/dst)
	private String dstTableName  = null;
	
	// columns(FK/dst)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	private Map<String,String> dstColumnMap = new LinkedHashMap<>();
	
	public SchemaEntityEachFK( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
		
		this.imageResourceName = "file:resources/images/index_fk.png";
	}
	
	// schema of Foreign Key
	public String getSrcFKSchema()
	{
		return this.srcFKSchema;
	}

	public void setSrcFKSchema( String srcFKSchema )
	{
		this.srcFKSchema = srcFKSchema;
	}
	
	// name of Foreign Key
	// -------------------------------------
	// = super.name
	// -------------------------------------
	public String getSrcConstraintName()
	{
		return this.srcConstraintName;
	}
	
	public void setSrcConstraintName( String srcConstraintName )
	{
		this.srcConstraintName = srcConstraintName;
		this.name = this.srcConstraintName;
	}
	
	// schema of the table(FK/src)
	public String getSrcTableSchema()
	{
		return this.srcTableSchema;
	}
	
	public void setSrcTableSchema( String srcTableSchema )
	{
		this.srcTableSchema = srcTableSchema;
	}

	// table(FK/src)
	public String getSrcTableName()
	{
		return this.srcTableName;
	}
	
	public void setSrcTableName( String srcTableName )
	{
		this.srcTableName = srcTableName;
	}
	
	// columns(FK/src)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	public Map<String,String> getSrcColumnMap()
	{
		return this.srcColumnMap;
	}
	
	public void addSrcColumnMap( String columnName, String pos )
	{
		this.srcColumnMap.put( columnName, pos );
	}
	
	// name of constraint name for referenced
	public String getDstConstraintName()
	{
		return this.dstConstraintName;
	}
	
	public void setDstConstraintName( String dstConstraintName )
	{
		this.dstConstraintName = dstConstraintName;
	}
	
	// schema of the table(FK/dst)
	public String getDstTableSchema()
	{
		return this.dstTableSchema;
	}
	
	public void setDstTableSchema( String dstTableSchema )
	{
		this.dstTableSchema = dstTableSchema;
	}
	
	// table(FK/dst)
	public String getDstTableName()
	{
		return this.dstTableName;
	}
	
	public void setDstTableName( String dstTableName )
	{
		this.dstTableName = dstTableName;
	}
	
	// columns(FK/dst)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	public Map<String,String> getDstColumnMap()
	{
		return this.dstColumnMap;
	}
	
	public void addDstColumnMap( String columnName, String pos )
	{
		this.dstColumnMap.put( columnName, pos );
	}
	
}
