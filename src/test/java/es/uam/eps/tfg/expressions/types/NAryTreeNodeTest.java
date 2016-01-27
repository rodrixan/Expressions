package es.uam.eps.tfg.expressions.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NAryTreeNodeTest {

	private NAryTreeNode<String> node;

	@Before
	public void setUp() {
		node = new NAryTreeNode<String>("root");
	}

	@Test
	public void shouldAddChildren() {
		assertEquals(0, node.getChildren().size());
		node.addChild(createSampleChildren("child1"));
		assertEquals(1, node.getChildren().size());
	}

	@Test
	public void shouldAddChildrenAtSpecifiedPosition() {
		final NAryTreeNode<String> child1 = createSampleChildren("child1");
		final NAryTreeNode<String> child2 = createSampleChildren("child2");
		node.addChild(0, child2);
		node.addChild(1, child1);
		assertEquals(0, node.indexOfChild(child2));
		assertEquals(1, node.indexOfChild(child1));

	}

	@Test
	public void shouldRemoveChildren() {
		final NAryTreeNode<String> child1 = createSampleChildren("child1");
		final NAryTreeNode<String> child2 = createSampleChildren("child2");
		node.addChild(child1);
		node.addChild(child2);

		assertEquals(2, node.getChildren().size());
		node.removeChildren();
		assertEquals(0, node.getChildren().size());
	}

	@Test
	public void testRemoveChildrenInt() {
		final NAryTreeNode<String> child1 = createSampleChildren("child1");
		final NAryTreeNode<String> child2 = createSampleChildren("child2");
		node.addChild(child1);
		node.addChild(child2);

		assertEquals(2, node.getChildren().size());
		node.removeChildren(0);
		assertEquals(1, node.getChildren().size());
		assertEquals(child2, node.getChildAt(0));
	}

	@Test
	public void shouldGetChildAtIndex() {
		final NAryTreeNode<String> child1 = createSampleChildren("child1");
		final NAryTreeNode<String> child2 = createSampleChildren("child2");
		node.addChild(child1);
		node.addChild(child2);

		assertEquals(child1, node.getChildAt(0));
		assertEquals(child2, node.getChildAt(1));
	}

	@Test
	public void shouldReturnIndexOfChild() {
		final NAryTreeNode<String> child = createSampleChildren("child1");
		node.addChild(child);

		assertEquals(0, node.indexOfChild(child));
	}

	@Test
	public void shouldCheckIfNodeContainsChild() {
		final NAryTreeNode<String> child1 = createSampleChildren("child1");
		final NAryTreeNode<String> child2 = createSampleChildren("child2");
		node.addChild(child1);
		node.addChild(child2);

		assertTrue(node.contains(child1));
		assertTrue(node.contains(child2));
	}

	@Test
	public void shouldFailIfNodeDoesNotContainChild() {
		final NAryTreeNode<String> child1 = createSampleChildren("child1");
		final NAryTreeNode<String> child2 = createSampleChildren("child2");
		node.addChild(child1);

		assertFalse(node.contains(child2));
	}

	@Test
	public void shouldCheckifNodeIsNotLeaf() {
		final NAryTreeNode<String> child = createSampleChildren("child");
		node.addChild(child);
		assertFalse(node.isLeaf());
	}

	@Test
	public void shouldCheckifNodeIsLeaf() {

		assertTrue(node.isLeaf());
	}

	@Test
	public void shouldCheckIfNodeHasParent() {
		final NAryTreeNode<String> child = createSampleChildren("child");
		node.addChild(child);
		final NAryTreeNode<String> childAdded = node.getChildAt(0);
		assertTrue(childAdded.hasParent());
		assertEquals(node, childAdded.getParent());
	}

	@Test
	public void shouldCheckIfNodeHasNoParent() {

		assertFalse(node.hasParent());

	}

	private NAryTreeNode<String> createSampleChildren(String data) {
		return new NAryTreeNode<String>(data);
	}
}
