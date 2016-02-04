package es.uam.eps.expressions.types;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jctree.ArrayListTree;
import com.googlecode.jctree.NodeNotFoundException;

/**
 * Class that implements a mathematic expression in tree form.
 *
 * It's a simple modification of the class ArrayListTree for the inOrder method
 *
 * @author Rodrigo de Blas
 *
 * @param <E>
 */
public class ExpressionTree<E> extends ArrayListTree<E> {
	/**
	 * This method returns an infix notation expression of the tree
	 *
	 * @return list with infix notation expression (nodes)
	 */
	@Override
	public List<E> inOrderTraversal() {
		if (isEmpty()) {
			return new ArrayList<E>();
		} else {
			return inOrderFrom(root(), new ArrayList<E>());
		}
	}

	private List<E> inOrderFrom(E node, List<E> list) {

		final List<E> children = getChildren(node);

		if (children.size() > 0) {

			for (final E child : children) {
				inOrderFrom(child, list);
				list.add(node);// add parent every time a child is added
			}
			// remove the last appearance of the parent
			list.remove(list.lastIndexOf(node));

		} else {
			list.add(node);
		}
		return list;
	}

	private List<E> getChildren(E node) {
		List<E> children = null;
		try {
			children = children(node);
		} catch (final NodeNotFoundException e) {
			// Should never get here as we are using the known tree nodes
		}

		return children;
	}

}
