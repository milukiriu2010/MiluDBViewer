package milu.ctrl.sqlparse;

import net.sf.jsqlparser.statement.select.SelectVisitor;

public interface AnalyzeSelectVisitor extends SelectVisitor 
{
	public void  setAnalyzeFromItemVisitor( AnalyzeFromItemVisitor  analyzeFromItemVisitor ); 
}
