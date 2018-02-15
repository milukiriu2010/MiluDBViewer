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
import javafx.scene.layout.VBox;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;

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
import net.sf.jsqlparser.statement.Statement; 
import net.sf.jsqlparser.JSQLParserException;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;

import net.sf.jsqlparser.parser.SimpleNode;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Database;

import net.sf.jsqlparser.util.TablesNamesFinder;

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
			if ( stmt instanceof Select)
			{
				Select select = (Select)stmt;
				System.out.println(select.getSelectBody());
		
				sb.append( "=== getExpression =====================\n" );
				PlainSelect pl = (PlainSelect)select.getSelectBody();
				/*
				for (SelectItem selectItem : pl.getSelectItems())
				{
					sb.append(selectItem.toString() + "\n" );
				    
			    	//Expression expression = ((SelectExpressionItem)selectItem).getExpression();
			    	//sb.append("Expression["+expression+"]\n");
			    	if (selectItem instanceof SelectExpressionItem)
			    	{
				    	Expression expression = ((SelectExpressionItem)selectItem).getExpression();
				    	sb.append("Expression["+expression+"]\n");
			    		SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
			    		if (selectExpressionItem.getAlias() != null)
			    		{ 
			    			sb.append( "SelectExpressionItem:ALIAS:" + selectExpressionItem.getAlias().getName() + "\n" ); 
		                }
			    		else if ( selectExpressionItem.getExpression() instanceof Column )
			    		{
			    			Column column = (Column) selectExpressionItem.getExpression();
			    			if ( column.getTable() != null )
			    			{
			    				Table table = column.getTable();
			    				sb.append( "SelectExpressionItem:Column[" + column.getColumnName() + "]Table[" + table.getName() + "]\n" );
			    			}
			    			else
			    			{
			    				sb.append( "SelectExpressionItem:Column[" + column.getColumnName() + "]Table[NULL]\n" );
			    			}
			    		}
			    		else
			    		{
			    			sb.append( "SelectExpressionItem:OTHER[" + selectExpressionItem.toString() + "\n" );
			    		}
			    	}
			    	else if ( selectItem instanceof AllTableColumns )
			    	{
			    		AllTableColumns atCols = (AllTableColumns)selectItem;
			    		sb.append( "AllTableColumns[" + atCols + "]\n" );
			    		
			    	}
			    	sb.append( "_____________\n" );
			    	
				}
				*/
				for (SelectItem selectItem : pl.getSelectItems())
				{
					processSelectItem( selectItem, 0, sb );
				}
				
				sb.append( "=== getIntoTables =====================\n" );
				List<Table> tblLst = pl.getIntoTables();
				if ( tblLst != null )
				{
					for ( int i = 0; i < tblLst.size(); i++ )
					{
						Table tbl = tblLst.get(i);
						sb.append( "TABLE:" + tbl.getName() + "\n" );
					}
				}
				
				sb.append( "=== getJoins =====================\n" );
				List<Join> joinLst = pl.getJoins();
				if ( joinLst != null )
				{
					/*
					for ( int i = 0; i < joinLst.size(); i++ )
					{
						Join join = joinLst.get(i);
						sb.append( "JOIN:" + join + "\n" );
					}
					*/
					for ( Join join : joinLst )
					{
						processJoin( join, 0, sb );
					}
				}
				
				sb.append( "=== TablesNamesFinder =====================\n" );
				TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
				List<String> tblLst2 = tablesNamesFinder.getTableList(select);
				for ( int i = 0; i < tblLst2.size(); i++ )
				{
					String tbl2 = tblLst2.get(i);
					sb.append( "TABLE:" + tbl2 + "\n" );
				}
				
				sb.append( "=== getFromItem =====================\n" );
				FromItem fromItem = pl.getFromItem();
				if ( fromItem != null )
				{
					processFromItem(fromItem, 1, sb );
				}
				
				sb.append( "=== getWhere =====================\n" );
				Expression expWhere = pl.getWhere();
				if ( expWhere != null )
				{
					SimpleNode sNode = expWhere.getASTNode();
					if ( sNode != null )
					{
						sb.append( "getWhere:SimpleNode:ChildNum:" + sNode.jjtGetNumChildren() );
					}
					processExpression(expWhere, 0, sb);
				}
								
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
	
	private void processSelectBody(SelectBody selectBody, int level, StringBuffer sb )
	{
		
	}
	
	private void processSelectItem(SelectItem selectItem, int level, StringBuffer sb )
	{
		if ( selectItem == null )
		{
			return;
		}
    	String tab = String.join( "",  Collections.nCopies( level, " " ) );
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

        	/*
            SubJoin subJoin = (SubJoin) fromItem; 
            if (subJoin.getJoin() != null) { 
                if (subJoin.getJoin().getRightItem() != null) { 
                    processFromItem(subJoin.getJoin().getRightItem(), level + 1, sb); 
                } 
            } 
            if (subJoin.getLeft() != null) { 
                processFromItem(subJoin.getLeft(), level + 1, sb); 
            } 
            */
        } 
        else if (fromItem instanceof SubSelect)
        {
        	processAlias( ((SubSelect)fromItem).getAlias(), level+1, sb );
        	/*
        	sb.append( "FromItem:SubSelect\n" );
            SubSelect subSelect = (SubSelect) fromItem; 
            if (subSelect.getSelectBody() != null) { 
                //processSelectBody(subSelect.getSelectBody(), level + 1, sb); 
            } 
            */
        } else if (fromItem instanceof ValuesList) {
        	sb.append( "FromItem:ValuesList\n" );
 
        } else if (fromItem instanceof LateralSubSelect) {
        	sb.append( "FromItem:LateralSubSelect\n" );
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem; 
            if (lateralSubSelect.getSubSelect() != null) { 
                SubSelect subSelect = lateralSubSelect.getSubSelect(); 
                if (subSelect.getSelectBody() != null) { 
                    //processSelectBody(subSelect.getSelectBody(), level + 1, sb); 
                } 
            } 
        } 
        else if ( fromItem instanceof Table )
        {
        	processTable( ((Table)fromItem), level+1, sb );
        	/*
        	Table tbl = (Table)fromItem;
        	sb.append( "FromItem:Table:" + tbl.getName() + "\n" );
        	if ( tbl.getAlias() != null)
        	{
        		sb.append( "FromItem:Table:Alias:" + tbl.getAlias().getName() + "\n" );
        	}
        	*/
        }
        else
        {
        	sb.append( "FromItem:else:" + fromItem.toString() + "\n" );
        }
        //Table时不用处理 
    } 	
    
    private void processExpression(Expression expression, int level, StringBuffer sb)
    {
    	if ( expression == null )
    	{
    		return;
    	}
    	String tab = String.join( "",  Collections.nCopies( level, " " ) );
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
