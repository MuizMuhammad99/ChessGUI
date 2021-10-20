package engine;

import java.util.ArrayList;
import java.util.Stack;

import engine.board.Board;
import engine.move.Move;

/**
 *	An interface observer for the chess game 
 *
 */
public interface GameObserver {

	public void moveMade(Move move);
	public void updateBoard(Board board);
	public void notifyMoveStackUpdated(Stack<Move> moveStack);
	public void highlightPossibleMoves(ArrayList<Move> moves);
	public void clearHighlights();
	public void notifyGameEnded(int statusCode);
	public String promptPromotionType();
}
