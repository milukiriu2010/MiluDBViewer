package milu.task.main;

import java.util.ResourceBundle;

public class InitialLoadLangResource extends InitialLoadAbstract 
{

	@Override
	public void load() 
	{
		String[] languages =
		{
			"conf.lang.entity.schema.SchemaEntity",
			"conf.lang.gui.common.MyAlert",
			"conf.lang.gui.common.NodeName",
			"conf.lang.gui.ctrl.common.DriverControlPane",
			"conf.lang.gui.ctrl.common.table.ObjTableView",
			"conf.lang.gui.ctrl.info.VersionTab",
			"conf.lang.gui.ctrl.menu.MainMenuBar",
			"conf.lang.gui.ctrl.menu.MainToolBar",
			"conf.lang.gui.ctrl.query.DBSqlTab",
			"conf.lang.gui.ctrl.schema.DBSchemaTab",
			"conf.lang.gui.ctrl.schema.SchemaTreeView",
			"conf.lang.gui.dlg.SystemInfoDialog",
			"conf.lang.gui.dlg.app.AppSettingDialog",
			"conf.lang.gui.dlg.db.DBSettingDialog"
		};
		
		for ( String lang : languages )
		{
			this.mainCtrl.addLangResource( lang, ResourceBundle.getBundle(lang) );
			if ( this.progressInf != null )
			{
				this.progressInf.addProgress( this.assignedSize/languages.length );
				this.progressInf.setMsg(lang);
			}
		}
	}

}
