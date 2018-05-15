package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root view" item is clicked on SchemaTreeView.
 * Show "View List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW] => selected
 *     - [VIEW]    => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootView extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		//this.loadChildLst( AbsDBFactory.FACTORY_TYPE.VIEW, SchemaTableViewTab.class, CollectDataType.LIST );
		
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.VIEW, SchemaTableViewTab.class, CollectDataType.LIST_AND_DEFINITION );
	}

}
