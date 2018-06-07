package milu.gui.ctrl.common.table;

import javafx.event.Event;

public interface CopyTableInterface
{
	/*********************************
	 * Copy Table Data Without Column
	 *********************************
	 */
	public void copyTableNoHead( Event event );
	
	/*********************************
	 * Copy Table Data With Column
	 *********************************
	 */
	public void copyTableWithHead( Event event );
}
