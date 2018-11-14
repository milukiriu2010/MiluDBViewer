package milu.gui.stmt.prepare;

import java.util.ArrayList;
import java.util.List;

public class PrepareExamplePostgres extends PrepareExampleAbs {

	@Override
	public String getSQL() {
		String sql = "select * from pg_class c join\n" +
				"  pg_namespace n on c.relnamespace = n.oid \n" +
				"where \n" + 
				" c.relkind = ? -- A\n" + 
				" and\n" + 
				" n.nspname = ? -- B\n" + 
				"";
		return sql;
	}

	@Override
	public List<Object> getHeadLst() {
		List<Object> headLst = new ArrayList<>();
		headLst.add("A");
		headLst.add("B");
		return headLst;
	}

	@Override
	public List<List<Object>> getDataRowLst() {
		List<Object> dataRow1 = new ArrayList<>();
		dataRow1.add("r");
		dataRow1.add("public");
		List<Object> dataRow2 = new ArrayList<>();
		dataRow2.add("v");
		dataRow2.add("public");
		List<List<Object>> dataRowLst = new ArrayList<>();
		dataRowLst.add(dataRow1);
		dataRowLst.add(dataRow2);

		return dataRowLst;
	}

}
