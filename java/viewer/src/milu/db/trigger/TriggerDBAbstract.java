package milu.db.trigger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import milu.db.MyDBAbstract;
import milu.entity.schema.SchemaEntity;
import milu.entity.schema.SchemaEntityFactory;

public abstract class TriggerDBAbstract 
{
	// DB Access Object
	protected MyDBAbstract  myDBAbs = null;
	
	public void setMyDBAbstract( MyDBAbstract myDBAbs )
	{
		this.myDBAbs = myDBAbs;
	}
	/*
	public List<SchemaEntity> getEntityLst()
	{
		List<SchemaEntity>  triggerEntityLst = new ArrayList<>();
		for ( Map<String,String> trigger : this.triggerLst )
		{
			SchemaEntity eachTriggerEntity = SchemaEntityFactory.createInstance( trigger.get("triggerName"), SchemaEntity.SCHEMA_TYPE.TRIGGER );
			String strStatus = trigger.get("status");
			if ( strStatus != null && "INVALID".equals(strStatus) )
			{
				eachTriggerEntity.setState( SchemaEntity.STATE.INVALID );
			}
			triggerEntityLst.add( eachTriggerEntity );
		}
		return triggerEntityLst;
	}
	*/	
	
	abstract public List<SchemaEntity> selectEntityLst( String schemaName ) throws SQLException;
	
	abstract protected String listSQL( String schemaName );

	// Source of Trigger
	abstract public String getSRC( String schemaName, String triggerName ) throws SQLException;
	
}
