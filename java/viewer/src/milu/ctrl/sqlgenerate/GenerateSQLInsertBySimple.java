package milu.ctrl.sqlgenerate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public class GenerateSQLInsertBySimple extends GenerateSQLAbstract 
{

	@Override
	public String generate(SchemaEntity selectedEntity, MyDBAbstract myDBAbs) 
	{
		String name = selectedEntity.getName();
		List<Map<String,String>>  definitionLst = selectedEntity.getDefinitionLst();
		
		String schemaName = this.getSchema(selectedEntity,myDBAbs);
		
		String lineSeparator = System.getProperty("line.separator");
		
		// ===================================
		// INSERT INTO USERS
		// (
		// -----------------------------------
		//   ID,
		//   NAME
		// -----------------------------------
		// )
		// VALUES
		// (
		// -----------------------------------
		//   ?,	-- # ID
		//   ?	-- # NAME
		// -----------------------------------
		// )
		// ===================================
		String strSQLHead = 
			definitionLst.stream()
				.map( data->data.get("column_name") )
				.collect
				(
					Collectors.joining
					(
						","+lineSeparator+"\t",
						"INSERT INTO "+
							((schemaName == null) ? "":schemaName+"." )+
							name+lineSeparator+"("+lineSeparator+"\t",
						lineSeparator+")"+lineSeparator+"VALUES"+lineSeparator+"("+lineSeparator
					)
				);
		String strSQLTail = 
			definitionLst.stream()
				.map
				( 
					(data)->
					{
						if ( definitionLst.lastIndexOf(data) == (definitionLst.size()-1) )
						{
							return "?\t--# "+data.get("column_name");
						}
						else
						{
							return "?,\t--# "+data.get("column_name");
						}
					}
				)
				.collect
				(
					Collectors.joining
					(
						lineSeparator+"\t",
						"\t",
						lineSeparator+")"
					)
				);
		
		return strSQLHead + strSQLTail;
	}

}
