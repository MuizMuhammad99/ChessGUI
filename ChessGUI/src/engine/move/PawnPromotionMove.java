package engine.move;

import engine.board.Board;
import engine.piece.Piece;
import engine.piece.Queen;

/**
 * Defines the pawn promotion move
 *
 */
public class PawnPromotionMove extends Move{

	private Piece pieceToPromoteTo;
	private final Move move;
	
	public PawnPromotionMove(Move move) {
		super(move.getSource(),move.getDestination(),move.getMovingPiece());
		this.move = move;
		pieceToPromoteTo = new Queen(move.getDestination(),move.getMovingPiece().getAlliance());
	}

	@Override
	public void execute(Board board) {
		move.execute(board);
		board.getTile(move.getDestination()).setPiece(pieceToPromoteTo);
	}

	@Override
	public void undo(Board board) {
		move.undo(board);
	}

	public void setPieceToPromoteTo(Piece pieceToPromoteTo) {
		this.pieceToPromoteTo = pieceToPromoteTo;
	}
	
	public Piece getPieceToPromoteTo() {
		return pieceToPromoteTo;
	}
	

}
