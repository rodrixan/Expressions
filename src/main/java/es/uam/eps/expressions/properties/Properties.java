package es.uam.eps.expressions.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;

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
	public static final String NEUTRAL_ELEMENT = "neutral element";
	public static final String INVERSE = "inverse";

	private static final List<String> acceptedProperties = new ArrayList<String>();

	static {
		acceptedProperties.add(ASSOCIATIVE);
		acceptedProperties.add(CONMUTATIVE);
		acceptedProperties.add(DISTRIBUTIVE);
		acceptedProperties.add(COMMON_FACTOR);
	}

	/** Error messages */
	private static final String PROP_ERROR = "Property \"?\" can't be applied to expression \"?\" in position \"?\"";
	private static final String TYPE_ERROR = "Type \"?\" doesn't match: Expected type \"?\"";

	private Properties() throws InstantiationException {
		throw new InstantiationException();
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
	public static ExpressionList<Expression> associate(ExpressionList<Expression> exp, int fromIndex, int toIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, ASSOCIATIVE, fromIndex);
		checkIndex(fromIndex, toIndex);
		if (allElementsAreSelected(exp.size(), fromIndex, toIndex)) {
			return exp;
		}
		final ExpressionList<Expression> ret = exp.getSameTypeExpressionList();
		final ExpressionList<Expression> associatedElements = getAssociatedElements(exp, fromIndex, toIndex);

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
	public static ExpressionList<Expression> disassociate(ExpressionList<Expression> expression, int chosenIndex)
			throws IllegalPropertyException {

		checkValidOperation(expression, ASSOCIATIVE, chosenIndex);

		final ExpressionList<Expression> chosenList = getExpressionList(expression.get(chosenIndex));
		checkValidOperation(chosenList, ASSOCIATIVE, chosenIndex);
		checkElementType(chosenList, expression.getClass());

		final ExpressionList<Expression> ret = expression.getSameTypeExpressionList();

		ret.addAll(expression.subList(0, chosenIndex));

		for (final Expression e : chosenList) {
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
	public static ExpressionList<Expression> conmute(ExpressionList<Expression> exp, int chosen, int end)
			throws IllegalPropertyException {
		checkValidOperation(exp, CONMUTATIVE, chosen);
		final ExpressionList<Expression> ret = exp.getSameTypeExpressionList();

		ret.addAll(exp);

		final Expression chosenElement = ret.remove(chosen);

		ret.add(end, chosenElement);

		return ret;
	}

	/**
	 * Distributes an element with one subexpression of main expression (f.e.
	 * a*(b+c)*d= ((a*b)+(a*c))*d
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
	public static ExpressionList<Expression> distribute(ExpressionList<Expression> exp, int elementIndex,
			int innerExpIndex) throws IllegalPropertyException {
		checkValidOperation(exp, DISTRIBUTIVE, elementIndex);

		final ExpressionList<Expression> innerOpList = getExpressionList(exp.get(innerExpIndex));

		checkValidOperation(innerOpList, COMMON_FACTOR, innerExpIndex);

		final ExpressionList<Expression> mainOpList = exp.getSameTypeExpressionList();

		final Expression elementToDistribute = exp.get(elementIndex);

		final ExpressionList<Expression> distributedList = distributeSingleElement(exp, innerOpList, elementIndex);

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
	 * Distributes all elements with one subexpression (f.e. a*(b+c)*d=
	 * ((a*b)+(a*c))*d
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
	public static ExpressionList<Expression> distribute(ExpressionList<Expression> exp, int innerExpIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, DISTRIBUTIVE, innerExpIndex);

		final ExpressionList<Expression> innerExp = getExpressionList(exp.get(innerExpIndex));
		checkValidOperation(innerExp, COMMON_FACTOR, innerExpIndex);

		final ExpressionList<Expression> orderedExp = createAssociatedExpressionWithInnerExpAtLast(exp, innerExpIndex);

		// ordered exp must have 2 elements
		final ExpressionList<Expression> distributedExp = distribute(orderedExp, 0, 1);

		final ExpressionList<Expression> disassociatedExp = distributedExp.getSameTypeExpressionList();
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
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	public static ExpressionList<Expression> commonFactor(ExpressionList<Expression> exp, Expression e, int[] positions)
			throws IllegalPropertyException {

		checkValidOperation(exp, COMMON_FACTOR, 0);
		checkPositionsSize(exp, positions.length);

		final ExpressionList<Expression> commonFactorExp = exp.getSameTypeExpressionList();
		ExpressionList<Expression> wrapperExp = null;
		for (final int i : positions) {

			final ExpressionList<Expression> subExpList = getSubexpressionWithGivenElementForCommonFactor(exp, e, i);
			final ExpressionList<Expression> orderedExpList = conmute(subExpList, subExpList.indexOf(e), 0);

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
	 * Removes the neutral element at given position
	 * (f.e. a+b+0 = a+b ; a*b*1 = a*b)
	 *
	 * @param exp
	 *            original expression
	 * @param position
	 *            index of the neutral element to be removed
	 * @return expression list without the neutral element at position desired.
	 *         Result expression can be one element sized. In this case, just
	 *         convert the list using "convertSingleListToSingleExpression".
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 * @see convertSingleListToSingleExpression
	 */
	public static ExpressionList<Expression> removeNeutralElement(ExpressionList<Expression> exp, int position)
			throws IllegalPropertyException {
		checkValidOperation(exp, NEUTRAL_ELEMENT, position);

		final ExpressionList<Expression> finalExp = exp.getSameTypeExpressionList();

		finalExp.addAll(exp);

		final Expression neutralElement = finalExp.remove(position);
		if (!(exp.getNeutralElement().equals(neutralElement))) {
			final String msg = "Neutral element for operation " + exp.getOperator().toString() + " is "
					+ exp.getNeutralElement().symbolicExpression() + ", but element selected is "
					+ neutralElement.symbolicExpression();
			throw new IllegalArgumentException(msg);
		}

		return finalExp;
	}

	/**
	 * Converts a single element expression list into a SingleExpression.
	 *
	 * @param exp
	 *            expression list to convert, MUST HAVE EXACTLY ONE ELEMENT. If
	 *            the single element is not a single expression, function will
	 *            fail
	 * @return single expression associated with the expression.
	 */
	public static SingleExpression convertSingleExpressionListToSingleExpression(ExpressionList<Expression> exp) {
		if (exp.size() != 1) {
			throw new IllegalArgumentException("Expression \"" + exp.symbolicExpression()
					+ "\" can't be converted to single expression. Must have excatly one element.");
		}

		checkElementType(exp.get(0), SingleExpression.class);

		return getSingleExpression(exp.get(0));// checked before

	}

	/**
	 * Converts a single element expression list into the inner expression list.
	 *
	 * @param exp
	 *            expression list to convert, MUST HAVE EXACTLY ONE ELEMENT. If
	 *            the single element is not a single expression, function will
	 *            fail
	 * @return single expression associated with the expression.
	 */
	public static ExpressionList<Expression> convertSingleExpressionListToExpression(ExpressionList<Expression> exp) {
		if (exp.size() != 1) {
			throw new IllegalArgumentException("Expression \"" + exp.symbolicExpression()
					+ "\" can't be converted to single expression. Must have excatly one element.");
		}

		checkElementType(exp.get(0), ExpressionList.class);

		return getExpressionList(exp.get(0));// checked before

	}

	/**
	 * @return list with accepted properties
	 */
	public static List<String> acceptedProperties() {
		return acceptedProperties;
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
	private static void checkValidOperation(Expression element, String propName, int position)
			throws IllegalPropertyException {
		if (!element.isValidProperty(propName)) {
			throw new IllegalPropertyException(createPropErrorMsg(propName, element.toString(), position));
		}
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
	private static ExpressionList<Expression> getAssociatedElements(ExpressionList<Expression> exp, int fromIndex,
			int toIndex) {
		final ExpressionList<Expression> associatedElement = exp.getSameTypeExpressionList();
		associatedElement.addAll(exp.subList(fromIndex, toIndex + 1));
		return associatedElement;
	}

	/**
	 *
	 * @param e
	 *            element to get as ExpressionList
	 * @return ExpressionList of the element
	 */
	@SuppressWarnings("unchecked") // checked before return
	private static ExpressionList<Expression> getExpressionList(Expression e) {
		checkExpressionList(e);
		return (ExpressionList<Expression>) e;
	}

	/**
	 *
	 * @param e
	 *            element to get as SingleExpression
	 * @return SingleExpression of the element
	 */
	private static SingleExpression getSingleExpression(Expression e) {
		checkElementType(e, SingleExpression.class);
		return (SingleExpression) e;
	}

	/**
	 * Checks if the given element is a type instance
	 *
	 * @param element
	 *            expression to check
	 * @param type
	 *            class that element should be
	 */
	private static void checkElementType(Expression element, Class<?> type) {
		if (!(type.isInstance(element))) {
			throw new IllegalArgumentException(createTypeErrorMsg(element.getClass().getName(), type.getName()));
		}
	}

	/**
	 * Checks if the element given is a MULList instance
	 *
	 * @param element
	 *            expression to check
	 */
	private static void checkExpressionList(Expression element) {
		try {
			checkElementType(element, ExpressionList.class);
		} catch (final IllegalArgumentException e) {
			throw e;
		}
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
	private static ExpressionList<Expression> createFinalExpression(ExpressionList<Expression> originalExp,
			int[] positions, ExpressionList<Expression> commonFactorExp) {
		final int minPos = Collections.min(Arrays.asList(ArrayUtils.toObject(positions)));

		final ExpressionList<Expression> finalExp = removeAllByIndex(originalExp, positions);

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
	private static ExpressionList<Expression> removeAllByIndex(ExpressionList<Expression> exp, int[] positions) {
		final ExpressionList<Expression> finalExp = exp.getSameTypeExpressionList();
		finalExp.addAll(exp);
		for (final int i : positions) {
			finalExp.remove(exp.get(i));
		}
		return finalExp;
	}

	/**
	 * Distributes a single element with a simple expression (f.e. a*(b+c) =
	 * (a*b)+(a*c)
	 *
	 * @param exp
	 *            main expression
	 * @param innerExp
	 *            subexpression to be destributed
	 * @param elementIndex
	 *            index of the element to distribute
	 * @return distributed list
	 */
	private static ExpressionList<Expression> distributeSingleElement(ExpressionList<Expression> exp,
			ExpressionList<Expression> innerExp, int elementIndex) {
		final ExpressionList<Expression> mainOpList = innerExp.getSameTypeExpressionList();

		for (final Expression e : innerExp) {
			final ExpressionList<Expression> item = exp.getSameTypeExpressionList();
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
	private static ExpressionList<Expression> createAssociatedExpressionWithInnerExpAtLast(
			ExpressionList<Expression> exp, int innerExpIndex) throws IllegalPropertyException {

		final ExpressionList<Expression> orderedExp = conmute(exp, innerExpIndex, exp.size() - 1);
		final int newInnerExpIndex = orderedExp.indexOf(exp.get(innerExpIndex));

		return associate(orderedExp, 0, newInnerExpIndex - 1);
	}

	/**
	 * Checks if there is at least 2 elements
	 *
	 * @param exp
	 *            expression
	 * @param size
	 *            size to check
	 * @throws IllegalPropertyException
	 *             if there is less than 2 elements
	 */
	private static void checkPositionsSize(ExpressionList<Expression> exp, int size) throws IllegalPropertyException {
		if (size < 2) {
			throw new IllegalPropertyException(
					createPropErrorMsg(COMMON_FACTOR, exp.toString(), 0) + ". Need at least 2 items");
		}
	}

	/**
	 * Retrieves a subexpression at given position, checking if that expression
	 * has a given element
	 *
	 * @param exp
	 *            main expression
	 * @param e
	 *            element to check if it's on the subexpression
	 * @param i
	 *            index of the subexpression to get
	 * @return subexpression with element given
	 * @throws IllegalPropertyException
	 *             if no subexpression was found
	 */
	private static ExpressionList<Expression> getSubexpressionWithGivenElementForCommonFactor(
			ExpressionList<Expression> exp, Expression e, final int i) throws IllegalPropertyException {
		final ExpressionList<Expression> subExpList = getExpressionList(exp.get(i));
		checkValidOperation(subExpList, DISTRIBUTIVE, i);

		if (!(subExpList.contains(e))) {
			throw new IllegalPropertyException(createPropErrorMsg(COMMON_FACTOR, subExpList.toString(), i)
					+ ".Element \"" + e.toString() + "\" not found");
		}
		return subExpList;
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
		return PROP_ERROR.replaceFirst("\\?", property).replaceFirst("\\?", exp).replaceFirst("\\?", position + "");
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
		return TYPE_ERROR.replaceFirst("\\?", received).replaceFirst("\\?", expected);
	}
}
