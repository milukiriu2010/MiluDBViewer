package milu.db.obj.indexcolumn;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.db.obj.abs.ObjDBInterface;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class IndexColumnDBAbstract implements ObjDBInterface
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Column Name List
	protected List<Map<String,String>>  indexColumnLst = new ArrayList<>();
	
	protected void clear()
	{
		this.indexColumnLst.clear();
	}
	
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected List<SchemaEntity> getEntityLst()
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
	
	@Override
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
	
	abstract public List<SchemaEntity> selectEntityLst( String schemaName, String tableName, String indexName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName, String tableName, String indexName );
	
	@Override
	public List<Map<String,String>> selectDefinition( String schemaName, String objName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSRC( String schemaName, String funcName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
}
