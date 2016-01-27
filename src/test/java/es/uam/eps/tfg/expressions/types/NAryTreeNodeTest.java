package es.uam.eps.tfg.expressions.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
		node.addChild(0, createSampleChildren("child2"));
		node.addChild(1, createSampleChildren("child1"));
		assertEquals(0, node.indexOfChild(createSampleChildren("child2")));
		assertEquals(1, node.indexOfChild(createSampleChildren("child1")));

	}

	@Test
	public void shouldRemoveChildren() {
		node.addChild(createSampleChildren("child1"));
		node.addChild(createSampleChildren("child2"));

		assertEquals(2, node.getChildren().size());
		node.removeChildren();
		assertEquals(0, node.getChildren().size());
	}

	@Test
	public void testRemoveChildrenInt() {
		node.addChild(createSampleChildren("child1"));
		node.addChild(createSampleChildren("child2"));

		assertEquals(2, node.getChildren().size());
		node.removeChildren(0);
		assertEquals(1, node.getChildren().size());
		assertEquals(createSampleChildren("child2"), node.getChildAt(0));
	}

	@Test
	public void shouldGetChildAtIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldReturnIndexOfChild() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldCheckIfNodeContainsChild() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldFailIfNodeDoesNotContainChild() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldCheckifNodeIsLeaf() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldCheckIfNodeHasParent() {
		fail("Not yet implemented");
	}

	private NAryTreeNode<String> createSampleChildren(String data) {
		return new NAryTreeNode<String>(data);
	}
}
