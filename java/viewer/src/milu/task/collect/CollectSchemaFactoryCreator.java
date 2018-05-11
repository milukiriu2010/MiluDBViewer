package milu.task.collect;

class CollectSchemaFactoryCreator 
{
	enum FACTORY_TYPE
	{
		CREATE_ME,
		SET_ME
	}
	
	static CollectSchemaFactoryAbstract createFactory( FACTORY_TYPE factoryType )
	{
		CollectSchemaFactoryAbstract csfAbs = null;
		if ( FACTORY_TYPE.CREATE_ME.equals(factoryType) )
		{
			csfAbs = new CollectSchemaFactoryCreateMe();
		}
		else if ( FACTORY_TYPE.SET_ME.equals(factoryType) )
		{
			csfAbs = new CollectSchemaFactorySetMe();
		}
		else
		{
			return null;
		}
		return csfAbs;
	}
}
