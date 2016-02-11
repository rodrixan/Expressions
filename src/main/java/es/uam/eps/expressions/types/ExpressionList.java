package es.uam.eps.expressions.types;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.interfaces.Operator;

/**
 * Implements a list of expressions
 *
 * @author Rodrigo de Blas
 *
 * @param <E>
 *            parameter of the class. Must implement Element interface
 *
 * @see es.uam.eps.expressions.types.interfaces.Expression
 */
public abstract class ExpressionList<E extends Expression> extends ArrayList<Expression> implements Expression {

	/** Type of the list */
	private final Operator operator;

	private final SingleExpression neutralElement;

	/** Names of the accepted properties */
	protected final List<String> validProperties;

	protected ExpressionList(Operator operator, SingleExpression neutralElement) {
		super();
		this.operator = operator;
		validProperties = new ArrayList<>();
		this.neutralElement = neutralElement;
	}

	@Override
	public String toString() {

		return operator + super.toString();
	}

	public Operator getOperator() {
		return operator;
	}

	@Override
	public String symbolicExpression() {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		for (final Expression e : this) {
			if (e instanceof ExpressionList) {
				sb.append("(" + e.symbolicExpression() + ")");
			} else {
				sb.append(e.symbolicExpression());
			}
			if (i != this.size() - 1) {
				sb.append(" " + operator + " ");
			}
			i++;
		}

		return sb.toString();
	}

	/**
	 * Returns a subexpression from a main one
	 *
	 * @param fromIndex
	 *            starting index
	 * @param toIndex
	 *            ending index
	 * @return subexpression from starting index to final one
	 * @see ArrayList "sublist" method
	 */
	public abstract ExpressionList<E> subExpressionList(int fromIndex, int toIndex);

	/**
	 * @return an ExpressionList of the same type
	 */
	public abstract ExpressionList<E> getSameTypeExpressionList();

	/**
	 * @return the neutral element of the expression list
	 */
	public SingleExpression getNeutralElement() {
		return neutralElement;
	}
}
