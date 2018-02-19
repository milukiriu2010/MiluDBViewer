package milu.entity.schema;

public class SearchSchemaEntity
{
	public static SchemaEntity search( SchemaEntity item, SchemaEntity.SCHEMA_TYPE searchSchemaType, String searchName )
	{
		if ( item == null )
		{
			return null;
		}
		
		SchemaEntity.SCHEMA_TYPE schemaType = item.getType();
		String name = item.getName();
		if ( ( schemaType == searchSchemaType ) && name.equals(searchName) )
		{
			return item;
		}
		
		SchemaEntity searchEntity = null;
		for ( SchemaEntity child : item.getEntityLst() )
		{
			searchEntity = search( child, searchSchemaType, searchName );
			if ( searchEntity != null )
			{
				break;
			}
		}
		
		return searchEntity;
	}
	
	public static SchemaEntity search( SchemaEntity item, SchemaEntity.SCHEMA_TYPE searchSchemaType )
	{
		if ( item == null )
		{
			return null;
		}
		
		SchemaEntity.SCHEMA_TYPE schemaType = item.getType();
		if ( schemaType == searchSchemaType )
		{
			return item;
		}
		
		SchemaEntity searchEntity = null;
		for ( SchemaEntity child : item.getEntityLst() )
		{
			searchEntity = search( child, searchSchemaType );
			if ( searchEntity != null )
			{
				break;
			}
		}
		
		return searchEntity;
	}
	
}
