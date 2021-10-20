package engine.piece;

import java.util.ArrayList;

import engine.board.Board;
import engine.board.Position;
import engine.move.AttackMove;
import engine.move.Move;

/**
 * Defines the Bishop piece in chess
 *
 */
public class Bishop extends Piece {

	public Bishop(Position position, Alliance alliance) {
		super(position, alliance);

	}

	@Override
	public ArrayList<Move> getPossibleMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();

		// bishop moves in 4 diagonal directions
		Position directions[] = { new Position(1, 1), new Position(1, -1), new Position(-1, 1), new Position(-1, -1) };

		// for each direction
		// check if the destination tile is valid and can move there
		for (Position dir : directions) {
			Position current = new Position(position.row, position.col);
			current.add(dir);

			while (board.isValidTile(current)
					&& (board.isEmpty(current) || board.getPiece(current).getAlliance() != alliance)) {
				
				if (!board.isEmpty(current) && board.getPiece(current).getAlliance() != alliance)
					moves.add(new AttackMove(position, new Position(current), this));
				else
					moves.add(new Move(position, new Position(current), this));

				if (!board.isEmpty(current))
					break;

				current.add(dir);
			}
		}

		removeCheckMoves(moves, board);
		return moves;
	}

	@Override
	public String toString() {
		return "Bishop";
	}

}
