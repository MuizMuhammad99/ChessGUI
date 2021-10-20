package engine.piece;

/**
 * Alliance
 *
 */
public enum Alliance {

	WHITE, BLACK;

	public Alliance getOpposing() {
		return this == WHITE ? BLACK : WHITE;
	}

}
