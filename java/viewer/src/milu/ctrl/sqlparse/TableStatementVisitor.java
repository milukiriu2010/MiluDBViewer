package milu.ctrl.sqlparse;

import java.util.List;

import milu.ctrl.sqlparse.SQLBag.COMMAND;
import milu.ctrl.sqlparse.SQLBag.TYPE;

import java.util.ArrayList;

import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.SetStatement;
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
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;

public class TableStatementVisitor implements AnalyzeStatementVisitor 
{
	private AnalyzeSelectVisitor  analyzeSelectVisitor = null;
	
	private List<String> sqlLst = new ArrayList<>();
	
	private SQLBag.COMMAND  sqlCommand = COMMAND.UNKNOWN_COMMAND;
	
	private SQLBag.TYPE     sqlType    = TYPE.UNKNOWN_TYPE;
	
	@Override
	public void visit(Commit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Delete delete) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.DELETE;
	}

	@Override
	public void visit(Update update) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.UPDATE;
	}

	@Override
	public void visit(Insert insert)
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.INSERT;
	}

	@Override
	public void visit(Replace replace) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.REPLACE;
	}

	@Override
	public void visit(Drop drop) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.DROP;
	}

	@Override
	public void visit(Truncate truncate) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.TRUNCATE;
	}

	@Override
	public void visit(CreateIndex createIndex) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.CREATE_INDEX;
	}

	@Override
	public void visit(CreateTable createTable) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.CREATE_TABLE;
	}

	@Override
	public void visit(CreateView createView) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.CREATE_VIEW;
	}

	@Override
	public void visit(AlterView alterView) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.ALTER_VIEW;
	}

	@Override
	public void visit(Alter alter) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.ALTER;
	}

	@Override
	public void visit(Statements stmts) 
	{
		this.sqlLst.clear();
		stmts.getStatements().forEach( (stmt)->this.sqlLst.add(stmt.toString()) );
	}

	@Override
	public void visit(Execute execute) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.ALTER;
	}

	@Override
	public void visit(SetStatement setStmt) 
	{
		// use miludb;

	}

	@Override
	public void visit(Merge merge)
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.MERGE;
	}

	@Override
	public void visit(Select select) 
	{
		this.sqlCommand = SQLBag.COMMAND.QUERY;
		this.sqlType    = SQLBag.TYPE.SELECT;
		SelectBody selectBody = select.getSelectBody();
		if ( selectBody != null )
		{
			selectBody.accept(this.analyzeSelectVisitor);
		}
	}

	@Override
	public void visit(Upsert arg0) 
	{
		this.sqlCommand = SQLBag.COMMAND.TRANSACTION;
		this.sqlType    = SQLBag.TYPE.UPSERT;
	}

	@Override
	public void visit(UseStatement arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAnalyzeSelectVistor(AnalyzeSelectVisitor analyzeSelectVisitor) 
	{
		this.analyzeSelectVisitor = analyzeSelectVisitor;
	}
	
	@Override
	public List<String> getSqlLst()
	{
		return this.sqlLst;
	}
	
	@Override
	public SQLBag.COMMAND getCommand()
	{
		return this.sqlCommand;
	}
	
	@Override
	public SQLBag.TYPE getType()
	{
		return this.sqlType;
	}

}
