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
		MULList<Element> mulList = new MULList<>();

		// simple expressions
		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));
		mulList.add(new SingleExpression("d"));
		mulList.add(new SingleExpression("e"));

		System.out.println("Original list: " + mulList.toString());

		try {
			final MULList<Element> associatedList = (MULList<Element>) Properties.associate(mulList, 2, 3);
			System.out.println("Associated list: " + associatedList);
			final MULList<Element> conmutedList = (MULList<Element>) Properties.conmute(associatedList, 2, 0);
			System.out.println("Conmuted list: " + conmutedList);
			final MULList<Element> disassociatedList = (MULList<Element>) Properties.disassociate(conmutedList, 0);
			System.out.println("Disassociated list: " + disassociatedList);

		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}
		// TODO CHECK THIS!!!!!!
		System.out.println(mulList.validProperties());
		// combined expressions
		mulList = new MULList<Element>();
		System.out.println(mulList.validProperties());
		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));

		final SUMList<Element> sumList = new SUMList<>();
		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));

		mulList.add(sumList);
		mulList.add(new SingleExpression("f"));

		System.out.println("--------------------------");
		System.out.println("Original list: " + mulList.toString());
		try {
			final ExpressionList<Element> distributedList = Properties.distribute(mulList, 1, 3);
			System.out.println("Distributed list: " + distributedList.toString());
		} catch (final Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}

	}

}
