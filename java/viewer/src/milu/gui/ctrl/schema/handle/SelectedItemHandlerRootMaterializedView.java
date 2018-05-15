package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root materialized view" item is clicked on SchemaTreeView.
 * Show "Materialized View List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_MATERIALIZED_VIEW] => selected
 *     - [MATERIALIZED_VIEW]    => get
 *   - [ROOT_FUNC]
 *     - [FUNC]
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerRootMaterializedView extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		//this.loadChildLst( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW, SchemaTableViewTab.class, CollectDataType.LIST );
		
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW, SchemaTableViewTab.class, CollectDataType.LIST_AND_DEFINITION );
	}

}
