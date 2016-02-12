package es.uam.eps.expressions.types.operations;

import java.util.List;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.interfaces.Operator;

/**
 * Implements the negation operation (-a). It only admits ONE ELEMENT
 *
 * @author Rodrigo de Blas
 *
 * @param <E>
 *            parameter of the class. Must implement Element interface
 */
public class NEG<E extends Expression> extends ExpressionList<E> implements Expression {

	private NEG() {
		super(Operator.NEG, Operator.SUM, new SingleExpression("0"));
	}

	public NEG(Expression e) {
		this();
		add(e);
		loadProperties();
	}

	@Override
	protected void loadProperties() {
		validProperties.add(Properties.INVERSE);
	}

	@Override
	public boolean add(Expression e) {
		if (size() == 1) {
			throw new IllegalArgumentException("This operation only admits one expression");
		}
		return super.add(e);
	}

	@Override
	public int getValue() {

		return VALUE_NOT_AVAILABLE;
	}

	@Override
	public String symbolicExpression() {
		final Expression e = get(0);
		if (e instanceof SingleExpression) {

			return "-" + e.symbolicExpression();
		}

		return "-(" + e.symbolicExpression() + ")";
	}

	@Override
	public boolean isValidProperty(String propName) {
		return validProperties.contains(propName);
	}

	@Override
	public List<String> validProperties() {
		return validProperties;
	}

	@Override
	public ExpressionList<E> subExpressionList(int fromIndex, int toIndex) {
		return this;
	}

	@Override
	public ExpressionList<E> getSameTypeExpressionList() {
		return new NEG<E>();
	}

}
