package milu.db.fk;

import java.sql.SQLException;
import java.util.List;

import milu.entity.schema.SchemaEntity;

public class FKDBOracle extends FKDBAbstract
{
	/**
	 * select Foreign Key List
	 */
	@Override
	public List<SchemaEntity> selectEntityLst(String schemaName) throws SQLException
	{
		return null;
	}
	

	@Override
	protected String listSQL(String schemaName)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
