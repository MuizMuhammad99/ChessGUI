package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Move log panel to logs the move
 *
 */
public class MoveLogPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = Cell.CELL_SIZE * 3;
	public static final int HEIGHT = Cell.CELL_SIZE * 6;

	private final JList<String> moveList;

	public MoveLogPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(new Color(81 / 256f, 42 / 256f, 42 / 256f, 1f));
		setBorder(new LineBorder(Color.BLACK, 2));

		JLabel moveLogLabel = new JLabel("M o v e  L o g s");
		moveLogLabel.setAlignmentX(0.5f);
		moveLogLabel.setFont(new Font("Monospaced",Font.BOLD,15));
		moveLogLabel.setForeground(Color.BLACK);

		moveList = new JList<String>();
		moveList.setFixedCellWidth(WIDTH);
		moveList.setAlignmentX(0.5f);
		moveList.setBackground(new Color(81 / 256f, 42 / 256f, 42 / 256f, 1f));
		moveList.setForeground(Color.WHITE);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) moveList.getCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);


		JScrollPane scrollPane = new JScrollPane(moveList);
		scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(moveLogLabel);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(scrollPane);
	}


	public void setLogList(String[] moveLogs) {
		moveList.setListData(moveLogs);
	}
}
