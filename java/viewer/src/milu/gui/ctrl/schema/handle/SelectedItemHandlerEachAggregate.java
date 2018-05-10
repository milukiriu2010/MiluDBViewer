package milu.gui.ctrl.schema.handle;

import milu.db.obj.abs.AbsDBFactory;
import milu.gui.ctrl.schema.SchemaProcViewTab;

import java.sql.SQLException;

/**
 * This class is invoked, when "aggregate" item is clicked on SchemaTreeView.
 * Show "Aggregate Source"
 * ---------------------------------------
 * [ROOT]
 * - [SCHEMA]
 *   - [ROOT_TABLE]
 *     - [TABLE]
 *   - [ROOT_VIEW]
 *     - [VIEW]
 *   - [ROOT_FUNC]
 *     - [FUNC]
 *   - [ROOT_AGGREGATE]
 *     - [AGGREGATE] => selected
 * ---------------------------------------
 * @author milu
 *
 */
public class SelectedItemHandlerEachAggregate extends SelectedItemHandlerAbstract
{
	@Override
	public void exec()
		throws
			UnsupportedOperationException,
			SQLException
	{
		/*
		SchemaEntity selectedEntity = this.itemSelected.getValue();
		TreeItem<SchemaEntity> itemParent = itemSelected.getParent();
		String schemaName     = itemParent.getParent().getValue().toString();
		String aggregateName  = itemSelected.getValue().getName();
		//String id             = schemaName + "@aggregate@" + aggregateName;
		String id         = schemaName + this.strPartUserData + aggregateName;
		System.out.println( "setAggregateDef:" + aggregateName );
		
		// Activate DBSchemaTableViewTab, if already exists.
		// Delete DBSchemaTableViewTab, if already exists.
		if ( MANIPULATE_TYPE.SELECT.equals(this.manipulateSpecifiedTab( SchemaProcViewTab.class , id )) )
		{
			return;
		}
		
		// Create DBSchemaProcViewTab, if it doesn't exist.
		SchemaProcViewTab newTab = new SchemaProcViewTab( this.dbView );
		//newTab.setId( id );
		newTab.setUserData( id );
		newTab.setText( aggregateName );
		this.tabPane.getTabs().add( newTab );
		this.tabPane.getSelectionModel().select( newTab );
		
		// set icon on Tab
		MainController mainCtrl = this.dbView.getMainController();
		Node imageGroup = MyTool.createImageView( 16, 16, mainCtrl, selectedEntity );
		newTab.setGraphic( imageGroup );
		
		// get aggregate ddl
		String strSrc = selectedEntity.getSrcSQL();
		if ( strSrc == null )
		{
			ObjDBFactory objDBFactory = AbsDBFactory.getFactory( AbsDBFactory.FACTORY_TYPE.AGGREGATE );
			if ( objDBFactory == null )
			{
				return;
			}
			ObjDBInterface objDBInf = objDBFactory.getInstance(myDBAbs);
			if ( objDBInf == null )
			{
				return;
			}
			strSrc = objDBInf.getSRC( schemaName, aggregateName );
			selectedEntity.setSrcSQL(strSrc);
		}
		
		// set aggregate source in SqlTextArea
		newTab.setSrcText( strSrc );
		*/
		
		this.loadSource( AbsDBFactory.FACTORY_TYPE.AGGREGATE, SchemaProcViewTab.class );
	}
	
}
