package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import engine.Chess;
import engine.ChessIO;
import engine.GameObserver;
import engine.board.Board;
import engine.board.Position;
import engine.move.Move;
import engine.piece.Piece;
import java.awt.CardLayout;

/**
 * Game controller class It acts as a controller between the GUI and the logic
 * of the game.
 * 
 */
public class Game implements GameObserver {

	public static final int FRAME_WIDTH = Cell.CELL_SIZE * 11;
	public static final int FRAME_HEIGHT = Cell.CELL_SIZE * 8;

	private MenuPanel menuPanel;
	private JPanel gamePanel;
	private JPanel container;
	private CardLayout cardLayout;

	private BoardPanel boardPanel;
	private ControlPanel controlPanel;
	private MoveLogPanel moveLogPanel;
	private JPanel sidePanel;
	private JFrame frame;
	private Chess chess;

	private boolean undoMade;
	private boolean redoMade;

	public Game() {
		Assets.load();

		// board panel
		boardPanel = new BoardPanel();
		boardPanel.setClickListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (boardPanel.isAnimating())
					return;

				chess.makeMove((Position) e.getSource());
			}

		});
		boardPanel.setAnimListener(new BoardAnimationListener() {

			@Override
			public void animationDone() {
				if (undoMade) {
					undoMade = false;
					chess.undo();
					return;
				} else if (redoMade) {
					redoMade = false;
					chess.redo();
					return;
				} else {
					chess.makeAIMove();
				}

				if (!boardPanel.isAnimating())
					updateBoard(chess.getBoard());
			}
		});

		// control panel
		controlPanel = new ControlPanel();
		controlPanel.setActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (boardPanel.isAnimating())
					return;

				String cmd = e.getActionCommand();

				switch (cmd) {
				case "undo":
					clearHighlights();
					undoMade = chess.undo();
					break;
				case "redo":
					clearHighlights();
					redoMade = chess.redo();
					break;
				case "hint":
					hint();
					break;
				case "save":
					save();
					break;
				case "exit":
					exit();
					break;
				}
			}

		});

		// move log panel
		moveLogPanel = new MoveLogPanel();

		// menu panel
		menuPanel = new MenuPanel();
		menuPanel.setActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();

				switch (cmd) {
				case "new_game":
					newGame();
					break;
				case "load_game":
					loadGame();
					break;
				case "exit_game":
					System.exit(0);
					break;
				}
			}

		});

		// side panel container
		sidePanel = new JPanel(new BorderLayout());
		sidePanel.add(moveLogPanel, BorderLayout.CENTER);
		sidePanel.add(controlPanel, BorderLayout.SOUTH);

		// game panel container
		gamePanel = new JPanel();
		gamePanel.add(boardPanel, BorderLayout.CENTER);
		gamePanel.add(sidePanel, BorderLayout.EAST);

		// card layout
		cardLayout = new CardLayout();
		container = new JPanel(cardLayout);
		container.add(gamePanel, "game");
		container.add(menuPanel, "menu");

		cardLayout.show(container, "menu");

		// main frame
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.add(container);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * Updates the board panel
	 */
	@Override
	public void updateBoard(Board board) {
		for (int i = 0; i < Board.SIZE; i++)
			for (int j = 0; j < Board.SIZE; j++) {
				Position p = new Position(i, j);

				if (!board.isEmpty(p)) {
					Piece piece = board.getPiece(p);
					String imageKey = piece.getAlliance().toString().toLowerCase() + "_"
							+ piece.toString().toLowerCase();
					boardPanel.setPieceImage(Assets.getImage(imageKey), p);
				} else
					boardPanel.setPieceImage(null, p);
			}
		boardPanel.repaint();
	}

	/**
	 * Highlights all the possible moves
	 */
	@Override
	public void highlightPossibleMoves(ArrayList<Move> moves) {
		for (Move move : moves)
			boardPanel.highlightCell(move.getDestination());
		boardPanel.repaint();
	}

	/**
	 * Clears the highlights
	 */
	@Override
	public void clearHighlights() {
		boardPanel.clearHighlights();
		boardPanel.repaint();
	}

	/**
	 * Notifies when the game is ended
	 */
	@Override
	public void notifyGameEnded(int statusCode) {
		String message = "";
		switch (statusCode) {
		case Chess.BLACK_CHECKMATE:
			message = "WHITE wins by checkmate";
			break;

		case Chess.WHITE_CHECKMATE:
			message = "BLACK wins by checkmate";
			break;

		case Chess.BLACK_STALEMATE:
			message = "Game is draw. Black has no valid moves";
			break;

		case Chess.WHITE_STALEMATE:
			message = "Game is draw. White has no valid moves";
			break;
		}

		JOptionPane.showMessageDialog(boardPanel, message);
		exit();
	}

	/**
	 * logs the move into the move log panel
	 */
	@Override
	public void notifyMoveStackUpdated(Stack<Move> moveStack) {
		String moveLogs[] = new String[moveStack.size()];
		for (int i = 0; i < moveStack.size(); i++)
			moveLogs[i] = moveStack.get(i).toString();

		moveLogPanel.setLogList(moveLogs);
		frame.repaint();

	}

	/**
	 * Starts the piece move animation
	 */
	@Override
	public void moveMade(Move move) {
		String imageKey = move.getMovingPiece().getAlliance().toString().toLowerCase() + "_"
				+ move.getMovingPiece().toString().toLowerCase();

		boardPanel.pieceMoved(Assets.getImage(imageKey), move.getSource(), move.getDestination());

	}

	// private methods

	/**
	 * Exits the game panel
	 */
	private void exit() {
		cardLayout.show(container, "menu");
	}

	/**
	 * Saves the game to a folder
	 */
	private void save() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select folder to save game file to");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		String name = JOptionPane.showInputDialog("Enter name for save file: ");

		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			ChessIO.save(chess, fileChooser.getSelectedFile().getPath(), name);
		}
	}

	/**
	 * Gives hint using the ai
	 */
	private void hint() {
		Move hintMove = chess.getMinmax().getBestMove(chess.getBoard(), chess.getCurrentPlayer(), 5);

		chess.deselectPiece();
		chess.selectPiece(hintMove.getMovingPiece());

		boardPanel.highlightCell(hintMove.getSource());
		boardPanel.highlightCell(hintMove.getDestination());

		boardPanel.repaint();
	}

	/**
	 * Loads the game from save file
	 */
	private void loadGame() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select save file to load game from");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			chess = ChessIO.loadChess(fileChooser.getSelectedFile().getPath(), this);
			updateBoard(chess.getBoard());

			cardLayout.show(container, "game");
		}
	}

	/**
	 * Creates a new standard game
	 */
	private void newGame() {
		chess = new Chess(this);
		updateBoard(chess.getBoard());

		cardLayout.show(container, "game");
	}

	/**
	 * Prompts the user for the piece to prmote the pawn to
	 */
	@Override
	public String promptPromotionType() {
		String message = "Enter Piece Type:\n1. Queen\n2. Rook\n3. Bishop\n4. Knight";
		String pieces[] = { "Queen", "Rook", "Bishop", "Knight" };
		int index = 0;

		do {
			try {
				index = Integer.parseInt(JOptionPane.showInputDialog(message));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(boardPanel, "Invalid Input");
			}

			if (index < 1 || index > 4) {
				JOptionPane.showMessageDialog(boardPanel, "Invalid Input");
			}

		} while (index < 1 || index > 4);

		return pieces[index];
	}

}
