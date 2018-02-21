package milu.db.sequence;

import java.sql.SQLException;
import java.util.List;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;

public abstract class SequenceDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	// Sequence List
	//protected List<Map<String,String>>  seqLst = new ArrayList<>();
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	/*
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
	*/
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );		
	
}
