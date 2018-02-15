package sqlparse;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import net.sf.jsqlparser.expression.Expression;

import net.sf.jsqlparser.schema.Table;

import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

import net.sf.jsqlparser.JSQLParserException;

public class JSQLParseExample1
{
	public static void main( String...arg )
	{
		try
		{
			String sqlStr = "SELECT u.first_name fn, u.last_name ln, c.name cn FROM user u, country c WHERE u.cid = c.cid";
			Select select = (Select)CCJSqlParserUtil.parse(sqlStr);
			System.out.println(select.getSelectBody());
	
			System.out.println( "=== getExpression =====================" );
			PlainSelect pl = (PlainSelect)select.getSelectBody();
			for (SelectItem item : pl.getSelectItems())
			{
			    System.out.println(item.toString());
			    
		    	Expression expression = ((SelectExpressionItem)item).getExpression();
		    	System.out.println("Expression["+expression+"]");
			}
			
			System.out.println( "=== getIntoTables =====================" );
			List<Table> tblLst = pl.getIntoTables();
			if ( tblLst != null )
			{
				for ( int i = 0; i < tblLst.size(); i++ )
				{
					Table tbl = tblLst.get(i);
					System.out.println( "TABLE:" + tbl.getName() );
				}
			}
			
			System.out.println( "=== getJoins =====================" );
			List<Join> joinLst = pl.getJoins();
			if ( joinLst != null )
			{
				for ( int i = 0; i < joinLst.size(); i++ )
				{
					Join join = joinLst.get(i);
					System.out.println( "JOIN:" + join );
				}
			}
			
			System.out.println( "=== TablesNamesFinder =====================" );
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List<String> tblLst2 = tablesNamesFinder.getTableList(select);
			for ( int i = 0; i < tblLst2.size(); i++ )
			{
				String tbl2 = tblLst2.get(i);
				System.out.println( "TABLE:" + tbl2 );
			}			
		}
		catch ( JSQLParserException jsqlEx )
		{
			jsqlEx.printStackTrace();
		}
	}
}
