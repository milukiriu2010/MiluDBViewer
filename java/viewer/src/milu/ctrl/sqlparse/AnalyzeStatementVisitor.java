package milu.ctrl.sqlparse;

import java.util.List;

import net.sf.jsqlparser.statement.StatementVisitor;

public interface AnalyzeStatementVisitor extends StatementVisitor 
{
	public void setAnalyzeSelectVistor( AnalyzeSelectVisitor analyzeSelectVisitor );
	
	public List<String> getSqlLst();
}
