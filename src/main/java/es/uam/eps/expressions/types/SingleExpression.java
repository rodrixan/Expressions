package es.uam.eps.expressions.types;

import java.util.List;

/**
 * Main Class for an expression: it can be a number, variable or a list
 *
 * @author Rodrigo de Blas
 *
 */
public class SingleExpression implements Element {

	private String txt;

	public SingleExpression(String txt) {
		this.txt = txt;
	}

	@Override
	public String toString() {

		return txt;
	}

	@Override
	public int getValue() {
		return Element.VALUE_NOT_AVAILABLE;
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

}
