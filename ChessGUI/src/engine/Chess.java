package engine;

import java.util.ArrayList;
import java.util.Stack;

import engine.ai.MinMax;
import engine.board.Board;
import engine.board.Position;
import engine.move.Move;
import engine.move.PawnPromotionMove;
import engine.piece.Alliance;
import engine.piece.Bishop;
import engine.piece.Knight;
import engine.piece.Piece;
import engine.piece.Queen;
import engine.piece.Rook;

/**
 * Chess Game The main game class for the chess game. It handles the logic of
 * game and notifies the observer of game also.
 *
 */
public class Chess {

	// game end status
	public static final int WHITE_CHECKMATE = 1;
	public static final int BLACK_CHECKMATE = 2;
	public static final int WHITE_STALEMATE = 3;
	public static final int BLACK_STALEMATE = 4;

	private final GameObserver observer;
	private final Player whitePlayer;
	private final Player blackPlayer;
	private Player currentPlayer;
	private final Board board;
	private Piece selectedPiece;
	private ArrayList<Move> possibleMoves;// selected piece's possible moves
	private final Stack<Move> moveStack;
	private final Stack<Move> redoStack;
	private final MinMax minmax = new MinMax();

	/**
	 * Constructor
	 * 
	 * @param observer game observer
	 */
	public Chess(GameObserver observer) {
		this.observer = observer;
		board = new Board();

		whitePlayer = new Player(Alliance.WHITE);
		blackPlayer = new Player(Alliance.BLACK);
		currentPlayer = whitePlayer;

		moveStack = new Stack<Move>();
		redoStack = new Stack<Move>();

	}

	/**
	 * This function is called whenever a move is made. A move is made by first
	 * selecting a piece on board. and then a possible destination tile for the
	 * piece is selected.
	 * 
	 * @param position selected position
	 */
	public void makeMove(Position position) {

		// if checkmate return
		if (board.isCheckMate(currentPlayer.getAlliance())) {
			observer.notifyGameEnded(currentPlayer.getAlliance() == Alliance.WHITE ? WHITE_CHECKMATE : BLACK_CHECKMATE);
			return;
		}

		// if stalemate,return
		if (board.isStalemate(currentPlayer.getAlliance())) {
			observer.notifyGameEnded(currentPlayer.getAlliance() == Alliance.WHITE ? WHITE_STALEMATE : BLACK_STALEMATE);
			return;
		}

		// select piece
		if (!isPieceSelected()) {

			if (!board.isEmpty(position) && board.getPiece(position).getAlliance() == currentPlayer.getAlliance()) {
				selectPiece(board.getPiece(position));

				observer.clearHighlights();
				observer.highlightPossibleMoves(possibleMoves);
			}
			return;
		}

		// check if destination position is a valid move
		// if possible move, then move piece
		// and change player turn
		for (Move move : possibleMoves)
			if (move.getDestination().equals(position)) {

				//if pawn promotion move
				if (move instanceof PawnPromotionMove) {
					
					//prompt player for input piece type
					String promotionPieceType = observer.promptPromotionType().toLowerCase();
					Piece piece = null;

					//create piece
					switch (promotionPieceType) {
					case "queen":
						piece = new Queen(move.getDestination(), move.getMovingPiece().getAlliance());
						break;
					case "bishop":
						piece = new Bishop(move.getDestination(), move.getMovingPiece().getAlliance());
						break;
					case "rook":
						piece = new Rook(move.getDestination(), move.getMovingPiece().getAlliance());
						break;
					case "knight":
						piece = new Knight(move.getDestination(), move.getMovingPiece().getAlliance());
						break;

					}

					//set piece
					((PawnPromotionMove) move).setMovingPiece(piece);

					
					//execute move
					move.execute(board);
				} else
					move.execute(board);
				deselectPiece();
				changePlayerTurn();
				moveStack.add(move);

				observer.clearHighlights();
				observer.moveMade(move);
				observer.notifyMoveStackUpdated(moveStack);

				if (board.isCheckMate(currentPlayer.getAlliance())) {
					observer.notifyGameEnded(
							currentPlayer.getAlliance() == Alliance.WHITE ? WHITE_CHECKMATE : BLACK_CHECKMATE);
				}

				return;
			}

		// if not possible move,then
		deselectPiece();
		observer.clearHighlights();
	}

	/**
	 * Uses the Alpha-beta AI to make move for the Black player
	 */
	public void makeAIMove() {
		if (currentPlayer.getAlliance() == Alliance.WHITE)
			return;

		if (board.isCheckMate(currentPlayer.getAlliance())) {
			observer.notifyGameEnded(currentPlayer.getAlliance() == Alliance.WHITE ? WHITE_CHECKMATE : BLACK_CHECKMATE);
			return;
		}

		if (board.isStalemate(currentPlayer.getAlliance())) {
			observer.notifyGameEnded(currentPlayer.getAlliance() == Alliance.WHITE ? WHITE_STALEMATE : BLACK_STALEMATE);
			return;
		}

		if (currentPlayer.getAlliance() == Alliance.BLACK) {
			Move bestMove = getMinmax().getBestMove(board, currentPlayer, 20);

			bestMove.execute(board);
			deselectPiece();
			changePlayerTurn();
			moveStack.add(bestMove);

			observer.clearHighlights();
			observer.moveMade(bestMove);
			observer.notifyMoveStackUpdated(moveStack);
			return;
		}
	}

	/**
	 * Undo the last move
	 * 
	 * @return true if undo was done
	 */
	public boolean undo() {
		if (moveStack.isEmpty())
			return false;

		Move move = moveStack.pop();
		move.undo(board);
		redoStack.add(move);
		changePlayerTurn();

		observer.moveMade(new Move(move.getDestination(), move.getSource(), move.getMovingPiece()));
		observer.notifyMoveStackUpdated(moveStack);

		return true;
	}

	/**
	 * redo the last move
	 * 
	 * @return true if redo was done
	 */
	public boolean redo() {
		if (redoStack.isEmpty())
			return false;

		Move move = redoStack.pop();
		move.execute(board);
		moveStack.add(move);
		changePlayerTurn();

		observer.moveMade(move);
		observer.notifyMoveStackUpdated(moveStack);

		return true;
	}

	/**
	 * de selects the piece
	 */
	public void deselectPiece() {
		selectedPiece = null;
	}

	/**
	 * selected the piece and stores the possible moves
	 * 
	 * @param piece piece to select
	 */
	public void selectPiece(Piece piece) {
		selectedPiece = piece;
		possibleMoves = selectedPiece.getPossibleMoves(board);
	}

	/**
	 * Change player turn
	 */
	private void changePlayerTurn() {
		currentPlayer = currentPlayer == whitePlayer ? blackPlayer : whitePlayer;
	}

	/**
	 * Checks if piece is selected or not
	 * 
	 * @return true if piece is selected
	 */
	private boolean isPieceSelected() {
		return selectedPiece != null;
	}

	// getters

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Board getBoard() {
		return board;
	}

	public Stack<Move> getMoveStack() {
		return moveStack;
	}

	public MinMax getMinmax() {
		return minmax;
	}
}
