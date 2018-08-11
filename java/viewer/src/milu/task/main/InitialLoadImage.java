package milu.task.main;

import javafx.scene.image.Image;

public class InitialLoadImage extends InitialLoadAbstract 
{

	@Override
	public void load() 
	{
		String[] images =
			{
				// SchemaEntity
				"file:resources/images/index_p.png",
				"file:resources/images/index_u.png",
				"file:resources/images/index_f.png",
				"file:resources/images/index_fk.png",
				"file:resources/images/index_i.png",
				"file:resources/images/order_a.png",
				"file:resources/images/order_d.png",
				"file:resources/images/column.png",
				"file:resources/images/aggregate.png",
				"file:resources/images/func.png",
				"file:resources/images/materialized_view.png",
				"file:resources/images/package_body.png",
				"file:resources/images/package_def.png",
				"file:resources/images/proc.png",
				"file:resources/images/schema.png",			// MainToolBar,DBSchemaTab
				"file:resources/images/seq.png",
				"file:resources/images/systemview.png",
				"file:resources/images/table.png",
				"file:resources/images/trigger.png",
				"file:resources/images/type.png",
				"file:resources/images/view.png",
				"file:resources/images/url.png",
				"file:resources/images/aggregate_root.png",
				"file:resources/images/func_root.png",
				"file:resources/images/index_root.png",
				"file:resources/images/materialized_view_root.png",
				"file:resources/images/package_body_root.png",
				"file:resources/images/package_def_root.png",
				"file:resources/images/proc_root.png",
				"file:resources/images/seq_root.png",
				"file:resources/images/systemview_root.png",
				"file:resources/images/table_root.png",
				"file:resources/images/trigger_root.png",
				"file:resources/images/type_root.png",
				"file:resources/images/view_root.png",
				"file:resources/images/ER_root.png",
				// MainMenuBar
				"file:resources/images/config.png",
				"file:resources/images/quit.png",
				"file:resources/images/sysinfo.png",
				"file:resources/images/jdbc.png",
				// MainToolBar
				"file:resources/images/commit.png",
				"file:resources/images/rollback.png",
				"file:resources/images/newtab.png",
				"file:resources/images/newwin.png",
				"file:resources/images/connect.png",
				// Dialog
				"file:resources/images/winicon.gif",
				// DBSqlTab
				"file:resources/images/sql.png",
				"file:resources/images/result.png",
				//"file:resources/images/script.png",
				"file:resources/images/execsql.png",
				"file:resources/images/explain.png",
				"file:resources/images/execQUERY.png",
				"file:resources/images/execTRANS.png",
				"file:resources/images/execTRANS_SEMI.png",
				"file:resources/images/direction.png",
				"file:resources/images/copy.png",
				"file:resources/images/copy2.png",
				"file:resources/images/save.png",
				// DBSettingDialog
				"file:resources/images/folder.png",
				"file:resources/images/folder_new.png",
				"file:resources/images/file.png",
				"file:resources/images/file_new.png",
				"file:resources/images/delete.png",
				"file:resources/images/edit.png",
				// DBSqlScriptTab
				"file:resources/images/sql_format.png",
				"file:resources/images/sql_oneline.png",
				// ImportDataTab
				"file:resources/images/import.png",
				"file:resources/images/next.png",
				"file:resources/images/back.png",
				"file:resources/images/close.png"
			};
	
		for ( String image : images )
		{
			this.mainCtrl.addImage( image, new Image( image ) );
			this.progressInf.addProgress( this.assignedSize/images.length );
			this.progressInf.setMsg(image);
		}
	}
}
