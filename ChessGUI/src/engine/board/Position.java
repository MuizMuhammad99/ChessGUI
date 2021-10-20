package engine.board;

/**
 * Defines a position coordinate on board
 *
 */
public class Position {

	private static final String[] COLUMN_ALPHABETS = { "a", "b", "c", "d", "e", "f", "g", "e" };

	public int row;
	public int col;

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public Position(Position position) {
		this(position.row, position.col);
	}

	public void add(Position position) {
		add(position.row, position.col);
	}

	public void subtract(Position position) {
		add(-position.row, -position.col);
	}

	public void add(int dr, int dc) {
		row += dr;
		col += dc;
	}

	@Override
	public boolean equals(Object o) {
		Position other = (Position) o;
		return row == other.row && col == other.col;
	}

	@Override
	public String toString() {
		return COLUMN_ALPHABETS[col].toUpperCase() + (Board.SIZE - row);
	}
}
