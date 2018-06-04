package sqlparse;

import java.util.Collections;
import java.util.List;

import java.io.StringWriter;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;

import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.JSQLParserException;

import net.sf.jsqlparser.util.cnfexpression.MultipleExpression;

import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;

import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.alter.Alter;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;

//import net.sf.jsqlparser.parser.SimpleNode;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Database;

import net.sf.jsqlparser.util.TablesNamesFinder;

// https://www.programcreek.com/java-api-examples/?api=net.sf.jsqlparser.parser.CCJSqlParserUtil
// https://www.programcreek.com/java-api-examples/index.php?source_dir=Mybatis-PageHelper-master/src/main/java/com/github/pagehelper/parser/SqlServer.java
public class JSQLParseExample2 extends Application
{
	private TextArea  taSQL    = new TextArea();
	private Button    btnParse = new Button("parse");
	private Button    btnParseStatements = new Button("parseStatements");
	private Button    btnParseExpression = new Button("parseExpression");
	private TextArea  taResult = new TextArea();
	
	
	public static void main( String...arg )
	{
		launch( arg );
	}
	
	@Override
	public void start(Stage stage) throws Exception
	{
		//taSQL.setText("SELECT u.first_name fn, u.last_name ln, c.name cn FROM user u, country c WHERE u.cid = c.cid");
		
		taSQL.setText
		(
			"SELECT \n" + 
			"  u.first_name as fn, \n" + 
			"  u.last_name ln, \n" + 
			"  c.name cn, \n" + 
			"   ( select count(*) from a ) cnt \n" + 
			"FROM \n " + 
			"  user u join country c on u.uid = c.cid  join \n" +
			"  continent cn on c.cid = cn.cnid \n" +
			"WHERE \n" +
			"  u.uid >= 10000 and u.uid < 20000 \n" +
			"order by u.uid, u.firmst_name"
		);
		
		btnParse.setOnAction( e->parse() );
		btnParseStatements.setOnAction( e->parseStatements() );
		btnParseExpression.setOnAction( e->parseExpression() );
		
		HBox hBoxParse = new HBox(2);
		hBoxParse.getChildren().addAll( btnParse, btnParseStatements, btnParseExpression );
		
		ToggleGroup tglGroup = new ToggleGroup();
		ToggleButton tb1 = new ToggleButton( "select" );
		tb1.setToggleGroup(tglGroup);
		ToggleButton tb2 = new ToggleButton( "insert" );
		tb2.setToggleGroup(tglGroup);
		ToggleButton tb3 = new ToggleButton( "cal" );
		tb3.setToggleGroup(tglGroup);
		ToggleButton tb4 = new ToggleButton( "select schema" );
		tb4.setToggleGroup(tglGroup);
		ToggleButton tb5 = new ToggleButton( "script" );
		tb5.setToggleGroup(tglGroup);
		tglGroup.selectedToggleProperty().addListener
		(
			(obs,oldVal,newVal)->
			{
				if ( newVal  == tb1 )
				{
					taSQL.setText
					(
						"SELECT \n" + 
						"  u.first_name as fn, \n" + 
						"  u.last_name ln, \n" + 
						"  c.name cn, \n" + 
						"   ( select count(*) from a ) cnt \n" + 
						"FROM \n " + 
						"  user u join country c on u.uid = c.cid  join \n" +
						"  continent cn on c.cid = cn.cnid \n" +
						"WHERE \n" +
						"  u.uid >= 10000 and u.uid < 20000 \n" +
						"order by u.uid, u.firmst_name"
					);
				}
				else if ( newVal == tb2 )
				{
					taSQL.setText
					(
						"insert into country \n" +
						"( ID, NAME ) \n" +
						"VALUES \n" +
						"( 81, 'JAPAN')"
					);
				}
				else if ( newVal == tb3 )
				{
					taSQL.setText( "4*(5+1)" );
				}
				else if ( newVal == tb4 )
				{
					taSQL.setText
					(
						"select c.id, c.name from sakila.country c where c.id = 80"
						/*
						"select \n" + 
						"  acs1.constraint_schema         acs_constraint_schema, \n" + 
						"  acs1.constraint_name           acs_constraint_name, \n" +
						"  acs1.table_schema              acs_table_schema, \n" +
						"  acs1.table_name                acs_table_name, \n" +
						"  acs2.unique_constraint_schema  acd_constraint_schema, \n" +
						"  acs2.unique_constraint_name    acd_constraint_name, \n" +
						"  acd.table_schema               acd_table_schema, \n" +
						"  acd.table_name                 acd_table_name \n" +
						"from \n" +
						"  information_schema.table_constraints  acs1 \n" + 
						"  left join \n" +
						"  information_schema.referential_constraints acs2 \n" + 
						"  on \n" +
						"  acs1.constraint_catalog = acs2.constraint_catalog \n" + 
						"  and \n" +
						"  acs1.constraint_schema = acs2.constraint_schema \n" + 
						"  and \n" +
						"  acs1.constraint_name = acs2.constraint_name \n" + 
						"  left join \n" +
						"  information_schema.table_constraints  acd \n" + 
						"  on \n" +
						"  acs2.unique_constraint_catalog = acd.constraint_catalog \n" + 
						"  and \n" +
						"  acs2.unique_constraint_schema   = acd.constraint_schema \n" + 
						"  and \n" +
						"  acs2.unique_constraint_name = acd.constraint_name \n" + 
						"where \n" +
						"  acs1.constraint_type = 'FOREIGN KEY' \n" + 
						"order by acs1.constraint_name"
						*/
					);
				}
				else if ( newVal == tb5 )
				{
					taSQL.setText
					(
						"select * from table; \n" +
						"select c.id, count(*) cnt from sakila.country c group by c.id having count(*) > 2; \n" +
						"update country set population= 100 where id = 5; "
					);
				}
			}
		);
		
		HBox hBoxTG = new HBox(2);
		hBoxTG.getChildren().addAll( tb1, tb2, tb3, tb4, tb5 );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( taSQL, hBoxParse, hBoxTG );
		
		SplitPane sp = new SplitPane();
		sp.setOrientation( Orientation.VERTICAL );
		sp.getItems().addAll( vBox, taResult );
		sp.setDividerPositions( 0.5f, 0.5f );
		
		Scene scene = new Scene( sp, 640, 480 );
		stage.setScene(scene);
		stage.show();
		
	}
	
