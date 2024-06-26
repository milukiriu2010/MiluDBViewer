package milu.task.main;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import milu.db.driver.DriverClassConst;
import milu.db.driver.DriverShim;
import milu.db.driver.LoadDriver;
import milu.file.json.MyJsonEachAbstract;
import milu.file.json.MyJsonEachFactory;
import milu.gui.dlg.MyAlertDialog;
import milu.main.AppConst;
import milu.tool.MyFileTool;

public class InitialLoadDriver extends InitialLoadAbstract 
{

	@Override
	public void load() 
	{
		this.loadDriverUser();
		this.loadDriverDefault();
	}

	private void loadDriverUser()
	{
		File folder = new File(AppConst.DRIVER_DIR.val());
		File[] fileArray = folder.listFiles();
		if ( fileArray == null )
		{
			return;
		}
		
		List<File> fileLst = new ArrayList<File>( Arrays.asList(fileArray) );
		
		List<File> jsonLst =
				fileLst.stream()
					.filter( file -> file.isFile() )
					.filter( file -> MyFileTool.getFileExtension(file).equals("json") )
					.collect(Collectors.toList());
		
		jsonLst.forEach
		(
			(json)->
			{
				try
				{
					/*
					MyJsonHandleAbstract myJsonAbs =
							new MyJsonHandleFactory().createInstance(DriverShim.class);
					myJsonAbs.open(json.getAbsolutePath());
					Object obj = myJsonAbs.load();
					if ( obj instanceof DriverShim )
					{
						DriverShim driverShim = (DriverShim)obj;
						if ( LoadDriver.isAlreadyLoadCheck( driverShim.getDriverClassName() ) == false )
						{
							DriverShim loadedDriver = LoadDriver.loadDriver( driverShim.getDriverClassName(), driverShim.getDriverPathLst() );
							loadedDriver.setTemplateUrl( driverShim.getTemplateUrl() );
							loadedDriver.setReferenceUrl( driverShim.getReferenceUrl() );
							System.out.println( driverShim.getDriverClassName() + " Driver(User) Load done." );
						}
						else
						{
							System.out.println( driverShim.getDriverClassName() + " Driver(User) Load skip." );
						}
					}
					*/
					MyJsonEachAbstract<DriverShim> myJsonAbs =
							MyJsonEachFactory.<DriverShim>getInstance(MyJsonEachFactory.factoryType.DRIVER_SHIM);
					DriverShim driverShim = myJsonAbs.load(new File(json.getAbsolutePath()));
					if ( driverShim == null )
					{
						return;
					}
					if ( LoadDriver.isAlreadyLoadCheck( driverShim.getDriverClassName() ) == false )
					{
						DriverShim loadedDriver = LoadDriver.loadDriver( driverShim.getDriverClassName(), driverShim.getDriverPathLst() );
						loadedDriver.setTemplateUrl( driverShim.getTemplateUrl() );
						loadedDriver.setReferenceUrl( driverShim.getReferenceUrl() );
						System.out.println( driverShim.getDriverClassName() + " Driver(User) Load done." );
					}
					else
					{
						System.out.println( driverShim.getDriverClassName() + " Driver(User) Load skip." );
					}
				}
				catch ( Exception ex )
				{
					// "driver/'driver class name'.json" exists
					// but, cannot read.
					
					// https://stackoverflow.com/questions/36309385/how-to-change-the-text-of-yes-no-buttons-in-javafx-8-alert-dialogs?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
					// https://stackoverflow.com/questions/29535395/javafx-default-focused-button-in-alert-dialog?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
					ButtonType btnYes = ButtonType.YES; //new ButtonType( "Yes", ButtonBar.ButtonData.YES );
					ButtonType btnNo  = ButtonType.NO; //new ButtonType( "No" , ButtonBar.ButtonData.NO );
					
					MyAlertDialog alertDlg = new MyAlertDialog( AlertType.WARNING, this.mainCtrl, btnYes, btnNo );
					((Button)alertDlg.getDialogPane().lookupButton(btnYes)).setDefaultButton(false);
					((Button)alertDlg.getDialogPane().lookupButton(btnNo)).setDefaultButton(true);
					ResourceBundle langRB = this.mainCtrl.getLangResource("conf.lang.gui.common.MyAlert");
					String msgExtra =
						MessageFormat.format( langRB.getString("MSG_DB_DRIVER_ERROR"), json.getAbsolutePath() );
					alertDlg.setHeaderText( langRB.getString("TITLE_DB_DRIVER_ERROR") );
					alertDlg.setTxtExp( ex, msgExtra );
					final Optional<ButtonType> result = alertDlg.showAndWait();
					if ( result.get() == btnYes )
					{
						System.out.println( "delete:"+json.getAbsolutePath() );
						new File(json.getAbsolutePath()).delete();
					}
					alertDlg = null;					
				}
				finally
				{
					this.progressInf.addProgress( this.assignedSize/2/jsonLst.size() );
					this.progressInf.setMsg( "User JDBC:" + json.getName() );					
				}
			}
		);
	}
	
