package sql.parse.visitor;

import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

public class ExampleSelectItemVisitor implements SelectItemVisitor 
{

	@Override
	public void visit( AllColumns allColumns )
	{
		System.out.println( "AllColumns:" + allColumns );

	}

	@Override
	public void visit( AllTableColumns allTableColumns )
	{
		System.out.println( "AllTableColumns:" + allTableColumns );

	}

	@Override
	public void visit( SelectExpressionItem selectExpItem ) 
	{
		System.out.println( "SelectExpressionItem:" + selectExpItem );
	}

}
