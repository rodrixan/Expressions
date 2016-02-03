package es.uam.eps.expressions.types;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import es.uam.eps.expressions.exceptions.LevelErrorException;
import es.uam.eps.expressions.exceptions.PathErrorException;

public class Node<T> {
	private int level;
	private int[] path;
	private T data;

	public Node(int level, int[] path, T data) {
		checkLevel(level);
		checkPath(level, path);
		this.level = level;
		this.path = path;
		this.data = data;
	}

	public Node(T data) {
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.data).append(this.level).append(this.path).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Node<?>)) {
			return false;
		}
		final Node<?> other = (Node<?>) obj;

		return new EqualsBuilder().append(this.data, other.getData()).append(this.level, other.getLevel())
				.append(this.path, other.getPath()).isEquals();
	}

}
