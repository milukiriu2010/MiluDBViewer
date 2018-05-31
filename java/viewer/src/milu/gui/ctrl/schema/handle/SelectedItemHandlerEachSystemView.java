package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;

/**
 * This class is invoked, when "system view" item is clicked on SchemaTreeView.
 * Show "System View Information"
 * ------------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_SYSTEM_VIEW]
 *     - [SYSTEM_VIEW]    => selected
 * ------------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachSystemView extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		this.loadDefinition( AbsDBFactory.FACTORY_TYPE.SYSTEM_VIEW, SchemaTableViewTab.class, SelectedItemHandlerAbstract.DEFINITION_TYPE.NO_DEFAULT );
	}

}
