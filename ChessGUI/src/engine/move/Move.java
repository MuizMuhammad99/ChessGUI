package engine.move;

import engine.board.Board;
import engine.board.Position;
import engine.piece.Piece;

/**
 * Defines a base type move
 * In base move, the piece is just move from source to destination
 */
public class Move {

	private final Position source;
	private final Position destination;
	private Piece movingPiece;
	private boolean hasMoved;

	public Move(Position source, Position destination, Piece movingPiece) {
		this.source = source;
		this.destination = destination;
		this.movingPiece = movingPiece;
	}

	public void execute(Board board) {
		hasMoved = movingPiece.hasMoved();
		board.movePiece(movingPiece, destination);
		movingPiece.moved();
	}

	public void undo(Board board) {
		board.movePiece(movingPiece, source);
		movingPiece.setHasMoved(hasMoved);

	}

	@Override
	public String toString() {
		return source + "  to  " + destination;
	}
	
	//getters
	
	public Position getSource() {
		return source;
	}

	public Position getDestination() {
		return destination;
	}

	public Piece getMovingPiece() {
		return movingPiece;
	}

	public void setMovingPiece(Piece movingPiece) {
		this.movingPiece = movingPiece;
	}

}
