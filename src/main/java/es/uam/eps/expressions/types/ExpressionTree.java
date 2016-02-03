package es.uam.eps.expressions.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.googlecode.jctree.NodeNotFoundException;
import com.googlecode.jctree.Tree;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 *
 * This classs has been entirely copied from the original for modify a method in
 * order to implement a different 'inOrder' algorithm.
 *
 * @author Gaurav Saxena, modified by por Rodrigo de Blas
 *
 * @see com.googlecode.jctree.ArrayListTree
 *
 * @param <E>
 */
public class ExpressionTree<E> implements Tree<E>, Cloneable {
	private static final int DEFAULT_TREE_SIZE = 16;
	private ArrayList<E> nodeList = new ArrayList<E>();
	private ArrayList<Integer> parentList = new ArrayList<Integer>();
	private ArrayList<IntArrayList> childrenList = new ArrayList<IntArrayList>();
	private IntArrayFIFOQueue emptySlotsList = new IntArrayFIFOQueue();
	private int size = 0;
	private int depth = 0;
	private int rootIndex = -1;

	public ExpressionTree(int size) {
		nodeList = new ArrayList<E>(size);
		parentList = new ArrayList<Integer>(size);
		childrenList = new ArrayList<IntArrayList>(size);
	}

	public ExpressionTree() {
		this(DEFAULT_TREE_SIZE);
	}

	/**
	 * If tree is empty, it adds a root. In case tree is not empty, it will
	 * attempt to add parameter as a child of the root
	 *
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		try {
			if (isEmpty()) {
				return add(null, e);
			} else {
				return add(nodeList.get(rootIndex), e);
			}
		} catch (final NodeNotFoundException ex) {
			throw new IllegalArgumentException(ex);// This should never happen
													// as when tree is empty, we
													// are adding the root and
													// when it is not then we
													// are adding to the root,
													// which will always be
													// present in a non-empty
													// tree
		}
	}

	@Override
	public boolean add(E parent, E child) throws NodeNotFoundException {
		checkNode(child);
		if (nodeList.size() == Integer.MAX_VALUE) {
			throw new OutOfMemoryError();
		}
		if (isRootElementBeingAdded(parent, child)) {
			return true;
		}
		final int parentIndex = nodeList.indexOf(parent);
		if (parentIndex > -1) {
			final int childIndex = nodeList.indexOf(child);
			if (childIndex == -1) {
				if (emptySlotsList.isEmpty()) {
					return addElementToTheEnd(child, parentIndex);
				} else {
					final int slot = emptySlotsList.dequeueInt();
					return addElementToTheSlot(slot, child, parentIndex);
				}
			} else {
				nodeList.set(childIndex, child);
				return false;
			}
		} else {
			throw new NodeNotFoundException("No node was found for parent object");
		}
	}

	private boolean addElementToTheSlot(int slot, E child, int parentIndex) {
		nodeList.set(slot, child);
		parentList.set(slot, parentIndex);
		childrenList.get(parentIndex).add(slot);
		childrenList.set(slot, new IntArrayList());
		size++;
		depth = Math.max(recalculateDepth(parentIndex, 2), depth);
		return true;
	}

	private boolean addElementToTheEnd(E child, int parentIndex) {
		nodeList.add(child);
		parentList.add(parentIndex);
		childrenList.get(parentIndex).add(nodeList.size() - 1);
		childrenList.add(new IntArrayList());
		size++;
		depth = Math.max(recalculateDepth(parentIndex, 2), depth);
		return true;
	}

	private boolean isRootElementBeingAdded(E parent, E child) {
		if (parent == null) {
			if (isEmpty()) {
				addRoot(child);
				return true;
			} else {
				throw new IllegalArgumentException(
						"parent cannot be null except for root element. The tree already has a root.");
			}
		} else {
			return false;
		}
	}

	private void addRoot(E child) {
		nodeList.add(child);
		rootIndex = nodeList.size() - 1;
		parentList.add(-1);
		childrenList.add(new IntArrayList());
		size++;
		depth++;
	}

	/**
	 * This method lets the sub-classes define the position at which new child
	 * may be added
	 *
	 * @param children
	 * @param newChild
	 * @return index at which new child will be added
	 */
	protected int getChildAddPosition(List<E> children, E newChild) {
		return children.size();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean retVal = false;
		for (final Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();) {
			retVal |= add(iterator.next());
		}
		return retVal;
	}

	@Override
	public boolean addAll(E parent, Collection<? extends E> c) {
		try {
			for (final Iterator<? extends E> iterator = c.iterator(); iterator.hasNext();) {
				add(parent, iterator.next());
			}
			return true;
		} catch (final NodeNotFoundException ex) {
			return false;
		}
	}

	@Override
	public List<E> children(E e) throws NodeNotFoundException {
		checkNode(e);
		final int index = nodeList.indexOf(e);
		if (index > -1) {
			final IntArrayList childrenIndexList = childrenList.get(index);
			final ArrayList<E> children = new ArrayList<E>(childrenIndexList.size());
			for (final Integer i : childrenIndexList) {
				children.add(nodeList.get(i));
			}
			return children;
		} else {
			throw new NodeNotFoundException("No node was found for object");
		}
	}

