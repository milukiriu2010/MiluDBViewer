package milu.ctrl.sql.generate;

public class GenerateSQLFactory 
{
	public enum TYPE
	{
		SELECT,
		INSERT_BY_NAME,
		INSERT_BY_SIMPLE,
		UPDATE_BY_NAME,
		UPDATE_BY_SIMPLE,
		DELETE
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
		
		return gsAbs;
	}
}