	private void loadDriverDefault()
	{
		// DriverClassConst <=> Driver Path List
		Map<DriverClassConst,List<String>>  driverMap = new HashMap<>();
		// DriverClassConst <=> Template URL
		Map<DriverClassConst,String>  driverTemplateUrlMap = new HashMap<>();
		// DriverClassConst <=> Reference URL
		Map<DriverClassConst,String>  driverReferenceUrlMap = new HashMap<>();
		
		// Oracle
		List<String>  driverPathLstOracle = new ArrayList<>();
		// jdk 20 deprecated
		//driverPathLstOracle.add( "file:lib/oracle/ojdbc8.jar" );
		//driverPathLstOracle.add( "file:lib/oracle/orai18n.jar" );
		//driverPathLstOracle.add( "file:lib/oracle/xdb6.jar" );
		//driverPathLstOracle.add( "file:lib/oracle/xmlparserv2.jar" );
		//driverPathLstOracle.add( "lib/oracle/ojdbc8.jar" );
		driverPathLstOracle.add( "lib/oracle/ojdbc11.jar" );
		driverPathLstOracle.add( "lib/oracle/orai18n.jar" );
		driverPathLstOracle.add( "lib/oracle/xdb6.jar" );
		driverPathLstOracle.add( "lib/oracle/xmlparserv2.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_ORACLE, driverPathLstOracle );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_ORACLE, "jdbc:oracle:thin:@//<host>[:1521]/<service_name>[?internal_logon=sysdba|sysoper]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_ORACLE, "https://docs.oracle.com/cd/B28359_01/java.111/b31224/urls.htm" );
		
		// PostgreSQL
		List<String>  driverPathLstPostgres = new ArrayList<>();
		// jdk 20 deprecated
		//driverPathLstPostgres.add( "file:lib/postgresql/postgresql-42.1.4.jar" );
		//driverPathLstPostgres.add( "file:lib/postgresql/postgresql-42.2.4.jar" );
		//driverPathLstPostgres.add( "lib/postgresql/postgresql-42.2.4.jar" );
		driverPathLstPostgres.add( "lib/postgresql/postgresql-42.6.2.jar" );
		//driverPathLstPostgres.add( "lib/postgresql/postgresql-42.7.3.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_POSTGRESQL, driverPathLstPostgres );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_POSTGRESQL, "jdbc:postgresql://host1:5432,host2:port2/database[?targetServerType=master]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_POSTGRESQL, "https://jdbc.postgresql.org/documentation/head/connect.html" );
		
		// MySQL
		List<String>  driverPathLstMySQL = new ArrayList<>();
		// jdk 20 deprecated
		//driverPathLstMySQL.add( "file:lib/mysql/mysql-connector-java-5.1.45-bin.jar" );
		//driverPathLstMySQL.add( "file:lib/mysql/mysql-connector-java-8.0.11.jar" );
		//driverPathLstMySQL.add( "lib/mysql/mysql-connector-java-5.1.45-bin.jar" );
		driverPathLstMySQL.add( "lib/mysql/mysql-connector-j-8.3.0.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_MYSQL, driverPathLstMySQL );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_MYSQL, "jdbc:mysql://[host1][:3306][,[host2][:port2]]...[/[database]][?autoReconnect=true][&autoClosePStmtStreams=true]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_MYSQL, "https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html" );
		
		// Cassandra
		List<String>  driverPathLstCassandra = new ArrayList<>();
		// jdk 20 deprecated
		//driverPathLstCassandra.add( "file:lib/cassandra/cassandra-jdbc-driver-0.6.4-shaded.jar" );
		driverPathLstCassandra.add( "lib/cassandra/cassandra-jdbc-driver-0.6.4-shaded.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_CASSANDRA1, driverPathLstCassandra );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_CASSANDRA1, "jdbc:c*:datastax//[host][:9042]/[keyspace][?consistencyLevel=ONE|ANY|...][&compression=LZ4|SNAPPY]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_CASSANDRA1, "https://github.com/zhicwu/cassandra-jdbc-driver" );
		
		// SQLite
		List<String>  driverPathLstSQLite = new ArrayList<>();
		// jdk 20 deprecated
		//driverPathLstSQLite.add( "file:lib/sqlite/sqlite-jdbc-3.23.1.jar" );
		//driverPathLstSQLite.add( "lib/sqlite/sqlite-jdbc-3.23.1.jar" );
		driverPathLstSQLite.add( "lib/sqlite/sqlite-jdbc-3.45.1.0.jar" );
		//driverPathLstSQLite.add( "lib/sqlite/slf4j-api-1.7.36.jar" );
		
		
		driverMap.put( DriverClassConst.CLASS_NAME_SQLITE, driverPathLstSQLite );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_SQLITE, "jdbc:sqlite:filename" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_SQLITE, "https://github.com/xerial/sqlite-jdbc" );
		
		// SQLServer
		List<String> driverPathLstSQLServer = new ArrayList<>();
		
		driverPathLstSQLServer.add( "lib/sqlserver/mssql-jdbc-12.6.1.jre11.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_SQLSERVER, driverPathLstSQLServer );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_SQLSERVER, "jdbc:sqlserver://[serverName[\\instanceName][:portNumber]][;property=value[;property=value]]" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_SQLSERVER, "https://docs.microsoft.com/en-us/sql/connect/jdbc/building-the-connection-url?view=sql-server-2017" );
		
		// MongoDB(Unity)
		List<String> driverPathLstMongoDB = new ArrayList<>();
		
		//driverPathLstMongoDB.add( "lib/mongo/mongo-java-driver-3.0.3.jar" );
		driverPathLstMongoDB.add( "lib/mongo/mongo-java-driver-3.12.14.jar" );
		driverPathLstMongoDB.add( "lib/mongo/mongodb_unityjdbc_free.jar" );
		
		driverMap.put( DriverClassConst.CLASS_NAME_MONGODB1, driverPathLstMongoDB );
		driverTemplateUrlMap.put( DriverClassConst.CLASS_NAME_MONGODB1, "jdbc:mongo://<serverName>:27017/<databaseName>" );
		driverReferenceUrlMap.put( DriverClassConst.CLASS_NAME_MONGODB1, "http://www.unityjdbc.com/mongojdbc/mongo_jdbc.php" );
		
		// --------------------------------------------------
		// Load Driver
		// --------------------------------------------------
		driverMap.forEach
		(
			(driverClassType,driverPathLst)->
			{
				try
				{
					// Driver is not yet loaded
					if ( LoadDriver.isAlreadyLoadCheck( driverClassType.val() ) == false )
					{
						DriverShim driver = LoadDriver.loadDriver( driverClassType.val(), driverPathLst );
						driver.setTemplateUrl( driverTemplateUrlMap.get(driverClassType) );
						driver.setReferenceUrl( driverReferenceUrlMap.get(driverClassType) );
						System.out.println( DriverShim.driverDBMap.get(driverClassType).val() + " Driver(Default) Load done." );
					}
					// Driver is already loaded
					else
					{
						System.out.println( DriverShim.driverDBMap.get(driverClassType).val() + " Driver(Default) Load skip." );
					}
				}
				catch ( Exception ex )
				{
					ex.printStackTrace();
					System.out.println( DriverShim.driverDBMap.get(driverClassType).val() + " Driver(Default) Load failed." );
				}
				finally
				{
					this.progressInf.addProgress( this.assignedSize/2/driverMap.size() );
					this.progressInf.setMsg( "Default JDBC:" + driverClassType.val() );					
				}
			}
		);
	}
}
