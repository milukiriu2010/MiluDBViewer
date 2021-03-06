package milu.db;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;
import milu.tool.MyStringTool;

/**
 * 
 * [SUPPORT OBJECT]
 * ROOT_TABLE
 * ROOT_VIEW
 * ROOT_MATERIALIZED_VIEW
 * ROOT_FUNC
 * ROOT_PROC
 * ROOT_PACKAGE_DEF
 * ROOT_PACKAGE_BODY
 * ROOT_TYPE
 * ROOT_TRIGGER
 * ROOT_SEQUENCE
 * ROOT_ER
 * 
 * @author milu
 *
 */
public class MyDBOracle extends MyDBAbstract 
{
	@Override
	void init()
	{
		//this.driverClassName = "oracle.jdbc.driver.OracleDriver"; 
	}
	
	@Override
	protected void loadSpecial()
	{
		//System.setProperty( "oracle.net.tns_admin", dbOptMap.get( "TNSAdmin" ) );
		
		// set system property(TNS)
		//   oracle.net.tns_admin => TNS_ADMIN
		this.dbOptsSpecial.forEach( (k,v)->System.setProperty(k,v) );
	}
	
	/**
	 * Get Driver URL
	 ***********************************************
	 * https://docs.oracle.com/cd/B28359_01/java.111/b31224/urls.htm
	 * [Thin-style Service Name Syntax]
	 * jdbc:oracle:thin:@localhost:1521/xe
	 *   Host   => localhost
	 *   Port   => 1521
	 *   DBName => xe
	 * [TNSNames Alias Syntax]
	 * jdbc:oracle:thin:@orcl
	 *   TNSName => orcl
	 *   TNSAdmin => /opt/oracle/12.2/product/network/admin
	 * 
	 */
	public String getDriverUrl( Map<String, String> dbOptMap, MyDBAbstract.UPDATE update )
	{
		String urlTmp = "";
		// URL Example
		// ---------------------------------------------------
		// (1) jdbc:oracle:thin:@localhost:1521:xe
		// (2) jdbc:oracle:thin:@localhost:1521/xe
		// ---------------------------------------------------
		// When using (1) stype, this ORA error happens.
		//   "ORA-12505 :TNS listener does not currently know of SID given in connect descriptor"
		// so, change to (2) style.
		// https://stackoverflow.com/questions/30861061/ora-12505-tns-listener-does-not-currently-know-of-sid-given-in-connect-descript
		if ( dbOptMap.size() == 0 || dbOptMap.containsKey("Port") )
		{
			urlTmp = "jdbc:oracle:thin:@" +
				dbOptMap.get( "Host" ) + ":" +
				dbOptMap.get( "Port" ) + "/" +
				dbOptMap.get( "DBName" );
		}
		else if ( 
			dbOptMap.containsKey( "TNSName" ) && 
			( dbOptMap.get( "TNSName" ) != null ) &&
			( dbOptMap.get( "TNSName" ).length() > 0 ) &&
			dbOptMap.containsKey( "TNSAdmin" ) && 
			( dbOptMap.get( "TNSAdmin" ) != null ) &&
			( dbOptMap.get( "TNSAdmin" ).length() > 0 )
		)
		{
			urlTmp = "jdbc:oracle:thin:@"+ dbOptMap.get( "TNSName" );
			//System.setProperty( "oracle.net.tns_admin", dbOptMap.get( "TNSAdmin" ) );
		}
		if ( update.equals(MyDBAbstract.UPDATE.WITH) )
		{
			this.dbOptsAux.clear();
			dbOptMap.forEach( (k,v)->this.dbOptsAux.put(k,v) );
			this.dbOptsSpecial.clear();
			if ( dbOptMap.containsKey( "TNSAdmin" ) )
			{
				this.dbOptsSpecial.put( "oracle.net.tns_admin", dbOptMap.get( "TNSAdmin" ) );
			}
			this.url = urlTmp;
		}
		return urlTmp;
	}
	
	/**
	 * Get Default Port Number
	 **********************************************
	 * @return 
	 */
	@Override
	public int getDefaultPort()
	{
		return 1521;
	}

	@Override
	public void processAfterException() throws SQLException
	{
	}
	
	@Override
	public void setSchemaRoot()
	{
		this.schemaRoot = SchemaEntityFactory.createInstance( this.url, SchemaEntity.SCHEMA_TYPE.ROOT );
	}
	
	@Override
	protected void processAfterConnection()
	{
	}
	
	@Override
	protected void addProp( Properties prop )
	{
		/*
		prop.put( "v$session.osuser" , "test" );
		prop.put( "v$session.machine", "abcd" );
		prop.put( "v$session.program", "test" );
		*/
		if ( appConf == null )
		{
			return;
		}
		
		// ----------------------------------------------
		// change value for v$session
		// ----------------------------------------------
		//   osuser
		//   machine
		//   program
		// ----------------------------------------------
		// https://stackoverflow.com/questions/1612657/change-oracle-jdbc-thin-client-identifier
		if ( MyStringTool.isEmpty(appConf.getOracleSessionOsuser()) == false )
		{
			prop.put( "v$session.osuser" , appConf.getOracleSessionOsuser() );
		}
		if ( MyStringTool.isEmpty(appConf.getOracleSessionMachine()) == false )
		{
			prop.put( "v$session.machine", appConf.getOracleSessionMachine() );
		}
		if ( MyStringTool.isEmpty(appConf.getOracleSessionProgram()) == false )
		{
			prop.put( "v$session.program", appConf.getOracleSessionProgram() );
		}
	}
}
