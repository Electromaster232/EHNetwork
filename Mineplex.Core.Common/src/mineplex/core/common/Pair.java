package mineplex.core.common;

import java.io.Serializable;

public class Pair<L, R> implements Serializable {

	private static final long serialVersionUID = -7631968541502978704L;

	private L left;
	private R right;

	public static <L, R> Pair<L, R> create(L left, R right)
	{
		return new Pair<L, R>(left, right);
	}

	private Pair(L left, R right) {
		this.setLeft(left);
		this.setRight(right);
	}

	public L getLeft()
	{
		return left;
	}

	public void setLeft(L left)
	{
		this.left = left;
	}

	public R getRight()
	{
		return right;
	}

	public void setRight(R right)
	{
		this.right = right;
	}

	@Override
	public String toString()
	{
		return getLeft().toString() + ":" + getRight().toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Pair))
			return false;

		Pair localPair = (Pair) obj;

		if (getLeft() != null ? !getLeft().equals(localPair.getLeft()) : localPair.getLeft() != null)
			return false;
		if (getRight() != null ? !getRight().equals(localPair.getRight()) : localPair.getRight() != null)
			return false;
		return true;
	}
}