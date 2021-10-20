package engine.move;

import engine.board.Board;
import engine.board.Position;
import engine.piece.Piece;

/**
 * Defines an attack move
 *
 */
public class AttackMove extends Move{

	private Piece attackedPiece;
	
	public AttackMove(Position source, Position destination, Piece movingPiece) {
		super(source, destination, movingPiece);
	}

	@Override
	public void execute(Board board) {
		attackedPiece = board.getPiece(getDestination());
		super.execute(board);
	}

	@Override
	public void undo(Board board) {
		super.undo(board);
		board.getTile(getDestination()).setPiece(getAttackedPiece());
	}

	public Piece getAttackedPiece() {
		return attackedPiece;
	}
	
	

}
