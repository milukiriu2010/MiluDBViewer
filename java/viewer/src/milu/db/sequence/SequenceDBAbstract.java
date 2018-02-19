package milu.db.sequence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class SequenceDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Sequence List
	protected List<Map<String,String>>  seqLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	
	protected void clear()
	{
		this.seqLst.clear();
	}
	
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  sequenceEntityLst = new ArrayList<>();
		for ( Map<String,String> seq : this.seqLst )
		{
			SchemaEntity eachSeqEntity = SchemaEntityFactory.createInstance( seq.get("sequenceName"), SchemaEntity.SCHEMA_TYPE.SEQUENCE );
			String strStatus = seq.get("status");
			if ( eachSeqEntity != null && "INVALID".equals(strStatus) )
			{
				eachSeqEntity.setState( SchemaEntity.STATE.INVALID );
			}
			sequenceEntityLst.add( eachSeqEntity );
		}
		return sequenceEntityLst;
	}	
	
	public void selectEntityLst( String schemaName ) throws SQLException
	{
		String sql = this.listSQL( schemaName );
		
		System.out.println( " -- selectSequenceLst ----------------" );
		System.out.println( sql );
		System.out.println( " -------------------------------------" );
		try
		(
			Statement stmt = this.myDBAbs.createStatement();
			ResultSet rs   = stmt.executeQuery( sql )
		)
		{
			while ( rs.next() )
			{
				Map<String,String> dataRow = new HashMap<>();
				dataRow.put( "sequenceName", rs.getString("object_name") );
				dataRow.put( "status"      , rs.getString("status") );
				this.seqLst.add( dataRow );
			}
		}
	}
	
	abstract protected String listSQL( String schemaName );		
	
}
