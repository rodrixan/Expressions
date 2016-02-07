package es.uam.eps.expressions.types;

import java.util.ArrayList;
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

	static {
		validProperties = new ArrayList<>();
		validProperties.add(Properties.ASSOCIATIVE);
		validProperties.add(Properties.CONMUTATIVE);
		validProperties.add(Properties.DISTRIBUTIVE);
	}

	public MULList() {
		super(Operator.MUL);

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

}
