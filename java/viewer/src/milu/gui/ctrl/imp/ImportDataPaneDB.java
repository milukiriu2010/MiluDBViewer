package milu.gui.ctrl.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import javafx.scene.layout.Pane;
import milu.gui.ctrl.common.PathTreeView;
import milu.gui.ctrl.common.inf.ChangePathInterface;
import milu.main.AppConst;
import milu.main.MainController;
import milu.gui.view.DBView;

public class ImportDataPaneDB extends Pane 
	implements
		ChangePathInterface
{
	private DBView          dbView = null;
	
	private WizardInterface wizardInf = null;
	
	private PathTreeView  pathTreeView = new PathTreeView();
	
	ImportDataPaneDB( DBView dbView, WizardInterface wizardInf, Map<String,Object> mapObj )
	{
		this.dbView = dbView;
		this.wizardInf = wizardInf;
		MainController mainCtrl = this.dbView.getMainController();
		
		this.pathTreeView.setMainController(mainCtrl);
		this.pathTreeView.setChangePathInterface(this);
		this.pathTreeView.setRootDir(AppConst.DB_DIR.val());
		this.pathTreeView.setFileExt("json");
		try
		{
			this.pathTreeView.init();
		}
		catch( IOException ioEx )
		{
			ioEx.printStackTrace();
		}

		this.pathTreeView.setEditable(false);
		
		this.getChildren().add(this.pathTreeView);
	}
	
	// ChangePathInterface
	@Override
	public void changePath( Path path )
	{
		System.out.println( "changePath[" + path.toString() + "]" );
		if ( Files.isRegularFile(path) == false )
		{
			return;
		}
		
	}
}
