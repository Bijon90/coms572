/**
 * 
 */
package line.of.action;

import java.util.Date;
import java.util.Scanner;


/**
 * @author dipanjankarmakar
 *
 */
public class LoaGameStart {
	static boolean debugging=true;
	static int delay=3;
	static int nodesExpanded=0;
	
	public static void main(String[] args) 
	{
		LoaGame game= new LoaGame();
		State stateAfterAgentMove = null;
		int[][] possibleActions=null;
		
		System.out.println("Please enter the value of depth :");
		Scanner sc=new Scanner(System.in);
		int depth=Integer.parseInt(sc.nextLine());
		System.out.println("X corresponds to WHITE, O corresponds to BLACK");
		System.out.println("Black has first turn");
		
		try{
			do{
				
				possibleActions = game.loaMovesPossible(game.gameState, game.player);
				int randomMove=(int)(Math.random() * possibleActions.length);
				
				int r=possibleActions[randomMove][0];
				int c=possibleActions[randomMove][1];
				int fr=possibleActions[randomMove][2];
				int fc=possibleActions[randomMove][3];

				System.out.println("Random moveMove = {"+r+","+c+"} - {"+fr+","+fc+"}");

				State state=game.getLoaNextState(game.gameState,new int[]{r,c},new int[]{fr,fc},game.player);
				if(game.isGameOver(state))
				{
					System.out.println("Random move has won !!!");
					System.exit(0);
				}
				if(state!=null)
				{
					state.printBoard();
				}
				else
				{
					state=game.gameState;
					System.out.println("That's an illegal move");
					state.printBoard();
					break;
				}
				
				System.out.println("Now Agent will generate the move");
				Thread.sleep(delay);
				
				State origState=new State(state);
				long start= new Date().getTime();
				
				int move[]=game.alphaBetaSearch(state,game.player.getOpponent(),depth);
				System.out.println("Agent generated the move. Move = {"+move[0]+","+move[1]+"} - {"+move[2]+","+move[3]+"}");
				
				//System.out.println("Agent generated the move. Nodes expanded >> "+ nodesExpanded);
				long stop=new Date().getTime();
				System.out.println("Moves generated in " + (stop-start) +" ms" );
				
				if(move!=null)
				{
					System.out.println("New State ");
					stateAfterAgentMove=game.getLoaNextState(origState,new int[]{move[0],move[1]}, new int[]{move[2],move[3]},game.player.getOpponent());
					stateAfterAgentMove.printBoard();
					game.gameState=stateAfterAgentMove;
				}
				if(game.isGameOver(stateAfterAgentMove))
				{
					System.out.println("Agent has won !!!");
					System.exit(0);
				}
				if(stateAfterAgentMove!=null)
				{
					possibleActions = game.loaMovesPossible(stateAfterAgentMove, game.player);
				}
			}while(possibleActions!=null && possibleActions.length>0);

		}catch(Exception e)
		{
			System.out.println("Incorrect input format");
			e.printStackTrace();
		}
		sc.close();
	}
}
