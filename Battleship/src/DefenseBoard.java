import java.util.ArrayList;


public class DefenseBoard extends Board {
	
	ArrayList<Boat> boats;
	
	//Assigns positions on the board that are occupied by boats to 'B' character
	public DefenseBoard(ArrayList<Boat> boats2) {
		super();
		this.boats = boats2;
		for (Boat boat : boats2) {
			for (Position pos : boat.spotsOccupied) {
				grid[pos.x][pos.y] = 'B';
			}
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
		DefenseBoard board = new DefenseBoard(boats);
		System.out.println(board);
	}
	
}
