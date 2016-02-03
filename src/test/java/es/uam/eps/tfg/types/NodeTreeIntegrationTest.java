package es.uam.eps.tfg.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jctree.ArrayListTree;
import com.googlecode.jctree.NodeNotFoundException;

import es.uam.eps.expressions.types.Node;

public class NodeTreeIntegrationTest {
	ArrayListTree<Node<String>> tree;

	@Before
	public void buildTree() {
		tree = new ArrayListTree<>();

		final Node<String> root = new Node<>("0");
		final Node<String> node1 = new Node<>(1, new int[] { 1 }, "1");
		final Node<String> node2 = new Node<>(1, new int[] { 2 }, "2");
		final Node<String> node3 = new Node<>(1, new int[] { 3 }, "3");

		final Node<String> node11 = new Node<>(2, new int[] { 1, 1 }, "11");
		final Node<String> node12 = new Node<>(2, new int[] { 1, 2 }, "12");
		final Node<String> node13 = new Node<>(2, new int[] { 1, 3 }, "13");

		final Node<String> node21 = new Node<>(2, new int[] { 2, 1 }, "21");
		final Node<String> node22 = new Node<>(2, new int[] { 2, 2 }, "22");

		final Node<String> node221 = new Node<>(3, new int[] { 2, 2, 1 }, "221");
		final Node<String> node222 = new Node<>(3, new int[] { 2, 2, 2 }, "222");
		final Node<String> node223 = new Node<>(3, new int[] { 2, 2, 3 }, "223");

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
			ancestor = tree.commonAncestor(new Node<>(3, new int[] { 2, 2, 3 }, "223"),
					new Node<>(2, new int[] { 2, 1 }, "21"));
		} catch (final NodeNotFoundException e) {

			fail(e.getMessage());
		}

		assertNotNull(ancestor);
		assertEquals(ancestor, new Node<>(1, new int[] { 2 }, "2"));
	}

}
