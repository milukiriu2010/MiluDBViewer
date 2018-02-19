package milu.entity.schema;

public class SchemaEntityRootSystemView extends SchemaEntity 
{
	public SchemaEntityRootSystemView()
	{
		super( SchemaEntity.SCHEMA_TYPE.ROOT_SYSTEM_VIEW );
		
		this.name = "SYSTEM VIEW";
		
		this.imageResourceName = "file:resources/images/systemview_root.png";
	}
}
