package es.uam.eps.expressions.types.interfaces;

/**
 * Operations accepted by the CAS
 *
 * @author Rodrigo de Blas
 *
 */
public enum Operator {
	SUM("+"), MUL("*"), NEG("-");

	private final String value;

	private Operator(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