	/*
	 * @Override public List<E> children(E e) throws NodeNotFoundException {
	 * checkNode(e); int index = nodeList.indexOf(e); if(index > -1) {
	 * ArrayList<Integer> childrenIndexList = childrenList.get(index); Object[]
	 * array = new Object[childrenList.size()]; int j = 0; for (Integer i :
	 * childrenIndexList) { array[j++] = (nodeList.get(i)); } return (List<E>)
	 * Arrays.asList(array); } else throw new NodeNotFoundException(
	 * "No node was found for object"); }
	 */
	@Override
	public void clear() {
		nodeList.clear();
		parentList.clear();
		childrenList.clear();
		size = 0;
		depth = 0;
		rootIndex = -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		ExpressionTree<E> v = null;
		try {
			v = (ExpressionTree<E>) super.clone();
			v.nodeList = (ArrayList<E>) nodeList.clone();
			v.parentList = (ArrayList<Integer>) parentList.clone();
			v.childrenList = new ArrayList<IntArrayList>();
			v.size = this.size;
			v.depth = this.depth;
			for (int i = 0; i < childrenList.size(); i++) {
				v.childrenList.add(childrenList.get(i).clone());
			}
		} catch (final CloneNotSupportedException e) {
			// This should't happen because we are cloneable
		}
		return v;
	}

	@Override
	public E commonAncestor(E node1, E node2) throws NodeNotFoundException {
		int height1 = 0;
		E e1 = node1;
		while (e1 != null) {
			height1++;
			e1 = parent(e1);
		}
		int height2 = 0;
		E e2 = node2;
		while (e2 != null) {
			height2++;
			e2 = parent(e2);
		}
		if (height1 > height2) {
			while (height1 - height2 > 0) {
				node1 = parent(node1);
				height1--;
			}
		} else {
			while (height2 - height1 > 0) {
				node2 = parent(node2);
				height2--;
			}
		}
		while (node1 != null && !node1.equals(node2)) {
			node1 = parent(node1);
			node2 = parent(node2);
		}
		return node1;
	}

