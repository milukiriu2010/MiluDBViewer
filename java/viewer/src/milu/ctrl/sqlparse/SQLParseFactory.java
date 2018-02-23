package milu.ctrl.sqlparse;

public interface SQLParseFactory 
{
	public AnalyzeFromItemVisitor  createAnalyzeFromItemVisitor();
	public AnalyzeSelectVisitor    createAnalyzeSelectVisitor( AnalyzeFromItemVisitor analyzeFromItemVisitor );
	public AnalyzeStatementVisitor createAnalyzeStatementVisitor( AnalyzeSelectVisitor analyzeSelectVisitor );
}
