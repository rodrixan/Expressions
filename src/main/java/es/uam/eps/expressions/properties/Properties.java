package es.uam.eps.expressions.properties;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.Operator;
import es.uam.eps.expressions.types.SUMList;

public class Properties {
	private static final String msg = "Property \"*\" can't be applied to expression: ";

	public static ExpressionList<Element> associate(ExpressionList<Element> exp, int fromIndex, int toIndex)
			throws IllegalPropertyException {
		checkValidOperation(exp, "associative");
		final ExpressionList<Element> ret = getSameTypeList(exp.getOperator());
		final ExpressionList<Element> associatedElements = getAssociatedElements(exp, fromIndex, toIndex);

		ret.addAll(exp.subList(0, fromIndex));

		ret.add(associatedElements);

		ret.addAll(exp.subList(toIndex + 1, exp.size()));

		return ret;
	}

	public static ExpressionList<Element> conmute(ExpressionList<Element> exp, int chosen, int end)
			throws IllegalPropertyException {
		checkValidOperation(exp, "conmutative");
		final ExpressionList<Element> ret = getSameTypeList(exp.getOperator());

		ret.addAll(exp);

		final Element chosenElement = ret.remove(chosen);

		ret.add(end, chosenElement);

		return ret;
	}

	private static void checkValidOperation(ExpressionList<Element> exp, String propName)
			throws IllegalPropertyException {
		if (!exp.isValidProperty(propName)) {
			throw new IllegalPropertyException(msg.replaceAll("\\*", propName) + exp.toString());
		}
	}

	private static ExpressionList<Element> getAssociatedElements(ExpressionList<Element> exp, int fromIndex,
			int toIndex) {
		final ExpressionList<Element> associatedElement = getSameTypeList(exp.getOperator());
		associatedElement.addAll(exp.subList(fromIndex, toIndex + 1));
		return associatedElement;
	}

	private static ExpressionList<Element> getSameTypeList(Operator op) {

		switch (op) {
		case SUM:
			return new SUMList<Element>();
		case MUL:
			return null;
		default:
			return null;
		}
	}
}
