package sql.format;

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

public class Example2StatementVisitor implements StatementVisitor {
	private String sqlType = null;
	
	public String getSqlType()
	{
		return this.sqlType;
	}
	
	@Override
	public void visit(Commit arg0) {
		this.sqlType = "COMMIT";

	}

	@Override
	public void visit(Delete arg0) {
		this.sqlType = "DELETE";

	}

	@Override
	public void visit(Update arg0) {
		this.sqlType = "UPDATE";

	}

	@Override
	public void visit(Insert arg0) {
		this.sqlType = "INSERT";

	}

	@Override
	public void visit(Replace arg0) {
		this.sqlType = "REPLACE";

	}

	@Override
	public void visit(Drop arg0) {
		this.sqlType = "DROP";

	}

	@Override
	public void visit(Truncate arg0) {
		this.sqlType = "TRUNCATE";

	}

	@Override
	public void visit(CreateIndex arg0) {
		this.sqlType = "CREATE INDEX";

	}

	@Override
	public void visit(CreateTable arg0) {
		this.sqlType = "CREATE TABLE";

	}

	@Override
	public void visit(CreateView arg0) {
		this.sqlType = "CREATE VIEW";

	}

	@Override
	public void visit(AlterView arg0) {
		this.sqlType = "ALTER VIEW";

	}

	@Override
	public void visit(Alter arg0) {
		this.sqlType = "ALTER";

	}

	@Override
	public void visit(Statements arg0) {
		this.sqlType = "STATEMENTS";

	}

	@Override
	public void visit(Execute arg0) {
		this.sqlType = "EXECUTE";

	}

	@Override
	public void visit(SetStatement arg0) {
		this.sqlType = "SET STATEMENT";

	}

	@Override
	public void visit(Merge arg0) {
		this.sqlType = "SET MERGE";

	}

	@Override
	public void visit(Select arg0) {
		this.sqlType = "SELECT";

	}

	@Override
	public void visit(Upsert arg0) {
		this.sqlType = "UPSERT";

	}

	@Override
	public void visit(UseStatement arg0) {
		this.sqlType = "USE STATEMENT";

	}

}
