import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class AttackBoard extends Board {
	
	DefenseBoard opposite;
	
	
	//Initializes board with access to its opposing DefenseBoard
	public AttackBoard(DefenseBoard opposite) {
		super();
		this.opposite = opposite;
	}
	
	//Initializes board without access (for Socket use)
	public AttackBoard() {
		super();
	}

	//Returns a certain character based on the character at the position of attack on the DefenseBoard (used by Player)
	public char sendAttack(Position pos) {
		char result;
		char oppPos = opposite.grid[pos.x][pos.y];
		if (oppPos == 'B') {
			result = 'H';
		}
		else if (oppPos == 'O') {
			result = 'M';
		}
		else { //If position has already been attacked
			result = 'X';
		}
		if (result != 'X') {
			opposite.grid[pos.x][pos.y] = result;
			grid[pos.x][pos.y] = result;
		}
		return result;
	}
	
	//Returns a certain character based on what the opposite Socket replies with (used by Socket classes)
	public String sendAttackSocket(Position pos, BufferedReader in, PrintWriter out) throws IOException {
		char result;
		out.println(pos.x + "," + pos.y);
		char[] receive = in.readLine().toCharArray(); 
		if (receive[0] == 'B') {
			result = 'H';
		}
		else {
			result = 'M';
		}
		if (receive.length > 1) {
			return result + "" + receive[1];
		}
		else {
			return result + "";
		}
		
	}
	
	//Testing
	public static void main(String[] args) {
		ArrayList<Boat> boats  = new ArrayList<Boat>();
		Boat boat1 = new Boat(new Position("A2"), new Position("E2"));
		Boat boat2 = new Boat(new Position("D4"), new Position("D7"));
		Boat boat3 = new Boat(new Position("F7"), new Position("H7"));
		boats.add(boat1);
		boats.add(boat2);
		boats.add(boat3);
		DefenseBoard oppBoard = new DefenseBoard(boats);
		AttackBoard myBoard = new AttackBoard(oppBoard);
		myBoard.sendAttack(new Position("I2"));
		System.out.println(myBoard);
		System.out.println(oppBoard);
	}
	
}
