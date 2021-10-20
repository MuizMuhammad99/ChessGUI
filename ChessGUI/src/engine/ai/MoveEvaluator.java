package engine.ai;

import java.util.ArrayList;

import engine.board.Board;
import engine.move.AttackMove;
import engine.move.CastlingMove;
import engine.move.Move;
import engine.move.PawnPromotionMove;
import engine.piece.Alliance;
import engine.piece.Bishop;
import engine.piece.King;
import engine.piece.Knight;
import engine.piece.Pawn;
import engine.piece.Piece;
import engine.piece.Queen;
import engine.piece.Rook;

/**
 * A static Move Evaluator for the chess game. It evaluates by material,
 * mobility, check, checkmate and move type
 *
 */
public class MoveEvaluator {

	/**
	 * Scores the move
	 * 
	 * @param move  move
	 * @param board board
	 * 
	 * @return the score
	 */
	public static int score(Move move, Board board) {
		Alliance alliance = move.getMovingPiece().getAlliance();
		Alliance opposing = alliance.getOpposing();

		int p1Value = getPiecesValue(board, alliance) + mobility(board, alliance) + check(board, alliance)
				+ checkmate(board, alliance);
		int p2Value = getPiecesValue(board, opposing) + mobility(board, opposing) + check(board, opposing)
				+ checkmate(board, opposing);


		return (p1Value + rateMove(move) + moveDanger(move,board) - p2Value);
	}
	
	private static int moveDanger(Move move,Board board) {
		ArrayList<Move> opponentMoves = board.getAllPossibleMoves(move.getMovingPiece().getAlliance().getOpposing());
		
		for(Move m : opponentMoves) 
			if(m instanceof AttackMove && m.getDestination().equals(move.getDestination()))
				return -50;
		
		
		return 0;
	}

	/**
	 * Rates the move based on its type
	 * 
	 * @param move move
	 * 
	 * @return a constant rating
	 */
	private static int rateMove(Move move) {
		if (move instanceof PawnPromotionMove)
			return 100;
		else if (move instanceof AttackMove) {
			return 50 + getPieceValue(((AttackMove) (move)).getAttackedPiece()) / 10;

		} else if (move instanceof CastlingMove)
			return 50;

		return 1;
	}

	/**
	 * 
	 * @param board    board
	 * @param alliance alliance
	 * 
	 * @return number of possible move available
	 */
	private static int mobility(Board board, Alliance alliance) {
		return board.getAllPossibleMoves(alliance).size();
	}

	/**
	 * Checks if opponent's king is in checkmate
	 * 
	 * @param board    board
	 * @param alliance alliance
	 * 
	 * @return score for checkmate
	 */
	private static int checkmate(Board board, Alliance alliance) {
		return board.isCheckMate(alliance.getOpposing()) ? Integer.MAX_VALUE : 0;
	}

	/**
	 * Checks if opponent's king is in check
	 * 
	 * @param board    board
	 * @param alliance alliance
	 * 
	 * @return score for check
	 */
	private static int check(Board board, Alliance alliance) {
		return board.isKingInCheck(alliance.getOpposing()) ? 100 : 0;
	}

	/**
	 * Calculates the sum of all pieces value
	 * 
	 * @param board    board
	 * @param alliance alliance
	 * 
	 * @return pieces value
	 */
	private static int getPiecesValue(Board board, Alliance alliance) {
		int piecesValue = 0;
		ArrayList<Piece> pieces = board.getActivePieces(alliance);

		for (Piece piece : pieces)
			piecesValue += getPieceValue(piece);

		return piecesValue;

	}

	/**
	 * 
	 * @param piece piece
	 * @return score value for the piece
	 */
	private static int getPieceValue(Piece piece) {
		if (piece instanceof Pawn)
			return 100;
		else if (piece instanceof Rook)
			return 200;
		else if (piece instanceof Knight)
			return 300;
		else if (piece instanceof Bishop)
			return 400;
		else if (piece instanceof Queen)
			return 500;
		else if (piece instanceof King)
			return 1000;

		return 0;
	}
}
