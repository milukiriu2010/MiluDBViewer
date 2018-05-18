package milu.ctrl.sql.parse;

public interface SQLParseFactory 
{
	public AnalyzeFromItemVisitor  createAnalyzeFromItemVisitor();
	public AnalyzeSelectVisitor    createAnalyzeSelectVisitor( AnalyzeFromItemVisitor analyzeFromItemVisitor );
	public AnalyzeStatementVisitor createAnalyzeStatementVisitor( AnalyzeSelectVisitor analyzeSelectVisitor );
}
