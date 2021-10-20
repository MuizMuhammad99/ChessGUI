package engine.piece;

import java.util.ArrayList;

import engine.board.Board;
import engine.board.Position;
import engine.move.AttackMove;
import engine.move.Move;


/**
 * Defines the Knight piece in chess
 *
 */
public class Knight extends Piece {

	public Knight(Position position, Alliance alliance) {
		super(position, alliance);

	}

	@Override
	public ArrayList<Move> getPossibleMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();

		Position offsets[] = { new Position(2, -1), new Position(1, -2), new Position(2, 1), new Position(1, 2),
				new Position(-2, -1), new Position(-1, -2), new Position(-2, 1), new Position(-1, 2) };

		for (Position offset : offsets) {
			Position current = new Position(position.row, position.col);
			current.add(offset);

			if (board.isValidTile(current)
					&& (board.isEmpty(current) || board.getPiece(current).getAlliance() != alliance)) {
				// attack move
				if (!board.isEmpty(current) && board.getPiece(current).getAlliance() != alliance)
					moves.add(new AttackMove(position, new Position(current), this));
				else// normal move
					moves.add(new Move(position, new Position(current), this));
			}
		}

		removeCheckMoves(moves, board);
		return moves;
	}

	@Override
	public String toString() {
		return "Knight";
	}

}
