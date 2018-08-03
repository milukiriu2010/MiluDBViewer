package milu.db.obj.fk;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityEachFK;
import milu.entity.schema.SchemaEntityFactory;

public class FKDBGeneral extends FKDBAbstract {

	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException {
		DatabaseMetaData dm = this.myDBAbs.getMetaData();
		
		List<String>  tableNameLst = new ArrayList<>();
		try
		(
			ResultSet rs   = dm.getTables( null, schemaName, "%", new String[] {"TABLE"} );
		)
		{
			while ( rs.next() )
			{
				tableNameLst.add( rs.getString("TABLE_NAME") );
			}
		}
		
		for ( String tableName : tableNameLst )
		{
			try
			(
				ResultSet rs = dm.getImportedKeys( null, schemaName, tableName )
			)
			{
				System.out.println( "================================================" );
				System.out.println( "PKTABLE_NAME :" + rs.getString("PKTABLE_NAME") );
				System.out.println( "PKCOLUMN_NAME:" + rs.getString("PKCOLUMN_NAME") );
				System.out.println( "FKTABLE_NAME :" + rs.getString("FKTABLE_NAME") );
				System.out.println( "FKCOLUMN_NAME:" + rs.getString("FKCOLUMN_NAME") );
				System.out.println( "KEY_SEQ      :" + rs.getString("KEY_SEQ") );
				System.out.println( "================================================" );
			}
		}		
		
		return null;
	}

	@Override
	protected String listSQL(String schemaName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> selectSrcColumnMap(SchemaEntityEachFK fkEntity) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> selectDstColumnMap(SchemaEntityEachFK fkEntity) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
