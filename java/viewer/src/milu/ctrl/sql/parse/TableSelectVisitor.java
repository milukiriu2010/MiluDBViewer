package milu.ctrl.sql.parse;

import java.util.List;

import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

public class TableSelectVisitor implements AnalyzeSelectVisitor 
{
	private AnalyzeFromItemVisitor  analyzeFromItemVisitor = null;

	@Override
	public void visit(PlainSelect plainSelect)
	{
		List<Join>   joinLst = plainSelect.getJoins();
		if ( joinLst != null )
		{
			for ( Join join : joinLst )
			{
				FromItem  fromItemInJoin = join.getRightItem();
				fromItemInJoin.accept( this.analyzeFromItemVisitor );
			}
		}
		
		FromItem fromItem = plainSelect.getFromItem();
		if ( fromItem != null )
		{
			fromItem.accept( this.analyzeFromItemVisitor );
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

	@Override
	public void setAnalyzeFromItemVisitor(AnalyzeFromItemVisitor analyzeFromItemVisitor) 
	{
		this.analyzeFromItemVisitor = analyzeFromItemVisitor;
	}

}
