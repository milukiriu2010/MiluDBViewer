package milu.gui.ctrl.query;

import javafx.event.Event;

public interface SQLExecWithoutParseInterface 
{
	public void execSQLQuery( Event event );
	public void execSQLTrans( Event event );
}
