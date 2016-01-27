package es.uam.eps.tfg.expressions.types;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Node structure for a N-ary tree
 *
 * @author Rodrigo de Blas
 *
 * @param <T>
 *            data to store in the node
 */
public class NAryTreeNode<T> {
	private T data;
	private List<NAryTreeNode<T>> children;
	private NAryTreeNode<T> parent;

	public NAryTreeNode(T data) {
		this.data = data;
		children = new ArrayList<>();
		parent = null;
	}

	public void addChild(NAryTreeNode<T> child) {
		child.setParent(this);
		children.add(child);
	}

	public void addChild(int index, NAryTreeNode<T> child) {
		child.setParent(this);
		children.add(index, child);
	}

	public void removeChildren() {
		children.clear();
	}

	public NAryTreeNode<T> removeChildren(int index) {
		return children.remove(index);
	}

	public int indexOfChild(NAryTreeNode<T> child) {
		return children.indexOf(child);
	}

	public NAryTreeNode<T> getChildAt(int index) {
		return children.get(index);
	}

	public boolean contains(NAryTreeNode<T> child) {
		return children.contains(child);
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<NAryTreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<NAryTreeNode<T>> children) {
		this.children = children;
	}

	public NAryTreeNode<T> getParent() {
		return parent;
	}

	public void setParent(NAryTreeNode<T> parent) {
		this.parent = parent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NAryTreeNode<?>)) {
			return false;
		}
		final NAryTreeNode<?> other = (NAryTreeNode<?>) o;

		return new EqualsBuilder().append(this.data, other.getData()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.data).append(this.parent).append(this.children).hashCode();
	}
}
