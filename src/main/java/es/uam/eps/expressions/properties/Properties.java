package es.uam.eps.expressions.properties;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.MULList;
import es.uam.eps.expressions.types.Operator;
import es.uam.eps.expressions.types.SUMList;

/**
 * Operations properties
 *
 * @author Rodrigo de Blas
 *
 */
public class Properties {
	/** Property names accepted by the CAS */
	public static final String ASSOCIATIVE = "associative";
	public static final String CONMUTATIVE = "conmutative";
	public static final String DISTRIBUTIVE = "distributive";

	private static final String PROP_ERROR = "Property \"*\" can't be applied to expression: ";
	private static final String ARG_ERROR = "Property \"*\" can't be applied: Expected argument with type \"*\"";

	/**
	 * Associates an arbitrary number of elements of an expression
	 * (i.e. a+b+c = (a+b)+c))
	 *
	 * @param exp
	 *            expression to which the property will be applied
	 * @param fromIndex
	 *            start index
	 * @param toIndex
	 *            end index
	 * @return expression of the same type with associated elements; the same
	 *         expression if all the elements were selected
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	public static ExpressionList<Element> associate(ExpressionList<Element> exp, int fromIndex, int toIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, ASSOCIATIVE);
		if (allElementsAreSelected(exp, fromIndex, toIndex)) {
			return exp;
		}
		final ExpressionList<Element> ret = getSameTypeList(exp.getOperator());
		final ExpressionList<Element> associatedElements = getAssociatedElements(exp, fromIndex, toIndex);

		ret.addAll(exp.subList(0, fromIndex));

		ret.add(associatedElements);

		ret.addAll(exp.subList(toIndex + 1, exp.size()));

		return ret;
	}

	/**
	 * Conmutes an element from one position to another one
	 * (i.e. a+b+c = b+a+c)
	 *
	 * @param exp
	 *            expression to which the property will be applied
	 * @param chosen
	 *            index of the element to conmute
	 * @param end
	 *            final position of the element to conmute
	 * @return expression of the same type with conmuted element
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	public static ExpressionList<Element> conmute(ExpressionList<Element> exp, int chosen, int end)
			throws IllegalPropertyException {
		checkValidOperation(exp, CONMUTATIVE);
		final ExpressionList<Element> ret = getSameTypeList(exp.getOperator());

		ret.addAll(exp);

		final Element chosenElement = ret.remove(chosen);

		ret.add(end, chosenElement);

		return ret;
	}

	public static ExpressionList<Element> distribute(ExpressionList<Element> exp, int chosen)
			throws IllegalPropertyException {
		checkValidOperation(exp, DISTRIBUTIVE);
		checkSUMList(exp.get(chosen));

		return null;
	}

	private static void checkSUMList(Element element) {
		if (!(element instanceof SUMList)) {
			throw new IllegalArgumentException(
					ARG_ERROR.replaceFirst("\\*", DISTRIBUTIVE).replaceFirst("\\*", SUMList.class.getName()));
		}
	}

	/**
	 * Checks if an expression admits a given property
	 *
	 * @param exp
	 *            expression to check
	 * @param propName
	 *            property to decide if can be applied
	 * @throws IllegalPropertyException
	 *             if the property can not be applied
	 */
	private static void checkValidOperation(ExpressionList<Element> exp, String propName)
			throws IllegalPropertyException {
		if (!exp.isValidProperty(propName)) {
			throw new IllegalPropertyException(PROP_ERROR.replaceFirst("\\*", propName) + exp.toString());
		}
	}

	private static boolean allElementsAreSelected(ExpressionList<Element> exp, int fromIndex, int toIndex) {
		return fromIndex == 0 && toIndex == exp.size() - 1;
	}

	/**
	 * Get the elements to associate
	 *
	 * @param exp
	 *            expression with the elements
	 * @param fromIndex
	 *            starting index
	 * @param toIndex
	 *            ending index
	 * @return ExpressionList with the selected elements
	 */
	private static ExpressionList<Element> getAssociatedElements(ExpressionList<Element> exp, int fromIndex,
			int toIndex) {
		final ExpressionList<Element> associatedElement = getSameTypeList(exp.getOperator());
		associatedElement.addAll(exp.subList(fromIndex, toIndex + 1));
		return associatedElement;
	}

	/**
	 * Returns an ExpressionList of the same type given by the operator
	 *
	 * @param op
	 *            operator of the expression
	 * @return ExpressionList of a given type
	 */
	private static ExpressionList<Element> getSameTypeList(Operator op) {

		switch (op) {
		case SUM:
			return new SUMList<Element>();
		case MUL:
			return new MULList<Element>();
		default:
			return null;
		}
	}
}
