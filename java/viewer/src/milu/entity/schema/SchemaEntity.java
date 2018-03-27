package milu.entity.schema;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ArrayList;

import milu.entity.schema.search.VisitorInterface;
import milu.gui.ctrl.common.ChangeLangInterface;

// This class will be abstract
abstract public class SchemaEntity
	implements ChangeLangInterface
{
	// ---------------------------------------
	// [0:ROOT]
	//   - [1:SCHEMA]
	//     - [2:ROOT_TABLE]
	//       - [3:TABLE]
	//         - [4:ROOT_COLUMN]
	//           - [5:COLUMN]
	//         - [4:ROOT_INDEX]
	//           - [5:INDEX]
	//             - [6:INDEX_COLUMN]
	//     - [2:ROOT_VIEW]
	//       - [3:VIEW]
	//     - [2:ROOT_SYSTEM_VIEW]
	//       - [3:SYSTEM_VIEW]
	//     - [2:ROOT_MATERIALIZED_VIEW]
	//       - [3:MATERIALIZED_VIEW]
	//     - [2:ROOT_FUNC]
	//		 - [3:FUNC]
	//     - [2:ROOT_AGGREGATE]
	//		 - [3:AGGREGATE]
	//     - [2:ROOT_PROC]
	//		 - [3:PROC]
	//     - [2:ROOT_PACKAGE_DEF]
	//		 - [3:PACKAGE_DEF]
	//     - [2:ROOT_PACKAGE_BODY]
	//		 - [3:PACKAGE_BODY]
	//     - [2:ROOT_TRIGGER]
	//		 - [3:TRIGGER]
	//     - [2:ROOT_TYPE]
	//		 - [3:TYPE]
	//     - [2:ROOT_SEQ]
	//		 - [3:SEQ]
	//     - [2:ROOT_ER]
	//       - [3:FOREIGN_KEY]
	// ---------------------------------------
	public enum SCHEMA_TYPE
	{
		// 0
		ROOT,
			// 1
			SCHEMA,
				// 2
				ROOT_TABLE,
					// 3
					TABLE,
					/*
						// 4
						ROOT_COLUMN,
							// 5
							COLUMN,
					*/
						// 4
						ROOT_INDEX,
							// 5
							INDEX,
								// 6
								INDEX_COLUMN,
				// 2
				ROOT_VIEW,
					// 3
					VIEW,
				// 2
				ROOT_SYSTEM_VIEW,
					// 3
					SYSTEM_VIEW,
				// 2
				ROOT_MATERIALIZED_VIEW,
					// 3
					MATERIALIZED_VIEW,
				// 2
				ROOT_FUNC,
					// 3
					FUNC,
					// 2
				ROOT_AGGREGATE,
					// 3
					AGGREGATE,
				// 2
				ROOT_PROC,
					// 3
					PROC,
				// 2
				ROOT_PACKAGE_DEF,
					// 3
					PACKAGE_DEF,
					// 2
				ROOT_PACKAGE_BODY,
					// 3
					PACKAGE_BODY,
				// 2
				ROOT_TRIGGER,
					// 3
					TRIGGER,
				// 2
				ROOT_TYPE,
					// 3
					TYPE,
				// 2
				ROOT_SEQUENCE,
					// 3
					SEQUENCE,
				// 2
				ROOT_ER,
					// 3
					FOREIGN_KEY
	}
	
	public enum STATE
	{
		VALID,
		INVALID
	}
	
	protected String       name = null;
	
	protected String       nameId = null;
	
	protected SCHEMA_TYPE  type = null;
	
	protected String       imageResourceName = null;
	
	protected STATE        state = STATE.VALID;
	
	// [KEY]
	// (1) COLUMN
	// (2) TYPE
	// (3) NULL?
	// (4) DEFAULT
	protected List<Map<String,String>>  definitionLst = new ArrayList<>();
	
	// DDL
	protected String       srcSQL = null;
	
	// parent
	protected SchemaEntity        parentEntity = null;
	
	// children
	protected List<SchemaEntity>  entityLst = new ArrayList<>();
	
	// Property File for this class 
	protected static final String PROPERTY_FILENAME = 
		 	"conf.lang.entity.schema.SchemaEntity";

	protected static ResourceBundle langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );	
	
	public SchemaEntity( SCHEMA_TYPE type )
	{
		this.type = type;
	}
	
	public SchemaEntity( String name, SCHEMA_TYPE type )
	{
		this.name = name;
		this.type = type;
	}
	
	public String toString()
	{
		return this.name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName( String name )
	{
		this.name = name;
	}
	
	public void setName()
	{
		if ( this.nameId != null )
		{
			this.name = langRB.getString( this.nameId );
		}
	}
	
	public SCHEMA_TYPE getType()
	{
		return this.type;
	}
	
	public STATE getState()
	{
		return this.state;
	}
	
	public void setState( SchemaEntity.STATE state )
	{
		this.state = state;
	}
	
	public void setDefinitionlst( List<Map<String,String>>  definitionLst )
	{
		this.definitionLst = definitionLst;
	}
	
	public List<Map<String,String>> getDefinitionLst()
	{
		return this.definitionLst;
	}
	
	public List<String> getDefinitionLst( String strFilter )
	{
		List<String>  filteredLst = new ArrayList<>();
		for ( Map<String,String> map : this.definitionLst )
		{
			String filteredVal = map.get(strFilter);
			if ( filteredVal != null )
			{
				filteredLst.add(filteredVal);
				continue;
			}
			
			filteredVal = map.get( strFilter.toUpperCase() );
			if ( filteredVal != null )
			{
				filteredLst.add(filteredVal);
				continue;
			}
			
			filteredVal = map.get(strFilter.toLowerCase() );
			if ( filteredVal != null )
			{
				filteredLst.add(filteredVal);
				continue;
			}
		}
		return filteredLst;
	}
	
	public String getSrcSQL()
	{
		return this.srcSQL;
	}
	
	public void setSrcSQL( String srcSQL)
	{
		this.srcSQL = srcSQL;
	}
	
	public String getImageResourceName()
	{
		return this.imageResourceName;
	}
	
	public void setImageResourceName( String imageResourceName )
	{
		this.imageResourceName = imageResourceName;
	}
	
	// get parent
	public SchemaEntity getParentEntity()
	{
		return this.parentEntity;
	}
	
	public void setParentEntity( SchemaEntity parentEntity )
	{
		this.parentEntity = parentEntity;
	}
	
	// get children
	public List<SchemaEntity> getEntityLst()
	{
		return this.entityLst;
	}
	
	// add child
	public void addEntity( SchemaEntity schemaEntity )
	{
		this.entityLst.add( schemaEntity );
		schemaEntity.setParentEntity(this);
	}
	
	// add children
	public void addEntityAll( Collection<? extends SchemaEntity> schemaEntityLst )
	{
		this.entityLst.addAll( schemaEntityLst );
		schemaEntityLst.forEach( (schemaEntity)->schemaEntity.setParentEntity(this) );
	}
	
	// del children
	public void delEntityAll()
	{
		this.entityLst.removeAll( this.entityLst );
	}
	
	public void accept( VisitorInterface visitor )
	{
		visitor.visit(this);
	}
	
	public static void loadResourceBundle()
	{
		langRB = ResourceBundle.getBundle( PROPERTY_FILENAME );
	}
	
	@Override
	public void changeLang()
	{
		this.setName();
	}
}
