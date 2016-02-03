package es.uam.eps.tfg.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jctree.NodeNotFoundException;

import es.uam.eps.expressions.types.ExpressionTree;
import es.uam.eps.expressions.types.Node;

public class NodeTreeIntegrationTest {
	ExpressionTree<Node<String>> tree;

	@Before
	public void buildTree() {
		tree = new ExpressionTree<>();

		final Node<String> root = new Node<>("+");
		final Node<String> node1 = new Node<>(1, new int[] { 1 }, "*");
		final Node<String> node2 = new Node<>(1, new int[] { 2 }, "*");
		final Node<String> node3 = new Node<>(1, new int[] { 3 }, "h");

		final Node<String> node11 = new Node<>(2, new int[] { 1, 1 }, "a");
		final Node<String> node12 = new Node<>(2, new int[] { 1, 2 }, "b");
		final Node<String> node13 = new Node<>(2, new int[] { 1, 3 }, "c");

		final Node<String> node21 = new Node<>(2, new int[] { 2, 1 }, "d");
		final Node<String> node22 = new Node<>(2, new int[] { 2, 2 }, "+");

		final Node<String> node221 = new Node<>(3, new int[] { 2, 2, 1 }, "e");
		final Node<String> node222 = new Node<>(3, new int[] { 2, 2, 2 }, "f");
		final Node<String> node223 = new Node<>(3, new int[] { 2, 2, 3 }, "g");

		tree.add(root);
		tree.addAll(root, Arrays.asList(node1, node2, node3));
		tree.addAll(node1, Arrays.asList(node11, node12, node13));
		tree.addAll(node2, Arrays.asList(node21, node22));
		tree.addAll(node22, Arrays.asList(node221, node222, node223));
	}

	@Test
	public void testDepth() {
		assertEquals(4, tree.depth());
	}

	@Test
	public void shouldReturnCommonAncestor() {
		Node<String> ancestor = null;
		try {
			ancestor = tree.commonAncestor(new Node<>(3, new int[] { 2, 2, 3 }, "g"),
					new Node<>(2, new int[] { 2, 1 }, "d"));
		} catch (final NodeNotFoundException e) {

			fail(e.getMessage());
		}

		assertNotNull(ancestor);
		assertEquals(ancestor, new Node<>(1, new int[] { 2 }, "*"));
	}

	@Test
	public void shouldReturnInOrderExpression() {
		final List<Node<String>> expression = tree.inOrderTraversal();
		final StringBuilder sb = new StringBuilder();
		for (final Node<String> n : expression) {
			sb.append(n.getData() + " ");
		}
		assertEquals("a * b * c + d * e + f + g + h", sb.toString().trim());
	}

}
