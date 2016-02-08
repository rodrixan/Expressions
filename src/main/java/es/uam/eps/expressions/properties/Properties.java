package es.uam.eps.expressions.properties;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.MULList;
import es.uam.eps.expressions.types.Operator;
import es.uam.eps.expressions.types.SUMList;

/**
 * Arithmetic Operation properties
 *
 * @author Rodrigo de Blas
 *
 */
public class Properties {
	/** Property names accepted by the CAS */
	public static final String ASSOCIATIVE = "associative";
	public static final String CONMUTATIVE = "conmutative";
	public static final String DISTRIBUTIVE = "distributive";

	/** Error messages */
	private static final String PROP_ERROR = "Property \"*\" can't be applied to expression: \"*\"";
	private static final String ARG_ERROR = "Property \"*\" can't be applied: Expected argument with type \"*\"";
	private static final String TYPE_ERROR = "Type \"*\" doesn't match: Expected argument with type \"*\"";

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
		if (allElementsAreSelected(exp.size(), fromIndex, toIndex)) {
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
	 * Disassociates an expression of the same type in an expression. Item must
	 * be same type that parent expression. If not, precedence can be lost
	 * (i.e. a+(b+c)+d = a+b+c+d)
	 *
	 * @param exp
	 *            expression to which the property will be applied
	 * @param chosenIndex
	 *            index of the subexpression to disassociate
	 * @return expression of the same type without the associated expression
	 * @throws IllegalPropertyException
	 *             if the expression doesn't support the property (can't be
	 *             applied)
	 */
	public static ExpressionList<Element> disassociate(ExpressionList<Element> exp, int chosenIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, ASSOCIATIVE);
		checkValidOperation(exp.get(chosenIndex), ASSOCIATIVE);
		checkElementType(exp.get(chosenIndex), exp.getClass());

		// checked before: this item must be same class that exp
		final ExpressionList<Element> chosenList = (ExpressionList<Element>) exp.get(chosenIndex);

		final ExpressionList<Element> ret = getSameTypeList(exp.getOperator());

		ret.addAll(exp.subList(0, chosenIndex));

		for (final Element e : chosenList) {
			ret.add(e);
		}

		ret.addAll(exp.subList(chosenIndex + 1, exp.size()));

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

	// TODO revisar!
	public static ExpressionList<Element> distribute(ExpressionList<Element> exp, int chosenIndex, int distributeIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, DISTRIBUTIVE);
		checkMULList(exp);
		checkSUMList(exp.get(chosenIndex));

		final SUMList<?> sumList = (SUMList<?>) exp.get(chosenIndex);

		final Element elementToDistribute = exp.get(distributeIndex);

		final SUMList<Element> distributedList = new SUMList<>();
		for (final Element e : sumList) {
			final MULList<Element> distributedItem = new MULList<>();
			distributedItem.add(elementToDistribute);
			distributedItem.add(e);
			distributedList.add(distributedItem);
		}

		if (exp.size() > 2) {
			final ExpressionList<Element> returnList = new MULList<>();
			for (int i = 0; i < exp.size(); i++) {
				if (i != distributeIndex && i < chosenIndex) {
					returnList.add(exp.get(i));
				} else if (i >= chosenIndex) {

				}
			}
			return returnList;
		} else {
			return distributedList;

		}

	}

	/**
	 * Checks if the element given is a MULList instance
	 *
	 * @param element
	 *            expression to check
	 */
	private static void checkMULList(Element element) {
		try {
			checkElementType(element, MULList.class);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException(createArgErrorMsg(DISTRIBUTIVE, MULList.class.getName()));
		}

	}

	/**
	 * Checks if the element given is a SUMList instance
	 *
	 * @param element
	 *            expression to check
	 */
	private static void checkSUMList(Element element) {
		try {
			checkElementType(element, SUMList.class);
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException(createArgErrorMsg(DISTRIBUTIVE, SUMList.class.getName()));
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
	private static void checkValidOperation(Element element, String propName) throws IllegalPropertyException {
		if (!element.isValidProperty(propName)) {
			throw new IllegalPropertyException(createPropErrorMsg(propName, element.toString()));
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

	/**
	 * Creates an argument error message
	 *
	 * @param property
	 *            property where the error came from
	 * @param clazz
	 *            class name of the expected param
	 * @return message with the given attributes
	 */
	private static String createArgErrorMsg(String property, String clazz) {
		return ARG_ERROR.replaceFirst("\\*", property).replaceFirst("\\*", clazz);
	}

	/**
	 * Creates a property error message
	 *
	 * @param property
	 *            property where the error came from
	 * @param exp
	 *            string representation of the expression
	 * @return message with the given attributes
	 */
	private static String createPropErrorMsg(String property, String exp) {
		return PROP_ERROR.replaceFirst("\\*", property).replaceFirst("\\*", exp);
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
	private static String createTypeErrorMsg(String expected, String received) {
		return TYPE_ERROR.replaceFirst("\\*", received).replaceFirst("\\*", expected);
	}
}
