package es.uam.eps.expressions.types.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.operations.SUMList;

public class SUMListTest {
	private SUMList<Expression> sumList = new SUMList<>();

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
		final SUMList<Expression> innerList = new SUMList<Expression>();
		innerList.add(new SingleExpression("f"));
		innerList.add(new SingleExpression("g"));

		sumList.add(innerList);
		assertEquals("a + b + c + d + e + (f + g)", sumList.symbolicExpression());
	}

	@Test
	public void shouldReturnValidProperties() {
		assertTrue(sumList.isValidProperty(Properties.ASSOCIATIVE));
		assertTrue(sumList.isValidProperty(Properties.CONMUTATIVE));
		assertTrue(sumList.isValidProperty(Properties.COMMON_FACTOR));
		assertFalse(sumList.isValidProperty(Properties.DISTRIBUTIVE));
	}

	@Test
	public void shouldReturnPlusOperator() {
		assertEquals("+", sumList.getOperator().toString());
	}

	@Test
	public void shouldReturnNotAvaliableValue() {
		assertEquals(Expression.VALUE_NOT_AVAILABLE, sumList.getValue());
	}
}
