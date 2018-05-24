package milu.gui.ctrl.common.table;

import javafx.geometry.Orientation;

class TableProcessFactory 
{
	static TableProcessAbstract getInstance( Orientation ot, ObjTableView objTableView )
	{
		TableProcessAbstract tpAbs = null;
		if ( Orientation.HORIZONTAL.equals(ot) )
		{
			tpAbs = new TableProcessHorizontal();
		}
		else
		{
			tpAbs = new TableProcessVertical();
		}
		
		tpAbs.setObjTableView(objTableView);
		
		return tpAbs;
	}
}
