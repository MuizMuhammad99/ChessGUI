package engine;

import java.util.ArrayList;
import java.util.Stack;

import engine.move.Move;

/**
 *	An interface observer for the chess game 
 *
 */
public interface GameObserver {

	void moveMade(Move move);

    void notifyMoveStackUpdated(Stack<Move> moveStack);
	void highlightPossibleMoves(ArrayList<Move> moves);
	void clearHighlights();
	void notifyGameEnded(int statusCode);
	String promptPromotionType();
}
