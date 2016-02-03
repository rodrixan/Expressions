package es.uam.eps.tfg.types;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import es.uam.eps.expressions.exceptions.LevelErrorException;
import es.uam.eps.expressions.exceptions.PathErrorException;
import es.uam.eps.expressions.types.Node;

public class NodeTest {

	Node<String> node;

	@Test(expected = LevelErrorException.class)
	public void shouldFailWhenNegativeLevel() {
		node = new Node<String>(-1, new int[2], "testNode");
	}

	@Test(expected = PathErrorException.class)
	public void shouldFailWhenBadSizePath() {
		node = new Node<String>(3, new int[2], "testNode");
	}

	@Test
	public void shouldReturnFalseWhenCompared() {
		node = new Node<String>(3, new int[] { 1, 2, 3 }, "testNode");
		final Node<String> node2 = new Node<String>(3, new int[] { 1, 2, 2 }, "testNode");
		assertNotEquals(node2, node);
	}

}
