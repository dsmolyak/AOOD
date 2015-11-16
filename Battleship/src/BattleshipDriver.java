import java.util.*;


public class BattleshipDriver {
	
	public BattleshipDriver() {
		
	}
	
	//Runs the game by doing setup and turns
	public void play() {
		Scanner scan = new Scanner(System.in); 
		System.out.println("Position format: LetterNumber (Example: A1, H3, J10)");
		int[] boatLengths = {2,2}; //each number in the array represents a boat and its length (currently one boat of length 2)
		System.out.println("Player one setup: \n");
		ArrayList<Boat> p1Boats = collectBoats(boatLengths, scan);
		ConsolePlayer p1 = new ConsolePlayer(p1Boats, "Player 1");
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
				"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //Ensures players don't see each other's boards
		System.out.println("Player two setup: \n");
		ArrayList<Boat> p2Boats = collectBoats(boatLengths, scan);
		ConsolePlayer p2 = new ConsolePlayer(p2Boats, "Player 2");
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
				"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		p1.pairAttackBoard(p2);
		p2.pairAttackBoard(p1);
		int counter = 0;
		while (!(p1.won || p2.won)) { //Players take turns until a player wins
			if (counter % 2 == 0) {
				System.out.println(p1.name + " ready?");
				scan.nextLine();
				makeTurn(p1, scan);
			}
			else {
				System.out.println(p2.name + " ready?");
				scan.nextLine();
				makeTurn(p2, scan);
			}
			++counter;
		}
	}

	//Players send an attack, which changes their board and the opposing Player's board
	private static void makeTurn(ConsolePlayer player, Scanner scan) {
		System.out.println(player);
		System.out.println("Send attack: ");
		String scanned = scan.nextLine();
		player.attack(new Position(scanned));
		if (!player.won) {
			System.out.println(player.name + " done?");
			scan.nextLine();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
					"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //Ensures players don't see each other's boards
		}
	}

	//Driver scans in positions for boats
	private static ArrayList<Boat> collectBoats(int[] boatLengths, Scanner scan) {
		ArrayList<Boat> result = new ArrayList<Boat>();
		for (int i = 0; i < boatLengths.length; i++) {
			boolean collected = false;
			while(!collected) { //Prompts user until valid positions are entered
				System.out.println("Enter positions for boat of size " + boatLengths[i] + ": ");
				System.out.print("Enter boat's front/bow position: ");
				String scanned1 = scan.nextLine();
				System.out.print("Enter boat's back/stern position: ");
				String scanned2 = scan.nextLine();
				Position pos1 = new Position(scanned1);
				Position pos2 = new Position(scanned2);
				if (pos1.distance(pos2) + 1 == boatLengths[i]) {
					collected = true;
					if (pos2.compareTo(pos1) == 1) {
						result.add(new Boat(pos1, pos2));
					}
					else {
						result.add(new Boat(pos2, pos1));
					}
				}
				else {
					System.out.println("Invalid boat positions, try again");
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		BattleshipDriver game = new BattleshipDriver();
		game.play();
	}

}
