package milu.gui.ctrl.imp;

public enum ImportData 
{
	DST_SCHEMA_ENTITY("dstSchemaEntity"),
	TABLE_NAME_PATH("tableNamePath"),
	SRC_FILE("SrcFile"),
	IMPORT_HEAD_LST("importHeadLst"),
	IMPORT_DATA_LST("importDataLst"),
	SKIP_ROW_COUNT("skipRowCount")
	;
	
	private String val = null;
	
	private ImportData( String val )
	{
		this.val = val;
	}
	
	public String val()
	{
		return this.val;
	}
}
