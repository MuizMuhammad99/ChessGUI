package engine.move;

import engine.board.Board;
import engine.board.Position;
import engine.piece.King;
import engine.piece.Rook;

/**
 * Defies the castling move
 *
 */
public class CastlingMove extends Move{

	private final Move rookMove;
	
	public CastlingMove(Position kingSource, Position kingDestination,
			Position rookSource, Position rookDestination, King king,Rook rook) {
		super(kingSource, kingDestination, king);
		rookMove = new Move(rookSource,rookDestination,rook);
	}

	@Override
	public void execute(Board board) {
		super.execute(board);
		rookMove.execute(board);
	}

	@Override
	public void undo(Board board) {
		super.undo(board);
		rookMove.execute(board);
	}

	public Move getRookMove() {
		return rookMove;
	}

	
	
	

}
