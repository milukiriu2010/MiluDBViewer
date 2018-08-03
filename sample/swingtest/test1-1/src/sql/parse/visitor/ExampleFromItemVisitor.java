package sql.parse.visitor;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesisFromItem;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.Node;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.parser.Token;

public class ExampleFromItemVisitor implements FromItemVisitor 
{
	private Map<String,String> tableAliasMap = new HashMap<>();
	
	private Map<String,List<String>> schemaTableMap = new HashMap<>();
	
	public Map<String,String> getTableAliasMap()
	{
		return this.tableAliasMap;
	}
	
	public Map<String,List<String>> getSchemaTableMap()
	{
		return this.schemaTableMap;
	}
	
	@Override
	public void visit(Table table) 
	{
		System.out.println( "FromItemVisitor:Table:" + table );
		SimpleNode simpleNode = table.getASTNode();
		if ( simpleNode != null )
		{
			System.out.println( "FromItemVisitor:Table:SimpleNode[" + simpleNode.toString() + "]" );		
			Token tokenFirst = simpleNode.jjtGetFirstToken();
			System.out.println( "FromItemVisitor:Table:SimpleNode:tokenFirst[" + tokenFirst.toString() + "]" );
			Token tokenLast  = simpleNode.jjtGetLastToken();
			System.out.println( "FromItemVisitor:Table:SimpleNode:tokenLast[" + tokenLast.toString() + "]" );
			for ( int i = 0; i < simpleNode.jjtGetNumChildren(); i++ )
			{
				Node node = simpleNode.jjtGetChild(i);
				System.out.println( "FromItemVisitor:Table:SimpleNode:Node[" + node.toString() + "]" );
			}
		}
		
		Alias alias = table.getAlias();
		if ( alias == null )
		{
			this.tableAliasMap.put( table.getName() , null );
		}
		else
		{
			this.tableAliasMap.put( table.getName() , alias.getName() );
		}
		
		String schemaName = table.getSchemaName();
		if ( schemaName != null )
		{
			if ( this.schemaTableMap.containsKey(schemaName) )
			{
				List<String> tableLst = this.schemaTableMap.get(schemaName);
				tableLst.add(table.getName());
			}
			else
			{
				List<String> tableLst = new ArrayList<>();
				tableLst.add(table.getName());
				this.schemaTableMap.put(schemaName,tableLst);
			}
		}
	}

	@Override
	public void visit(SubSelect subSelect ) {
		System.out.println( "FromItemVisitor:SubSelect:" + subSelect );

	}

	@Override
	public void visit(SubJoin subJoin) {
		System.out.println( "FromItemVisitor:SubJoin:" + subJoin );

	}

	@Override
	public void visit(LateralSubSelect lateralSubSelect) {
		System.out.println( "FromItemVisitor:LateralSubSelect:" + lateralSubSelect );

	}

	@Override
	public void visit(ValuesList valuesList) {
		System.out.println( "FromItemVisitor:ValuesList:" + valuesList );

	}

	@Override
	public void visit(TableFunction tableFunction) {
		System.out.println( "FromItemVisitor:TableFunction:" + tableFunction );

	}

	// 1.3
	@Override
	public void visit(ParenthesisFromItem parenthesisFromItem) {
		System.out.println( "FromItemVisitor:ParenthesisFromItem:" + parenthesisFromItem );

	}
}
