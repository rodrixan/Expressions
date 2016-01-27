package es.uam.eps.tfg.expressions.types;

public class NAryTree<T> {
	private NAryTreeNode<T> root;

	public NAryTree(NAryTreeNode<T> root) {
		this.root = root;
		this.root.setParent(null);
	}

	public boolean isEmpty() {
		return root == null;
	}

	public int maxDepth() {
		if (isEmpty()) {
			return -1;
		}
		return depthFrom(root);
	}

	public int depthFrom(NAryTreeNode<T> node) {
		if (node.isLeaf()) {
			return 0;
		}

		int maxDepth = 0;
		for (final NAryTreeNode<T> child : node.getChildren()) {
			maxDepth = Math.max(maxDepth, depthFrom(child));
		}
		return maxDepth + 1;

	}

	public int depthOf(NAryTreeNode<T> node) {
		if (!node.hasParent()) {
			return 0;
		}
		return depthOf(node.getParent()) + 1;
	}

	public NAryTreeNode<T> getRoot() {
		return root;
	}

	public void setRoot(NAryTreeNode<T> root) {
		this.root = root;
	}
}
