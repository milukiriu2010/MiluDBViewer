package milu.ctrl.sqlparse;

public class TableVisitorFactory implements SQLParseFactory 
{

	@Override
	public AnalyzeFromItemVisitor createAnalyzeFromItemVisitor() 
	{
		return new TableFromItemVisitor();
	}

	@Override
	public AnalyzeSelectVisitor   createAnalyzeSelectVisitor( AnalyzeFromItemVisitor analyzeFromItemVisitor )
	{
		AnalyzeSelectVisitor  tableSelectVisitor = new TableSelectVisitor();
		tableSelectVisitor.setAnalyzeFromItemVisitor(analyzeFromItemVisitor);
		return tableSelectVisitor;
	}
	
	@Override
	public AnalyzeStatementVisitor createAnalyzeStatementVisitor( AnalyzeSelectVisitor analyzeSelectVisitor )
	{
		AnalyzeStatementVisitor tableStatementVisitor = new TableStatementVisitor();
		tableStatementVisitor.setAnalyzeSelectVistor(analyzeSelectVisitor);
		return tableStatementVisitor;
	}

}
