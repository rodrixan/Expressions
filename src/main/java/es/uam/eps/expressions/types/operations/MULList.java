package es.uam.eps.expressions.types.operations;

import java.util.List;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.interfaces.Operator;

/**
 * Implements the product operation (a*b*...)
 *
 * @author Rodrigo de Blas
 *
 * @param <E>
 *            parameter of the class. Must implement Element interface
 */
public class MULList<E extends Expression> extends ExpressionList<Expression> {

	public MULList() {
		super(Operator.MUL, new SingleExpression("1"));
		loadProperties();
	}

	private void loadProperties() {
		validProperties.add(Properties.ASSOCIATIVE);
		validProperties.add(Properties.CONMUTATIVE);
		validProperties.add(Properties.DISTRIBUTIVE);
		validProperties.add(Properties.NEUTRAL_ELEMENT);
	}

	@Override
	public List<String> validProperties() {

		return validProperties;
	}

	@Override
	public boolean isValidProperty(String property) {

		return validProperties.contains(property);
	}

	@Override
	public int getValue() {
		return Expression.VALUE_NOT_AVAILABLE;
	}

	@Override
	public ExpressionList<Expression> subExpressionList(int fromIndex, int toIndex) {
		final ExpressionList<Expression> ret = new MULList<>();
		ret.addAll(subList(fromIndex, toIndex));
		return ret;
	}

	@Override
	public ExpressionList<Expression> getSameTypeExpressionList() {

		return new MULList<Expression>();
	}

}
