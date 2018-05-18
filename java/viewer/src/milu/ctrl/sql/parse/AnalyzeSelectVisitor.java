package milu.ctrl.sql.parse;

import net.sf.jsqlparser.statement.select.SelectVisitor;

public interface AnalyzeSelectVisitor extends SelectVisitor 
{
	public void  setAnalyzeFromItemVisitor( AnalyzeFromItemVisitor  analyzeFromItemVisitor ); 
}
