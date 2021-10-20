package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

import engine.board.Board;
import engine.board.Position;
import engine.move.AttackMove;
import engine.move.CastlingMove;
import engine.move.Move;
import engine.move.PawnPromotionMove;
import engine.piece.Bishop;
import engine.piece.King;
import engine.piece.Knight;
import engine.piece.Piece;
import engine.piece.Queen;
import engine.piece.Rook;

/**
 *	ChessIO Class
 *	Handles the saving and loading of the chess game.
 *	
 *	It saves the chess game by saving all the moves from the move stack and
 *	loads the game by executing each move loaded from the .sav file
 *
 */
public class ChessIO {

	/**
	 * Loads a chess game from the save file.
	 * 
	 * @param filePath	path of save file
	 * @param o	game observer
	 * 
	 * @return	loaded chess game
	 */
	public static Chess loadChess(String filePath, GameObserver o) {
		Chess chess = new Chess(o);
		Stack<Move> moveStack = chess.getMoveStack();
		Board board = chess.getBoard();

		try (Scanner scan = new Scanner(new File(filePath))) {

			while (scan.hasNext()) {
				String moveStr = scan.next();
				Move move = null;
				int srow, scol;
				int drow, dcol;
				Position src;
				Position des;

				switch (moveStr) {
				case "Move":
					srow = Integer.parseInt(scan.next());
					scol = Integer.parseInt(scan.next());
					drow = Integer.parseInt(scan.next());
					dcol = Integer.parseInt(scan.next());
					src = new Position(srow, scol);
					des = new Position(drow, dcol);

					move = new Move(src, des, board.getPiece(src));
					break;

				case "AttackMove":
					srow = Integer.parseInt(scan.next());
					scol = Integer.parseInt(scan.next());
					drow = Integer.parseInt(scan.next());
					dcol = Integer.parseInt(scan.next());
					src = new Position(srow, scol);
					des = new Position(drow, dcol);

					move = new AttackMove(src, des, board.getPiece(src));
					break;

				case "CastlingMove":
					// king
					srow = Integer.parseInt(scan.next());
					scol = Integer.parseInt(scan.next());
					drow = Integer.parseInt(scan.next());
					dcol = Integer.parseInt(scan.next());
					src = new Position(srow, scol);
					des = new Position(drow, dcol);

					// rook
					srow = Integer.parseInt(scan.next());
					scol = Integer.parseInt(scan.next());
					drow = Integer.parseInt(scan.next());
					dcol = Integer.parseInt(scan.next());
					Position src2 = new Position(srow, scol);
					Position des2 = new Position(drow, dcol);

					move = new CastlingMove(src, des, src2, des2, (King) board.getPiece(src),
							(Rook) board.getPiece(src2));
					break;

				case "PawnPromotionMove":
					srow = Integer.parseInt(scan.next());
					scol = Integer.parseInt(scan.next());
					drow = Integer.parseInt(scan.next());
					dcol = Integer.parseInt(scan.next());
					src = new Position(srow, scol);
					des = new Position(drow, dcol);

					// promotion move
					String promotionPieceType = scan.next().toLowerCase();
					Piece piece = null;
					switch (promotionPieceType) {
					case "queen":
						piece = new Queen(des, board.getPiece(src).getAlliance());
						break;
					case "rook":
						piece = new Rook(des, board.getPiece(src).getAlliance());
						break;
					case "bishop":
						piece = new Bishop(des, board.getPiece(src).getAlliance());
						break;
					case "knight":
						piece = new Knight(des, board.getPiece(src).getAlliance());
						break;
					}

					Move m = new Move(src, des, board.getPiece(src));
					PawnPromotionMove ppm = new PawnPromotionMove(m);
					ppm.setPieceToPromoteTo(piece);

					move = ppm;
					break;
				}

				move.execute(board);
				moveStack.add(move);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return chess;
	}

	/**
	 * Saves the game to a .sav file
	 * 
	 * @param chess	chess
	 * @param path	folder to save to
	 * @param name	name of sav file
	 */
	public static void save(Chess chess, String path, String name) {

		// create a new file in the folder
		// and then rename it to 'file' with name 'name'
		File folder = new File(path);
		File newFile = null;
		try {
			newFile = File.createTempFile("temp", ".sav", folder);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File file = new File(folder.getPath() + "/" + name + ".sav");
		newFile.renameTo(file);
		newFile.delete();

		// print data in save file
		try (PrintWriter p = new PrintWriter(new FileWriter(file.getPath()))) {
			for (int i = 0; i < chess.getMoveStack().size(); i++) {
				Move move = chess.getMoveStack().get(i);

				if (move instanceof AttackMove) {
					p.println("AttackMove " + move.getSource().row + " " + move.getSource().col + " "
							+ move.getDestination().row + " " + move.getDestination().col + " ");

				} else if (move instanceof CastlingMove) {
					CastlingMove cm = (CastlingMove) move;

					p.println("CastlingMove " + move.getSource().row + " " + move.getSource().col
							+ move.getDestination().row + " " + move.getDestination().col + " "
							+ cm.getRookMove().getSource().row + " " + cm.getRookMove().getSource().col
							+ cm.getRookMove().getDestination().row + " " + cm.getRookMove().getDestination().col);

				} else if (move instanceof PawnPromotionMove) {
					PawnPromotionMove ppm = (PawnPromotionMove) move;
					p.println("PawnPromotionMove " + move.getSource().row + " " + move.getSource().col + " "
							+ move.getDestination().row + " " + move.getDestination().col + " "
							+ ppm.getPieceToPromoteTo());
				} else if (move instanceof Move) {
					p.println("Move " + move.getSource().row + " " + move.getSource().col + " "
							+ move.getDestination().row + " " + move.getDestination().col + " ");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
