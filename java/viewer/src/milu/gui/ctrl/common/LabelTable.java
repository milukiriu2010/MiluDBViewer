package milu.gui.ctrl.common;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.application.Platform;
import javafx.scene.Scene;

import milu.entity.schema.SchemaEntity;

public class LabelTable extends Group
{
	// [TABLE]
	private SchemaEntity tblEntity = null;
	
	//private String schemaName = null;
	
	private Label  lblTable = new Label();
	
	private List<Label>  lblColumnLst = new ArrayList<>();
	
	private VBox   vBox = new VBox(2);
	
	public LabelTable( SchemaEntity tblEntity )
	{
		super();
		
		this.tblEntity = tblEntity;
		
		this.vBox.getStyleClass().add("label_table");
		
		// Label of Table
		this.lblTable.setText( this.tblEntity.getName() );
		this.lblTable.getStyleClass().add("table_name");
		//this.lblTable.applyCss();
		//this.lblTable.layout();
		this.lblTable.prefWidth(-1);
		this.lblTable.prefHeight(-1);
		
		this.vBox.getChildren().add(this.lblTable);
		
		// Column List
		List<Map<String,String>> tableDefLst = this.tblEntity.getDefinitionLst();
		for ( Map<String,String> defMap : tableDefLst )
		{
			String colName = defMap.get("column_name");
			Label lblColumn = new Label( colName );
			lblColumn.getStyleClass().add("column_white");
			//lblColumn.applyCss();
			//lblColumn.layout();
			lblColumn.prefWidth(-1);
			lblColumn.prefHeight(-1);
			this.lblColumnLst.add( lblColumn );
			this.vBox.getChildren().add(lblColumn);
		}
		//this.vBox.applyCss();
		//this.vBox.layout();
		this.getChildren().add( this.vBox );
		
		
		//this.applyCss();
		//this.layout();
		//Scene scene = new Scene( this );
		
		
		// set Label(lblTable) width to the longest Label(lblColumn) width
		/*
		Platform.runLater
		( 
			()->
			{
				try
				{
					if ( System.getProperty("os.name").contains("Windows") == true )
					{
					}
					else
					{
						Thread.sleep(100);
					}
				}
				catch ( InterruptedException intEx )
				{
				}
				double maxW = this.lblTable.widthProperty().get();
				Label lblMax = this.lblTable;
				for ( Label lblColumn : this.lblColumnLst )
				{
					double colW = lblColumn.widthProperty().get();
					if ( colW > maxW )
					{
						maxW = colW;
						lblMax = lblColumn;
					}
				}
				if ( lblMax != this.lblTable )
				{
					this.lblTable.prefWidthProperty().bind(lblMax.widthProperty());
				}
				for ( Label lblColumn : this.lblColumnLst )
				{
					if ( lblMax != lblColumn )
					{
						lblColumn.prefWidthProperty().bind(lblMax.widthProperty());
					}
				}
			} 
		);
		*/
	}
	
	// set Label(lblTable) width to the longest Label(lblColumn) width
	public void calculate()
	{
		//System.out.println( "aaa:" + this.lblTable.getWidth() );
		//System.out.println( "bbb:" + this.lblTable.getBoundsInParent().getWidth() );
		//System.out.println( "ccc:" + this.lblTable.getBoundsInLocal().getWidth() );
		double maxW = this.lblTable.widthProperty().get();
		//double maxW = this.lblTable.getLayoutBounds().getWidth();
		Label lblMax = this.lblTable;
		for ( Label lblColumn : this.lblColumnLst )
		{
			double colW = lblColumn.widthProperty().get();
			//double colW = lblColumn.getLayoutBounds().getWidth();
			//System.out.println( this.lblTable.getText() + ":colW[" + colW + "]maxW[" + maxW + "]" );
			if ( colW > maxW )
			{
				maxW = colW;
				lblMax = lblColumn;
			}
		}
		if ( lblMax != this.lblTable )
		{
			this.lblTable.prefWidthProperty().bind(lblMax.widthProperty());
		}
		for ( Label lblColumn : this.lblColumnLst )
		{
			if ( lblMax != lblColumn )
			{
				lblColumn.prefWidthProperty().bind(lblMax.widthProperty());
			}
		}
	
	/*
		Runnable thread = ()->
		{
			try
			{
				if ( System.getProperty("os.name").contains("Windows") == true )
				{
				}
				else
				{
					Thread.sleep(100);
				}
			}
			catch ( InterruptedException intEx )
			{
			}
			Platform.runLater
			( 
				()->
				{
					//System.out.println( "aaa:" + this.lblTable.getWidth() );
					//System.out.println( "bbb:" + this.lblTable.getBoundsInParent().getWidth() );
					//System.out.println( "ccc:" + this.lblTable.getBoundsInLocal().getWidth() );
					double maxW = this.lblTable.widthProperty().get();
					//double maxW = this.lblTable.getLayoutBounds().getWidth();
					Label lblMax = this.lblTable;
					for ( Label lblColumn : this.lblColumnLst )
					{
						double colW = lblColumn.widthProperty().get();
						//double colW = lblColumn.getLayoutBounds().getWidth();
						System.out.println( this.lblTable.getText() + ":colW[" + colW + "]maxW[" + maxW + "]" );
						if ( colW > maxW )
						{
							maxW = colW;
							lblMax = lblColumn;
						}
					}
					if ( lblMax != this.lblTable )
					{
						this.lblTable.prefWidthProperty().bind(lblMax.widthProperty());
					}
					for ( Label lblColumn : this.lblColumnLst )
					{
						if ( lblMax != lblColumn )
						{
							lblColumn.prefWidthProperty().bind(lblMax.widthProperty());
						}
					}
				}
			);
		};
		thread.run();
		*/
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
}
