package es.uam.eps.expressions.types;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

import es.uam.eps.expressions.types.interfaces.Expression;

/**
 * Main Class for an expression: it can be a number, variable or a list
 *
 * @author Rodrigo de Blas
 *
 */
public class SingleExpression implements Expression {

	protected final String txt;

	public SingleExpression(String txt) {
		this.txt = txt;
	}

	@Override
	public String toString() {
		return txt;
	}

	@Override
	public int getValue() {
		return Expression.VALUE_NOT_AVAILABLE;
	}

	@Override
	public String symbolicExpression() {
		return txt;
	}

	@Override
	public boolean isValidProperty(String propName) {
		return false;
	}

	@Override
	public List<String> validProperties() {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SingleExpression)) {
			return false;
		}
		final SingleExpression other = (SingleExpression) obj;

		return new EqualsBuilder().append(this.txt, other.toString()).isEquals();
	}
}
