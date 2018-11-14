package milu.gui.stmt.prepare;

import java.util.ArrayList;
import java.util.List;

public class PrepareExampleOracle extends PrepareExampleAbs {

	@Override
	public String getSQL() {
		String sql = "select * from all_objects \n" + 
				"where \n" + 
				" status = ? -- A\n" + 
				" and\r\n" + 
				" namespace = ? -- B\n" + 
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
		dataRow1.add("INVALID");
		dataRow1.add(1);
		List<Object> dataRow2 = new ArrayList<>();
		dataRow2.add("INVALID");
		dataRow2.add(2);
		List<List<Object>> dataRowLst = new ArrayList<>();
		dataRowLst.add(dataRow1);
		dataRowLst.add(dataRow2);

		return dataRowLst;
	}

}
