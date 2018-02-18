package milu.entity.schema;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

// This class will be abstract
public class SchemaEntity
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
					SEQUENCE
	}
	
	protected String       name = null;
	
	protected SCHEMA_TYPE  type = null;
	
	protected String       imageResourceName = null;
	
	protected List<SchemaEntity>  entityLst = new ArrayList<>();
	
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
	
	public SCHEMA_TYPE getType()
	{
		return this.type;
	}
	
	public String getImageResourceName()
	{
		return this.imageResourceName;
	}
	
	public List<SchemaEntity> getEntityLst()
	{
		return this.entityLst;
	}
	
	public void addEntity( SchemaEntity schemaEntity )
	{
		this.entityLst.add( schemaEntity );
	}
	
	public void addEntityAll( Collection<? extends SchemaEntity> schemaEntityLst )
	{
		this.entityLst.addAll( schemaEntityLst );
	}
}
