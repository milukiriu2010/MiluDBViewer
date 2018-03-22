package sqlparse.visitor;

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
	
	public ExampleStatementVisitor( SelectVisitor selectVisitor )
	{
		this.selectVisitor = selectVisitor;
	}
	

	@Override
	public void visit(Commit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Delete arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Update arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Insert arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Replace arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Drop arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Truncate arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CreateIndex arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CreateTable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CreateView arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AlterView arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Alter arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Statements stmts)
	{
		System.out.println( "StatementVisitor:Statements[" + stmts.getStatements().size() + "]");

	}

	@Override
	public void visit(Execute arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SetStatement arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Merge arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Select select) {
		// TODO Auto-generated method stub
		System.out.println( "StatementVisitor:Select[" + select + "]" );
		
		SelectBody selectBody = select.getSelectBody();
		if ( selectBody != null )
		{
			selectBody.accept(this.selectVisitor);
		}
	}

	@Override
	public void visit(Upsert arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(UseStatement arg0) {
		// TODO Auto-generated method stub

	}

}
