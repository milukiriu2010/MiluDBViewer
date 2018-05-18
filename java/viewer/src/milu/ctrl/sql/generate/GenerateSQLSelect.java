package milu.ctrl.sql.generate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public class GenerateSQLSelect extends GenerateSQLAbstract 
{

	@Override
	public String generate(SchemaEntity selectedEntity, MyDBAbstract myDBAbs ) 
	{
		String name = selectedEntity.getName();
		List<Map<String,String>>  definitionLst = selectedEntity.getDefinitionLst();
		
		String schemaName = this.getSchema(selectedEntity,myDBAbs);
		
		String lineSeparator = System.getProperty("line.separator");
		// ===================================
		// SELECT
		// -----------------------------------
		//   ID,
		//   NAME
		// -----------------------------------
		// FROM
		//   USERS
		// ===================================
		String strSQL = 
			definitionLst.stream()
				.map( data->data.get("column_name") )
				.collect
				(
					Collectors.joining
					(
						","+lineSeparator+"\t",
						"SELECT"+lineSeparator+"\t",
						lineSeparator+"FROM"+lineSeparator+"\t"+
							((schemaName == null) ? "":schemaName+"." )+
							name
					)
				);
		
		return strSQL;
	}

}
