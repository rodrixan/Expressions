package es.uam.eps.expressions.types;

import java.util.ArrayList;

public abstract class ExpressionList<E> extends ArrayList<E> implements Element {

	private final Operator operator;

	public ExpressionList(Operator operator) {
		super();
		this.operator = operator;
	}

	@Override
	public String toString() {

		return operator.toString() + super.toString();
	}

}
