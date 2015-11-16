import java.awt.*;
import java.util.*;

import javax.swing.*;


public class Board {
	
	char[][] grid;
	TreeMap<String, JButton> buttons;
	TreeMap<Character, Color> colorMap;
	
	
	//Initializes 10x10 grid of 'O' characters
	public Board() {
		buttons = new TreeMap<String, JButton>();
		grid = new char[10][10];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = 'O';
			}
		}
		
		colorMap = new TreeMap<Character, Color>();
		colorMap.put('H', Color.RED);
		colorMap.put('M', Color.CYAN);
		colorMap.put('B', Color.BLACK);
		colorMap.put('O', new JButton().getBackground());
	}
	
	public void addButton(String position, JButton button) {
		buttons.put(position, button);
	}

	//Prints out grid with labels on the left column and top row
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("");
		result.append("   ");
		for (int i = 1; i < 11; i++) {
			result.append(i + " ");
		}
		result.append("\n");
		for (int i = 0; i < grid.length; i++) {
			result.append((char)('A' + i));
			result.append("| ");
			for (int j = 0; j < grid[0].length; j++) {
				result.append(grid[i][j] + " ");
			}
			result.append("|\n");
		}
		return result.toString();
	}
	
	//Testing
	public static void main(String[] args) {
		Board board = new Board();
		System.out.println(board);
	}

	
	
}
