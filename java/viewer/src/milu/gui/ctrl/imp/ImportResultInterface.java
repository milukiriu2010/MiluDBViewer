package milu.gui.ctrl.imp;

import java.util.List;

public interface ImportResultInterface 
{
	public void setTotal( int cntTotal );
	public void setOK( int cntOK );
	public void setNG( int cntNG );
	public void setSQL( String strSQL );
	public void setTableViewData( List<Object> columnLst, List<List<Object>> dataLst );
}
