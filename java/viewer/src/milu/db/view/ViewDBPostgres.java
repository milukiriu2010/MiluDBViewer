package milu.db.view;

import java.sql.SQLException;

import milu.db.MyDBAbstract;

public class ViewDBPostgres extends ViewDBAbstract
{
	public ViewDBPostgres( MyDBAbstract myDBAbs )
	{
		super( myDBAbs );
	}

	@Override
	public void selectViewLst(String schemaName) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	protected String viewLstSQL(String schemaName) {
		// TODO Auto-generated method stub
		return null;
	}

}
