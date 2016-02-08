package es.uam.eps.expressions.types;

import java.util.List;

import es.uam.eps.expressions.properties.Properties;

/**
 * Implements the product operation (a*b*...)
 *
 * @author Rodrigo de Blas
 *
 * @param <E>
 *            parameter of the class. Must implement Element interface
 */
public class MULList<E extends Element> extends ExpressionList<Element> {

	public MULList() {
		super(Operator.MUL);
		loadProperties();
	}

	private void loadProperties() {
		validProperties.add(Properties.ASSOCIATIVE);
		validProperties.add(Properties.CONMUTATIVE);
		validProperties.add(Properties.DISTRIBUTIVE);
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
		final ExpressionList<Element> ret = new MULList<>();
		ret.addAll(subList(fromIndex, toIndex));
		return ret;
	}

}
