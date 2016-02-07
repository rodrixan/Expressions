package es.uam.eps.tfg.types;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.Expression;
import es.uam.eps.expressions.types.SUMList;

public class SUMListTest {
	private SUMList<Element> sumList = new SUMList<>();

	@Before
	public void setup() {
		sumList.add(new Expression("a"));
		sumList.add(new Expression("b"));
		sumList.add(new Expression("c"));
		sumList.add(new Expression("d"));
		sumList.add(new Expression("e"));
	}

	@Test
	public void toStringTest() {
		assertEquals("+[a, b, c, d, e]", sumList.toString());
	}

}
