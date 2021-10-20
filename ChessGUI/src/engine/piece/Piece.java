package engine.piece;

import java.util.ArrayList;

import engine.board.Board;
import engine.board.Position;
import engine.move.Move;

/**
 * Defines an abstract piece
 *
 */
public abstract class Piece {

	protected Position position;
	protected Alliance alliance;
	protected boolean hasMoved;

	/**
	 * Constructor
	 * @param position position
	 * @param alliance	alliance
	 */
	public Piece(Position position, Alliance alliance) {
		this.position = position;
		this.alliance = alliance;
	}

	/**
	 * 
	 * @param board	board
	 * @return	all the possible moves available on board
	 */
	public abstract ArrayList<Move> getPossibleMoves(Board board);

	/**
	 * Removes all the moves that puts the king in check
	 * 
	 * @param moves	moves
	 * @param board	board
	 */
	protected void removeCheckMoves(ArrayList<Move> moves, Board board) {
		for (int i = moves.size() - 1; i >= 0; i--)
			if (board.isCheckMoveFor(alliance, moves.get(i)))
				moves.remove(i);
	}
	
	/**
	 * is called when piece moves
	 */
	public void moved() {
		hasMoved = true;
	}
	
	//getter and setter
	
	public boolean hasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean isFirstMove) {
		this.hasMoved = isFirstMove;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Alliance getAlliance() {
		return alliance;
	}

	public void setAlliance(Alliance alliance) {
		this.alliance = alliance;
	}

}
