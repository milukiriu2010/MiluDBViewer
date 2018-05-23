package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "root index" item is clicked on SchemaTreeView.
 * Show "Index Information"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX] => selected
 *         - [INDEX]    => get
 *   - [ROOT_VIEW]
 *     - [VIEW]
 * ---------------------------------------
 * @author miluk
 *
 */
public class SelectedItemHandlerRootIndex extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.INDEX, null, CollectDataType.LIST );
	}

}
