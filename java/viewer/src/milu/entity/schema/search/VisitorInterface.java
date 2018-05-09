package milu.entity.schema.search;

import milu.entity.schema.SchemaEntity;

public interface VisitorInterface 
{
	public void visit( SchemaEntity schemaEntity );
	
	public void visitParent( SchemaEntity schemaEntity );
}
