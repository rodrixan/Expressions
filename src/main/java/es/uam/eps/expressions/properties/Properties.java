package es.uam.eps.expressions.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.interfaces.Element;

/**
 * Arithmetic Operation properties
 *
 * @author Rodrigo de Blas
 *
 */
public final class Properties {
	/** Property names accepted by the CAS */
	public static final String ASSOCIATIVE = "associative";
	public static final String CONMUTATIVE = "conmutative";
	public static final String DISTRIBUTIVE = "distributive";
	public static final String COMMON_FACTOR = "common factor";

	private static final List<String> acceptedProperties = new ArrayList<String>();

	static {
		acceptedProperties.add(ASSOCIATIVE);
		acceptedProperties.add(CONMUTATIVE);
		acceptedProperties.add(DISTRIBUTIVE);
		acceptedProperties.add(COMMON_FACTOR);
	}

	/** Error messages */
	private static final String PROP_ERROR = "Property \"*\" can't be applied to expression \"*\" in position \"*\"";
	private static final String TYPE_ERROR = "Type \"*\" doesn't match: Expected type \"*\"";

	private Properties() throws InstantiationException {
		throw new InstantiationException();
	}

	/**
	 * @return list with accepted properties
	 */
	public static List<String> acceptedProperties() {
		return acceptedProperties;
	}

