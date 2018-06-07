package sql.format;

import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectVisitor;
//import net.sf.jsqlparser.util.deparser.SelectDeParser;

public class Example2ExpressionDeParser extends ExpressionDeParser 
{
	private StringBuilder buffer = null;
	
	private SelectVisitor selectorVisitor = null;
	
	@Override
	public void setBuffer( StringBuilder buffer )
	{
		System.out.println( "setBuffer" );
		this.buffer = buffer;
		super.setBuffer(buffer);
	}
	
	@Override
	public void setSelectVisitor( SelectVisitor selectorVisitor )
	{
		System.out.println( "setSelectVisitor" );
		this.selectorVisitor = selectorVisitor;
		super.setSelectVisitor(selectorVisitor);
	}
	
	// @Override
	public void	visit​(Column tableColumn)
	{
		System.out.println( "Column:" + tableColumn );
		//super.visit(tableColumn);
		this.buffer.append( tableColumn + "\n" );
	}
}
