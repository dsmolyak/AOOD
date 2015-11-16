import java.util.ArrayList;


public class Boat {
	
	ArrayList<Position> spotsOccupied;
	int size;
	
	//Initializes a boat using positions for its front and back (one < two)
	public Boat(Position one, Position two) {
		spotsOccupied = new ArrayList<Position>();
		if (one.x == two.x) {
			for (int i = one.y; i <= two.y; i++) {
				spotsOccupied.add(new Position(one.x,i));
			}
		}
		if (one.y == two.y) {
			for (int i = one.x; i <= two.x; i++) {
				spotsOccupied.add(new Position(i,one.y));
			}
		}
		size = (int)(one.distance(two)) + 1;
	}
	
	//Testing
	public static void main(String[] args) {
		Boat boat = new Boat(new Position("A1"), new Position("E1"));
		for (Position pos : boat.spotsOccupied) {
			System.out.println(pos);
		}
	}
	
}
