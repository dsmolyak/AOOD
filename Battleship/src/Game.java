
//Class for choosing which type of Battleship to play
public class Game {
	
	public static void playConsoleGame() {
		BattleshipDriver driver = new BattleshipDriver();
		driver.play();
	}
	
	public static void playGuiGame() {
		BoatSetupGUI driver = new BoatSetupGUI();
		driver.init();
	}
	
	public static void playSocketGame() {
		BattleshipServer driver = new BattleshipServer();
		driver.start();
	}
	

	public static void main(String[] args) {
		playSocketGame();

	}

}
