package milu.ctrl.sqlparse;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;

import net.sf.jsqlparser.expression.Alias;

public class TableFromItemVisitor implements AnalyzeFromItemVisitor 
{
	// Table List
	private List<String>  tableLst = new ArrayList<>();
	
	// Alias <=> Table
	private Map<String,String>  aliasMap = new HashMap<>();
	
	// Schema has Table List
	private Map<String,List<String>> schemaTableMap = new HashMap<>();
	
	@Override
	public List<String> getTableLst()
	{
		return this.tableLst;
	}
	
	@Override
	public Map<String,String> getAliasMap()
	{
		return this.aliasMap;
	}
	
	@Override
	public Map<String,List<String>> getSchemaTableMap()
	{
		return this.schemaTableMap;
	}

	@Override
	public void visit(Table table) 
	{
		System.out.println( "TableFromItemVisitor.visit:" + table );
		String tableName = table.getName();
		if ( this.tableLst.contains( tableName ) == false )
		{
			this.tableLst.add( tableName );
		}
		Alias alias = table.getAlias();
		if ( alias != null )
		{
			this.aliasMap.put( alias.getName() , tableName );
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
	public void visit(SubSelect arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SubJoin arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LateralSubSelect arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ValuesList arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TableFunction arg0)
	{
		// TODO Auto-generated method stub

	}

}
