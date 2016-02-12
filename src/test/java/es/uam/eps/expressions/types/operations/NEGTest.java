package es.uam.eps.expressions.types.operations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;

public class NEGTest {

	private NEG<Expression> neg;

	@Test
	public void testSingleExpression() {
		neg = new NEG<>(new SingleExpression("a"));
		assertEquals("-[a]", neg.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailToAddElements() {
		neg = new NEG<>(new SingleExpression("a"));
		neg.add(new SingleExpression("b"));
	}

	@Test
	public void testExpressionList() {

		final SUMList<Expression> sumList = new SUMList<>();
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));

		neg = new NEG<>(sumList);
		assertEquals("-[+[a, b]]", neg.toString());
	}

	@Test
	public void testSimpleSymbolicExpression() {
		neg = new NEG<>(new SingleExpression("a"));
		assertEquals("-a", neg.symbolicExpression());
	}

	@Test
	public void testComplexSymbolicExpression() {

		final SUMList<Expression> sumList = new SUMList<>();
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));

		neg = new NEG<>(sumList);
		assertEquals("-(a + b)", neg.symbolicExpression());
	}

}
