package es.uam.eps.expressions.exceptions;

public class PathErrorException extends RuntimeException {
	private static final long serialVersionUID = 1048210600426129983L;

	public PathErrorException(String msg) {
		super(msg);
	}
}
