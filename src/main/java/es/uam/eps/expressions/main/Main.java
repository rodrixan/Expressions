package es.uam.eps.expressions.main;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.Expression;
import es.uam.eps.expressions.types.SUMList;

public class Main {

	public static void main(String[] args) {
		final SUMList<Element> sumList = new SUMList<>();

		sumList.add(new Expression("a"));
		sumList.add(new Expression("b"));
		sumList.add(new Expression("c"));
		sumList.add(new Expression("d"));
		sumList.add(new Expression("e"));

		System.out.println(sumList.toString());

		SUMList<Element> associatedSUMList = null;
		SUMList<Element> conmutedSUMList = null;
		try {
			associatedSUMList = (SUMList<Element>) Properties.associate(sumList, 2, 3);
			System.out.println("Lista asociada: " + associatedSUMList);
			conmutedSUMList = (SUMList<Element>) Properties.conmute(associatedSUMList, 2, 1);
			System.out.println("Lista conmutada: " + conmutedSUMList);

		} catch (final IllegalPropertyException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(-1);
		}

	}

}
