package sql.parse.visitor;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
		System.out.println( "FromItemVisitor:table[" + table + "]" );
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
