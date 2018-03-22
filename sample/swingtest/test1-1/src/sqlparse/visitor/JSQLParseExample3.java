package sqlparse.visitor;

import java.util.Map;

import java.io.StringWriter;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.JSQLParserException;


// https://www.programcreek.com/java-api-examples/?api=net.sf.jsqlparser.parser.CCJSqlParserUtil
// https://www.programcreek.com/java-api-examples/index.php?source_dir=Mybatis-PageHelper-master/src/main/java/com/github/pagehelper/parser/SqlServer.java
public class JSQLParseExample3 extends Application
{
	private TextArea  taSQL    = new TextArea();
	private Button    btnParse = new Button("parse");
	private Button    btnParseStatements = new Button("parseStatements");
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
		
		HBox hBoxParse = new HBox(2);
		hBoxParse.getChildren().addAll( btnParse, btnParseStatements );
		
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
			
			this.analyze(stmt, sb);
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
			stmts.getStatements().forEach( (stmt)->analyze(stmt,sb) );
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
	
	private void analyze( Statement stmt, StringBuffer sb )
	{
		ExampleFromItemVisitor fromItemVisitor = new ExampleFromItemVisitor();
		stmt.accept
		(
			new ExampleStatementVisitor
			(
				new ExampleSelectVisitor( fromItemVisitor )
			)
		);
		Map<String,String>  tableMap = fromItemVisitor.getTableMap();
		tableMap.forEach
		( 
			(k,v)->
			{
				sb.append( "Table[" + k + "]Alias[" + v + "]\n" );
			}
		);
		System.out.println( "========================\n\n" );
	}
}
