package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Defines the menu of the game
 *
 */
public class MenuPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;

	private final JLabel titleLabel;
	private final JLabel newGameButton;
    private final JLabel loadGameButton;
    private final JLabel exitButton;

	private ActionListener actionListener;
	
	public MenuPanel() {
		setPreferredSize(new Dimension(Game.FRAME_WIDTH,Game.FRAME_HEIGHT));
		setBackground(new Color(81 / 256f, 42 / 256f, 42 / 256f, 1f));
		
		titleLabel = new JLabel("CHESS GAME 2D");
		newGameButton = new JLabel("New Game");
		loadGameButton = new JLabel("Load Game");
		exitButton = new JLabel("Exit");
		
		titleLabel.setAlignmentX(0.5f);
		newGameButton.setAlignmentX(0.5f);
		loadGameButton.setAlignmentX(0.5f);
		exitButton.setAlignmentX(0.5f);
		
		Font font = new Font("Monospaced",Font.BOLD,20);
		titleLabel.setFont(new Font("Arial",Font.BOLD,30));
		newGameButton.setFont(font);
		loadGameButton.setFont(font);
		exitButton.setFont(font);
		
		titleLabel.setForeground(Color.WHITE);
		newGameButton.setForeground(Color.WHITE);
		loadGameButton.setForeground(Color.WHITE);
		exitButton.setForeground(Color.WHITE);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(0,50)));
		add(titleLabel);
		add(Box.createRigidArea(new Dimension(0,50)));
		add(newGameButton);
		add(Box.createRigidArea(new Dimension(0,30)));
		add(loadGameButton);
		add(Box.createRigidArea(new Dimension(0,30)));
		add(exitButton);
		
		//add listeners
		newGameButton.addMouseListener(this);
		loadGameButton.addMouseListener(this);
		exitButton.addMouseListener(this);
	}
	
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == newGameButton)
			actionListener.actionPerformed(new ActionEvent(e.getSource(),0,"new_game"));
		else if(e.getSource() == loadGameButton)
			actionListener.actionPerformed(new ActionEvent(e.getSource(),0,"load_game"));
		else if(e.getSource() == exitButton)
			actionListener.actionPerformed(new ActionEvent(e.getSource(),0,"exit_game"));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == newGameButton)
			newGameButton.setForeground(Color.GRAY);
		else if(e.getSource() == loadGameButton)
			loadGameButton.setForeground(Color.GRAY);
		else if(e.getSource() == exitButton)
			exitButton.setForeground(Color.GRAY);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == newGameButton)
			newGameButton.setForeground(Color.WHITE);
		else if(e.getSource() == loadGameButton)
			loadGameButton.setForeground(Color.WHITE);
		else if(e.getSource() == exitButton)
			exitButton.setForeground(Color.WHITE);
	}
	
}
