package es.uam.eps.tfg.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.uam.eps.expressions.exceptions.LevelErrorException;
import es.uam.eps.expressions.exceptions.PathErrorException;
import es.uam.eps.expressions.types.ExpressionNode;

public class NodeTest {

	ExpressionNode<String> node;

	@Test(expected = LevelErrorException.class)
	public void shouldFailWhenNegativeLevel() {
		node = new ExpressionNode<String>(-1, new int[2], "testNode");
	}

	@Test(expected = PathErrorException.class)
	public void shouldFailWhenBadSizePath() {
		node = new ExpressionNode<String>(3, new int[2], "testNode");
	}

	@Test
	public void shouldReturnFalseWhenCompared() {
		node = new ExpressionNode<String>(3, new int[] { 1, 2, 3 }, "testNode");
		final ExpressionNode<String> node2 = new ExpressionNode<String>(3, new int[] { 1, 2, 2 }, "testNode");
		assertNotEquals(node2, node);
	}

	@Test
	public void shouldReturnTrueWhenSameSubPath() {
		node = new ExpressionNode<String>(3, new int[] { 1, 2, 3 }, "testNode");
		final ExpressionNode<String> node2 = new ExpressionNode<String>(3, new int[] { 1, 2, 4 }, "testNode2");

		assertTrue(node.isOnTheSamePath(node2));

	}

	@Test
	public void shouldReturnFalseWhenDifferentSubPathLenght() {
		node = new ExpressionNode<String>(3, new int[] { 1, 2, 3 }, "testNode");
		final ExpressionNode<String> node2 = new ExpressionNode<String>(4, new int[] { 1, 2, 3, 5 }, "testNode2");

		assertFalse(node.isOnTheSamePath(node2));

	}

	@Test
	public void shouldReturnTrueWhenSamePosition() {
		node = new ExpressionNode<String>(3, new int[] { 1, 2, 3 }, "testNode");
		final ExpressionNode<String> node2 = new ExpressionNode<String>(3, new int[] { 1, 2, 3 }, "testNode2");

		assertTrue(node.equals(node2, false));

	}
}
