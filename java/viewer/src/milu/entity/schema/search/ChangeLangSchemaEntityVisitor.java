package milu.entity.schema.search;

import milu.entity.schema.SchemaEntity;

public class ChangeLangSchemaEntityVisitor implements VisitorInterface {

	@Override
	public void visit(SchemaEntity schemaEntity) 
	{
		if ( schemaEntity == null )
		{
			return;
		}
		
		schemaEntity.changeLang();
		
		for ( SchemaEntity child : schemaEntity.getEntityLst() )
		{
			child.accept(this);
		}
	}

}
