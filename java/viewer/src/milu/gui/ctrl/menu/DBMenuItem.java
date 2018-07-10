package milu.gui.ctrl.menu;

import javafx.scene.control.MenuItem;
import java.io.File;
import java.nio.file.Path;

import milu.db.MyDBAbstract;
import milu.file.json.MyJsonEachAbstract;
import milu.file.json.MyJsonEachFactory;
import milu.main.MainController;
import milu.tool.MyGUITool;
import milu.tool.MyFileTool;

public class DBMenuItem extends MenuItem 
{
	private Path path = null;
	
	private MainController mainCtrl = null;
	
	public DBMenuItem( Path path, MainController mainCtrl )
	{
		super(MyFileTool.getBaseName(path.getFileName().toString()));
		this.path = path;
		this.mainCtrl = mainCtrl;
		
		this.setAction();
	}
	
	private void setAction()
	{
		this.setOnAction((event)->{
			try
			{
				/*
				MyJsonHandleAbstract myJsonAbs =
					new MyJsonHandleFactory().createInstance(MyDBAbstract.class);
				myJsonAbs.open(path.toString());
				Object obj = myJsonAbs.load();
				if ( obj instanceof MyDBAbstract )
				{
					MyDBAbstract myDBAbsTmp = (MyDBAbstract)obj;
					myDBAbsTmp.setPassword(this.mainCtrl.getSecretKey());
					System.out.println( "Class[" + myDBAbsTmp.getClass().toString() + "]" );
					System.out.println( "User [" + myDBAbsTmp.getUsername() + "]" );
					System.out.println( "URL  [" + myDBAbsTmp.getUrl() + "]" );
					System.out.println( "JDBC [" + myDBAbsTmp.getDriveShim().getDriverClassName() + "]" );
					myDBAbsTmp.getDBOpts().forEach( (k,v)->System.out.println("DBOpts:k["+k+"]v["+v+"]") );
					myDBAbsTmp.getDBOptsSpecial().forEach( (k,v)->System.out.println("DBOptsSpeicial:k["+k+"]v["+v+"]") );
					myDBAbsTmp.getDBOptsAux().forEach( (k,v)->System.out.println("DBOptsAux:k["+k+"]v["+v+"]") );
					myDBAbsTmp.connect();
					this.mainCtrl.createNewWindow(myDBAbsTmp);
				}
				*/				
				MyJsonEachAbstract<MyDBAbstract> myJsonAbs =
						MyJsonEachFactory.<MyDBAbstract>getInstance(MyJsonEachFactory.factoryType.MY_DB_ABS);
				MyDBAbstract myDBAbsTmp = myJsonAbs.load(new File(path.toString()));
				myDBAbsTmp.setPassword(this.mainCtrl.getSecretKey());
				System.out.println( "Class[" + myDBAbsTmp.getClass().toString() + "]" );
				System.out.println( "User [" + myDBAbsTmp.getUsername() + "]" );
				System.out.println( "URL  [" + myDBAbsTmp.getUrl() + "]" );
				System.out.println( "JDBC [" + myDBAbsTmp.getDriveShim().getDriverClassName() + "]" );
				myDBAbsTmp.getDBOpts().forEach( (k,v)->System.out.println("DBOpts:k["+k+"]v["+v+"]") );
				myDBAbsTmp.getDBOptsSpecial().forEach( (k,v)->System.out.println("DBOptsSpeicial:k["+k+"]v["+v+"]") );
				myDBAbsTmp.getDBOptsAux().forEach( (k,v)->System.out.println("DBOptsAux:k["+k+"]v["+v+"]") );
				myDBAbsTmp.connect();
				this.mainCtrl.createNewWindow(myDBAbsTmp);
			}
			catch ( Exception ex )
			{
				MyGUITool.showException( this.mainCtrl, "conf.lang.gui.common.MyAlert", "TITLE_MISC_ERROR", ex );
			}			
			
		});
	}
}
