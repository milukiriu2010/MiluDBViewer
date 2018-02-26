package milu.ctrl.visitor;

import milu.entity.schema.SchemaEntity;

public interface SearchSchemaEntityInterface extends VisitorInterface
{
	public SchemaEntity getHitSchemaEntity();
}
