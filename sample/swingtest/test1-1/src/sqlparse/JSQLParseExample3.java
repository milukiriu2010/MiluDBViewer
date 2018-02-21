package sqlparse;

import java.util.Map;

import java.io.StringWriter;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import net.sf.jsqlparser.statement.Statement; 
import net.sf.jsqlparser.JSQLParserException;


// https://www.programcreek.com/java-api-examples/?api=net.sf.jsqlparser.parser.CCJSqlParserUtil
// https://www.programcreek.com/java-api-examples/index.php?source_dir=Mybatis-PageHelper-master/src/main/java/com/github/pagehelper/parser/SqlServer.java
public class JSQLParseExample3 extends Application
{
	private TextArea  taSQL    = new TextArea();
	private Button    btnGo    = new Button("Go");
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
		
		btnGo.setOnAction( e->parse() );
		
		VBox vBox = new VBox(2);
		vBox.getChildren().addAll( taSQL, btnGo );
		
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
    
}
