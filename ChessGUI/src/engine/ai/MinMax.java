package engine.ai;

import java.util.ArrayList;

import engine.Player;
import engine.board.Board;
import engine.move.Move;

/**
 * An implementation of the MinMax AI with Alpha-beta pruning
 *
 */
public class MinMax {

	private Player nodePlayer;// the player that called the minmax

	/**
	 * Gets the best move for the player
	 * 
	 * @param board         board
	 * @param currentPlayer current player
	 * @param depth         depth
	 * 
	 * @return the best maximized move
	 */
	public Move getBestMove(Board board, Player currentPlayer, int depth) {
		this.nodePlayer = currentPlayer;
		Move bestMove = null;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int alpha = min;
		int beta = max;

		ArrayList<Move> allPossibleMoves = board.getAllPossibleMoves(nodePlayer.getAlliance());
		for (Move move : allPossibleMoves) {
			move.execute(board);
			int eval = minmax(move, board, alpha, beta, depth - 1, false);
			move.undo(board);

			if (eval > max)
				bestMove = move;
			max = Math.max(max, eval);
		}
		return bestMove;
	}

	/**
	 * Algorithm for the min max ai with alpha beta pruning.
	 * 
	 * 
	 * @param m                move
	 * @param board            board
	 * @param alpha            alpha
	 * @param beta             beta
	 * @param depth            depth
	 * @param maximizingPlayer is maximizing player turn
	 * 
	 * @return minmax value
	 */
	private int minmax(Move m, Board board, int alpha, int beta, int depth, boolean maximizingPlayer) {
		if (depth == 0 || board.isCheckMate(m.getMovingPiece().getAlliance())) {
			int score = MoveEvaluator.score(m, board);
			return score * (maximizingPlayer ? -1 : 1);

		}

		if (maximizingPlayer) {
			ArrayList<Move> moves = board.getAllPossibleMoves(nodePlayer.getAlliance());
			int maxEval = Integer.MIN_VALUE;
			for (Move move : moves) {
				move.execute(board);
				int eval = minmax(move, board, alpha, beta, depth - 1, false);
				move.undo(board);
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if (beta <= alpha)
					break;
			}

			return maxEval;

		} else {
			ArrayList<Move> moves = board.getAllPossibleMoves(nodePlayer.getAlliance().getOpposing());
			int minEval = Integer.MAX_VALUE;
			for (Move move : moves) {
				move.execute(board);
				int eval = minmax(move, board, alpha, beta, depth - 1, true);
				move.undo(board);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if (beta <= alpha)
					break;
			}

			return minEval;

		}

	}

}
