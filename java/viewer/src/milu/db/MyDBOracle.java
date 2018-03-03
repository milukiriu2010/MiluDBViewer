package milu.db;

import java.util.Map;

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
 * 
 * @author milu
 *
 */
public class MyDBOracle extends MyDBAbstract 
{
	/*
	public MyDBOracle()
	{
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TABLE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_MATERIALIZED_VIEW );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_FUNC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_PROC );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_PACKAGE_DEF );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_PACKAGE_BODY );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TYPE );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_TRIGGER );
		this.suppoertedTypeLst.add( SCHEMA_TYPE.ROOT_SEQUENCE );
	}
	*/
	/**
	 * Load JDBC Driver
	 ***********************************************
	 * @throws ClassNotFoundException
	 */
	@Override
	protected void loadDriver() throws ClassNotFoundException 
	{
		Class.forName( "oracle.jdbc.driver.OracleDriver" );
	}
	
	/**
	 * Get Driver URL
	 ***********************************************
	 */
	public String getDriverUrl( Map<String, String> dbOptMap )
	{
		// URL Example
		// ---------------------------------------------------
		// (1) jdbc:oracle:thin:@localhost:1521:xe
		// (2) jdbc:oracle:thin:@localhost:1521/xe
		// ---------------------------------------------------
		// When using (1) stype, this ORA error happens.
		//   "ORA-12505 :TNS listener does not currently know of SID given in connect descriptor"
		// so, change to (2) style.
		// https://stackoverflow.com/questions/30861061/ora-12505-tns-listener-does-not-currently-know-of-sid-given-in-connect-descript
		this.url = 
				"jdbc:oracle:thin:@"+
				dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
				dbOptMap.get( "DBName" );
		return this.url;
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
	
	/**
	 * Name on GUI Items
	 */
	@Override
	public String toString()
	{
		return "Oracle";
	}
	
	/**
	 * 
	 */
	/*
	@Override
	protected void createConnectionParameter(Map<String, String> dbOptMap) 
	{
		// URL Example
		// ---------------------------------------------------
		// (1) jdbc:oracle:thin:@localhost:1521:xe
		// (2) jdbc:oracle:thin:@localhost:1521/xe
		// ---------------------------------------------------
		// When using (1) stype, this ORA error happens.
		//   "ORA-12505 :TNS listener does not currently know of SID given in connect descriptor"
		// so, change to (2) style.
		// https://stackoverflow.com/questions/30861061/ora-12505-tns-listener-does-not-currently-know-of-sid-given-in-connect-descript
		this.url = 
				"jdbc:oracle:thin:@"+
				dbOptMap.get( "Host" )+":"+dbOptMap.get( "Port" )+"/"+
				dbOptMap.get( "DBName" );
	}
	*/
}
