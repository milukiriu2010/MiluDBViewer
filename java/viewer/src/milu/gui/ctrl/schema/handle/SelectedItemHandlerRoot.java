package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

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
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		SchemaEntity rootEntity = this.myDBAbs.getSchemaRoot();
		if ( rootEntity.getEntityLst().size() > 0 )
		{
			if ( this.itemRoot == null )
			{
				List<SchemaEntity> schemaEntityLst = new ArrayList<>();
				schemaEntityLst.add(rootEntity);
				schemaTreeView.addEntityLst( null, schemaEntityLst, true );
			}
			else if ( this.itemSelected.getChildren().size() == 0 )
			{
				schemaTreeView.addEntityLst( this.itemSelected, rootEntity.getEntityLst(), true );
			}
		}
		
	}
	
}
