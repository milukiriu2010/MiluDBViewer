package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root system view" item is clicked on SchemaTreeView.
 * Show "System View List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_SYSTEM_VIEW]
 *     - [SYSTEM_VIEW]
 *   - [ROOT_MATERIALIZED_VIEW] => selected
 *     - [MATERIALIZED_VIEW]    => get
 *   - [ROOT_FUNC]
 *     - [FUNC]
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootSystemView extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		//this.loadChildLst( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW, SchemaTableViewTab.class, CollectDataType.LIST );
		
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW, SchemaTableViewTab.class, CollectDataType.LIST_AND_DEFINITION );
	}

}
