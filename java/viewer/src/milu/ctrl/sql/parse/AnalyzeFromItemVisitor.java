package milu.ctrl.sql.parse;

import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.statement.select.FromItemVisitor;

public interface AnalyzeFromItemVisitor extends FromItemVisitor 
{
	public List<String> getTableLst();
	
	public Map<String,String> getAliasMap();
	
	public Map<String,List<String>> getSchemaTableMap();
}
