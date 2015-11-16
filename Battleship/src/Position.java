import java.util.Scanner;


public class Position implements Comparable<Position> {
	
	int x;
	int y;
	

	public Position(String coordinate) {
		convertString(coordinate);
		while (x > 9 || y > 9 || x < 0 || y < 0) { //Prompts user until a valid position is entered
			Scanner scan = new Scanner(System.in);
			System.out.print("Invalid coordinates, enter new position: ");
			String result = scan.nextLine();
			convertString(result);
			scan.close();
		}
	}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//Converts the user's string value into a usable numeric position
	public void convertString(String coordinate) {
		char[] coordinates = coordinate.toCharArray();
		x = coordinates[0] - 'A';
		y = coordinates[1] - '1';
		if (coordinates.length == 3) {
			y = Integer.valueOf(coordinates[1] + "" + coordinates[2]) - 1;
		}
	}
	
	//Takes the pythagorean distance between two points (used for Boat)
	public double distance(Position o) {
		return (Math.sqrt((o.x - x)*(o.x - x) + (o.y - y)*(o.y - y)));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public int compareTo(Position o) {
		if (x > o.x) {
			return 1;
		}
		else if (x < o.x) {
			return -1;
		}
		else {
			if (y > o.y) {
				return 1;
			}
			else if (y < o.y) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
	
	//Testing
	public static void main(String[] args) {
		Position pos = new Position("J11");
		System.out.println(pos);
	}
	
}
