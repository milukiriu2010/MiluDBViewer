package milu.ctrl.sql.generate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public class GenerateSQLCreateTable extends GenerateSQLAbstract {

	@Override
	public String generate(SchemaEntity selectedEntity, MyDBAbstract myDBAbs) 
	{
		String name = selectedEntity.getName();
		List<Map<String,String>>  definitionLst = selectedEntity.getDefinitionLst();
		
		String schemaName = this.getSchema(selectedEntity,myDBAbs);
		String tableName = null;
		if ( schemaName != null )
		{
			tableName = schemaName + "." + name;
		}
		else
		{
			tableName = name;
		}
		
		String lineSeparator = System.getProperty("line.separator");
		
		// Column Name
		String strSQL =
				definitionLst.stream()
					.map( (mapObj)->{
						String colName  = mapObj.get("column_name");
						String dataType = mapObj.get("data_type");
						String dataSize = mapObj.get("data_size");
						String dataType2 = "";
						if ( dataSize == null )
						{
							dataType2 = dataType;
						}
						else
						{
							// Oracle => TIMESTAMP(*)
							if ( dataType.contains("(") )
							{
								dataType2 = dataType;
							}
							else
							{
								dataType2 = dataType + "(" + dataSize + ")";
							}
						}
						
						String nullAble = mapObj.get("nullable");
						if ( "NULL NG".equals(nullAble) )
						{
							return "\t" + colName + "\t\t" + dataType2 + "\t" + "NOT NULL";
						}
						else
						{
							return "\t" + colName + "\t\t" + dataType2;
						}
					})
					.collect
					(
						Collectors.joining
						(
							","+lineSeparator,
							"CREATE TABLE " + tableName + lineSeparator + "(" + lineSeparator,
							lineSeparator+")"+lineSeparator
						)
					);

		return strSQL;
	}

}