	/**
	 * Associates an arbitrary number of elements of an expression (f.e. a+b+c =
	 * (a+b)+c))
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
		checkValidOperation(exp, ASSOCIATIVE, fromIndex);
		checkIndex(fromIndex, toIndex);
		if (allElementsAreSelected(exp.size(), fromIndex, toIndex)) {
			return exp;
		}
		final ExpressionList<Element> ret = exp.getSameTypeExpressionList();
		final ExpressionList<Element> associatedElements = getAssociatedElements(exp, fromIndex, toIndex);

		ret.addAll(exp.subList(0, fromIndex));

		ret.add(associatedElements);

		ret.addAll(exp.subList(toIndex + 1, exp.size()));

		return ret;
	}

	/**
	 * Disassociates an expression of the same type in an expression. Item must
	 * be same type that parent expression. If not, precedence can be lost (f.e.
	 * a+(b+c)+d = a+b+c+d)
	 *
	 * @param expression
	 *            expression to which the property will be applied
	 * @param chosenIndex
	 *            index of the subexpression to disassociate
	 * @return expression of the same type without the associated expression
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	public static ExpressionList<Element> disassociate(ExpressionList<Element> expression, int chosenIndex)
			throws IllegalPropertyException {

		checkValidOperation(expression, ASSOCIATIVE, chosenIndex);

		final ExpressionList<Element> chosenList = getExpressionList(expression.get(chosenIndex));
		checkValidOperation(chosenList, ASSOCIATIVE, chosenIndex);
		checkElementType(chosenList, expression.getClass());

		final ExpressionList<Element> ret = expression.getSameTypeExpressionList();

		ret.addAll(expression.subList(0, chosenIndex));

		for (final Element e : chosenList) {
			ret.add(e);
		}

		ret.addAll(expression.subList(chosenIndex + 1, expression.size()));

		return ret;

	}

	/**
	 * Conmutes an element from one position to another one (f.e. a+b+c = b+a+c)
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
		checkValidOperation(exp, CONMUTATIVE, chosen);
		final ExpressionList<Element> ret = exp.getSameTypeExpressionList();

		ret.addAll(exp);

		final Element chosenElement = ret.remove(chosen);

		ret.add(end, chosenElement);

		return ret;
	}

	/**
	 * Distributes an element with one subexpression of main expression
	 * (f.e. a*(b+c)*d= ((a*b)+(a*c))*d
	 *
	 * @param exp
	 *            main expression. Must contains the element and the
	 *            subexpression
	 * @param elementIndex
	 *            index of the element to distribute
	 * @param innerExpIndex
	 *            index of the subexpression to be distributed
	 * @return distributed expression. Can be the same type of exp if there are
	 *         more than 2 elements, or a expression of the subexpression type
	 *         if there are only 2 elements
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	public static ExpressionList<Element> distribute(ExpressionList<Element> exp, int elementIndex, int innerExpIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, DISTRIBUTIVE, elementIndex);

		final ExpressionList<Element> innerOpList = getExpressionList(exp.get(innerExpIndex));

		checkValidOperation(innerOpList, COMMON_FACTOR, innerExpIndex);

		final ExpressionList<Element> mainOpList = exp.getSameTypeExpressionList();

		final Element elementToDistribute = exp.get(elementIndex);

		final ExpressionList<Element> distributedList = distributeSingleElement(exp, innerOpList, elementIndex);

		// If there is only 2 elements in the expression, distribute one against
		// another
		if (exp.size() == 2) {
			return distributedList;
		}

		mainOpList.addAll(exp);
		mainOpList.add(innerExpIndex, distributedList);
		mainOpList.remove(innerOpList);
		mainOpList.remove(elementToDistribute);

		return mainOpList;

	}

	/**
	 * Distributes all elements with one subexpression
	 * (f.e. a*(b+c)*d= ((a*b)+(a*c))*d
	 *
	 * @param exp
	 *            main expression. Must contains the element and the
	 *            subexpression
	 * @param innerExpIndex
	 *            index of the subexpression to be distributed
	 * @return distributed expression. Can be the same type of exp if there are
	 *         more than 2 elements, or a expression of the subexpression type
	 *         if there are only 2 elements
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	public static ExpressionList<Element> distribute(ExpressionList<Element> exp, int innerExpIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, DISTRIBUTIVE, innerExpIndex);

		final ExpressionList<Element> innerExp = getExpressionList(exp.get(innerExpIndex));
		checkValidOperation(innerExp, COMMON_FACTOR, innerExpIndex);

		final ExpressionList<Element> orderedExp = createAssociatedExpressionWithInnerExpAtLast(exp, innerExpIndex);

		// ordered exp must have 2 elements
		final ExpressionList<Element> distributedExp = distribute(orderedExp, 0, 1);

		final ExpressionList<Element> disassociatedExp = distributedExp.getSameTypeExpressionList();
		for (int i = 0; i < distributedExp.size(); i++) {
			disassociatedExp.add(disassociate(getExpressionList(distributedExp.get(i)), 0));
		}
		return disassociatedExp;
	}

	/**
	 * Extracts a common element in subexpressions given by their positions
	 * (f.e. a + b*c + b*d*e + b*(f+g) = a + b*(c+(d*e)+(f+g)
	 *
	 * @param exp
	 *            main expression
	 * @param e
	 *            element to extract as a common factor
	 * @param positions
	 *            positions of the subexpressions that contains the element
	 * @return expresion list with a subexpression as a common factor and the
	 *         rest of the main expression
	 * @throws IllegalPropertyException
	 */
	public static ExpressionList<Element> commonFactor(ExpressionList<Element> exp, Element e, int[] positions)
			throws IllegalPropertyException {
		checkValidOperation(exp, COMMON_FACTOR, 0);

		if (positions.length < 2) {
			throw new IllegalPropertyException(
					createPropErrorMsg(COMMON_FACTOR, exp.toString(), positions[0]) + ". Need at least 2 items");
		}

		final ExpressionList<Element> commonFactorExp = exp.getSameTypeExpressionList();
		ExpressionList<Element> wrapperExp = null;
		for (final int i : positions) {

			final ExpressionList<Element> subExpList = getExpressionList(exp.get(i));
			checkValidOperation(subExpList, DISTRIBUTIVE, i);

			if (!(subExpList.contains(e))) {
				throw new IllegalPropertyException(createPropErrorMsg(COMMON_FACTOR, subExpList.toString(), i)
						+ ".Element \"" + e.toString() + "\" not found");
			}

			final ExpressionList<Element> orderedExpList = conmute(subExpList, subExpList.indexOf(e), 0);

			if (wrapperExp == null) {
				wrapperExp = subExpList.getSameTypeExpressionList();
			}

			if (orderedExpList.size() > 2) {
				commonFactorExp.add(orderedExpList.subExpressionList(1, orderedExpList.size()));
			} else {
				commonFactorExp.addAll(orderedExpList.subList(1, orderedExpList.size()));
			}

		}

		wrapperExp.add(commonFactorExp);
		wrapperExp.add(0, e);

		if (positions.length == exp.size()) {
			return wrapperExp;
		}

		return createFinalExpression(exp, positions, wrapperExp);
	}

	/**
	 *
	 * @param e
	 *            element to get as ExpressionList
	 * @return ExpressionList of the element
	 */
	@SuppressWarnings("unchecked") // checked before return
	private static ExpressionList<Element> getExpressionList(Element e) {
		checkExpressionList(e);
		return (ExpressionList<Element>) e;
	}

	/**
	 * Creates an expression, result of applying the common factor property
	 *
	 * @param originalExp
	 *            original expression
	 * @param positions
	 *            positions where the common factor is applied
	 * @param commonFactorExp
	 *            expression with the common factor element and the
	 *            subexpression where it was extracted from
	 * @return
	 */
	private static ExpressionList<Element> createFinalExpression(ExpressionList<Element> originalExp, int[] positions,
			ExpressionList<Element> commonFactorExp) {
		final int minPos = Collections.min(Arrays.asList(ArrayUtils.toObject(positions)));

		final ExpressionList<Element> finalExp = removeAllByIndex(originalExp, positions);

		if (minPos >= finalExp.size()) {
			finalExp.add(commonFactorExp);
		} else {
			finalExp.add(minPos, commonFactorExp);
		}
		return finalExp;
	}

