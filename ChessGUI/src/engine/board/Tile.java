package engine.board;

import engine.piece.Piece;

/**
 * A single tile which can hold a piece
 *
 */
public class Tile {

	private Piece piece;
	
	public Tile() {
		
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}
}
