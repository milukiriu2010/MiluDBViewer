package sql.parse.visitor;

import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.OrderByVisitor;

public class ExampleOrderByVisitor implements OrderByVisitor {

	@Override
	public void visit(OrderByElement arg0) {
		System.out.println( "OrderByElement:" + arg0 );

	}

}
