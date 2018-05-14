package milu.ctrl.sqlgenerate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public class GenerateSQLUpdateByName extends GenerateSQLAbstract 
{

	@Override
	public String generate(SchemaEntity selectedEntity, MyDBAbstract myDBAbs) 
	{
		String name = selectedEntity.getName();
		List<Map<String,String>>  definitionLst = selectedEntity.getDefinitionLst();
		
		String schemaName = this.getSchema(selectedEntity,myDBAbs);
		
		String lineSeparator = System.getProperty("line.separator");
		
		// ===================================
		// UPDATE USERS
		// SET
		// -----------------------------------
		//   ID   = :i_ID,
		//   NAME = :i_NAME
		// -----------------------------------
		// ===================================
		String strSQL = 
			definitionLst.stream()
				.map( data->data.get("column_name")+"\t= :i_"+data.get("column_name") )
				.collect
				(
					Collectors.joining
					(
						","+lineSeparator+"\t",
						"UPDATE "+
							((schemaName == null) ? "":schemaName+"." )+
							name+lineSeparator+
							"SET"+lineSeparator+"\t",
						""
					)
				);
		return strSQL;
	}

}
