package milu.gui.stmt.call;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import milu.ctrl.sql.parse.MySQLType;
import milu.gui.view.DBView;

public class CallTableView extends TableView<CallObj> 
{
	private DBView  dbView = null;
	
	public CallTableView( DBView dbView )
	{
		super();
		this.dbView = dbView;
		
		// Editable
		this.setEditable(true);
		
		// ---------------------------------------------
		// IN/OUTパラメータの番号
		// ---------------------------------------------
		TableColumn<CallObj,CallObj.ParamType>  paramNoCol =
				new TableColumn<>("No");
		paramNoCol.setCellValueFactory(new PropertyValueFactory<>("paramNo"));
		
		// ---------------------------------------------
		// IN/OUTパラメータのType
		// ---------------------------------------------
		TableColumn<CallObj,CallObj.ParamType>  paramTypeCol =
				new TableColumn<>("IN/OUT");
		
		ObservableList<CallObj.ParamType> paramTypeLst = 
				FXCollections.observableArrayList(CallObj.ParamType.values());
		
		paramTypeCol.setCellValueFactory(new Callback<CellDataFeatures<CallObj, CallObj.ParamType>, ObservableValue<CallObj.ParamType>>(){
			@Override
			public ObservableValue<CallObj.ParamType> call(CellDataFeatures<CallObj,CallObj.ParamType> param) 
			{
				CallObj callObj = param.getValue();
				return new SimpleObjectProperty<CallObj.ParamType>(callObj.getParamType());
			}
		});
		paramTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(paramTypeLst));
		
		paramTypeCol.setOnEditCommit((event) -> {
			TablePosition<CallObj,CallObj.ParamType> pos = event.getTablePosition();
			CallObj.ParamType newParamType = event.getNewValue();
			
			int row = pos.getRow();
			CallObj callObj = event.getTableView().getItems().get(row);
			callObj.setParamType(newParamType);
		});
		
		//paramTypeCol.setMinWidth(120);
		
		
		// ---------------------------------------------
		// MySQLType(java.sql.Types)
		// ---------------------------------------------
		TableColumn<CallObj,MySQLType> sqlTypeCol =
				new TableColumn<>("Type for OUT Parameter");
		
		ObservableList<MySQLType> sqlTypeLst =
				FXCollections.observableArrayList(MySQLType.values());
		// NULLの選択項目をリストに追加
		sqlTypeLst.add(null);
		
		sqlTypeCol.setCellValueFactory(new Callback<CellDataFeatures<CallObj, MySQLType>, ObservableValue<MySQLType>>(){
			@Override
			public ObservableValue<MySQLType> call(CellDataFeatures<CallObj,MySQLType> param) 
			{
				CallObj callObj = param.getValue();
				return new SimpleObjectProperty<MySQLType>(callObj.getSqlType());
			}
		});
		
		sqlTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(sqlTypeLst));
		sqlTypeCol.setOnEditCommit((event) -> {
			TablePosition<CallObj,MySQLType> pos = event.getTablePosition();
			MySQLType newSqlType = event.getNewValue();
			
			int row = pos.getRow();
			CallObj callObj = event.getTableView().getItems().get(row);
			callObj.setSqlType(newSqlType);
		});
	
		// ---------------------------------------------
		// INパラメータに使う列
		// ---------------------------------------------
		TableColumn<CallObj,String> inColNameCol =
				new TableColumn<>("Column for IN parameter");

		ObservableList<String> inColNameLst =
				FXCollections.observableArrayList(null,"A","B","C");

		inColNameCol.setCellValueFactory(new Callback<CellDataFeatures<CallObj, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<CallObj,String> param) 
			{
				CallObj callObj = param.getValue();
				return new SimpleObjectProperty<String>(callObj.getInColName());
			}
		});
		
		inColNameCol.setCellFactory(ComboBoxTableCell.forTableColumn(inColNameLst));
		inColNameCol.setOnEditCommit((event) -> {
			TablePosition<CallObj,String> pos = event.getTablePosition();
			String newInColName = event.getNewValue();
			
			int row = pos.getRow();
			CallObj callObj = event.getTableView().getItems().get(row);
			callObj.setInColName(newInColName);
		});
		
		this.getColumns().addAll(paramNoCol,paramTypeCol,sqlTypeCol,inColNameCol);
	}
}
