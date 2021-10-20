package engine.piece;

import java.util.ArrayList;

import engine.board.Board;
import engine.board.Position;
import engine.move.AttackMove;
import engine.move.Move;


/**
 * Defines the Queen piece in chess
 *
 */
public class Queen extends Piece {

	public Queen(Position position, Alliance alliance) {
		super(position, alliance);

	}

	@Override
	public ArrayList<Move> getPossibleMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();

		// queen moves in 4 diagonal and 4 straight directions
		Position directions[] = { new Position(1, 1), new Position(1, -1), new Position(-1, 1), new Position(-1, -1),
				new Position(0, -1), new Position(0, 1), new Position(-1, 0), new Position(1, 0) };

		// for each direction
		// check if the destination tile is valid and can move there
		for (Position dir : directions) {
			Position current = new Position(position.row, position.col);
			current.add(dir);

			while (board.isValidTile(current)
					&& (board.isEmpty(current) || board.getPiece(current).getAlliance() != alliance)) {
				// attack move
				if (!board.isEmpty(current) && board.getPiece(current).getAlliance() != alliance)
					moves.add(new AttackMove(position, new Position(current), this));
				else// normal move
					moves.add(new Move(position, new Position(current), this));

				// if the destination tile has piece,stop there
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
		return "Queen";
	}

}
