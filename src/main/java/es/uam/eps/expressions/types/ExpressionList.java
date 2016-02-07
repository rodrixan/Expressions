package es.uam.eps.expressions.types;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpressionList<E extends Element> extends ArrayList<Element> implements Element {

	private final Operator operator;
	protected static List<String> validProperties;

	public ExpressionList(Operator operator) {
		super();
		this.operator = operator;
	}

	@Override
	public String toString() {

		return operator + super.toString();
	}

	public Operator getOperator() {
		return operator;
	}

	public abstract List<String> validProperties();

	public abstract boolean isValidProperty(String property);
}
