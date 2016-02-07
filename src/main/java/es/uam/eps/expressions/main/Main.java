package es.uam.eps.expressions.main;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.Expression;
import es.uam.eps.expressions.types.MULList;

/**
 * Main class for testing and viewing how to use the classes and how they work
 *
 * @author Rodrigo de Blas
 *
 */
public class Main {

	public static void main(String[] args) {
		/** Same for all sublist types: SUM, MUL,... */
		final MULList<Element> mulList = new MULList<>();

		mulList.add(new Expression("a"));
		mulList.add(new Expression("b"));
		mulList.add(new Expression("c"));
		mulList.add(new Expression("d"));
		mulList.add(new Expression("e"));

		System.out.println(mulList.toString());

		MULList<Element> associatedSUMList = null;
		MULList<Element> conmutedSUMList = null;
		try {
			associatedSUMList = (MULList<Element>) Properties.associate(mulList, 2, 3);
			System.out.println("Associated list: " + associatedSUMList);
			conmutedSUMList = (MULList<Element>) Properties.conmute(associatedSUMList, 2, 0);
			System.out.println("Conmuted list: " + conmutedSUMList);

		} catch (final IllegalPropertyException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}

	}

}
