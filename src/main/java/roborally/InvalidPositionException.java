package roborally;

import be.kuleuven.cs.som.annotate.*;

public class InvalidPositionException extends Exception {

	public InvalidPositionException(Vector position) {
		this.position = position;
	}

	public InvalidPositionException(long x, long y) {
		this(new Vector(x, y));
	}

	@Basic
	@Immutable
	public Vector getPosition() {
		return position;
	}

	@Immutable
	public long getX() {
		if(getPosition() == null)
			return 0;
		return getPosition().getX();
	}
	
	@Immutable
	public long getY() {
		if(getPosition() == null)
			return 0;
		return getPosition().getY();
	}

	final Vector position;

	private static final long serialVersionUID = 1L;

}
