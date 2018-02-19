package milu.entity.schema;

public class SchemaEntityRootView extends SchemaEntity
{
	public SchemaEntityRootView()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_VIEW );
		
		this.name = "VIEW";
		
		this.imageResourceName = "file:resources/images/view_root.png";
	}
}
