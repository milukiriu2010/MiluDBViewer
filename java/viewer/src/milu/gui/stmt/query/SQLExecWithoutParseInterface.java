package milu.gui.stmt.query;

import javafx.event.Event;

public interface SQLExecWithoutParseInterface 
{
	public void execSQLQuery( Event event );
	public void execSQLTrans( Event event );
	public void execSQLTransSemi( Event event );
}
