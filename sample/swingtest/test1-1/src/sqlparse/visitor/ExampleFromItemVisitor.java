package sqlparse.visitor;

import java.util.Map;
import java.util.HashMap;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;

import net.sf.jsqlparser.expression.Alias;

public class ExampleFromItemVisitor implements FromItemVisitor 
{
	private Map<String,String> tableMap = new HashMap<>();
	
	public Map<String,String> getTableMap()
	{
		return this.tableMap;
	}
	
	@Override
	public void visit(Table table) {
		System.out.println( "FromItemVisitor:table[" + table + "]" );
		Alias alias = table.getAlias();
		if ( alias == null )
		{
			//System.out.println( "Table[" + table.getName() + "]" );
			this.tableMap.put( table.getName() , null );
		}
		else
		{
			//System.out.println( "Table[" + table.getName() + "]Alias[" + alias.getName() + "]" );
			this.tableMap.put( table.getName() , alias.getName() );
		}
	}

	@Override
	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LateralSubSelect arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ValuesList arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TableFunction arg0) {
		// TODO Auto-generated method stub

	}

}
