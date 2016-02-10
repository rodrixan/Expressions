package es.uam.eps.expressions.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import es.uam.eps.expressions.properties.Properties;
import es.uam.eps.expressions.types.SingleExpression;

public class SingleExpressionTest {
	private SingleExpression singleExp;

	@Test
	public void shouldReturnTheNameOfTheExpression() {
		singleExp = new SingleExpression("a random variable");
		assertEquals("a random variable", singleExp.toString());
	}

	@Test
	public void shouldReturnTheInfixExpression() {
		singleExp = new SingleExpression("a random variable");
		assertEquals("a random variable", singleExp.symbolicExpression());
	}

	@Test
	public void shouldNotHaveValidProperties() {
		singleExp = new SingleExpression("a random variable");
		assertFalse(singleExp.isValidProperty(Properties.ASSOCIATIVE));
		assertFalse(singleExp.isValidProperty(Properties.CONMUTATIVE));
		assertFalse(singleExp.isValidProperty(Properties.DISTRIBUTIVE));
		assertFalse(singleExp.isValidProperty(Properties.COMMON_FACTOR));
	}

}
