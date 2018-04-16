package milu.ctrl.sqlparse;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.JSQLParserException;


public class SQLParse 
{
	private String strSQL = null;
	
	// Schema List
	private List<String> schemaLst = new ArrayList<>();
	
	// Table List
	private List<String> tableLst = new ArrayList<>();
	
	// Alias <=> Table
	private Map<String,String>  aliasMap = new HashMap<>();
	
	// SQLBag List
	private List<SQLBag> sqlBagLst = new ArrayList<>();
	
	public SQLParse()
	{
	}
	
	public void setStrSQL( String strSQL )
	{
		this.strSQL = strSQL;
	}
	
	public List<String> getSchemaLst()
	{
		return this.schemaLst;
	}
	
	public List<String> getTableLst()
	{
		return this.tableLst;
	}
	
	// Alias <=> Table
	public Map<String,String> getAliasMap()
	{
		return this.aliasMap;
	}
	
	// SQLBag List
	public List<SQLBag> getSQLBagLst()
	{
		return this.sqlBagLst;
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
		
		this.tableLst = analyzeFromItemVisitor.getTableLst();
		this.tableLst.forEach( (item)->System.out.println( "parse table:" + item ) );
		
		this.aliasMap = analyzeFromItemVisitor.getAliasMap();
		this.aliasMap.forEach( (k,v)->System.out.println( "parse alias:k[" + k + "]v[" + v + "]" ) );
		
		this.schemaLst.clear();
		// Schema <=> Table List
		Map<String,List<String>> schemaTableMap = analyzeFromItemVisitor.getSchemaTableMap();
		schemaTableMap.forEach( (k,v)->{ this.schemaLst.add(k); System.out.println( "parse schema:" + k ); } ); 
	}
	
	public void parseStatements() throws JSQLParserException
	{
		if ( this.strSQL == null )
		{
			return;
		}
		
		this.tableLst.clear();
		this.aliasMap.clear();
		this.schemaLst.clear();
		this.sqlBagLst.clear();
		
		Statements stmts = CCJSqlParserUtil.parseStatements(this.strSQL);
		for ( Statement stmt : stmts.getStatements() )
		{
			SQLParseFactory  sqlParseFactory = new TableVisitorFactory();
			AnalyzeFromItemVisitor  analyzeFromItemVisitor  = sqlParseFactory.createAnalyzeFromItemVisitor();
			AnalyzeSelectVisitor    analyzeSelectVisitor    = sqlParseFactory.createAnalyzeSelectVisitor(analyzeFromItemVisitor);
			AnalyzeStatementVisitor analyzeStatementVisitor = sqlParseFactory.createAnalyzeStatementVisitor(analyzeSelectVisitor);
			stmt.accept( analyzeStatementVisitor );
			
			// Table List
			List<String> tableLstTmp = analyzeFromItemVisitor.getTableLst();
			this.tableLst.addAll(tableLstTmp);
			
			// Alias <=> Table
			Map<String,String>  aliasMapTmp = analyzeFromItemVisitor.getAliasMap();
			this.aliasMap.putAll(aliasMapTmp);
			
			// Schema List
			Map<String,List<String>> schemaTableMap = analyzeFromItemVisitor.getSchemaTableMap();
			schemaTableMap.forEach( (k,v)->this.schemaLst.add(k) );
			
			// add SQLBag
			SQLBag sqlBag = new SQLBag();
			// error => "exec sp_databases;"
			sqlBag.setSQL(stmt.toString());
			sqlBag.setCommand(analyzeStatementVisitor.getCommand());
			sqlBag.setType(analyzeStatementVisitor.getType());
			this.sqlBagLst.add(sqlBag);
		}
	}
}
