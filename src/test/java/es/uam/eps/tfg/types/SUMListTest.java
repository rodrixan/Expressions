package es.uam.eps.tfg.types;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uam.eps.expressions.types.Element;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.SUMList;

public class SUMListTest {
	private SUMList<Element> sumList = new SUMList<>();

	@Before
	public void setup() {
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));
		sumList.add(new SingleExpression("c"));
		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));
	}

	@Test
	public void toStringTest() {
		assertEquals("+[a, b, c, d, e]", sumList.toString());
	}

	@Test
	public void simpleSymbolicExpressionTest() {
		assertEquals("a + b + c + d + e", sumList.symbolicExpression());
	}

	@Test
	public void complexSymbolicExpressionTest() {
		final SUMList<Element> innerList = new SUMList<Element>();
		innerList.add(new SingleExpression("f"));
		innerList.add(new SingleExpression("g"));

		sumList.add(innerList);
		assertEquals("a + b + c + d + e + (f + g)", sumList.symbolicExpression());
	}

}
