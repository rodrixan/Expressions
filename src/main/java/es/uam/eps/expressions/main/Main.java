package es.uam.eps.expressions.main;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.operations.MULList;
import es.uam.eps.expressions.types.operations.SUMList;

/**
 * Main class for visual testing and viewing how to use the classes and how they
 * work
 *
 * @author Rodrigo de Blas
 *
 */
public class Main {

	public static void main(String[] args) {

		System.out.println("----CAS: EXAMPLES SHOWCASE----\n");
		System.out.println("----SIMPLE EXPRESSIONS----\n");
		System.out.println("---ASSOCIATIVE, CONMUTATIVE, DISOCIATIVE---");
		showSimpleExpressions();

		System.out.println();
		System.out.println("---NEUTRAL ELEMENT---");
		showRemoveNeutralElement();

		System.out.println();
		System.out.println("----COMPOUND EXPRESSIONS---\n");
		System.out.println("----SIMPLE DISTRIBUTIVE---");
		showSimpleDistributive();

		System.out.println();
		System.out.println("---COMPLEX DISTRIBUTIVE: ONE ELEMENT---");
		showCommplexOneElementDistributive();

		System.out.println();
		System.out.println("---COMPLEX DISTRIBUTIVE: ALL ELEMENTS---");
		showCommplexAllElementDistributive();

		System.out.println();
		System.out.println("---SIMPLE COMMON FACTOR---");
		showSimpleCommonFactor();

		System.out.println();
		System.out.println("---COMPLEX COMMON FACTOR---");
		showComplexCommonFactor();

	}

	private static void showSimpleExpressions() {

		/** Same for all sublist types: SUM, MUL,... */
		final ExpressionList<Expression> mulList = new MULList<>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));
		mulList.add(new SingleExpression("d"));
		mulList.add(new SingleExpression("e"));

		System.out.println("Original list: " + mulList.toString());

		try {
			final ExpressionList<Expression> associatedList = Properties.associate(mulList, 2, 3);
			System.out.println("Associated list: " + associatedList);
			final ExpressionList<Expression> conmutedList = Properties.conmute(associatedList, 2, 0);
			System.out.println("Conmuted list: " + conmutedList);
			final ExpressionList<Expression> disassociatedList = Properties.disassociate(conmutedList, 0);
			System.out.println("Disassociated list: " + disassociatedList);

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showSimpleDistributive() {

		final ExpressionList<Expression> mulList = new MULList<Expression>();

		mulList.add(new SingleExpression("a"));

		final ExpressionList<Expression> sumList = new SUMList<>();

		sumList.add(new SingleExpression("b"));
		sumList.add(new SingleExpression("c"));
		sumList.add(new SingleExpression("d"));

		mulList.add(sumList);

		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Expression> distributedList = Properties.distribute(mulList, 0, 1);
			System.out.println("Distributed list: " + distributedList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showCommplexOneElementDistributive() {

		final ExpressionList<Expression> mulList = new MULList<Expression>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));

		final ExpressionList<Expression> sumList = new SUMList<>();

		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));

		mulList.add(sumList);
		mulList.add(new SingleExpression("f"));

		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Expression> distributedList = Properties.distribute(mulList, 1, 3);
			System.out.println("Distributed list: " + distributedList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showCommplexAllElementDistributive() {

		final ExpressionList<Expression> mulList = new MULList<Expression>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));

		final ExpressionList<Expression> sumList = new SUMList<>();

		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));

		mulList.add(sumList);
		mulList.add(new SingleExpression("f"));

		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Expression> distributedList = Properties.distribute(mulList, 3);
			System.out.println("Distributed list: " + distributedList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showSimpleCommonFactor() {

		final ExpressionList<Expression> sumList = new SUMList<Expression>();

		final ExpressionList<Expression> item1 = new MULList<>();

		item1.add(new SingleExpression("a"));
		item1.add(new SingleExpression("b"));

		final ExpressionList<Expression> item2 = new MULList<>();

		item2.add(new SingleExpression("a"));
		item2.add(new SingleExpression("c"));
		item2.add(new SingleExpression("d"));

		final ExpressionList<Expression> item3 = new MULList<>();

		final ExpressionList<Expression> subItem31 = new SUMList<>();

		item3.add(new SingleExpression("a"));
		subItem31.add(new SingleExpression("e"));
		subItem31.add(new SingleExpression("f"));

		item3.add(subItem31);

		sumList.add(item1);
		sumList.add(item2);
		sumList.add(item3);

		System.out.println("Original list: " + sumList.toString());
		try {
			final ExpressionList<Expression> commonFactorList = Properties.commonFactor(sumList, new SingleExpression("a"),
					new int[] { 0, 1, 2 });
			System.out.println("Common factor list: " + commonFactorList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showComplexCommonFactor() {

		final ExpressionList<Expression> sumList = new SUMList<Expression>();

		final ExpressionList<Expression> item1 = new MULList<>();

		item1.add(new SingleExpression("a"));
		item1.add(new SingleExpression("b"));

		final ExpressionList<Expression> item2 = new MULList<>();

		item2.add(new SingleExpression("a"));
		item2.add(new SingleExpression("c"));
		item2.add(new SingleExpression("d"));

		final ExpressionList<Expression> item3 = new MULList<>();

		final ExpressionList<Expression> subItem31 = new SUMList<>();

		item3.add(new SingleExpression("a"));
		subItem31.add(new SingleExpression("e"));
		subItem31.add(new SingleExpression("f"));

		item3.add(subItem31);

		sumList.add(new SingleExpression("i"));
		sumList.add(item1);
		sumList.add(new SingleExpression("h"));
		sumList.add(item2);
		sumList.add(item3);
		sumList.add(new SingleExpression("g"));

		System.out.println("Original list: " + sumList.toString());
		try {
			final ExpressionList<Expression> commonFactorList = Properties.commonFactor(sumList, new SingleExpression("a"),
					new int[] { 1, 3, 4 });
			System.out.println("Common factor list: " + commonFactorList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	public static void showRemoveNeutralElement() {
		final ExpressionList<Expression> mulList = new MULList<Expression>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("1"));
		mulList.add(new SingleExpression("c"));
		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Expression> neutralElementRemovedList = Properties.removeNeutralElement(mulList, 1);
			System.out.println("Common factor list: " + neutralElementRemovedList.toString());
		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
