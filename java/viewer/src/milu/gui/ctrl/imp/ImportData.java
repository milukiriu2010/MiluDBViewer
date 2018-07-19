package milu.gui.ctrl.imp;

public enum ImportData 
{
	DST_SCHEMA_ENTITY("DstSchemaEntity"),
	TABLE_NAME_PATH("tableNamePath"),
	SRC_TYPE("SrcType"),
	SRC_FILE("SrcFile"),
	SRC_SCHEMA_ENTITY("SrcSchemaEntity"),
	SRC_DB("SrcDB"),
	SRC_TABLE("SrcTable"),
	SRC_FETCH_POS("SrcFetchPos"),
	SRC_FETCH_MAX("SrcFetchMax"),
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
