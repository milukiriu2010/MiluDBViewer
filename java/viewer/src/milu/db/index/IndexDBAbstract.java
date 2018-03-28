package milu.db.index;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.abs.ObjDBInterface;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

abstract public class IndexDBAbstract implements ObjDBInterface
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Index List
	protected List<Map<String,String>> indexLst = new ArrayList<>();
	
	protected void clear()
	{
		this.indexLst.clear();
	}
	
	@Override
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  indexEntityLst = new ArrayList<>();
		for ( Map<String,String> index : this.indexLst )
		{
			SchemaEntity eachIndexEntity = SchemaEntityFactory.createInstance( index.get("indexName"), SchemaEntity.SCHEMA_TYPE.INDEX );
			// create Index Item
			String is_primary    = index.get("is_primary");
			String is_unique     = index.get("is_unique");
			String is_functional = index.get("is_functional");
			String iconFileName  = null;
			if ( "t".equals(is_primary) )
			{
				iconFileName = "file:resources/images/index_p.png";
			}
			else if ( "t".equals(is_unique) )
			{
				iconFileName = "file:resources/images/index_u.png";
			}
			else if ( "t".equals(is_functional) )
			{
				iconFileName = "file:resources/images/index_f.png";
			}
			else
			{
				iconFileName = "file:resources/images/index_i.png";
			}
			eachIndexEntity.setImageResourceName(iconFileName);
			indexEntityLst.add( eachIndexEntity );
		}
		return indexEntityLst;
	}
	
	@Override
	public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException
	{
		throw new UnsupportedOperationException();
	}
	
	// select index list
	abstract public List<SchemaEntity> selectEntityLst( String schemaName, String tableName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName, String tableName );
	
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
