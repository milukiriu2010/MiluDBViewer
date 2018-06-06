package sql.format;

import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.schema.Column;

public class Example2ExpressionDeParser extends ExpressionDeParser 
{
	// @Override
	public void	visit​(Column tableColumn)
	{
		super.visit(tableColumn);
		System.out.println( "Column:" + tableColumn );
		StringBuilder buffer = this.getBuffer();
		buffer.append( tableColumn + "\n" );
	}
}
