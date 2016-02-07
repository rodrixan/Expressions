package es.uam.eps.expressions.types;

/**
 * Generic interface for a CAS Element
 *
 * @author Rodrigo de Blas
 *
 */
public interface Element {
	public static final int VALUE_NOT_AVAILABLE = Integer.MIN_VALUE;

	/**
	 * @return numeric value of the expression, VALUE_NOT_AVAILABLE if the
	 *         expression is a symbolic one
	 */
	public int getValue();

	/**
	 * @return infix notation expression for the element (i.e. a+b+(b*c)...)
	 */
	public String symbolicExpression();
}
