package es.uam.eps.expressions.main;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Element;
import es.uam.eps.expressions.types.interfaces.ExpressionList;
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

	}

	private static void showSimpleExpressions() {

		/** Same for all sublist types: SUM, MUL,... */
		final ExpressionList<Element> mulList = new MULList<>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));
		mulList.add(new SingleExpression("d"));
		mulList.add(new SingleExpression("e"));

		System.out.println("Original list: " + mulList.toString());

		try {
			final ExpressionList<Element> associatedList = Properties.associate(mulList, 2, 3);
			System.out.println("Associated list: " + associatedList);
			final ExpressionList<Element> conmutedList = Properties.conmute(associatedList, 2, 0);
			System.out.println("Conmuted list: " + conmutedList);
			final ExpressionList<Element> disassociatedList = Properties.disassociate(conmutedList, 0);
			System.out.println("Disassociated list: " + disassociatedList);

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showSimpleDistributive() {

		final ExpressionList<Element> mulList = new MULList<Element>();

		mulList.add(new SingleExpression("a"));

		final ExpressionList<Element> sumList = new SUMList<>();

		sumList.add(new SingleExpression("b"));
		sumList.add(new SingleExpression("c"));
		sumList.add(new SingleExpression("d"));

		mulList.add(sumList);

		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Element> distributedList = Properties.distribute(mulList, 0, 1);
			System.out.println("Distributed list: " + distributedList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showCommplexOneElementDistributive() {

		final ExpressionList<Element> mulList = new MULList<Element>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));

		final ExpressionList<Element> sumList = new SUMList<>();

		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));

		mulList.add(sumList);
		mulList.add(new SingleExpression("f"));

		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Element> distributedList = Properties.distribute(mulList, 1, 3);
			System.out.println("Distributed list: " + distributedList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showCommplexAllElementDistributive() {

		final ExpressionList<Element> mulList = new MULList<Element>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));

		final ExpressionList<Element> sumList = new SUMList<>();

		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));

		mulList.add(sumList);
		mulList.add(new SingleExpression("f"));

		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Element> distributedList = Properties.distribute(mulList, 3);
			System.out.println("Distributed list: " + distributedList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

	private static void showSimpleCommonFactor() {

		final ExpressionList<Element> sumList = new SUMList<Element>();

		final ExpressionList<Element> item1 = new MULList<>();

		item1.add(new SingleExpression("a"));
		item1.add(new SingleExpression("b"));

		final ExpressionList<Element> item2 = new MULList<>();

		item2.add(new SingleExpression("a"));
		item2.add(new SingleExpression("c"));
		item2.add(new SingleExpression("d"));

		final ExpressionList<Element> item3 = new MULList<>();

		final ExpressionList<Element> subItem31 = new SUMList<>();

		item3.add(new SingleExpression("a"));
		subItem31.add(new SingleExpression("e"));
		subItem31.add(new SingleExpression("f"));

		item3.add(subItem31);

		sumList.add(item1);
		sumList.add(item2);
		sumList.add(item3);

		System.out.println("Original list: " + sumList.toString());
		try {
			final ExpressionList<Element> commonFactorList = Properties.commonFactor(sumList, new SingleExpression("a"),
					new int[] { 0, 1, 2 });
			System.out.println("Common factor list: " + commonFactorList.toString());

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
