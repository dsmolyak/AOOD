import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;


public class BattleshipClient implements Runnable, ActionListener {

	BufferedReader in;
	PrintWriter out;
	
	ArrayList<Boat> clientBoats;
	AttackBoard attBoard;
	DefenseBoard defBoard;
	Color normal;
	JLabel instructions;
	boolean p2Turn;
	boolean gameOver;

	//Is run by Battleship server, connects the socket
	@Override
	public void run() {
		try {
			Socket client = new Socket("0.0.0.0", 20001);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			collectBoats();
			playGame();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Runs gui to collect boats
	private void collectBoats() {
		BoatSetupSocket collection = new BoatSetupSocket(false);
		while (!collection.finished);
		clientBoats = collection.pBoats;
		out.println("ready");
		while (true) {
			try {
				if (in.ready() && in.readLine().equals("ready")) { //checks if other player is ready
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Runs the battleship game until "game over" is declared
	private void playGame() throws IOException {
		init();
		while (!gameOver) {
			if (!p2Turn) {
				if (!instructions.getText().contains("sunk")) {
					instructions.setText("Player 1's Turn");
				}
				String received = in.readLine();
				if (received.length() == 3) {
					String send = parseReceived(received);
					out.println(send);
					p2Turn = true;
					instructions.setText("Player 2's Turn");
				}
			}
		}
		if (clientBoats.size() > 0) {
			instructions.setText("Player 2 wins!");
		}
		else {
			instructions.setText("Player 1 wins!");
		}
	}

	// Handles various functions dealing with data received from the other player's turn 
	private String parseReceived(String received) {
		String[] xy = received.split(",");
		int x = Integer.valueOf(xy[0]);
		int y = Integer.valueOf(xy[1]);
		String send = defBoard.grid[x][y] + "";
		String xPos = (char)('A' + x) + "";
		String yPos = (char)('0' + 1 + y) + "";
		if (y == 9) {
			yPos = "10";
		}
		if (send.equals("O")) { //If miss, changes button's color
			defBoard.buttons.get(xPos + "" + yPos).setBackground(defBoard.colorMap.get('M'));
		}
		else { //If hit, changes button's color and checks if boat is sunk
			defBoard.buttons.get(xPos + "" + yPos).setBackground(defBoard.colorMap.get('H'));
			int status = checkHit(new Position(xPos + yPos));
			if (status != 0) {
				send = send + status;
			}
		}
		return send;
	}

	//Returns a value based on what the hit has caused
	//0 if it's a normal hit
	//Boat size if a boat has been sunk
	//9 if the game is over
	private int checkHit(Position pos) {
		int result = 0;
		int removalIndex = -1;
		for (Boat boat : clientBoats) {
			if (boat.spotsOccupied.contains(pos)) {
				boat.spotsOccupied.remove(pos);
				if (boat.spotsOccupied.size() == 0) { //Checks if all of the positions in a boat are hit
					result = boat.size;
					removalIndex = clientBoats.indexOf(boat);
				}
				else { //No boats have been sunk
					return result; 
				}
			}
		}
		if (removalIndex != -1) { //If a boat is ready for removal (no non-hit positions left)
			clientBoats.remove(removalIndex);
			if (clientBoats.size() == 0) { //If the player has no more boats left, game is over
				gameOver = true;
				return 9;
			}
		}
		return result;
	}

	//Set up Player 1's board
	private void init() {
		defBoard = new DefenseBoard(clientBoats);
		attBoard = new AttackBoard();
		normal = new JButton().getBackground(); //color of a button
		p2Turn = false;
		JFrame frame = new JFrame("Player 2 Battleship");
		frame.setSize(500, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, 0));
		instructions = new JLabel("Player 1's turn");
		panel.add(instructions);
		panel.add(buildMainPanel());
		frame.add(panel);
		frame.setVisible(true);
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
	private JPanel buildMainPanel() {
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
	public void actionPerformed(ActionEvent e) { //checks if not clicked yet
		if (p2Turn && !gameOver) {
			JButton button = (JButton) e.getSource();
			if (button.getBackground() == normal) {
				try {
					Position pos = new Position(button.getName());
					char[] result = attBoard.sendAttackSocket(pos, in, out).toCharArray();
					button.setBackground(defBoard.colorMap.get(result[0]));
					if (result.length > 1) {
						if (result[1] != '9') {
							instructions.setText(result[1] + "-boat sunk!");
						}
						else {
							gameOver = true;
						}
					}
					p2Turn = false;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
