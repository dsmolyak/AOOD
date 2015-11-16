import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

public class BattleshipDriverGUI implements ActionListener{
	
	AttackBoard attBoardP1;
    AttackBoard attBoardP2;
    DefenseBoard defBoardP1;
    DefenseBoard defBoardP2;
	Color normal;
	boolean p1Turn = true;
	JLabel instructions1;
	JLabel instructions2;
	ArrayList<Boat> p1Boats;
	ArrayList<Boat> p2Boats;
	boolean gameOver;
	
	public BattleshipDriverGUI() {
		
	}
	
	//Game Setup
	public void init(ArrayList<Boat> p1Boats, ArrayList<Boat> p2Boats) {
		this.p1Boats = p1Boats;
		this.p2Boats = p2Boats;
		
		defBoardP1 = new DefenseBoard(p1Boats);
        defBoardP2 = new DefenseBoard(p2Boats);
        attBoardP1 = new AttackBoard(defBoardP2);
        attBoardP2 = new AttackBoard(defBoardP1);
		
		normal = new JButton().getBackground(); //color of a button
		
		//Set up Player 1's board
		JFrame frameP1 = new JFrame("Player 1 Battleship");
        frameP1.setSize(500, 800);
        frameP1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, 0));
        instructions1 = new JLabel("Player 1's turn");
        panel1.add(instructions1);
        panel1.add(buildMainPanel(attBoardP1, defBoardP1));
        frameP1.add(panel1);
        frameP1.setVisible(true);
        
        //Set up Player 2's board
        JFrame frameP2 = new JFrame("Player 2 Battleship");
        frameP2.setSize(500, 800);
        frameP2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, 0));
        instructions2 = new JLabel("Player 1's turn");
        panel2.add(instructions2);
        panel2.add(buildMainPanel(attBoardP2, defBoardP2));
        frameP2.add(panel2);
		frameP2.setVisible(true);
		
	}
	
	//Creates a grid of 10x10 buttons
	private JPanel createBoard(Board board, boolean attack) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(11,11));
		panel.add(new JLabel(""));
		for (int i = 0; i < 10; i++) {
			JLabel coordinate = new JLabel(1 + i + "");
			coordinate.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(coordinate);
		}
		for (int i = 0; i < 10; i++) {
			String row = (char)('A' + i) + "";
			JLabel coordinate = new JLabel(row);
			coordinate.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(coordinate);
			for (int j = 0; j < 10; j++) {
				JButton button = new JButton();
				if (attack) {
					button.addActionListener(this);
				}
				button.setName(row + "" + (j + 1));
				button.setBackground(board.colorMap.get(board.grid[i][j]));
				board.addButton(button.getName(), button);
				panel.add(button);
			}
		}
		return panel;
	}
	
	//Sets up the panel with the grids (main section of the player board)
	private JPanel buildMainPanel(AttackBoard attBoard, DefenseBoard defBoard) {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JLabel attackLabel = new JLabel("Attack Board");
        attackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel defenseLabel = new JLabel("Defense Board");
        defenseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainPanel.add(attackLabel);
        mainPanel.add(createBoard(attBoard, true));
        mainPanel.add(defenseLabel);
        mainPanel.add(createBoard(defBoard, false));
		
		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		Position pos = new Position(button.getName());
		boolean turnMade = false;
		boolean hit = false;
		if (button.getBackground().equals(normal) && !gameOver) { //makes sure that the position hasn't already been pressed
			if (p1Turn && attBoardP1.buttons.containsValue(button)) { //checks that the right player is making a turn
				turnMade = true;
				Color posColor = attBoardP1.colorMap.get(attBoardP1.sendAttack(pos));
				button.setBackground(posColor); //sets color of current button
				defBoardP2.buttons.get(button.getName()).setBackground(posColor); //sets color of opposing board's button
				if (posColor.equals(Color.RED)) {
					hit = true;
				}
			}
			else if (!p1Turn && attBoardP2.buttons.containsValue(button)) {
				turnMade = true;
				Color posColor = attBoardP2.colorMap.get(attBoardP2.sendAttack(pos));
				button.setBackground(posColor);
				defBoardP1.buttons.get(button.getName()).setBackground(posColor);
				if (posColor.equals(Color.RED)) {
					hit = true;
				}
			}
			if (turnMade) { //checks if a valid button has been pressed
				if (!p1Turn) {
					instructions1.setText("Player 1's turn");
					instructions2.setText("Player 1's turn");
				}
				else {
					instructions1.setText("Player 2's turn");
					instructions2.setText("Player 2's turn");
				}
				if (hit) {
					if (p1Turn) { 
						checkHit(pos, p2Boats); 
					}
					else { 
						checkHit(pos, p1Boats); 
					}
					if (gameOver) {
						if (p1Boats.size() == 0) {
							instructions1.setText("Player 2 wins!");
							instructions2.setText("Player 2 wins!");
						}
						else {							
							instructions1.setText("Player 1 wins!");
							instructions2.setText("Player 1 wins!");
						}
					}
				}
				p1Turn = !p1Turn;
				
			}
		}
	}
	
	//Keeps track of remaining boats and not hit positions on those boats
	public void checkHit(Position pos, ArrayList<Boat> boats) {
		int removalIndex = -1;
		for (Boat boat : boats) {
			if (boat.spotsOccupied.contains(pos)) {
				boat.spotsOccupied.remove(pos);
				if (boat.spotsOccupied.size() == 0) { //Checks if all of the positions in a boat are hit
					if (p1Turn) {
						instructions1.setText(boat.size + "-boat sunk!");
					}
					else {
						instructions2.setText(boat.size + "-boat sunk!");
					}
					removalIndex = boats.indexOf(boat);
				}
			}
		}
		if (removalIndex != -1) { //If a boat is ready for removal (no non-hit positions left)
			boats.remove(removalIndex);
			if (boats.size() == 0) { //If the opposing player has no more boats left, game is over
				gameOver = true;
			}
		}
	}

	public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
        
            @Override
            public void run() {
				BoatSetupGUI boatCollect = new BoatSetupGUI();
				boatCollect.init();
            }
        });
    }
}
