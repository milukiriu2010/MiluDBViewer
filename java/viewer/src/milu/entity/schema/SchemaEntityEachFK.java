package milu.entity.schema;

import java.util.Map;
import java.util.LinkedHashMap;

public class SchemaEntityEachFK extends SchemaEntity 
{
	// schema of Foreign Key
	private String schemaFK = null;
	
	// = super.name, so comment out
	// name of Foreign Key
	//private String nameFK   = null;
	
	// schema of the table(FK/src)   
	private String srcSchema = null;

	// table(FK/src)
	private String srcTable  = null;
	
	// columns(FK/src)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	private Map<String,String> srcColumnMap = new LinkedHashMap<>();
	
	
	// schema of the table(FK/dst)
	private String dstSchema = null;
	
	// table(FK/dst)
	private String dstTable  = null;
	
	// columns(FK/dst)
	// [KEY]
	//   column name
	// [VALUE]
	//   position
	private Map<String,String> dstColumnMap = new LinkedHashMap<>();
	
	public SchemaEntityEachFK( String name )
	{
		super( name, SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
		
		this.imageResourceName = "file:resources/images/index_i.png";
	}
	
	// schema of Foreign Key
	public String getSchemaFK()
	{
		return this.schemaFK;
	}

	public void setSchemaFK( String schemaFK )
	{
		this.schemaFK = schemaFK;
	}
	
	// name of Foreign Key
	//public String getNameFK()
	//{
	//	return this.nameFK;
	//}
	
	//public void setNameFK( String nameFK )
	//{
	//	this.nameFK = nameFK;
	//}
	
	// schema of the table(FK/src)   
	public String getSrcSchema()
	{
		return this.srcSchema;
	}
	
	public void setSrcSchema( String srcSchema )
	{
		this.srcSchema = srcSchema;
	}

	// table(FK/src)
	public String getSrcTable()
	{
		return this.srcTable;
	}
	
	public void setSrcTable( String srcTable )
	{
		this.srcTable = srcTable;
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
	
	// schema of the table(FK/dst)
	public String getDstSchema()
	{
		return this.dstSchema;
	}
	
	public void setDstSchema( String dstSchema )
	{
		this.dstSchema = dstSchema;
	}
	
	// table(FK/dst)
	public String getDstTable()
	{
		return this.dstTable;
	}
	
	public void setDstTable( String dstTable )
	{
		this.dstTable = dstTable;
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
