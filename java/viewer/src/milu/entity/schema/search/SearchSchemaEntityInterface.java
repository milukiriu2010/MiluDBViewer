package milu.entity.schema.search;

import milu.entity.schema.SchemaEntity;

public interface SearchSchemaEntityInterface extends VisitorInterface
{
	public SchemaEntity getHitSchemaEntity();
}
