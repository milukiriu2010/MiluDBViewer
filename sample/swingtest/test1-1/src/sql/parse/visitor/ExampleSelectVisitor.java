package sql.parse.visitor;

import java.util.Collections;
import java.util.List;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.OrderByVisitor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

import net.sf.jsqlparser.parser.Node;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.parser.Token;

public class ExampleSelectVisitor implements SelectVisitor 
{
	private SelectItemVisitor selectItemVisitor = null;
	private FromItemVisitor   fromItemVisitor   = null;
	private ExpressionVisitor expVisitor        = null;
	private OrderByVisitor    orderByVisitor    = null;
	
	public ExampleSelectVisitor( 
			SelectItemVisitor selectItemVisitor, 
			FromItemVisitor   fromItemVisitor,
			ExpressionVisitor expVisitor,
			OrderByVisitor    orderByVisitor
	)
	{
		this.selectItemVisitor = selectItemVisitor;
		this.fromItemVisitor   = fromItemVisitor;
		this.expVisitor        = expVisitor;
		this.orderByVisitor    = orderByVisitor;
	}
	
	private void skimNode( Node node, int level )
	{
		for ( int i = 0; i < node.jjtGetNumChildren(); i++ )
		{
			Node nodeChild = node.jjtGetChild(i);
			System.out.println( "SelectVisitor:PlainSelect:Node:Node[" + level + ":" +
					String.join("", Collections.nCopies(level, "  ") ) +
					nodeChild.toString() + "]" );
			this.skimNode(nodeChild, level+1 );
		}
	}

	@Override
	public void visit(PlainSelect plainSelect) 
	{
		System.out.println( "SelectVisitor:PlainSelect[" + plainSelect + "]" );
		SimpleNode simpleNode = plainSelect.getASTNode();
		System.out.println( "SelectVisitor:PlainSelect:SimpleNode[" + simpleNode.toString() + "]" );
		Token tokenFirst = simpleNode.jjtGetFirstToken();
		System.out.println( "SelectVisitor:PlainSelect:SimpleNode:tokenFirst[" + tokenFirst.toString() + "]" );
		Token tokenLast  = simpleNode.jjtGetLastToken();
		System.out.println( "SelectVisitor:PlainSelect:SimpleNode:tokenLast[" + tokenLast.toString() + "]" );
		this.skimNode(simpleNode, 0);
		
		// select
		List<SelectItem> selectItemLst = plainSelect.getSelectItems();
		if ( selectItemLst != null )
		{
			for ( SelectItem selectItem : selectItemLst )
			{
				selectItem.accept( this.selectItemVisitor );
			}
		}
		
		// from
		FromItem fromItem = plainSelect.getFromItem();
		if ( fromItem != null )
		{
			fromItem.accept( this.fromItemVisitor );
		}
		
		// join
		List<Join>   joinLst = plainSelect.getJoins();
		if ( joinLst != null )
		{
			for ( Join join : joinLst )
			{
				FromItem   fromItemInJoin = join.getRightItem();
				if ( fromItem != null )
				{
					fromItemInJoin.accept( this.fromItemVisitor );
				}
				Expression joinExp = join.getOnExpression();
				if ( joinExp != null )
				{
					joinExp.accept( this.expVisitor );
				}
			}
		}
		
		// where
		Expression exp = plainSelect.getWhere();
		if ( exp != null )
		{
			exp.accept( this.expVisitor );
		}
		
		// group by
		List<Expression>  groupExpLst = plainSelect.getGroupByColumnReferences();
		if ( groupExpLst != null )
		{
			for ( Expression groupExp: groupExpLst )
			{
				groupExp.accept(this.expVisitor);
			}
		}
		
		// having
		Expression hav = plainSelect.getHaving();
		if ( hav != null )
		{
			hav.accept(this.expVisitor);
		}
		
		// order by
		List<OrderByElement>  orderByLst = plainSelect.getOrderByElements();
		if ( orderByLst != null )
		{
			for ( OrderByElement orderBy : orderByLst )
			{
				orderBy.accept(this.orderByVisitor);
			}
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
