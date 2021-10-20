package engine.piece;

import java.util.ArrayList;

import engine.board.Board;
import engine.board.Position;
import engine.move.AttackMove;
import engine.move.CastlingMove;
import engine.move.Move;

/**
 * Defines the King piece in chess
 *
 */
public class King extends Piece {

	public King(Position position, Alliance alliance) {
		super(position, alliance);

	}

	@Override
	public ArrayList<Move> getPossibleMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();

		Position offsets[] = { new Position(1, 1), new Position(1, -1), new Position(-1, 1), new Position(-1, -1),
				new Position(0, -1), new Position(0, 1), new Position(-1, 0), new Position(1, 0) };

		for (Position offset : offsets) {
			Position current = new Position(position.row, position.col);
			current.add(offset);

			if (board.isValidTile(current)
					&& (board.isEmpty(current) || board.getPiece(current).getAlliance() != alliance)
					&& !isCheckPosition(current, board)) {
				// attack move
				if (!board.isEmpty(current) && board.getPiece(current).getAlliance() != alliance)
					moves.add(new AttackMove(position, new Position(current), this));
				else// normal move
					moves.add(new Move(position, new Position(current), this));

			}
		}

		// check if king has castling moves
		if (!hasMoved && board.isHomeRankTile(position, alliance)) {

			Position current = new Position(position);
			Position[] directions = { new Position(0, 1), new Position(0, -1) };// left & right castling direction
			for (Position direction : directions) {
				boolean noPiecesBetween = true;
				boolean isCastleAtCorner = false;
				boolean isCastleMoved = false;

				// check if any pieces between king and rook
				current.add(direction);
				while (board.isValidTile(current)) {
					if (!board.isEmpty(current) && !(board.getPiece(current) instanceof Rook)) {
						noPiecesBetween = false;
						break;
					}
					current.add(direction);
				}

				// check if last piece in the direction is a rook piece
				current.subtract(direction);
				if (noPiecesBetween && !board.isEmpty(current) && board.getPiece(current) instanceof Rook
						&& board.getPiece(current).getAlliance() == alliance)
					isCastleAtCorner = true;
				else
					continue;

				// check if rook is previously moved
				isCastleMoved = board.getPiece(current).hasMoved();

				// check if conditions for castling is cleared
				boolean canCastle = noPiecesBetween && isCastleAtCorner && !isCastleMoved;

				if (canCastle) {
					King king = this;
					Rook rook = (Rook) board.getPiece(current);
					Position kingDestination = new Position(position.row, position.col + direction.col * 2);
					Position rookDestination = new Position(position.row, position.col + direction.col);

					moves.add(new CastlingMove(position, kingDestination, rook.getPosition(), rookDestination, king,
							rook));
				}
			}
		}

		return moves;
	}

	/**
	 * @param board	board
	 * @return	true if king is in check
	 */
	public boolean isInCheck(Board board) {
		return isCheckPosition(position, board);
	}

	/**
	 * 
	 * @param position	position
	 * @param board	board
	 * @return	true if the position puts king in check
	 */
	private boolean isCheckPosition(Position position, Board board) {

		// pawn check
		int direction = alliance == Alliance.BLACK ? 1 : -1;// 1 for down direction, -1 for up direction
		Position pawnAttackPositions[] = { new Position(position.row + direction, position.col + 1),
				new Position(position.row + direction, position.col - 1) };
		for (Position p : pawnAttackPositions)
			if (board.isValidTile(p) && !board.isEmpty(p) && board.getPiece(p) instanceof Pawn
					&& board.getPiece(p).getAlliance() != alliance)
				return true;

		// straight enemy piece(rook,queen) attacks check
		Position straightDirections[] = { new Position(0, -1), new Position(0, 1), new Position(-1, 0),
				new Position(1, 0) };
		for (Position dir : straightDirections) {
			Position current = new Position(position.row, position.col);
			current.add(dir);

			// keep going in one direction until hit a piece
			while (board.isValidTile(current) && board.isEmpty(current))
				current.add(dir);

			// if there is a enemy piece(rook or queen),then its a check
			if (board.isValidTile(current)
					&& (board.getPiece(current) instanceof Rook || board.getPiece(current) instanceof Queen)
					&& board.getPiece(current).getAlliance() != alliance)
				return true;

		}

		// diagonal enemy piece(bishop,queen) attacks check
		Position diagonalDirections[] = { new Position(1, 1), new Position(1, -1), new Position(-1, 1),
				new Position(-1, -1) };
		for (Position dir : diagonalDirections) {
			Position current = new Position(position.row, position.col);
			current.add(dir);

			// keep going in one direction until hit a piece
			while (board.isValidTile(current) && board.isEmpty(current))
				current.add(dir);

			// if there is a enemy piece(rook or queen),then its a check
			if (board.isValidTile(current)
					&& (board.getPiece(current) instanceof Bishop || board.getPiece(current) instanceof Queen)
					&& board.getPiece(current).getAlliance() != alliance)
				return true;

		}

		// knight attack check
		Position knightOffsets[] = { new Position(2, -1), new Position(1, -2), new Position(2, 1), new Position(1, 2),
				new Position(-2, -1), new Position(-1, -2), new Position(-2, 1), new Position(-1, 2) };

		for (Position offset : knightOffsets) {
			Position current = new Position(position.row, position.col);
			current.add(offset);

			if (board.isValidTile(current) && !board.isEmpty(current) && board.getPiece(current) instanceof Knight
					&& board.getPiece(current).getAlliance() != alliance)
				return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "King";
	}

}
