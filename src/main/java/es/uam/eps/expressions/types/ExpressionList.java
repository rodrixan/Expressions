package es.uam.eps.expressions.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a list of expressions
 *
 * @author Rodrigo de Blas
 *
 * @param <E>
 *            parameter of the class. Must implement Element interface
 *
 * @see es.uam.eps.expressions.types.Element
 */
public abstract class ExpressionList<E extends Element> extends ArrayList<Element> implements Element {
	/** Type of the list */
	private final Operator operator;
	/** Names of the accepted properties */
	protected final List<String> validProperties;

	public ExpressionList(Operator operator) {
		super();
		this.operator = operator;
		validProperties = new ArrayList<>();
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
		for (final Element e : this) {
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

	public abstract ExpressionList<E> subExpressionList(int fromIndex, int toIndex);
}
