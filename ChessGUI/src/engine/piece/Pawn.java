package engine.piece;

import java.util.ArrayList;

import engine.board.Board;
import engine.board.Position;
import engine.move.AttackMove;
import engine.move.Move;
import engine.move.PawnPromotionMove;

/**
 * Defines the Pawn piece in chess
 *
 */
public class Pawn extends Piece {

	public Pawn(Position position, Alliance alliance) {
		super(position, alliance);
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		int direction = alliance == Alliance.BLACK ? 1 : -1;// 1 for down direction, -1 for up direction

		// one row move
		Position p1 = new Position(position.row + direction, position.col);
		if (board.isValidTile(p1) && board.isEmpty(p1)) {
			moves.add(new Move(position, p1, this));

		}

		// two row move
		if (!hasMoved) {
			Position p2 = new Position(position.row + direction * 2, position.col);
			if (board.isEmpty(p1) && board.isEmpty(p2))
				moves.add(new Move(position, p2, this));
		}

		// attack moves
		Position a1 = new Position(position.row + direction, position.col + 1);
		Position a2 = new Position(position.row + direction, position.col - 1);

		if (board.isValidTile(a1) && !board.isEmpty(a1) && board.getPiece(a1).getAlliance() != alliance)
			moves.add(new AttackMove(position, a1, this));

		if (board.isValidTile(a2) && !board.isEmpty(a2) && board.getPiece(a2).getAlliance() != alliance)
			moves.add(new AttackMove(position, a2, this));

		//check if any of the moves lead to pawn promotion
		for (int i = moves.size() - 1; i >= 0; i--)
			if (board.isHomeRankTile(moves.get(i).getDestination(), alliance.getOpposing())) {
				moves.set(i, new PawnPromotionMove(moves.get(i)));
			}

		removeCheckMoves(moves, board);
		return moves;
	}

	@Override
	public String toString() {
		return "Pawn";
	}

}
