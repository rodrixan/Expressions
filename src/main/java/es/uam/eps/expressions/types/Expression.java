package es.uam.eps.expressions.types;

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

}