	/**
	 * Removes all the elements at specified positions without modifying the
	 * original expression
	 *
	 * @param exp
	 *            original expression
	 * @param positions
	 *            indexes of the elements to remove
	 * @return new expression list without the desired elements
	 */
	private static ExpressionList<Element> removeAllByIndex(ExpressionList<Element> exp, int[] positions) {
		final ExpressionList<Element> finalExp = exp.getSameTypeExpressionList();
		finalExp.addAll(exp);
		for (final int i : positions) {
			finalExp.remove(exp.get(i));
		}
		return finalExp;
	}

	/**
	 * Distributes a single element with a simple expression
	 * (f.e. a*(b+c) = (a*b)+(a*c)
	 *
	 * @param exp
	 *            main expression
	 * @param innerExp
	 *            subexpression to be destributed
	 * @param elementIndex
	 *            index of the element to distribute
	 * @return distributed list
	 */
	private static ExpressionList<Element> distributeSingleElement(ExpressionList<Element> exp,
			ExpressionList<Element> innerExp, int elementIndex) {
		final ExpressionList<Element> mainOpList = innerExp.getSameTypeExpressionList();

		for (final Element e : innerExp) {
			final ExpressionList<Element> item = exp.getSameTypeExpressionList();
			item.add(exp.get(elementIndex));
			item.add(e);
			mainOpList.add(item);
		}

		return mainOpList;
	}

	/**
	 *
	 * @param exp
	 *            expression to order
	 * @param innerExpIndex
	 *            index of the subexpression to move
	 * @return a two elements expression with all the elements associated in
	 *         pos. 0 and inner exp at last pos.
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	private static ExpressionList<Element> createAssociatedExpressionWithInnerExpAtLast(ExpressionList<Element> exp,
			int innerExpIndex) throws IllegalPropertyException {

		final ExpressionList<Element> orderedExp = conmute(exp, innerExpIndex, exp.size() - 1);
		final int newInnerExpIndex = orderedExp.indexOf(exp.get(innerExpIndex));

		return associate(orderedExp, 0, newInnerExpIndex - 1);
	}

	/**
	 * Checks if the element given is a MULList instance
	 *
	 * @param element
	 *            expression to check
	 */
	private static void checkExpressionList(Element element) {
		try {
			checkElementType(element, ExpressionList.class);
		} catch (final IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Checks if the given element is a type instance
	 *
	 * @param element
	 *            expression to check
	 * @param type
	 *            class that element should be
	 */
	private static void checkElementType(Element element, Class<?> type) {
		if (!(type.isInstance(element))) {
			throw new IllegalArgumentException(createTypeErrorMsg(element.getClass().getName(), type.getName()));
		}
	}

	/**
	 * Checks if an expression admits a given property
	 *
	 * @param element
	 *            expression to check
	 * @param propName
	 *            property to decide if can be applied
	 * @throws IllegalPropertyException
	 *             if the property can not be applied
	 */
	private static void checkValidOperation(Element element, String propName, int position)
			throws IllegalPropertyException {
		if (!element.isValidProperty(propName)) {
			throw new IllegalPropertyException(createPropErrorMsg(propName, element.toString(), position));
		}
	}

	/**
	 * Checks if all elements of an ExpressionList are selected
	 *
	 * @param size
	 *            size of the list
	 * @param fromIndex
	 *            starting index
	 * @param toIndex
	 *            ending index
	 * @return true if all elements are selected; false if not
	 */
	private static boolean allElementsAreSelected(int size, int fromIndex, int toIndex) {
		return fromIndex == 0 && toIndex == size - 1;
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
		final ExpressionList<Element> associatedElement = exp.getSameTypeExpressionList();
		associatedElement.addAll(exp.subList(fromIndex, toIndex + 1));
		return associatedElement;
	}

	/**
	 * Creates a property error message
	 *
	 * @param property
	 *            property where the error came from
	 * @param exp
	 *            string representation of the expression
	 * @param position
	 *            index where tried to apply the property
	 * @return message with the given attributes
	 */
	private static String createPropErrorMsg(String property, String exp, int position) {
		return PROP_ERROR.replaceFirst("\\*", property).replaceFirst("\\*", exp).replaceFirst("\\*", position + "");
	}

	/**
	 * Creates a type error message
	 *
	 * @param expected
	 *            expected type
	 * @param received
	 *            received type
	 * @return message with the given attributes
	 */
	private static String createTypeErrorMsg(String received, String expected) {
		return TYPE_ERROR.replaceFirst("\\*", received).replaceFirst("\\*", expected);
	}

	/**
	 * Checks if two indexes are well positionated
	 *
	 * @param fromIndex
	 *            starting index
	 * @param toIndex
	 *            ending index
	 */
	private static void checkIndex(int fromIndex, int toIndex) {
		if (toIndex - fromIndex <= 0) {
			throw new IllegalArgumentException(
					"fromIndex (" + fromIndex + ") should be smaller than toIndex (" + toIndex + ")");
		}

	}
}
