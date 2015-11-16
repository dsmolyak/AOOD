import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;



public class BoatSetupSocket implements ActionListener {
	
	Color normal;
	ArrayList<String> boatCommands;
	int commandCounter;
	Position pos1;
	Position pos2;
	int[] boatLengths = {2,3}; //Select length of boats
	ArrayList<Boat> pBoats;
	JLabel instructions;
	TreeMap<Position, JButton> boardMap;
	boolean finished;
	
	public BoatSetupSocket(boolean p1) {
		init(p1);
	}
	
	private void init(boolean p1) {	
		finished = false;
		normal = new JButton().getBackground();
		pos1 = new Position("A1");
		pos2 = new Position("A1");
		collectBoats(p1);
	}

	//Boat collection setup
	private void collectBoats(boolean p1) {
		JFrame frame;
		
		int num = p1 ? 1 : 2;
		frame = new JFrame("Player " + num + " Boat Collection");
		pBoats = new ArrayList<Boat>();
		
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        instructions = new JLabel("Click first position for " + boatLengths[0] + "-boat");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(instructions);
        panel.add(createBoard());
        frame.add(panel);
        frame.setVisible(true);
        
        boatCommands = new ArrayList<String>();
        for (int i = 0; i < boatLengths.length; i++) { //Generates list of instructions
        	boatCommands.add("Click first position for " + boatLengths[i] + "-boat");
        	boatCommands.add("Click second position for " + boatLengths[i] + "-boat");
		}
        boatCommands.add("Finished");
        commandCounter = 0;
	}
	
	//Creates a grid of 10x10 buttons
	private JPanel createBoard() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(11,11));
		boardMap = new TreeMap<Position, JButton>();
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
				button.addActionListener(this);
				button.setName(row + "" + (j + 1));
				panel.add(button);
				boardMap.put(new Position(i, j), button);
			}
		}
		return panel;

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		if (!(button.getBackground() == Color.GRAY || button.getBackground() == Color.GREEN)) { //Checks if spot is already occupied
			if (commandCounter % 2 == 0) { //If this is the first boat position
				if (boardMap.get(pos1).getBackground().equals(Color.RED)) {
					boardMap.get(pos1).setBackground(normal);
				}
				if (boardMap.get(pos2).getBackground().equals(Color.RED)) {
					boardMap.get(pos2).setBackground(normal);
				}
				button.setBackground(Color.GRAY);
				pos1 = new Position(button.getName());
				commandCounter++;
			}
			else {
				boolean validBoat = false;
				pos2 = new Position(button.getName());
				if (pos2.distance(pos1) == boatLengths[commandCounter/2] - 1) { //Checks if boat length matches selection
					if (pos2.compareTo(pos1) == 1) {
						validBoat = addBoat(new Boat(pos1,pos2));
					}
					else {
						validBoat = addBoat(new Boat(pos2,pos1));
					}
					if (validBoat) {
						commandCounter++;
					}
				}
				else if (!validBoat) { //If boat isn't valid, goes back to asking for first position
					commandCounter--;
					button.setBackground(Color.red);
					boardMap.get(pos1).setBackground(Color.red);
				}
			}
			instructions.setText(boatCommands.get(commandCounter));
			if (commandCounter == boatCommands.size() - 1) { //Once all boats are collected for the player
				button.getParent().getParent().setVisible(false);
				finished = true;
			}
		}
	}
	
	//Adds to a player's boats if the boat is valid
	public boolean addBoat(Boat boat) {
		boolean valid = false;
		if (!intersects(boat)) {
			valid = true;
			for (Position pos : boat.spotsOccupied) {
				boardMap.get(pos).setBackground(Color.GREEN);
			}
			pBoats.add(boat);
		}
		return valid;
	}
	
	//Checks whether the current boat intersects with an already added boat
	private boolean intersects(Boat newBoat) {
		ArrayList<Boat> boats = pBoats;
		if (boats.size() != 0) {
			for (Boat boat : boats) {
				for (Position spot : boat.spotsOccupied) {
					for (Position pos : newBoat.spotsOccupied) {
						if (spot.equals(pos)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
