package es.uam.eps.expressions.types;

/**
 * Main Class for an expression: it can be a number, variable or a list
 *
 * @author Rodrigo de Blas
 *
 */
public class Expression implements Element {

	private String txt;

	public Expression(String txt) {
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

}
