package milu.db.indexcolumn;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class IndexColumnDBAbstract
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Column Name List
	protected List<Map<String,String>>  indexColumnLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.indexColumnLst.clear();
	}
	
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  indexColumnEntityLst = new ArrayList<>();
		for ( Map<String,String> indexColumn : this.indexColumnLst )
		{
			SchemaEntity eachIndexColumnEntity = SchemaEntityFactory.createInstance( indexColumn.get("columnName"), SchemaEntity.SCHEMA_TYPE.INDEX_COLUMN );
			String clusteringOrder = indexColumn.get("clusteringOrder");
			String iconFileName  = null;
			if ( "asc".equals( clusteringOrder ) )
			{
				iconFileName = "file:resources/images/order_a.png";
			}
			else if ( "desc".equals( clusteringOrder ) )
			{
				iconFileName = "file:resources/images/order_d.png";
			}
			else
			{
				iconFileName = "file:resources/images/column.png";
			}
			eachIndexColumnEntity.setImageResourceName(iconFileName);
			indexColumnEntityLst.add( eachIndexColumnEntity );
		}
		return indexColumnEntityLst;
	}
	
	abstract public void selectEntityLst( String schemaName, String tableName, String indexName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName, String tableName, String indexName );
}
