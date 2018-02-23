package milu.ctrl.sqlparse;

import java.util.List;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import net.sf.jsqlparser.statement.Statement; 
import net.sf.jsqlparser.JSQLParserException;


public class SQLParse 
{
	private String strSQL = null;
	
	public SQLParse()
	{
		
	}
	
	public void setStrSQL( String strSQL )
	{
		this.strSQL = strSQL;
	}
	
	public void parse() throws JSQLParserException
	{
		if ( this.strSQL == null )
		{
			return;
		}
		
		Statement stmt = CCJSqlParserUtil.parse( this.strSQL );
		
		SQLParseFactory  sqlParseFactory = new TableVisitorFactory();
		AnalyzeFromItemVisitor  analyzeFromItemVisitor  = sqlParseFactory.createAnalyzeFromItemVisitor();
		AnalyzeSelectVisitor    analyzeSelectVisitor    = sqlParseFactory.createAnalyzeSelectVisitor(analyzeFromItemVisitor);
		AnalyzeStatementVisitor analyzeStatementVisitor = sqlParseFactory.createAnalyzeStatementVisitor(analyzeSelectVisitor);
		
		stmt.accept( analyzeStatementVisitor );
		
		List<String> tableLst = analyzeFromItemVisitor.getTableLst();
		
		tableLst.forEach( (item)->System.out.println( "parse:" + item ) );
	}
}
