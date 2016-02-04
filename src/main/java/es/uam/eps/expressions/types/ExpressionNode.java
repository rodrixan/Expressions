package es.uam.eps.expressions.types;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import es.uam.eps.expressions.exceptions.LevelErrorException;
import es.uam.eps.expressions.exceptions.PathErrorException;

public class ExpressionNode<T> {
	private int level;
	private int[] path;
	private T data;

	public ExpressionNode(int level, int[] path, T data) {
		checkLevel(level);
		checkPath(level, path);
		this.level = level;
		this.path = path;
		this.data = data;
	}

	public ExpressionNode(T data) {
		this.level = 0;
		this.path = new int[] { 0 };
		this.data = data;
	}

	private void checkLevel(int level) {
		if (level < 0) {
			throw new LevelErrorException("Level must be greater than or equal to 0");
		}
	}

	private void checkPath(int level, int[] path) {
		if (path.length != level) {
			throw new PathErrorException("Path must have a size given by level");
		}
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int[] getPath() {
		return path;
	}

	public void setPath(int[] path) {
		this.path = path;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isOnTheSamePath(ExpressionNode<T> node) {
		if (node.getLevel() != this.level) {
			return false;
		}

		for (int i = 0; i < level - 1; i++) {
			if (this.path[i] != node.getPath()[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.level).append(this.path).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return equals(obj, false);
	}

	/**
	 * Indicates whether some other object is "equal to" this one, choosing to
	 * compare or not the Data of the Node.
	 *
	 * @see equals
	 *
	 * @param obj
	 *            object to check
	 * @param compareData
	 *            flag which determines whether to check the data or not
	 * @return true if equals, false if not
	 */
	public boolean equals(Object obj, boolean compareData) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ExpressionNode<?>)) {
			return false;
		}
		final ExpressionNode<?> other = (ExpressionNode<?>) obj;

		return equalsBuilderGiven(other, compareData);
	}

	private boolean equalsBuilderGiven(ExpressionNode<?> other, boolean compareData) {

		final EqualsBuilder eb = new EqualsBuilder().append(this.level, other.getLevel()).append(this.path,
				other.getPath());

		if (compareData) {
			return eb.append(this.data, other.getData()).isEquals();
		} else {
			return eb.isEquals();
		}
	}
}
