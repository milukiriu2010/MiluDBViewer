package sql.parse.visitor;

import java.util.List;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;

public class ExampleSelectVisitor implements SelectVisitor 
{
	private FromItemVisitor fromItemVisitor = null;
	
	public ExampleSelectVisitor( FromItemVisitor fromItemVisitor )
	{
		this.fromItemVisitor = fromItemVisitor;
	}

	@Override
	public void visit(PlainSelect plainSelect) 
	{
		System.out.println( "SelectVisitor:PlainSelect[" + plainSelect + "]" );
		
		List<Join>   joinLst = plainSelect.getJoins();
		if ( joinLst != null )
		{
			for ( Join join : joinLst )
			{
				FromItem  fromItemInJoin = join.getRightItem();
				fromItemInJoin.accept( this.fromItemVisitor );
			}
		}
		
		FromItem fromItem = plainSelect.getFromItem();
		if ( fromItem != null )
		{
			fromItem.accept( this.fromItemVisitor );
		}
	}

	@Override
	public void visit(SetOperationList arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(WithItem arg0) {
		// TODO Auto-generated method stub

	}

}
