package sql.parse.visitor;

import java.util.List;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;

import net.sf.jsqlparser.parser.Node;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.parser.Token;

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
		SimpleNode simpleNode = plainSelect.getASTNode();
		System.out.println( "SelectVisitor:PlainSelect:SimpleNode[" + simpleNode.toString() + "]" );
		Token tokenFirst = simpleNode.jjtGetFirstToken();
		System.out.println( "SelectVisitor:PlainSelect:SimpleNode:tokenFirst[" + tokenFirst.toString() + "]" );
		Token tokenLast  = simpleNode.jjtGetLastToken();
		System.out.println( "SelectVisitor:PlainSelect:SimpleNode:tokenLast[" + tokenLast.toString() + "]" );
		for ( int i = 0; i < simpleNode.jjtGetNumChildren(); i++ )
		{
			Node node = simpleNode.jjtGetChild(i);
			System.out.println( "SelectVisitor:PlainSelect:SimpleNode:Node[" + node.toString() + "]" );
		}
		
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
