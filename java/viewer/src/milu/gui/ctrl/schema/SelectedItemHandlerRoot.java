package milu.gui.ctrl.schema;

import java.sql.SQLException;

import milu.entity.schema.SchemaEntity;

/**
 * This class is invoked, when there is no root item on SchemaTreeView
 * @author milu
 *
 */
public class SelectedItemHandlerRoot extends SelectedItemHandlerAbstract
{
	/*
	public SelectedItemHandlerRoot
	( 
		SchemaTreeView schemaTreeView, 
		TabPane        tabPane,
		MyDBAbstract   myDBAbs,
		SelectedItemHandlerAbstract.REFRESH_TYPE  refreshType
	)
	{
		super( schemaTreeView, tabPane, myDBAbs, refreshType );
	}
	*/
	@Override
	protected boolean isMyResponsible()
	{
		if ( this.itemRoot == null )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		/*
		SchemaDBAbstract schemaDBAbs = SchemaDBFactory.getInstance( this.myDBAbs );
		if ( schemaDBAbs != null )
		{
			schemaDBAbs.selectSchemaLst();
			List<Map<String,String>> schemaNameLst = schemaDBAbs.getSchemaNameLst();
			this.schemaTreeView.setInitialData( this.myDBAbs.getUrl(), schemaNameLst, this.myDBAbs.getSupportedTypeLst() );
		}
		*/
		
		SchemaEntity rootEntity = this.myDBAbs.getSchemaRoot();
		if ( rootEntity != null )
		{
			this.schemaTreeView.setInitialData( rootEntity );
		}
		
	}
	
}
