package coms572;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author Bijon
 * Testing the agent program by running experiments
 */
public class TestGame {

	public static void main(String args[]) {
		double cavgTime = 0.0, cNoMoves = 0, cMaxTime = 0.0, cMinTime = 0.0, cTotalTime = 0.0;
		try {
			PrintWriter writer1 = new PrintWriter("C:\\Users\\Bijon\\workspace\\LineOfAction\\LOA_Output_Cumulative.txt","UTF-8");
			Game g;
			for (int i = 1; i <= 50; i++) {
				g = new Game(i);
				g.startGame();
				writer1.println("************************");
				writer1.println();
				writer1.println("Game "+i+" :");
				writer1.println("Total number of moves by machine: "+(g.getBoard().movesMade()/2));
				cNoMoves += g.getBoard().movesMade()/2;
				writer1.println("Maximum Time taken for a move: "+g.maxTime);
				cMaxTime += g.maxTime;
				writer1.println("Minimum Time taken for a move: "+g.minTime);
				cMinTime += g.minTime;
				writer1.println("Average Time taken per move: "+g.avgTime);
				cavgTime += g.avgTime;
				writer1.println("Total Time taken by machine: "+g.totalTime);
				cTotalTime += g.totalTime;
				writer1.println("Result: "+g.result);
				writer1.println();
				writer1.println("************************");
			}
			cavgTime = cavgTime/50;
			cTotalTime = cTotalTime/50;
			cMaxTime = cMaxTime/50;
			cMinTime = cMinTime/50;
			cNoMoves = cNoMoves/50;
			writer1.println("Cumulative Average Time: "+cavgTime);
			writer1.println("Cumulative Average Moves: "+cNoMoves);
			writer1.println("Cumulative Average maxTime: "+cMaxTime);
			writer1.println("Cumulative Average minTime: "+cMinTime);
			writer1.println("Cumulative Average Total Time: "+cTotalTime);
			writer1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
