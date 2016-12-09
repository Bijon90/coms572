package coms572;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class TestGame {

	public static void main(String args[]) {
		try {
			PrintWriter writer1 = new PrintWriter("C:\\Users\\Bijon\\workspace\\LineOfAction\\LOA_Output_Cumulative.txt","UTF-8");
			Game g;
			for (int i = 1; i <= 50; i++) {
				g = new Game(i);
				/*Board b = new Board();
				char[][] board = new char[8][8];
				board = Constants.INITIAL_PIECES.clone();
				b.setBoard(board);
				System.out.println("Setting up new Board");
				b.printBoard();
				b.setCurrPlayer(Constants.user);
				g.setBoard(b);*/
				g.startGame();
				writer1.println("************************");
				writer1.println();
				writer1.println("Game "+i+" :");
				writer1.println("Total number of moves by machine: "+(g.getBoard().movesMade()/2));
				writer1.println("Maximum Time taken for a move: "+g.maxTime);
				writer1.println("Minimum Time taken for a move: "+g.minTime);
				writer1.println("Average Time taken per move: "+g.avgTime);
				writer1.println("Total Time taken by machine: "+g.totalTime);
				writer1.println("Result: "+g.result);
				writer1.println();
				writer1.println("************************");
			}
			writer1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
