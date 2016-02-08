package es.uam.eps.expressions.types;

import java.util.List;

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

	/**
	 * Decides whether or not a property can be applied to the expression
	 *
	 * @param property
	 *            name of the property.
	 * @return true if the property can be applied, false if not
	 *
	 * @see es.uam.eps.expressions.properties.Properties
	 */
	public boolean isValidProperty(String propName);

	/**
	 * @return List with the names of the properties than can be applied to the
	 *         expression
	 * @see es.uam.eps.expressions.properties.Properties
	 */
	public List<String> validProperties();
}
