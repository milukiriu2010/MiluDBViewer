package milu.task.collect;

import java.sql.SQLException;

public class CollectSchemaSkip extends CollectSchemaAbstract 
{

	@Override
	void retrieveChildren() throws SQLException 
	{
		this.progressInf.addProgress(this.assignedSize);
	}

}
