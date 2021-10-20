package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import engine.board.Board;
import engine.board.Position;
import gui.sfx.AudioFile;

/**
 * Board panel of game
 *
 */
public class BoardPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Cell cells[][];
	private BoardAnimationListener animListener;
	
	private Timer timer;
	private boolean isAnimating;
	private float animTime = 1f;
	private float animTimer;
	private Vector2f pos, src, des;
	private BufferedImage movingPieceImage;
	private Position pieceDestination;
	
	private AudioFile moveSound;

	/**
	 * Constructor
	 */
	public BoardPanel() {
		timer = new Timer(16, this);
		moveSound = Assets.getAudioFile("move");
		moveSound.play();

		setPreferredSize(new Dimension(Cell.CELL_SIZE * Board.SIZE, Cell.CELL_SIZE * Board.SIZE));
		setBorder(new LineBorder(Color.BLACK, 1));

		cells = new Cell[Board.SIZE][Board.SIZE];
		initCells();

	}

	/**
	 * Animates the movement of piece by using interpolation
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		animTimer += 0.16f;

		pos.x = interpolate(src.x, des.x, animTimer / animTime);
		pos.y = interpolate(src.y, des.y, animTimer / animTime);

		if (animTimer >= animTime) {
			animTimer = 0;
			isAnimating = false;
			setPieceImage(movingPieceImage, pieceDestination);
			timer.stop();
			animListener.animationDone();
		}

		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (int i = 0; i < Board.SIZE; i++)
			for (int j = 0; j < Board.SIZE; j++)
				cells[i][j].render(g2);

		if (isAnimating)
			g2.drawImage(movingPieceImage, (int) pos.x, (int) pos.y, Cell.CELL_SIZE, Cell.CELL_SIZE, null);

	}
	

	/**
	 * Sets the click listener for the cells
	 * 
	 * @param actionListener	action listener
	 */
	public void setClickListener(ActionListener actionListener) {
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				actionListener.actionPerformed(
						new ActionEvent(new Position(e.getY() / Cell.CELL_SIZE, e.getX() / Cell.CELL_SIZE), 0, ""));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	/**
	 * Starts the animation for the piece to move
	 * 
	 * @param image	piece image
	 * @param from	from
	 * @param to	to
	 */
	public void pieceMoved(BufferedImage image, Position from, Position to) {
		setPieceImage(null, from);

		isAnimating = true;
		movingPieceImage = image;

		pos = new Vector2f(from.col * Cell.CELL_SIZE, from.row * Cell.CELL_SIZE);
		src = new Vector2f(from.col * Cell.CELL_SIZE, from.row * Cell.CELL_SIZE);
		des = new Vector2f(to.col * Cell.CELL_SIZE, to.row * Cell.CELL_SIZE);
		pieceDestination = to;
		timer.start();
		
		moveSound.reset();
		moveSound.play();
	}

	/**
	 * Sets the piece image at position
	 * 
	 * @param image	image
	 * @param position	position
	 */
	public void setPieceImage(BufferedImage image, Position position) {
		cells[position.row][position.col].setPieceImage(image);
	}

	/**
	 * highlights the cell at position
	 * @param p	position
	 */
	public void highlightCell(Position p) {
		cells[p.row][p.col].setHighlighted(true);
	}

	/**
	 * clears all highlighted cells
	 */
	public void clearHighlights() {
		for (int i = 0; i < Board.SIZE; i++)
			for (int j = 0; j < Board.SIZE; j++)
				cells[i][j].setHighlighted(false);
	}

	

	/**
	 * Initializes the cells
	 */
	private void initCells() {
		for (int i = 0; i < Board.SIZE; i++)
			for (int j = 0; j < Board.SIZE; j++) {
				String key = "";
				if (i % 2 == 0)
					key = j % 2 == 0 ? "light_brown_cell" : "dark_brown_cell";
				else
					key = j % 2 == 0 ? "dark_brown_cell" : "light_brown_cell";

				cells[i][j] = new Cell(j * Cell.CELL_SIZE, i * Cell.CELL_SIZE, Assets.getImage(key));

			}

	}

	//interpolation function
	private float interpolate(float a, float b, float p) {
		return a + ((b - a) * p);
	}
	
	//getters
	
	public boolean isAnimating() {
		return isAnimating;
	}
	

	public void setAnimListener(BoardAnimationListener animListener) {
		this.animListener = animListener;
	}


}
