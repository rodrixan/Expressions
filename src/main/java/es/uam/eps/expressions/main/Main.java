package es.uam.eps.expressions.main;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.MULList;
import es.uam.eps.expressions.types.SUMList;
import es.uam.eps.expressions.types.SingleExpression;

/**
 * Main class for testing and viewing how to use the classes and how they work
 *
 * @author Rodrigo de Blas
 *
 */
public class Main {

	public static void main(String[] args) {
		/** Same for all sublist types: SUM, MUL,... */
		final ExpressionList<Element> mulList = new MULList<>();

		// simple expressions
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
			System.exit(-1);
		}
		// TODO CHECK THIS!!!!!!

		// combined expressions
		final ExpressionList<Element> mulList1 = new MULList<Element>();

		mulList1.add(new SingleExpression("a"));
		mulList1.add(new SingleExpression("b"));
		mulList1.add(new SingleExpression("c"));

		final ExpressionList<Element> sumList = new SUMList<>();

		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));

		mulList1.add(sumList);
		mulList1.add(new SingleExpression("f"));

		System.out.println("--------------------------");
		System.out.println("Original list: " + mulList1.toString());
		try {
			final ExpressionList<Element> distributedList = Properties.distribute(mulList1, 1, 3);
			System.out.println("Distributed list: " + distributedList.toString());

			System.out.println("--------------------------");
			final ExpressionList<Element> mulList2 = mulList1.subExpressionList(2, 4);
			System.out.println("Original list: " + mulList2.toString());

			final ExpressionList<Element> distributedList1 = Properties.distribute(mulList2, 0, 1);
			System.out.println("Distributed list: " + distributedList1.toString());
		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}

	}

}
