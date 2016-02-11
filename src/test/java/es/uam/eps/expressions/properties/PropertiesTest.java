package es.uam.eps.expressions.properties;

import static es.uam.eps.expressions.properties.Properties.acceptedProperties;
import static es.uam.eps.expressions.properties.Properties.associate;
import static es.uam.eps.expressions.properties.Properties.conmute;
import static es.uam.eps.expressions.properties.Properties.disassociate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import es.uam.eps.expressions.exceptions.IllegalPropertyException;
import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.operations.MULList;
import es.uam.eps.expressions.types.operations.SUMList;

public class PropertiesTest {

	@Test
	public void testAcceptedProperties() {
		assertEquals(4, acceptedProperties().size());
		assertTrue(acceptedProperties().contains(Properties.ASSOCIATIVE));
		assertTrue(acceptedProperties().contains(Properties.CONMUTATIVE));
		assertTrue(acceptedProperties().contains(Properties.DISTRIBUTIVE));
		assertTrue(acceptedProperties().contains(Properties.COMMON_FACTOR));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailAssociate() {
		final ExpressionList<Expression> sumList = new SUMList<>();
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));
		sumList.add(new SingleExpression("c"));

		try {
			associate(sumList, 1, 1);
			fail("ERROR: Shouldn't reach this point");
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}

	}

	@Test
	public void testAssociate() {
		final ExpressionList<Expression> sumList = new SUMList<>();
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));

		final ExpressionList<Expression> mulList = new MULList<>();
		mulList.add(new SingleExpression("c"));
		mulList.add(new SingleExpression("d"));

		sumList.add(mulList);
		sumList.add(new SingleExpression("e"));
		sumList.add(new SingleExpression("f"));

		try {
			assertEquals("+[a, +[b, *[c, d], e], f]", associate(sumList, 1, 3).toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test
	public void testDisassociate() {
		final ExpressionList<Expression> sumList = new SUMList<>();
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));

		final ExpressionList<Expression> innerSumList = new SUMList<>();
		innerSumList.add(new SingleExpression("c"));
		innerSumList.add(new SingleExpression("d"));

		sumList.add(innerSumList);
		sumList.add(new SingleExpression("e"));

		final ExpressionList<Expression> innerSumList2 = new SUMList<>();
		innerSumList2.add(new SingleExpression("f"));
		innerSumList2.add(new SingleExpression("g"));

		sumList.add(innerSumList2);
		sumList.add(new SingleExpression("h"));

		try {
			assertEquals("+[a, b, c, d, e, +[f, g], h]", disassociate(sumList, 2).toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailDisassociate() {
		final ExpressionList<Expression> sumList = new SUMList<>();
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));

		final ExpressionList<Expression> mulList = new MULList<>();
		mulList.add(new SingleExpression("c"));
		mulList.add(new SingleExpression("d"));

		sumList.add(mulList);
		sumList.add(new SingleExpression("e"));

		try {
			disassociate(sumList, 2).toString();
			fail("ERROR: Shouldn't reach this point");
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}

	}

	@Test
	public void testConmute() {
		final ExpressionList<Expression> sumList = new SUMList<>();
		sumList.add(new SingleExpression("a"));
		sumList.add(new SingleExpression("b"));

		final ExpressionList<Expression> mulList = new MULList<>();
		mulList.add(new SingleExpression("c"));
		mulList.add(new SingleExpression("d"));

		sumList.add(mulList);
		sumList.add(new SingleExpression("e"));

		final ExpressionList<Expression> innerSumList = new SUMList<>();
		innerSumList.add(new SingleExpression("f"));
		innerSumList.add(new SingleExpression("g"));

		sumList.add(innerSumList);
		sumList.add(new SingleExpression("h"));

		try {
			assertEquals("+[*[c, d], a, b, e, +[f, g], h]", conmute(sumList, 2, 0).toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test(expected = IllegalPropertyException.class)
	public void testFailDistribute() throws IllegalPropertyException {

		final ExpressionList<Expression> sumList = new SUMList<>();

		sumList.add(new SingleExpression("b"));
		sumList.add(new SingleExpression("c"));
		sumList.add(new SingleExpression("d"));

		Properties.distribute(sumList, 0, 1);

	}

	@Test
	public void testSimpleDistributeOneElement() {

		final ExpressionList<Expression> mulList = new MULList<Expression>();

		mulList.add(new SingleExpression("a"));

		final ExpressionList<Expression> sumList = new SUMList<>();

		sumList.add(new SingleExpression("b"));
		sumList.add(new SingleExpression("c"));
		sumList.add(new SingleExpression("d"));

		mulList.add(sumList);

		try {
			assertEquals("+[*[a, b], *[a, c], *[a, d]]", Properties.distribute(mulList, 0, 1).toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test
	public void testComplexDistributeOneElement() {
		final ExpressionList<Expression> mulList = new MULList<Expression>();

		final ExpressionList<Expression> sumList1 = new SUMList<>();

		sumList1.add(new SingleExpression("a"));

		final ExpressionList<Expression> innerMulList = new MULList<Expression>();
		innerMulList.add(new SingleExpression("b"));
		innerMulList.add(new SingleExpression("c"));

		sumList1.add(innerMulList);

		mulList.add(sumList1);

		mulList.add(new SingleExpression("d"));

		final ExpressionList<Expression> sumList2 = new SUMList<>();

		sumList2.add(new SingleExpression("e"));
		sumList2.add(new SingleExpression("f"));

		mulList.add(sumList2);
		mulList.add(new SingleExpression("g"));

		try {
			assertEquals("*[+[*[d, a], *[d, *[b, c]]], +[e, f], g]", Properties.distribute(mulList, 1, 0).toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test
	public void testDistributeAllElements() {
		final ExpressionList<Expression> mulList = new MULList<Expression>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));
		mulList.add(new SingleExpression("c"));

		final ExpressionList<Expression> sumList = new SUMList<>();

		sumList.add(new SingleExpression("d"));
		sumList.add(new SingleExpression("e"));

		mulList.add(sumList);
		mulList.add(new SingleExpression("f"));

		try {
			assertEquals("+[*[a, b, c, f, d], *[a, b, c, f, e]]", Properties.distribute(mulList, 3).toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test
	public void testSimpleCommonFactor() {
		final ExpressionList<Expression> sumList = new SUMList<Expression>();

		final ExpressionList<Expression> item1 = new MULList<>();

		item1.add(new SingleExpression("a"));
		item1.add(new SingleExpression("b"));

		final ExpressionList<Expression> item2 = new MULList<>();

		item2.add(new SingleExpression("a"));
		item2.add(new SingleExpression("c"));
		item2.add(new SingleExpression("d"));

		final ExpressionList<Expression> item3 = new MULList<>();

		final ExpressionList<Expression> subItem31 = new SUMList<>();

		item3.add(new SingleExpression("a"));
		subItem31.add(new SingleExpression("e"));
		subItem31.add(new SingleExpression("f"));

		item3.add(subItem31);

		sumList.add(item1);
		sumList.add(item2);
		sumList.add(item3);
		try {
			assertEquals("*[a, +[b, *[c, d], +[e, f]]]",
					Properties.commonFactor(sumList, new SingleExpression("a"), new int[] { 0, 1, 2 }).toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}

	}

	@Test(expected = IllegalPropertyException.class)
	public void testFailPropertyCommonFactor() throws IllegalPropertyException {

		final ExpressionList<Expression> mulList = new MULList<>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("b"));

		Properties.commonFactor(mulList, new SingleExpression("a"), new int[] { 0, 1, 2 });

		fail("ERROR: Shouldn't reach this point");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailArgumentCommonFactor() {
		final ExpressionList<Expression> sumList = new SUMList<Expression>();

		final ExpressionList<Expression> item1 = new MULList<>();

		item1.add(new SingleExpression("a"));
		item1.add(new SingleExpression("b"));

		final ExpressionList<Expression> item2 = new MULList<>();

		item2.add(new SingleExpression("a"));
		item2.add(new SingleExpression("c"));
		item2.add(new SingleExpression("d"));

		final ExpressionList<Expression> item3 = new MULList<>();

		final ExpressionList<Expression> subItem31 = new SUMList<>();

		item3.add(new SingleExpression("a"));
		subItem31.add(new SingleExpression("e"));
		subItem31.add(new SingleExpression("f"));

		item3.add(subItem31);

		sumList.add(new SingleExpression("i"));
		sumList.add(item1);
		sumList.add(new SingleExpression("h"));
		sumList.add(item2);
		sumList.add(item3);
		sumList.add(new SingleExpression("g"));
		try {
			Properties.commonFactor(sumList, new SingleExpression("a"), new int[] { 0, 1, 4 });
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test
	public void testComplexCommonFactor() {
		final ExpressionList<Expression> sumList = new SUMList<Expression>();

		final ExpressionList<Expression> item1 = new MULList<>();

		item1.add(new SingleExpression("a"));
		item1.add(new SingleExpression("b"));

		final ExpressionList<Expression> item2 = new MULList<>();

		item2.add(new SingleExpression("a"));
		item2.add(new SingleExpression("c"));
		item2.add(new SingleExpression("d"));

		final ExpressionList<Expression> item3 = new MULList<>();

		final ExpressionList<Expression> subItem31 = new SUMList<>();

		item3.add(new SingleExpression("a"));
		subItem31.add(new SingleExpression("e"));
		subItem31.add(new SingleExpression("f"));

		item3.add(subItem31);

		sumList.add(new SingleExpression("i"));
		sumList.add(item1);
		sumList.add(new SingleExpression("h"));
		sumList.add(item2);
		sumList.add(item3);
		sumList.add(new SingleExpression("g"));

		try {
			final ExpressionList<Expression> commonFactorList = Properties.commonFactor(sumList, new SingleExpression("a"),
					new int[] { 1, 3, 4 });
			assertEquals("+[i, *[a, +[b, *[c, d], +[e, f]]], h, g]", commonFactorList.toString());
		} catch (final IllegalPropertyException e) {
			fail("ERROR: Shouldn't throw IllegalPropertyException");
		}
	}

	@Test
	public void testRemoveNeutralElement() {
		final ExpressionList<Expression> mulList = new MULList<Expression>();

		mulList.add(new SingleExpression("a"));
		mulList.add(new SingleExpression("1"));
		mulList.add(new SingleExpression("c"));

		try {
			final ExpressionList<Expression> neutralElementRemovedList = Properties.removeNeutralElement(mulList, 1);
			assertEquals("*[a, c]", neutralElementRemovedList.toString());
		} catch (final Exception e) {
			fail("ERROR: Shouldn't throw Exception");
		}
	}

}
