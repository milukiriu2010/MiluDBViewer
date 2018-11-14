package milu.gui.stmt.prepare;

import java.util.ArrayList;
import java.util.List;

public class PrepareExampleMySQL extends PrepareExampleAbs {

	@Override
	public String getSQL() {
		String sql = "select * from information_schema.tables \n" + 
				"where \n" + 
				" table_schema = ? -- A\n" + 
				" and \n" + 
				" table_type = ? -- B\n" + 
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
		dataRow1.add("information_schema");
		dataRow1.add("SYSTEM VIEW");
		List<Object> dataRow2 = new ArrayList<>();
		dataRow2.add("mysql");
		dataRow2.add("BASE TABLE");
		List<List<Object>> dataRowLst = new ArrayList<>();
		dataRowLst.add(dataRow1);
		dataRowLst.add(dataRow2);

		return dataRowLst;
	}

}