	private void parse()
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String sqlStr = this.taSQL.getText();
			Statement stmt = CCJSqlParserUtil.parse(sqlStr);
			process( stmt, sb );
		}
		catch ( JSQLParserException jsqlEx )
		{
			//jsqlEx.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter  pw = new PrintWriter(sw);
			jsqlEx.printStackTrace(pw);
			sb.append( "=== JSQLParserException =============\n" );
			sb.append( sw.toString() );
		}
		finally
		{
			this.taResult.setText( sb.toString() );
		}
	}
	
	private void parseStatements()
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String sqlStr = this.taSQL.getText();
			Statements stmts = CCJSqlParserUtil.parseStatements(sqlStr);
			for ( Statement stmt : stmts.getStatements() )
			{
				process( stmt, sb );
				sb.append( "\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n" );
			}
		}
		catch ( JSQLParserException jsqlEx )
		{
			//jsqlEx.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter  pw = new PrintWriter(sw);
			jsqlEx.printStackTrace(pw);
			sb.append( "=== JSQLParserException =============\n" );
			sb.append( sw.toString() );
		}
		finally
		{
			this.taResult.setText( sb.toString() );
		}
	}
	
	private void process( Statement stmt, StringBuffer sb )
	{
		System.out.println( "SQL:" + stmt );
		if ( stmt instanceof Select )
		{
			Select select = (Select)stmt;
			processSelectBody( select.getSelectBody(), 0, sb );
			
			sb.append( "=== TablesNamesFinder =====================\n" );
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List<String> tblLst2 = tablesNamesFinder.getTableList(select);
			for ( int i = 0; i < tblLst2.size(); i++ )
			{
				String tbl2 = tblLst2.get(i);
				sb.append( "TABLE:" + tbl2 + "\n" );
			}				
		}
		else if ( stmt instanceof Insert )
		{
			Insert insert = (Insert)stmt;
			sb.append( "=== getColumns =====================\n" );
			for ( Column column : insert.getColumns() )
			{
				processColumn( column, 0, sb );
				sb.append( "----------------------" );
			}
		}
		else if ( stmt instanceof Alter )
		{
			Alter alter = (Alter)stmt;
			sb.append( "=== Alter Table ================\n" );
			Table table = alter.getTable();
			sb.append( table.getName() );
		}
	}
	
	private void parseExpression()
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			String sqlStr = this.taSQL.getText();
			Expression expr = CCJSqlParserUtil.parseExpression(sqlStr);
			processExpression( expr, 0, sb );
		}
		catch ( JSQLParserException jsqlEx )
		{
			//jsqlEx.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter  pw = new PrintWriter(sw);
			jsqlEx.printStackTrace(pw);
			sb.append( "=== JSQLParserException =============\n" );
			sb.append( sw.toString() );
		}
		finally
		{
			this.taResult.setText( sb.toString() );
		}
	}
	
	private void processSelectBody(SelectBody selectBody, int level, StringBuffer sb )
	{
		String tab = String.join( "",  Collections.nCopies( level, "  " ) );
		if ( selectBody instanceof PlainSelect )
		{
			PlainSelect pl = (PlainSelect)selectBody;
			
			sb.append( tab + "=== getSelectItems =====================\n" );
			for (SelectItem selectItem : pl.getSelectItems())
			{
				processSelectItem( selectItem, level+1, sb );
			}
			
			sb.append( tab + "=== getIntoTables =====================\n" );
			List<Table> tblLst = pl.getIntoTables();
			if ( tblLst != null )
			{
				for ( Table tbl : tblLst )
				{
					processTable( tbl, level+1, sb );
				}
			}
			
			sb.append( tab + "=== getJoins =====================\n" );
			List<Join> joinLst = pl.getJoins();
			if ( joinLst != null )
			{
				for ( Join join : joinLst )
				{
					processJoin( join, level+1, sb );
				}
			}
			
			sb.append( tab + "=== getFromItem =====================\n" );
			processFromItem( pl.getFromItem(), level+1, sb );
			
			sb.append( tab + "=== getWhere =====================\n" );
			processExpression( pl.getWhere(), level+1, sb);
			
			sb.append( tab + "=== getGroupByColumnReferences =====================\n" );
			List<Expression> groupLst = pl.getGroupByColumnReferences();
			if ( groupLst != null )
			{
				for ( Expression group : groupLst )
				{
					processExpression( group, level+1, sb);
				}
			}
			
			sb.append( tab + "=== getHaving =====================\n" );
			processExpression( pl.getHaving(), level+1, sb);
			
			sb.append( tab + "=== getOrderByElements =====================\n" );
			List<OrderByElement> orderLst = pl.getOrderByElements();
			if ( orderLst != null )
			{
				for ( OrderByElement order : orderLst )
				{
					processExpression( order.getExpression(), level+1, sb);
				}
			}
		}
	}
	
	private void processSelectItem(SelectItem selectItem, int level, StringBuffer sb )
	{
		if ( selectItem == null )
		{
			return;
		}
    	String tab = String.join( "",  Collections.nCopies( level, "  " ) );
		sb.append( tab + "SelectItem:Class:" + selectItem.getClass() + "\n" );
		sb.append( tab + "SelectItem:" + selectItem + "\n" );
		
    	if ( selectItem instanceof SelectExpressionItem )
    	{
    		processAlias( ((SelectExpressionItem)selectItem).getAlias(), level+1, sb );
    		processExpression( ((SelectExpressionItem) selectItem).getExpression(), level+1, sb );
    	}
    	else if ( selectItem instanceof AllTableColumns )
    	{
    		processTable( ((AllTableColumns) selectItem).getTable(), level+1, sb );
    	}
    	else if ( selectItem instanceof AllColumns )
    	{
    		sb.append( tab + "SelectItem:AllColumns:" + selectItem.toString() + "\n" );
    	}
        else
        {
        	sb.append( tab + "SelectItem:UnkonwnClass:" + selectItem.toString() + "\n" );
        }    	
		
	}
	
	private void processJoin( Join join, int level, StringBuffer sb )
	{
		if ( join == null )
		{
			return;
		}
    	String tab = String.join( "",  Collections.nCopies( level, " " ) );
		sb.append( tab + "Join:" + join + "\n" );
		processExpression( join.getOnExpression(), level+1, sb );
		processFromItem( join.getRightItem(), level+1, sb );
	}
	
    private void processFromItem(FromItem fromItem, int level, StringBuffer sb)
    {
    	if ( fromItem == null )
    	{
    		return;
    	}
    	String tab = String.join( "",  Collections.nCopies( level, " " ) );
		sb.append( tab + "FromItem:Class:" + fromItem.getClass() + "\n" );
		sb.append( tab + "FromItem:" + fromItem + "\n" );
    	
        if (fromItem instanceof SubJoin)
        {
        	processAlias( ((SubJoin)fromItem).getAlias(), level+1, sb );
        	processJoin( ((SubJoin)fromItem).getJoin(), level+1, sb );
        	processFromItem( ((SubJoin)fromItem).getLeft(), level+1, sb );
        } 
        else if (fromItem instanceof SubSelect)
        {
        	processAlias( ((SubSelect)fromItem).getAlias(), level+1, sb );
        	processSelectBody( ((SubSelect)fromItem).getSelectBody(), level+1, sb );
        } 
        else if (fromItem instanceof ValuesList)
        {
        	processAlias( ((ValuesList)fromItem).getAlias(), level+1, sb );
        	for ( String colName: ((ValuesList)fromItem).getColumnNames() )
        	{
        		System.out.println( tab + "ValuesList:" + colName + "\n" );
        	}
 
        } 
        else if (fromItem instanceof LateralSubSelect) 
        {
        	processAlias( ((LateralSubSelect)fromItem).getAlias(), level+1, sb );
        	processFromItem( ((LateralSubSelect)fromItem).getSubSelect(), level+1, sb );
        } 
        else if ( fromItem instanceof Table )
        {
        	processTable( ((Table)fromItem), level+1, sb );
        }
        else
        {
        	sb.append( tab + "FromItem:UnkonwnClass:" + fromItem.getClass() + ":" + fromItem.toString() + "\n" );
        }
    } 	
    
    private void processExpression(Expression expression, int level, StringBuffer sb)
    {
    	if ( expression == null )
    	{
    		return;
    	}
    	String tab = String.join( "",  Collections.nCopies( level, "  " ) );
		sb.append( tab + "Expression:Class:" + expression.getClass() + "\n" );
		sb.append( tab + "Expression:" + expression + "\n" );
    	
    	if ( expression instanceof AndExpression )
    	{
    		processExpression( ((AndExpression) expression).getLeftExpression(), level+1, sb );
    		processExpression( ((AndExpression) expression).getRightExpression(), level+1, sb );
    	}
    	else if ( expression instanceof GreaterThanEquals )
    	{
    		processExpression( ((GreaterThanEquals) expression).getLeftExpression(), level+1, sb );
    		processExpression( ((GreaterThanEquals) expression).getRightExpression(), level+1, sb );
    	}
    	else if ( expression instanceof MinorThan )
    	{
    		processExpression( ((MinorThan) expression).getLeftExpression(), level+1, sb );
    		processExpression( ((MinorThan) expression).getRightExpression(), level+1, sb );
    	}
    	else if ( expression instanceof Column )
    	{
    		processColumn( (Column)expression, level, sb );
    	}
    	else if ( expression instanceof MultipleExpression )
    	{
    		for ( Expression expressionEach : ((MultipleExpression)expression).getList() )
    		{
    			processExpression( expressionEach, level+1, sb );
    		}
    	}
    	else if ( expression instanceof Multiplication )
    	{
    		sb.append( tab + "Multiplication:" + ((Multiplication)expression).getStringExpression() );
    		processExpression( ((Multiplication)expression).getLeftExpression(), level+1, sb );
    		processExpression( ((Multiplication)expression).getRightExpression(), level+1, sb );
    	}
    	else
    	{
    		sb.append( tab + "Expression:UnkonwnClass:" + expression.getClass() + ":" + expression.toString() + "\n" );
    	}
    }
    
    private void processColumn( Column col, int level, StringBuffer sb )
    {
    	if ( col == null )
    	{
    		return;
    	}
    	String tab = String.join( "",  Collections.nCopies( level, "  " ) );
    	sb.append( tab + "Column:ColumnName:" + col.getColumnName() + "\n" );
    	sb.append( tab + "Column:FullyQualifiedName:" + col.getFullyQualifiedName() + "\n" );
    	Table tbl = col.getTable();
    	this.processTable( tbl, level, sb );
    }
    
    private void processTable( Table tbl, int level, StringBuffer sb )
    {
    	if ( tbl == null )
    	{
    		return;
    	}
    	String tab = String.join( "",  Collections.nCopies( level, "  " ) );
    	sb.append( tab + "Table:TableName:" + tbl.getName() + "\n" );
    	sb.append( tab + "Table:FullyQualifiedName:" + tbl.getFullyQualifiedName() + "\n" );
    	sb.append( tab + "Table:SchemaName:" + tbl.getSchemaName() + "\n" );
    	processAlias( tbl.getAlias(), level, sb );
    	processDatabase( tbl.getDatabase(), level, sb );
    }
    
    private void processAlias( Alias alias, int level, StringBuffer sb )
    {
    	if ( alias == null )
    	{
    		return;
    	}
    	String tab = String.join( "",  Collections.nCopies( level, "  " ) );
    	sb.append( tab + "AliasName:" + alias.getName() + "\n" );
    }
    
    private void processDatabase( Database database, int level, StringBuffer sb )
    {
    	if ( database == null )
    	{
    		return;
    	}
    	String tab = String.join( "",  Collections.nCopies( level, "  " ) );
    	sb.append( tab + "Database:DatabaseName:" + database.getDatabaseName() + "\n" );
    }    
}
