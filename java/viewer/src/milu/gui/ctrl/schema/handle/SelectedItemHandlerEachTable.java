package milu.gui.ctrl.schema.handle;


import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;

import java.sql.SQLException;

/**
 * This class is invoked, when "table" item is clicked on SchemaTreeView.
 * Show "Table Definition"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE] => selected
 *   - [ROOT_VIEW]
 *     - [VIEW]
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachTable extends SelectedItemHandlerAbstract
{
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		this.loadDefinition( AbsDBFactory.FACTORY_TYPE.TABLE, SchemaTableViewTab.class, SelectedItemHandlerAbstract.DEFINITION_TYPE.NO_DEFAULT );
	}
	
}
