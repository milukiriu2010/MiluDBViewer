package milu.db.fk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public class FKDBOracle extends FKDBAbstract
{
	/**
	 * select Foreign Key List
	 */
	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException
	{
		List<SchemaEntity>  fkEntityLst = new ArrayList<>();

		String sql = this.listSQL( schemaName );
		System.out.println( " -- selectEntityLst(FK) ---------" );
		System.out.println( sql );
		System.out.println( " ----------------------------------" );
		
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql );
		)
		{
			while ( rs.next() )
			{
				SchemaEntity fkEntity = SchemaEntityFactory.createInstance( rs.getString("constraint_name"), SchemaEntity.SCHEMA_TYPE.FOREIGN_KEY );
				String strStatus = rs.getString("status");
				if ( strStatus != null && "ENABLED".equals(strStatus) == false )
				{
					fkEntity.setState( SchemaEntity.STATE.INVALID );
				}
				fkEntityLst.add( fkEntity );
			}
		}
		
		return fkEntityLst;
	}
	

	@Override
	protected String listSQL(String schemaName)
	{
		String sql = 
			" select distinct \n" +
			"   owner, \n"             +    // src fk schema & src table schema
			"   constraint_name, \n"   +	// src fk name
			"   table_name, \n"        +	// src table name
			"   r_owner, \n"           +	// dst constraint owner
			"   r_constraint_name, \n" +	// dst constraint name
			"   status \n" +				// status
			" from \n" +
			"   all_constraints \n" +
			" where \n" +
			"   constraint_type='R' \n" + 
			"   and \n" + 
			"   owner = '" + schemaName + "' \n" +
			" order by constraint_name";
		return sql;
	}

}
