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
	protected static List<String> validProperties;

	public ExpressionList(Operator operator) {
		super();
		this.operator = operator;
	}

	@Override
	public String toString() {

		return operator + super.toString();
	}

	public Operator getOperator() {
		return operator;
	}

	/**
	 * @return List with the names of the properties than can be applied to the
	 *         expression
	 * @see es.uam.eps.expressions.properties.Properties
	 */
	public abstract List<String> validProperties();

	/**
	 * Decides whether or not a property can be applied to the expression
	 *
	 * @param property
	 *            name of the property.
	 * @return true if the property can be applied, false if not
	 *
	 * @see es.uam.eps.expressions.properties.Properties
	 */
	public abstract boolean isValidProperty(String property);

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
}
