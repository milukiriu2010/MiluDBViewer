package milu.gui.ctrl.common.table;

import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import milu.gui.ctrl.common.table.ObjTableView.COPY_TYPE;

abstract class TableProcessAbstract 
{
	protected ObjTableView  objTableView = null;
	
	void setObjTableView( ObjTableView  objTableView )
	{
		this.objTableView = objTableView;
	}
	
	abstract int getRowSize();
	
	abstract void switchDirection();
	
	abstract void setData( List<Object> headLst, List<List<Object>> dataLst );
	
	abstract void copyTable( COPY_TYPE copyType );
	
	abstract List<Object> getHeadList();
	
	abstract List<List<Object>> getDataList();
	
	void setTableColumnCellValueFactory( final int index, TableColumn<List<Object>,Object> tableCol )
	{
		tableCol.setCellValueFactory
		(
			(p)->
			{
				List<Object> x = p.getValue();
				if ( x != null )
				{
					Object obj = x.get( index-1 );
					if ( obj instanceof Number )
					{
						tableCol.setStyle( "-fx-alignment: CENTER-RIGHT;" );
						return new SimpleObjectProperty<Object>( obj );
					}
					else if ( obj instanceof Timestamp )
					{
						Timestamp ts = (Timestamp)obj;
						String strTS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(ts);
						return new SimpleObjectProperty<Object>( strTS ); 
					}
					else
					{
						return new SimpleObjectProperty<Object>( obj );
					}
				}
				else
				{
					return new SimpleObjectProperty<Object>( "<NULL>" );
				}						
			}
		);
	}
}