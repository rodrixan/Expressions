package es.uam.eps.expressions.types.operations;

import java.util.List;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.interfaces.Element;
import es.uam.eps.expressions.types.interfaces.Operator;

/**
 * Implements the sum operation (a+b+...)
 *
 * @author Rodrigo de Blas
 *
 * @param <E>
 *            parameter of the class. Must implement Element interface
 */
public class SUMList<E extends Element> extends ExpressionList<Element> {

	public SUMList() {
		super(Operator.SUM);
		loadProperties();
	}

	private void loadProperties() {
		validProperties.add(Properties.ASSOCIATIVE);
		validProperties.add(Properties.CONMUTATIVE);
		validProperties.add(Properties.COMMON_FACTOR);
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
		return Element.VALUE_NOT_AVAILABLE;
	}

	@Override
	public ExpressionList<Element> subExpressionList(int fromIndex, int toIndex) {
		final ExpressionList<Element> ret = new SUMList<>();
		ret.addAll(subList(fromIndex, toIndex));
		return ret;
	}

	@Override
	public ExpressionList<Element> getSameTypeExpressionList() {
		return new SUMList<Element>();
	}
}
