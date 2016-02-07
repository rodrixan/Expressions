package es.uam.eps.expressions.types;

import java.util.Arrays;
import java.util.List;

public class SUMList<E extends Element> extends ExpressionList<Element> {

	static {
		validProperties = Arrays.asList("associative", "conmutative");
	}

	public SUMList() {
		super(Operator.SUM);

	}

	@Override
	public List<String> validProperties() {

		return validProperties;
	}

	@Override
	public boolean isValidProperty(String property) {

		return validProperties().contains(property);
	}

	@Override
	public int getValue() {
		return Element.VALUE_NOT_AVAILABLE;
	}

}
