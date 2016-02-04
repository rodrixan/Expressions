package es.uam.eps.expressions.types;

public class Expression {
	private ExpressionTree<ExpressionNode<String>> tree;

	public Expression() {
		tree = new ExpressionTree<>();
	}

	public ExpressionTree<ExpressionNode<String>> asExpressionTree() {
		return tree;
	}
}
