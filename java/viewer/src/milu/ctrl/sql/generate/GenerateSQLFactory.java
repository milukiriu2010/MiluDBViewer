package milu.ctrl.sql.generate;

public class GenerateSQLFactory 
{
	public enum TYPE
	{
		SELECT,
		INSERT_BY_NAME,
		INSERT_BY_SIMPLE,
		INSERT_BY_SIMPLE_WITHOUT_COMMENT,
		UPDATE_BY_NAME,
		UPDATE_BY_SIMPLE,
		DELETE,
		CREATE_TABLE
	}

	public static GenerateSQLAbstract getInstance( TYPE type )
	{
		GenerateSQLAbstract gsAbs = null;
		if ( TYPE.SELECT.equals(type) )
		{
			gsAbs = new GenerateSQLSelect();
		}
		else if ( TYPE.INSERT_BY_NAME.equals(type) )
		{
			gsAbs = new GenerateSQLInsertByName();
		}
		else if ( TYPE.INSERT_BY_SIMPLE.equals(type) )
		{
			gsAbs = new GenerateSQLInsertBySimple();
		}
		else if ( TYPE.INSERT_BY_SIMPLE_WITHOUT_COMMENT.equals(type) )
		{
			gsAbs = new GenerateSQLInsertBySimpleWithoutComment();
		}
		else if ( TYPE.UPDATE_BY_NAME.equals(type) )
		{
			gsAbs = new GenerateSQLUpdateByName();
		}
		else if ( TYPE.UPDATE_BY_SIMPLE.equals(type) )
		{
			gsAbs = new GenerateSQLUpdateBySimple();
		}
		else if ( TYPE.DELETE.equals(type) )
		{
			gsAbs = new GenerateSQLDelete();
		}
		else if ( TYPE.CREATE_TABLE.equals(type) )
		{
			gsAbs = new GenerateSQLCreateTable();
		}
		
		return gsAbs;
	}
}
