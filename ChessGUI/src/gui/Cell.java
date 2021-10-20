package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A single cell on board
 *
 */
public class Cell {

	public static final int CELL_SIZE = 64;
	public static final int HIGHLIGHT_SIZE = 32;

	private int x,y;
	
	private BufferedImage cellImage;
	private BufferedImage pieceImage;
	private boolean isHighlighted;

	/**
	 * Constructor
	 * @param x	x
	 * @param y	y
	 * @param cellImage	cell image
	 */
	public Cell(int x,int y,BufferedImage cellImage) {
		this.x = x;
		this.y = y;
		this.cellImage = cellImage;
	}

	public void render(Graphics2D g2) {
		//cell image
		g2.drawRect(x,y,CELL_SIZE,CELL_SIZE);
		g2.drawImage(cellImage,x,y, CELL_SIZE,CELL_SIZE, null);

		//piece image
		if (pieceImage != null)
			g2.drawImage(pieceImage,x,y,Cell.CELL_SIZE,Cell.CELL_SIZE, null);
		
		//highlighting
		if (isHighlighted) {
			g2.setColor(new Color(1, 1, 0, 0.5f));
			g2.fillOval(x + CELL_SIZE / 2 - HIGHLIGHT_SIZE / 2,y +  CELL_SIZE / 2 - HIGHLIGHT_SIZE / 2, HIGHLIGHT_SIZE, HIGHLIGHT_SIZE);
		}

			
	}
	
	//setter

	public void setPieceImage(BufferedImage image) {
		this.pieceImage = image;
	}

	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}

}
