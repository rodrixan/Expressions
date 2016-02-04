package es.uam.eps.tfg.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jctree.NodeNotFoundException;

import es.uam.eps.expressions.types.ExpressionNode;
import es.uam.eps.expressions.types.ExpressionTree;

public class NodeTreeIntegrationTest {
	ExpressionTree<ExpressionNode<String>> tree;

	@Before
	public void buildTree() {
		tree = new ExpressionTree<>();

		final ExpressionNode<String> root = new ExpressionNode<>("+");
		final ExpressionNode<String> node1 = new ExpressionNode<>(1, new int[] { 1 }, "*");
		final ExpressionNode<String> node2 = new ExpressionNode<>(1, new int[] { 2 }, "*");
		final ExpressionNode<String> node3 = new ExpressionNode<>(1, new int[] { 3 }, "h");

		final ExpressionNode<String> node11 = new ExpressionNode<>(2, new int[] { 1, 1 }, "a");
		final ExpressionNode<String> node12 = new ExpressionNode<>(2, new int[] { 1, 2 }, "b");
		final ExpressionNode<String> node13 = new ExpressionNode<>(2, new int[] { 1, 3 }, "c");

		final ExpressionNode<String> node21 = new ExpressionNode<>(2, new int[] { 2, 1 }, "d");
		final ExpressionNode<String> node22 = new ExpressionNode<>(2, new int[] { 2, 2 }, "+");

		final ExpressionNode<String> node221 = new ExpressionNode<>(3, new int[] { 2, 2, 1 }, "e");
		final ExpressionNode<String> node222 = new ExpressionNode<>(3, new int[] { 2, 2, 2 }, "f");
		final ExpressionNode<String> node223 = new ExpressionNode<>(3, new int[] { 2, 2, 3 }, "g");

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
		ExpressionNode<String> ancestor = null;
		try {
			ancestor = tree.commonAncestor(new ExpressionNode<>(3, new int[] { 2, 2, 3 }, "g"),
					new ExpressionNode<>(2, new int[] { 2, 1 }, "d"));
		} catch (final NodeNotFoundException e) {

			fail(e.getMessage());
		}

		assertNotNull(ancestor);
		assertEquals(ancestor, new ExpressionNode<>(1, new int[] { 2 }, "*"));
	}

	@Test
	public void shouldReturnInOrderExpression() {
		final List<ExpressionNode<String>> expression = tree.inOrderTraversal();
		final StringBuilder sb = new StringBuilder();
		for (final ExpressionNode<String> n : expression) {
			sb.append(n.getData() + " ");
		}
		assertEquals("a * b * c + d * e + f + g + h", sb.toString().trim());
	}

}
