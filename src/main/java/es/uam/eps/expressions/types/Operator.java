package es.uam.eps.expressions.types;

/**
 * Operations accepted by the CAS
 * 
 * @author Rodrigo de Blas
 *
 */
public enum Operator {
	SUM("+"), MUL("*");

	private final String value;

	private Operator(String value) {
		this.value = value;
	}

	// OR
	@Override
	public String toString() {
		return value;
	}
}
