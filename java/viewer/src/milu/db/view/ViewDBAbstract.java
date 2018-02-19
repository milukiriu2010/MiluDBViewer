package milu.db.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.SQLException;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class ViewDBAbstract
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// View Name List
	protected List<Map<String,String>> viewLst = new ArrayList<>();
	
	public ViewDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.viewLst.clear();
	}
	
	public List<SchemaEntity> getViewEntityLst()
	{
		List<SchemaEntity>  viewEntityLst = new ArrayList<>();
		for ( Map<String,String> view : this.viewLst )
		{
			SchemaEntity eachViewEntity = SchemaEntityFactory.createInstance( view.get("viewName"), SchemaEntity.SCHEMA_TYPE.VIEW );
			String strStatus = view.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachViewEntity.setState( SchemaEntity.STATE.INVALID );
			}
			viewEntityLst.add( eachViewEntity );
		}
		return viewEntityLst;
	}	
	
	abstract public void selectViewLst( String schemaName ) throws SQLException;
	
	abstract protected String viewLstSQL( String schemaName );
}
