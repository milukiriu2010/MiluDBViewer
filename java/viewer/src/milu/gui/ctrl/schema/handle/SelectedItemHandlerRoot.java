package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.entity.schema.SchemaEntity;

/**
 * This class is invoked, when there is no root item on SchemaTreeView
 * @author milu
 *
 */
public class SelectedItemHandlerRoot extends SelectedItemHandlerAbstract
{
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemRoot == null )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		SchemaEntity rootEntity = this.myDBAbs.getSchemaRoot();
		if ( rootEntity != null )
		{
			this.schemaTreeView.setInitialData( rootEntity );
		}
		
	}
	
}
