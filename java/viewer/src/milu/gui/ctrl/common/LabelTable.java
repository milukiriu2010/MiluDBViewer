package milu.gui.ctrl.common;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;


import milu.entity.schema.SchemaEntity;

public class LabelTable extends Group
{
	// [TABLE]
	private SchemaEntity tblEntity = null;
	
	//private String schemaName = null;
	
	private Label  lblTable = new Label();
	
	private List<Label>  lblColumnLst = new ArrayList<>();
	
	private VBox   vBox = new VBox(2);
	
	/*
	public LabelTable( String schemaName, String tableName, List<String> colNameList )
	{
		super();
		
		this.schemaName = schemaName;
		
		this.vBox.getStyleClass().add("label_table");
		
		this.lblTable.setText( tableName );
		this.lblTable.getStyleClass().add("table_name");
		this.lblTable.prefWidth(-1);
		this.lblTable.prefWidth(-1);
		
		this.vBox.getChildren().add(this.lblTable);
		
		for ( String colName: colNameList )
		{
			Label lblColumn = new Label( colName );
			lblColumn.getStyleClass().add("column_w");
			this.lblColumnLst.add( lblColumn );
			this.vBox.getChildren().add(lblColumn);
		}
		
		this.getChildren().add( this.vBox );
	}
	*/
	
	public LabelTable( SchemaEntity tblEntity )
	{
		super();
		
		this.tblEntity = tblEntity;
		
		this.vBox.getStyleClass().add("label_table");
		
		// Label of Table
		this.lblTable.setText( this.tblEntity.getName() );
		this.lblTable.getStyleClass().add("table_name");
		this.lblTable.prefWidth(-1);
		this.lblTable.prefWidth(-1);
		
		this.vBox.getChildren().add(this.lblTable);
		
		// Column List
		List<Map<String,String>> tableDefLst = this.tblEntity.getDefinitionLst();
		for ( Map<String,String> defMap : tableDefLst )
		{
			String colName = defMap.get("column_name");
			Label lblColumn = new Label( colName );
			lblColumn.getStyleClass().add("column_white");
			this.lblColumnLst.add( lblColumn );
			this.vBox.getChildren().add(lblColumn);
		}
		this.getChildren().add( this.vBox );
		
		//ReadOnlyDoubleProperty w1 = this.vBox.widthProperty();
		//this.lblTable.prefWidthProperty().bind(w1);
	}
	
	public Bounds getTableBoundsInLocal()
	{
		Bounds b = this.lblTable.getBoundsInLocal();
		return b;
	}
	
	public List<Label> getColumnLabelLst()
	{
		return this.lblColumnLst;
	}
	
	public Label getColumnLabel( String column )
	{
		Label lblColumn = null;
		for ( Label lbl : this.lblColumnLst )
		{
			if ( lbl.getText().equals( column ) )
			{
				lblColumn = lbl;
			}
		}
		return lblColumn;
	}
	
	public Point2D getTablePoint2D()
	{
		return new Point2D( this.lblTable.getWidth(), this.lblTable.getHeight() );
	}
	/*
	@Override
	public boolean equals( Object obj )
	{
		if ( obj == null )
		{
			return false;
		}
		
		if ( obj instanceof LabelTable )
		{
			LabelTable lt = (LabelTable)obj;
			if ( this.schemaName.equals( lt.schemaName ) == false )
			{
				return false;
			}
			if ( this.lblTable.getText().equals( lt.lblTable.getText() ) == false )
			{
				return false;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	*/
}
