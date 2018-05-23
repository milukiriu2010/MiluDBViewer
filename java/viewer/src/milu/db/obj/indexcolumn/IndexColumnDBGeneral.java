package milu.db.obj.indexcolumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;

public class IndexColumnDBGeneral extends IndexColumnDBAbstract {

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName, String tableName, String indexName)
			throws SQLException 
	{
		this.clear();
		
		DatabaseMetaData dm = this.myDBAbs.getMetaData();
		try
		(
			ResultSet        rs = dm.getIndexInfo( null, schemaName, tableName, false, false );
		)
		{
			while ( rs.next() )
			{
				if ( indexName.equals(rs.getString("INDEX_NAME")) == false )
				{
					continue;
				}				
				Map<String, String> mapColumn = new HashMap<String,String>();
				mapColumn.put( "columnName", rs.getString("COLUMN_NAME") );
				String asc_or_desc = rs.getString("ASC_OR_DESC");
				mapColumn.put( "clusteringOrder", asc_or_desc.equals("A") ? "asc":"desc" );
				this.indexColumnLst.add( mapColumn );
			}
			return this.getEntityLst();
		}
	}

	@Override
	protected String listSQL(String schemaName, String tableName, String indexName) 
	{
		return null;
	}

}
