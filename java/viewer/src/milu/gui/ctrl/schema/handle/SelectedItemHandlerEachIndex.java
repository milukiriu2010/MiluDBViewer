package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;
import milu.db.obj.abs.AbsDBFactory;
import milu.task.collect.CollectDataType;

/**
 * This class is invoked, when "index" item is clicked on SchemaTreeView.
 * Show "Index Column List"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]          => selected
 *           - [INDEX_COLUMN] => get
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachIndex extends SelectedItemHandlerAbstract
{
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		this.loadChildLst( AbsDBFactory.FACTORY_TYPE.INDEX_COLUMN, null, CollectDataType.LIST );
	}
	
}