	@Override
	public boolean contains(Object o) {
		if (o == null) {
			return false;
		} else {
			return nodeList.indexOf(o) > -1;
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return nodeList.containsAll(c);
	}

	@Override
	public int depth() {
		return depth;
	}

	@Override
	@Deprecated
	public List<E> inorderOrderTraversal() {
		return inorderOrderTraversal(0, new ArrayList<E>());
	}

	@Override
	public List<E> inOrderTraversal() {
		if (isEmpty()) {
			return new ArrayList<E>();
		} else {
			return inorderOrderTraversal(rootIndex, new ArrayList<E>());
		}
	}

	@Override
	public boolean isAncestor(E node, E child) throws NodeNotFoundException {
		checkNode(child);
		return new TreeHelper().isAncestor(this, node, child);
	}

	@Override
	public boolean isDescendant(E parent, E node) throws NodeNotFoundException {
		checkNode(parent);
		return new TreeHelper().isDescendant(this, parent, node);
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Iterator returns nodes as expected from inOrderTraversal
	 *
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return getCurrentList().iterator();
	}

	@Override
	public List<E> leaves() {
		if (isEmpty()) {
			return new ArrayList<E>();
		} else {
			return leaves(rootIndex, new ArrayList<E>());
		}
	}

	private List<E> leaves(int nodeIndex, ArrayList<E> list) {
		final IntArrayList children = childrenList.get(nodeIndex);
		if (children.size() > 0) {
			int i = 0;
			for (; i < (int) Math.ceil((double) children.size() / 2); i++) {
				leaves(children.get(i), list);
			}
			if (childrenList.get(nodeIndex).isEmpty()) {
				list.add(nodeList.get(nodeIndex));
			}
			for (; i < children.size(); i++) {
				leaves(children.get(i), list);
			}
		} else if (childrenList.get(nodeIndex).isEmpty()) {
			list.add(nodeList.get(nodeIndex));
		}
		return list;
	}

	@Override
	public List<E> levelOrderTraversal() {
		if (isEmpty()) {
			return new ArrayList<E>();
		} else {
			final LinkedList<Integer> queue = new LinkedList<Integer>();
			queue.add(0);
			return levelOrderTraversal(new ArrayList<E>(), queue);
		}
	}

	@Override
	public E parent(E e) throws NodeNotFoundException {
		checkNode(e);
		final int index = nodeList.indexOf(e);
		if (index == 0) {
			return null;
		} else if (index > 0) {
			return nodeList.get(parentList.get(index));
		} else {
			throw new NodeNotFoundException("No node was found for object");
		}
	}

	@Override
	public List<E> postOrderTraversal() {
		if (isEmpty()) {
			return new ArrayList<E>();
		} else {
			return postOrderTraversal(rootIndex, new ArrayList<E>());
		}
	}

	@Override
	public List<E> preOrderTraversal() {
		if (isEmpty()) {
			return new ArrayList<E>();
		} else {
			return preOrderTraversal(rootIndex, new ArrayList<E>());
		}
	}

	/**
	 * Removes the sub-tree rooted at the node passed
	 *
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		checkNode(o);
		final int i = nodeList.indexOf(o);
		if (i > -1) {
			boolean wasRemoved;
			if (i != rootIndex) {
				wasRemoved = remove(i);
				depth = recalculateDepth(rootIndex, 0);
			} else {
				wasRemoved = remove(i);
				depth = 0;
			}
			return wasRemoved;
		} else {
			return false;
		}
	}

	/**
	 * Removes the sub-tree rooted at the nodes in the collection passed
	 *
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean retVal = false;
		for (final Iterator<?> iterator = c.iterator(); iterator.hasNext();) {
			retVal |= remove(iterator.next());
		}
		return retVal;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Tree interface doesn't support retainAll");
	}

	@Override
	public E root() {
		if (isEmpty()) {
			return null;
		} else {
			return nodeList.get(rootIndex);
		}
	}

	@Override
	public List<E> siblings(E e) throws NodeNotFoundException {
		checkNode(e);
		final E parent = parent(e);
		if (parent != null) {
			final List<E> children = children(parent);
			children.remove(e);
			return children;
		} else {
			return new ArrayList<E>();
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Object[] toArray() {
		return getCurrentList().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return getCurrentList().toArray(a);
	}

	private void checkNode(Object child) {
		if (child == null) {
			throw new IllegalArgumentException("null nodes are not allowed");
		}
	}

	private List<E> getCurrentList() {
		return inOrderTraversal();
	}

	private List<E> inorderOrderTraversal(int nodeIndex, ArrayList<E> list) {
		final IntArrayList children = childrenList.get(nodeIndex);
		if (children.size() > 0) {
			int i = 0;
			// visit the parent every time a child is visited except the last
			// one
			for (; i < children.size() - 1; i++) {
				inorderOrderTraversal(children.get(i), list);

				list.add(nodeList.get(nodeIndex));
			}
			for (; i < children.size(); i++) {
				inorderOrderTraversal(children.get(i), list);
			}
		} else {
			list.add(nodeList.get(nodeIndex));
		}
		return list;
	}

	private List<E> levelOrderTraversal(ArrayList<E> list, LinkedList<Integer> queue) {
		while (!queue.isEmpty()) {
			list.add(nodeList.get(queue.getFirst()));
			final IntArrayList children = childrenList.get(queue.getFirst());
			for (int i = 0; i < children.size(); i++) {
				queue.add(children.get(i));
			}
			queue.remove();
		}
		return list;
	}

	private List<E> postOrderTraversal(int nodeIndex, ArrayList<E> list) {
		final IntArrayList children = childrenList.get(nodeIndex);
		for (int i = 0; i < children.size(); i++) {
			postOrderTraversal(children.get(i), list);
		}
		if (nodeList.get(nodeIndex) != null) {
			list.add(nodeList.get(nodeIndex));
		}
		return list;
	}

	private List<E> preOrderTraversal(int nodeIndex, ArrayList<E> list) {
		if (nodeList.get(nodeIndex) != null) {
			list.add(nodeList.get(nodeIndex));
		}
		final IntArrayList children = childrenList.get(nodeIndex);
		for (int i = 0; i < children.size(); i++) {
			preOrderTraversal(children.get(i), list);
		}
		return list;
	}

	private boolean remove(int index) {
		if (index > -1) {
			if (index == rootIndex) {
				rootIndex = -1;
				size = 0;
				nodeList.clear();
				parentList.clear();
				childrenList.clear();
				return true;
			} else {
				final Integer parentIndex = parentList.set(index, -1);
				if (parentIndex > -1) {
					childrenList.get(parentIndex).remove(Integer.valueOf(index));
				}
				nodeList.set(index, null);
				emptySlotsList.enqueue(index);
				size--;
				final IntArrayList children = childrenList.get(index);
				for (final int j = 0; j < children.size();) {
					remove(children.get(0).intValue());
				}
				childrenList.set(index, null);
				return true;
			}
		} else {
			return false;
		}
	}

	private int recalculateDepth(int index, int depth) {
		final int childDepth = depth + 1;
		if (childrenList.get(index).isEmpty()) {
			return childDepth;
		} else {
			for (final Integer i : childrenList.get(index)) {
				depth = Math.max(depth, recalculateDepth(i, childDepth));
			}
		}
		return depth;
	}

	@Override
	public String toString() {
		return getCurrentList().toString();
	}

	@Override
	public int hashCode() {
		return getCurrentList().hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof ExpressionTree) {
			try {
				return new TreeHelper().isEqual((ExpressionTree<E>) o, this, ((ExpressionTree<E>) o).root(), root());
			} catch (final NodeNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Default visibility for unit testing
	 *
	 * @return
	 */
	ArrayList<E> getNodeList() {
		return nodeList;
	}
}