package milu.gui.stmt.prepare;

import java.util.ArrayList;
import java.util.List;

public class PrepareExampleSQLite extends PrepareExampleAbs {

	@Override
	public String getSQL() {
		String sql = "select * from sqlite_master \n" +
				"where \n" + 
				" type = ? -- A\n" + 
				"";
		return sql;
	}

	@Override
	public List<Object> getHeadLst() {
		List<Object> headLst = new ArrayList<>();
		headLst.add("A");
		return headLst;
	}

	@Override
	public List<List<Object>> getDataRowLst() {
		List<Object> dataRow1 = new ArrayList<>();
		dataRow1.add("table");
		List<Object> dataRow2 = new ArrayList<>();
		dataRow2.add("view");
		List<List<Object>> dataRowLst = new ArrayList<>();
		dataRowLst.add(dataRow1);
		dataRowLst.add(dataRow2);

		return dataRowLst;
	}

}
