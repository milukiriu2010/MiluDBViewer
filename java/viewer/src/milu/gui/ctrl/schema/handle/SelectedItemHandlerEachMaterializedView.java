package milu.gui.ctrl.schema.handle;

import java.sql.SQLException;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaTableViewTab;

/**
 * This class is invoked, when "view" item is clicked on SchemaTreeView.
 * Show "View Information"
 * ------------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *       - [ROOT_INDEX]
 *         - [INDEX]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_MATERIALIZED_VIEW]
 *     - [MATERIALIZED_VIEW]    => selected
 * ------------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachMaterializedView extends SelectedItemHandlerAbstract
{
	@Override
	public void exec() 
		throws 
			UnsupportedOperationException, 
			SQLException
	{
		this.loadDefinition( AbsDBFactory.FACTORY_TYPE.MATERIALIZED_VIEW, SchemaTableViewTab.class, SelectedItemHandlerAbstract.DEFINITION_TYPE.NO_DEFAULT );
	}

}
