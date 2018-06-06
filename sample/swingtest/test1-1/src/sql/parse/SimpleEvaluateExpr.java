package sql.parse;

import java.util.List;
import java.util.Stack;
import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

import net.sf.jsqlparser.parser.CCJSqlParserManager;

public class SimpleEvaluateExpr
{
    public static void main( String[] args ) throws JSQLParserException {
    	try
    	{
			evaluate("4+5*6");
			evaluate("4*5+6");
			evaluate("4*(5+6)");
			evaluate("4*(5+6)*(2+3)");
			
			simpleSQL();
			
			example1();
			
			example2();

    	}
    	catch (JSQLParserException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void evaluate(String expr) throws JSQLParserException
    {
		final Stack<Long> stack = new Stack<Long>();
		System.out.println("expr=" + expr);
		Expression parseExpression = CCJSqlParserUtil.parseExpression(expr);
		ExpressionDeParser deparser = new ExpressionDeParser() {
		    @Override
		    public void visit(Addition addition) {
			super.visit(addition); 
			
			long sum1 = stack.pop();
			long sum2 = stack.pop();
			
			stack.push(sum1 + sum2);
		    }
	
		    @Override
		    public void visit(Multiplication multiplication) {
			super.visit(multiplication); 
			
			long fac1 = stack.pop();
			long fac2 = stack.pop();
			
			stack.push(fac1 * fac2);
		    }
	
		    @Override
		    public void visit(LongValue longValue) {
			super.visit(longValue); 
			stack.push(longValue.getValue());
		    }
		};
		StringBuilder b = new StringBuilder();
		deparser.setBuffer(b);
		parseExpression.accept(deparser);
		
		System.out.println(expr + " = " + stack.pop() );
		System.out.println( "===========================" );
    }
    
    // https://github.com/JSQLParser/JSqlParser/wiki/Examples-of-SQL-parsing
    public static void simpleSQL() throws JSQLParserException
    {
    	Statement statement = 
    			CCJSqlParserUtil.parse(
    				"SELECT " +
    				"  M1.ID, " +
    				"  M1.NAME, " +
    				"  M1.AGE,  " +
    				"  M2.* " +
    				"FROM " +
    				"  MY_TABLE1 M1, MY_TABLE2 M2 " + 
    				" WHERE " +
    				"M1.ID=M2.ID"
    			);
    	Select selectStatement = (Select) statement;
    	TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    	List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
    	for ( String table : tableList )
    	{
    		System.out.println( "TABLE:"+ table );
    	}
    	
    	
    	System.out.println( "===========================" );
    }
    
    // https://stackoverflow.com/questions/16768365/how-to-retrieve-table-and-column-names-from-sql-using-jsqlparse
    @SuppressWarnings("rawtypes")
    public static void example1() throws JSQLParserException
    {
        String statement=
        	"SELECT " +
        	"  LOCATION_D.REGION_NAME, " +
        	"  LOCATION_D.AREA_NAME, " +
        	"  COUNT(DISTINCT INCIDENT_FACT.TICKET_ID) " +
        	"FROM " +
        	"  LOCATION_D, " +
        	"  INCIDENT_FACT " + 
        	"WHERE " +
        	"  ( LOCATION_D.LOCATION_SK=INCIDENT_FACT.LOCATION_SK ) " +
        	"GROUP BY LOCATION_D.REGION_NAME, LOCATION_D.AREA_NAME"; 
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select=(Select) parserManager.parse(new StringReader(statement));

        PlainSelect plain=(PlainSelect)select.getSelectBody();     
        List selectitems=plain.getSelectItems();
        System.out.println(selectitems.size());
        for(int i=0;i<selectitems.size();i++)
        {
           Expression expression=((SelectExpressionItem) selectitems.get(i)).getExpression();  
           System.out.println("Expression:-"+expression);
           if ( expression instanceof Column)
           {
	           Column col=(Column)expression;
	           System.out.println(col.getTable()+","+col.getColumnName());
           }
           else if (expression instanceof Function)
           {
               Function function = (Function) expression;
               System.out.println(function.getAttribute()+","+function.getName()+","+function.getParameters());      

           }
        }
        System.out.println( "===========================" );
    }
    
    
    // https://stackoverflow.com/questions/16768365/how-to-retrieve-table-and-column-names-from-sql-using-jsqlparse
    @SuppressWarnings("rawtypes")
    public static void example2() throws JSQLParserException
    {
    	/*
        String statement=
        	"SELECT " +
        	"  LOCATION_D.REGION_NAME, " +
        	"  LOCATION_D.AREA_NAME, " +
        	"  COUNT(DISTINCT INF.TICKET_ID) " +
        	"FROM " +
        	"  LOCATION_D, " +
        	"  INCIDENT_FACT AS INF" + 
        	"WHERE " +
        	"  LOCATION_D.LOCATION_SK = INF.LOCATION_SK " +
        	"GROUP BY LOCATION_D.REGION_NAME, LOCATION_D.AREA_NAME";
        */
    	String statement =
    		"SELECT \n" +
    		"  A.LAST_NAME  AS LN, \n" +
    		"  A.FIRST_NAME AS FN  \n" +
    		"FROM \n" +
    		"  USER  A\n" +
    		"WHERE \n" +
    		"  A.ID = 1";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select=(Select) parserManager.parse(new StringReader(statement));

        PlainSelect plain=(PlainSelect)select.getSelectBody();     
        List selectitems=plain.getSelectItems();
        System.out.println(selectitems.size());
        for(int i=0;i<selectitems.size();i++)
        {
           Expression expression=((SelectExpressionItem) selectitems.get(i)).getExpression();  
           System.out.println("Expression:-"+expression);
           if ( expression instanceof Column)
           {
	           Column col=(Column)expression;
	           System.out.println("COLUMN:"+col.getTable()+","+col.getColumnName());
           }
           else if (expression instanceof Alias)
           {
               Alias alias = (Alias) expression;
               System.out.println("ALIAS:"+alias.getName());      
           }
           else if (expression instanceof Function)
           {
               Function function = (Function) expression;
               System.out.println("FUNCTION:"+function.getAttribute()+","+function.getName()+","+function.getParameters());      
           }
           System.out.println();
        }
        System.out.println( "===========================" );
    }
    
}
