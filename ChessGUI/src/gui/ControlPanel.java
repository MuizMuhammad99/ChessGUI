package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Control panel of the game which contains the control buttons
 *
 */
public class ControlPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = Cell.CELL_SIZE * 3;
	public static final int HEIGHT = Cell.CELL_SIZE * 2;

	private JLabel undoButton, redoButton, hintButton, saveButton, exitButton;
	private ActionListener actionListener;

	/**
	 * Constructor
	 */
	public ControlPanel() {
		setBackground(new Color(124f / 256f, 76f / 256f, 62f / 256f, 1f));
		setBorder(new LineBorder(Color.BLACK, 1));

		undoButton = new JLabel();
		redoButton = new JLabel();
		hintButton = new JLabel();
		saveButton = new JLabel();
		exitButton = new JLabel();

		undoButton.setIcon(new ImageIcon(Assets.getImage("undo")));
		redoButton.setIcon(new ImageIcon(Assets.getImage("redo")));
		hintButton.setIcon(new ImageIcon(Assets.getImage("hint")));
		saveButton.setIcon(new ImageIcon(Assets.getImage("save")));
		exitButton.setIcon(new ImageIcon(Assets.getImage("exit")));

		setLayout(new GridLayout(2, 3));
		add(undoButton);
		add(redoButton);
		add(hintButton);
		add(saveButton);
		add(exitButton);

		// add listener
		undoButton.addMouseListener(this);
		redoButton.addMouseListener(this);
		hintButton.addMouseListener(this);
		saveButton.addMouseListener(this);
		exitButton.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		String cmd = "";
		if (e.getSource() == undoButton)
			cmd = "undo";
		else if (e.getSource() == redoButton)
			cmd = "redo";
		else if (e.getSource() == hintButton)
			cmd = "hint";
		else if (e.getSource() == saveButton)
			cmd = "save";
		else if (e.getSource() == exitButton)
			cmd = "exit";

		actionListener.actionPerformed(new ActionEvent(e.getSource(), 1, cmd));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

}
