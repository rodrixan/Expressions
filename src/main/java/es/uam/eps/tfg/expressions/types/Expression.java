package es.uam.eps.tfg.expressions.types;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Expression<T> {

	private int level;
	private List<Integer> position;
	private T data;

	public Expression(T data, int level) {
		this.data = data;
		this.level = level;
		this.position = new ArrayList<Integer>(level);
	}

	public long getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<Integer> getPosition() {
		return position;
	}

	public void setPosition(List<Integer> position) {
		this.position = position;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isOnTheSameLevel(Expression<T> node) {
		return (this.level == node.getLevel());
	}

	public boolean isOnTheSameExpression(Expression<T> node) {
		if (isOnTheSameLevel(node)) {
			return checkPathOnSameLevel(node);
		}
		return false;
	}

	private boolean checkPathOnSameLevel(Expression<T> node) {
		// only have to check the level-1 first positions
		for (int i = 0; i < level - 1; i++) {
			if (position.get(i) != node.getPosition().get(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.data).append(this.level).append(this.position).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Expression<?>)) {
			return false;
		}
		final Expression<?> other = (Expression<?>) obj;

		return new EqualsBuilder().append(this.data, other.getData()).append(this.level, other.getLevel())
				.append(this.position, other.getPosition()).isEquals();
	}

}
