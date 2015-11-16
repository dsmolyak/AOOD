import java.util.ArrayList;
import java.util.Scanner;


public class ConsolePlayer {

	AttackBoard attack;
	DefenseBoard defense;
	ArrayList<Boat> oppBoats;
	String name;
	boolean won;
	
	//Takes a list of boats to initialize its DefenseBoard
	public ConsolePlayer(ArrayList<Boat> boats, String name) {
		defense = new DefenseBoard(boats);
		this.name = name;
		won = false;
	}
	
	//Initializes an AttackBoard with access to opposing player's DefenseBoard
	public void pairAttackBoard(ConsolePlayer other) {
		attack = new AttackBoard(other.defense);
		oppBoats = other.defense.boats;
	}
	
	//Scans in position for attack and checks for the state of the opposite players boats after the attack 
	public void attack(Position pos) {
		char result = attack.sendAttack(pos);
		
		while (result == 'X') { //Prompts user until a valid position is chosen for attack
			Scanner scan = new Scanner(System.in);
			System.out.println("Already attacked, enter new position: ");
			String scanned = scan.nextLine();
			pos = new Position(scanned);
			result = attack.sendAttack(pos);
			scan.close();
		}
		if (result == 'M') {
			System.out.println("Miss");
		}
		else if (result == 'H') {
			int removalIndex = -1;
			for (Boat boat : oppBoats) {
				if (boat.spotsOccupied.contains(pos)) {
					boat.spotsOccupied.remove(pos);
					System.out.println("Hit!");
					if (boat.spotsOccupied.size() == 0) { //Checks if all of the positions in a boat are hit
						System.out.println(boat.size + "-boat sunk!");
						removalIndex = oppBoats.indexOf(boat);
					}
				}
			}
			if (removalIndex != -1) { //If a boat is ready for removal (no non-hit positions left)
				oppBoats.remove(removalIndex);
				if (oppBoats.size() == 0) { //If the opposing player has no more boats left, game is over
					System.out.println("Game over, " + name + " wins!");
					won = true;
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("\n" + name + "\n");
		result.append("Attack: \n");
		result.append(attack.toString() + "\n");
		result.append("Defense: \n");
		result.append(defense.toString() + "\n");
		return result.toString();
	}

}
