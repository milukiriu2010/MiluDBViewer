package milu.ctrl.sqlparse;

import net.sf.jsqlparser.statement.StatementVisitor;

public interface AnalyzeStatementVisitor extends StatementVisitor 
{
	public void setAnalyzeSelectVistor( AnalyzeSelectVisitor analyzeSelectVisitor );
}
