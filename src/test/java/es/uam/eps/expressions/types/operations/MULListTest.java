package es.uam.eps.expressions.types.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Element;
import es.uam.eps.expressions.types.operations.MULList;

public class MULListTest {
	private MULList<Element> mulList = new MULList<>();

	@Before
	public void setup() {
		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));
		mulList.add(new SingleExpression("d"));
		mulList.add(new SingleExpression("e"));
	}

	@Test
	public void toStringTest() {
		assertEquals("*[a, b, c, d, e]", mulList.toString());
	}

	@Test
	public void simpleSymbolicExpressionTest() {
		assertEquals("a * b * c * d * e", mulList.symbolicExpression());
	}

	@Test
	public void complexSymbolicExpressionTest() {
		final MULList<Element> innerList = new MULList<Element>();
		innerList.add(new SingleExpression("f"));
		innerList.add(new SingleExpression("g"));

		mulList.add(innerList);
		assertEquals("a * b * c * d * e * (f * g)", mulList.symbolicExpression());
	}

	@Test
	public void shouldReturnValidProperties() {
		assertTrue(mulList.isValidProperty(Properties.ASSOCIATIVE));
		assertTrue(mulList.isValidProperty(Properties.CONMUTATIVE));
		assertFalse(mulList.isValidProperty(Properties.COMMON_FACTOR));
		assertTrue(mulList.isValidProperty(Properties.DISTRIBUTIVE));
	}

	@Test
	public void shouldReturnPlusOperator() {
		assertEquals("*", mulList.getOperator().toString());
	}

	@Test
	public void shouldReturnNotAvaliableValue() {
		assertEquals(Element.VALUE_NOT_AVAILABLE, mulList.getValue());
	}

}
