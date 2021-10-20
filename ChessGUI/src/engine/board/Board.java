package engine.board;

import java.util.ArrayList;

import engine.move.Move;
import engine.piece.Alliance;
import engine.piece.Bishop;
import engine.piece.King;
import engine.piece.Knight;
import engine.piece.Pawn;
import engine.piece.Piece;
import engine.piece.Queen;
import engine.piece.Rook;

/**
 * Defines and creates the board of the game 
 *
 */
public class Board {

	public static final int SIZE = 8;
	public static final int WHITE_HOME_ROW = SIZE - 1;
	public static final int BLACK_HOME_ROW = 0;

	private final Tile[][] tiles;
	private King whiteKing, blackKing;

	/**
	 * Constructor
	 */
	public Board() {
		tiles = new Tile[SIZE][SIZE];
		initStandardBoard();
	}

	/**
	 * Moves a piece to the destination tile
	 * 
	 * @param p piece to move	
	 * @param destination	destination position
	 */
	public void movePiece(Piece p, Position destination) {
		Position src = p.getPosition();

		tiles[src.row][src.col].setPiece(null);
		tiles[destination.row][destination.col].setPiece(p);
		p.setPosition(destination);
	}

	/**
	 * 
	 * @param p	position
	 * 
	 * @return	true if tile position is valid
	 */
	public boolean isValidTile(Position p) {
		return p.row >= 0 && p.row < SIZE && p.col >= 0 && p.col < SIZE;
	}

	/**
	 * Checks if the move puts the king in a check
	 * 
	 * @param alliance	alliance
	 * @param move	move
	 * 
	 * @return	true if king is placed in check
	 */
	public boolean isCheckMoveFor(Alliance alliance, Move move) {
		boolean isCheckMove = false;

		move.execute(this);
		isCheckMove = isKingInCheck(alliance);
		move.undo(this);

		return isCheckMove;
	}

	/**
	 * Checks if the position is a home rank tile for the alliance
	 * 
	 * @param p	position
	 * @param alliance	alliance
	 * 
	 * @return	true if it is a home rank tile
	 */
	public boolean isHomeRankTile(Position p, Alliance alliance) {
		return alliance == Alliance.WHITE ? p.row == WHITE_HOME_ROW : p.row == BLACK_HOME_ROW;
	}

	public boolean isKingInCheck(Alliance alliance) {
		King king = alliance == whiteKing.getAlliance() ? whiteKing : blackKing;
		return king.isInCheck(this);

	}

	/**
	 * Checks if the alliance is in stalemate. Ann alliance is in
	 * stalemate if it has no legal moves.
	 * 
	 * @param alliance	alliance
	 * 
	 * @return	true if it is in stalemate
	 */
	public boolean isStalemate(Alliance alliance) {
		ArrayList<Piece> pieces = getActivePieces(alliance);

		for (Piece piece : pieces)
			if (piece.getPossibleMoves(this).size() != 0)
				return false;

		return true;
	}

	/**
	 * Checks if the alliance king is in checkmate
	 * 
	 * @param alliance	alliance
	 * 
	 * @return	true if it is checkmated
	 */
	public boolean isCheckMate(Alliance alliance) {
		King king = alliance == whiteKing.getAlliance() ? whiteKing : blackKing;
		return king.isInCheck(this) && isStalemate(alliance);
	}
	

	// private methods

	/**
	 * Initializes a standard board with black pieces on top.
	 */
	private void initStandardBoard() {
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				tiles[i][j] = new Tile();

		// black pieces
		blackKing = new King(new Position(0, 4), Alliance.BLACK);
		tiles[0][0].setPiece(new Rook(new Position(0, 0), Alliance.BLACK));
		tiles[0][1].setPiece(new Bishop(new Position(0, 1), Alliance.BLACK));
		tiles[0][2].setPiece(new Knight(new Position(0, 2), Alliance.BLACK));
		tiles[0][3].setPiece(new Queen(new Position(0, 3), Alliance.BLACK));
		tiles[0][4].setPiece(blackKing);
		tiles[0][5].setPiece(new Knight(new Position(0, 5), Alliance.BLACK));
		tiles[0][6].setPiece(new Bishop(new Position(0, 6), Alliance.BLACK));
		tiles[0][7].setPiece(new Rook(new Position(0, 7), Alliance.BLACK));
		for (int i = 0; i < SIZE; i++)
			tiles[1][i].setPiece(new Pawn(new Position(1, i), Alliance.BLACK));

		// white pieces
		whiteKing = new King(new Position(7, 4), Alliance.WHITE);
		tiles[7][0].setPiece(new Rook(new Position(7, 0), Alliance.WHITE));
		tiles[7][1].setPiece(new Bishop(new Position(7, 1), Alliance.WHITE));
		tiles[7][2].setPiece(new Knight(new Position(7, 2), Alliance.WHITE));
		tiles[7][3].setPiece(new Queen(new Position(7, 3), Alliance.WHITE));
		tiles[7][4].setPiece(whiteKing);
		tiles[7][5].setPiece(new Knight(new Position(7, 5), Alliance.WHITE));
		tiles[7][6].setPiece(new Bishop(new Position(7, 6), Alliance.WHITE));
		tiles[7][7].setPiece(new Rook(new Position(7, 7), Alliance.WHITE));
		for (int i = 0; i < SIZE; i++)
			tiles[6][i].setPiece(new Pawn(new Position(6, i), Alliance.WHITE));
	}

	// getters

	/**
	 * Gets all the active pieces for the alliance
	 * 
	 * @param alliance	alliance
	 * @return	all active pieces
	 */
	public ArrayList<Piece> getActivePieces(Alliance alliance) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();

		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++) {
				Position p = new Position(i, j);
				if (!isEmpty(p) && getPiece(p).getAlliance() == alliance)
					pieces.add(getPiece(p));
			}

		return pieces;
	}
	
	/**
	 * Gets all the possible moves for the alliance
	 * 
	 * @param alliance	alliance
	 * 
	 * @return	all possible moves
	 */
	public ArrayList<Move> getAllPossibleMoves(Alliance alliance){
		ArrayList<Move> allPossibleMoves = new ArrayList<Move>();
		ArrayList<Piece> allActivePieces = getActivePieces(alliance);
		
		for(Piece piece : allActivePieces)
			allPossibleMoves.addAll(piece.getPossibleMoves(this));
		
		return allPossibleMoves;
		
	}

	public Piece getPiece(Position position) {
		return tiles[position.row][position.col].getPiece();
	}

	public boolean isEmpty(Position position) {
		return getPiece(position) == null;
	}

	public Tile getTile(Position position) {
		return tiles[position.row][position.col];
	}

}
