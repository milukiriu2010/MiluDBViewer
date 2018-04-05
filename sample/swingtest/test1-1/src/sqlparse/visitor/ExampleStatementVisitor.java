package sqlparse.visitor;

import java.util.List;
import java.util.ArrayList;

import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;

import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitor;

public class ExampleStatementVisitor implements StatementVisitor 
{
	private SelectVisitor selectVisitor = null;
	
	private String sqlType = null;
	
	private List<String>  sqlLst = new ArrayList<>();
	
	private List<String>  tableLst = new ArrayList<>();
	
	public ExampleStatementVisitor( SelectVisitor selectVisitor )
	{
		this.selectVisitor = selectVisitor;
	}
	
	public String getSqlType()
	{
		return this.sqlType;
	}
	
	public List<String> getSqlLst()
	{
		return this.sqlLst;
	}
	
	public List<String> getTableLst()
	{
		return this.tableLst;
	}

	@Override
	public void visit(Commit commit) 
	{
		this.sqlType = "COMMIT";
	}

	@Override
	public void visit(Delete delete)
	{
		this.sqlType = "DELETE";
		this.tableLst.clear();
		//delete.getTables().forEach( (table)->this.tableLst.add(table.getName()) );
		this.tableLst.add( delete.getTable().getName() );
	}

	@Override
	public void visit(Update update)
	{
		this.sqlType = "UPDATE";
		this.tableLst.clear();
		update.getTables().forEach( (table)->this.tableLst.add(table.getName()) );
	}

	@Override
	public void visit(Insert insert) 
	{
		this.sqlType = "INSERT";
		this.tableLst.clear();
		this.tableLst.add( insert.getTable().getName() );
	}

	@Override
	public void visit(Replace arg0) 
	{
		this.sqlType = "REPLACE";
	}

	@Override
	public void visit(Drop drop) 
	{
		this.sqlType = "DROP";
	}

	@Override
	public void visit(Truncate truncate) 
	{
		this.sqlType = "TRUNCATE";
	}

	@Override
	public void visit(CreateIndex createIndex) 
	{
		this.sqlType = "CREATE INDEX";
	}

	@Override
	public void visit(CreateTable createTable) 
	{
		this.sqlType = "CREATE TABLE";
	}

	@Override
	public void visit(CreateView createView) 
	{
		this.sqlType = "CREATE VIEW";
	}

	@Override
	public void visit(AlterView alterView) 
	{
		this.sqlType = "ALTER VIEW";
	}

	@Override
	public void visit(Alter alter)
	{
		this.sqlType = "ALTER";
	}

	@Override
	public void visit(Statements stmts)
	{
		System.out.println( "StatementVisitor:Statements[" + stmts.getStatements().size() + "]");
		this.sqlLst.clear();
		stmts.getStatements().forEach( (stmt)->this.sqlLst.add(stmt.toString()) );
	}

	@Override
	public void visit(Execute execute) 
	{
		this.sqlType = "EXECUTE";
	}

	@Override
	public void visit(SetStatement setStmt) 
	{
		this.sqlType = "SET STATEMENT";
	}

	@Override
	public void visit(Merge merge) 
	{
		this.sqlType = "SET STATEMENT";
	}

	@Override
	public void visit(Select select) 
	{
		this.sqlType = "SELECT";		
		System.out.println( "StatementVisitor:Select[" + select + "]" );
		
		SelectBody selectBody = select.getSelectBody();
		if ( selectBody != null )
		{
			selectBody.accept(this.selectVisitor);
		}
	}

	@Override
	public void visit(Upsert upsert) 
	{
		this.sqlType = "UPSERT";
	}

	@Override
	public void visit(UseStatement useStmt) 
	{
		this.sqlType = "SET STATEMENT";
	}

}
