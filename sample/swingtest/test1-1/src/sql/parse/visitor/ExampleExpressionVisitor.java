package sql.parse.visitor;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public class ExampleExpressionVisitor implements ExpressionVisitor {

	@Override
	public void visit(BitwiseRightShift arg0) {
		System.out.println( "BitwiseRightShift:" + arg0 );

	}

	@Override
	public void visit(BitwiseLeftShift arg0) {
		System.out.println( "BitwiseLeftShift:" + arg0 );

	}

	@Override
	public void visit(NullValue arg0) {
		System.out.println( "NullValue:" + arg0 );

	}

	@Override
	public void visit(Function arg0) {
		System.out.println( "Function:" + arg0 );

	}

	@Override
	public void visit(SignedExpression arg0) {
		System.out.println( "SignedExpression:" + arg0 );

	}

	@Override
	public void visit(JdbcParameter arg0) {
		System.out.println( "JdbcParameter:" + arg0 );

	}

	@Override
	public void visit(JdbcNamedParameter arg0) {
		System.out.println( "JdbcNamedParameter:" + arg0 );

	}

	@Override
	public void visit(DoubleValue arg0) {
		System.out.println( "DoubleValue:" + arg0 );

	}

	@Override
	public void visit(LongValue arg0) {
		System.out.println( "LongValue:" + arg0 );

	}

	@Override
	public void visit(HexValue arg0) {
		System.out.println( "HexValue:" + arg0 );

	}

	@Override
	public void visit(DateValue arg0) {
		System.out.println( "DateValue:" + arg0 );

	}

	@Override
	public void visit(TimeValue arg0) {
		System.out.println( "TimeValue:" + arg0 );

	}

	@Override
	public void visit(TimestampValue arg0) {
		System.out.println( "TimestampValue:" + arg0 );

	}

	@Override
	public void visit(Parenthesis arg0) {
		System.out.println( "Parenthesis:" + arg0 );

	}

	@Override
	public void visit(StringValue arg0) {
		System.out.println( "StringValue:" + arg0 );

	}

	@Override
	public void visit(Addition arg0) {
		System.out.println( "Addition:" + arg0 );

	}

	@Override
	public void visit(Division arg0) {
		System.out.println( "Division:" + arg0 );

	}

	@Override
	public void visit(Multiplication arg0) {
		System.out.println( "Multiplication:" + arg0 );

	}

	@Override
	public void visit(Subtraction arg0) {
		System.out.println( "Subtraction:" + arg0 );

	}

	@Override
	public void visit(AndExpression arg0) {
		System.out.println( "AndExpression:" + arg0 );

	}

	@Override
	public void visit(OrExpression arg0) {
		System.out.println( "OrExpression:" + arg0 );

	}

	@Override
	public void visit(Between arg0) {
		System.out.println( "Between:" + arg0 );

	}

	@Override
	public void visit(EqualsTo arg0) {
		System.out.println( "EqualsTo:" + arg0 );

	}

	@Override
	public void visit(GreaterThan arg0) {
		System.out.println( "GreaterThan:" + arg0 );

	}

	@Override
	public void visit(GreaterThanEquals arg0) {
		System.out.println( "GreaterThanEquals:" + arg0 );

	}

	@Override
	public void visit(InExpression arg0) {
		System.out.println( "InExpression:" + arg0 );

	}

	@Override
	public void visit(IsNullExpression arg0) {
		System.out.println( "IsNullExpression:" + arg0 );

	}

	@Override
	public void visit(LikeExpression arg0) {
		System.out.println( "LikeExpression:" + arg0 );

	}

	@Override
	public void visit(MinorThan arg0) {
		System.out.println( "MinorThan:" + arg0 );

	}

	@Override
	public void visit(MinorThanEquals arg0) {
		System.out.println( "MinorThanEquals:" + arg0 );

	}

	@Override
	public void visit(NotEqualsTo arg0) {
		System.out.println( "NotEqualsTo:" + arg0 );

	}

	@Override
	public void visit(Column arg0) {
		System.out.println( "Column:" + arg0 );

	}

	@Override
	public void visit(SubSelect arg0) {
		System.out.println( "SubSelect:" + arg0 );

	}

	@Override
	public void visit(CaseExpression arg0) {
		System.out.println( "CaseExpression:" + arg0 );

	}

	@Override
	public void visit(WhenClause arg0) {
		System.out.println( "WhenClause:" + arg0 );

	}

	@Override
	public void visit(ExistsExpression arg0) {
		System.out.println( "ExistsExpression:" + arg0 );

	}

	@Override
	public void visit(AllComparisonExpression arg0) {
		System.out.println( "AllComparisonExpression:" + arg0 );

	}

	@Override
	public void visit(AnyComparisonExpression arg0) {
		System.out.println( "AnyComparisonExpression:" + arg0 );

	}

	@Override
	public void visit(Concat arg0) {
		System.out.println( "Concat:" + arg0 );

	}

	@Override
	public void visit(Matches arg0) {
		System.out.println( "Matches:" + arg0 );

	}

	@Override
	public void visit(BitwiseAnd arg0) {
		System.out.println( "BitwiseAnd:" + arg0 );

	}

	@Override
	public void visit(BitwiseOr arg0) {
		System.out.println( "BitwiseOr:" + arg0 );
	}

	@Override
	public void visit(BitwiseXor arg0) {
		System.out.println( "BitwiseXor:" + arg0 );

	}

	@Override
	public void visit(CastExpression arg0) {
		System.out.println( "CastExpression:" + arg0 );

	}

	@Override
	public void visit(Modulo arg0) {
		System.out.println( "Modulo:" + arg0 );

	}

	@Override
	public void visit(AnalyticExpression arg0) {
		System.out.println( "AnalyticExpression:" + arg0 );

	}

	@Override
	public void visit(ExtractExpression arg0) {
		System.out.println( "ExtractExpression:" + arg0 );

	}

	@Override
	public void visit(IntervalExpression arg0) {
		System.out.println( "IntervalExpression:" + arg0 );

	}

	@Override
	public void visit(OracleHierarchicalExpression arg0) {
		System.out.println( "OracleHierarchicalExpression:" + arg0 );

	}

	@Override
	public void visit(RegExpMatchOperator arg0) {
		System.out.println( "RegExpMatchOperator:" + arg0 );

	}

	@Override
	public void visit(JsonExpression arg0) {
		System.out.println( "JsonExpression:" + arg0 );

	}

	@Override
	public void visit(JsonOperator arg0) {
		System.out.println( "JsonOperator:" + arg0 );

	}

	@Override
	public void visit(RegExpMySQLOperator arg0) {
		System.out.println( "RegExpMySQLOperator:" + arg0 );


	}

	@Override
	public void visit(UserVariable arg0) {
		System.out.println( "UserVariable:" + arg0 );

	}

	@Override
	public void visit(NumericBind arg0) {
		System.out.println( "NumericBind:" + arg0 );

	}

	@Override
	public void visit(KeepExpression arg0) {
		System.out.println( "KeepExpression:" + arg0 );

	}

	@Override
	public void visit(MySQLGroupConcat arg0) {
		System.out.println( "MySQLGroupConcat:" + arg0 );

	}

	@Override
	public void visit(RowConstructor arg0) {
		System.out.println( "RowConstructor:" + arg0 );

	}

	@Override
	public void visit(OracleHint arg0) {
		System.out.println( "OracleHint:" + arg0 );

	}

	@Override
	public void visit(TimeKeyExpression arg0) {
		System.out.println( "TimeKeyExpression:" + arg0 );

	}

	@Override
	public void visit(DateTimeLiteralExpression arg0) {
		System.out.println( "DateTimeLiteralExpression:" + arg0 );

	}

	@Override
	public void visit(NotExpression arg0) {
		System.out.println( "NotExpression:" + arg0 );

	}

	// 1.3
	@Override
	public void visit(ValueListExpression arg0) {
		System.out.println( "ValueListExpression:" + arg0 );

	}

}
