package line.of.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;


/**
 * The game is started here
 * <p> We have to provide the depth of the alpha-beta search when asked
 * <p> All the files are generated in the folder
 */
public class LoaGameStart {
	static boolean debugging=true;
	static int delay=0;
	static int nodesExpanded=0;
	public static String fileName="";
	public static final char W='X',B='O',EMPTY='_';
	static String folder="depth3a";
	
	public static void main(String[] args) 
	{
		int agentWins=0,randomWins=0, agentMoves=0,randomMoves=0;
		File anaFile = new File(folder+"/Analysis.txt");
		FileWriter afw;
		try {
			afw = new FileWriter(anaFile.getAbsoluteFile(),true);
			BufferedWriter abw = new BufferedWriter(afw);
			
			System.out.println("Please enter the value of depth :");
			Scanner sc=new Scanner(System.in);
			int depth=Integer.parseInt(sc.nextLine());
			for(int noF=0;noF<50;noF++)
			{
				try{
					Random rand = new Random(System.currentTimeMillis());
					fileName=folder+"/testFile"+noF+".txt";
					File file = new File(LoaGameStart.fileName);
					// if file doesn't exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
					BufferedWriter bw = new BufferedWriter(fw);

					State stateAfterAgentMove = null;
					int[][] possibleActions=null;
					long totalAgentTime=0;
					
					bw.write("Depth : "+depth+"\n");
					System.out.println("X corresponds to WHITE, O corresponds to BLACK");
					System.out.println("Black has first turn");
					int noOfMoves=0;
					LoaGame game= new LoaGame();
					do{

						possibleActions = game.loaMovesPossible(game.gameState, game.player);
						// select a random move from all the possible moves for the player
						int randomMove=(int)(rand.nextDouble() * possibleActions.length);

						int r=possibleActions[randomMove][0];
						int c=possibleActions[randomMove][1];
						int fr=possibleActions[randomMove][2];
						int fc=possibleActions[randomMove][3];

						System.out.println("Random moveMove = {"+r+","+c+"} - {"+fr+","+fc+"}");
						bw.write("Random moveMove = {"+r+","+c+"} - {"+fr+","+fc+"}"+"\n");

						State state=game.getLoaNextState(game.gameState,new int[]{r,c},new int[]{fr,fc},game.player);
						if(game.piecesContiguous(state,B))
						{
							randomWins++;
							randomMoves+= noOfMoves;
							System.out.println("Random move has won !!!");
							System.out.println("Total number of moves : " + noOfMoves);
							bw.write("Random move has won !!! \n");
							bw.write("Total number of moves : " + noOfMoves+"\n");
							long averageAgtMoveTime=totalAgentTime/noOfMoves;
							bw.write("Average time per move by agent : " + averageAgtMoveTime+"\n");
							abw.write("Game "+ noF +" won by Random in " + noOfMoves + " moves \n");
							printBoardOnFile(state,bw);
							abw.flush();
							bw.flush();
							bw.close();
							break;
						}
						if(game.piecesContiguous(state,W))
						{
							agentWins++;
							agentMoves+=noOfMoves;
							System.out.println("Agent has won !!!");
							System.out.println("Total number of moves : " + noOfMoves);
							bw.write("Agent has won !!! \n");
							bw.write("Total number of moves : " + noOfMoves+"\n");
							long averageAgtMoveTime=totalAgentTime/noOfMoves;
							bw.write("Average time per move by agent : " + averageAgtMoveTime+"\n");
							abw.write("Game "+ noF +" won by Agent in " + noOfMoves +" moves \n");
							printBoardOnFile(state,bw);
							abw.flush();
							bw.flush();
							bw.close();
							break;
						}
						if(state!=null)
						{
							state.printBoard();
							printBoardOnFile(state,bw);
						}
						else
						{
							state=game.gameState;
							System.out.println("That's an illegal move");
							state.printBoard();
							break;
						}

						System.out.println("Now Agent will generate the move");
						bw.write("Now Agent will generate the move\n");
						Thread.sleep(delay);

						State origState=new State(state);
						long start= new Date().getTime();

						int move[]=game.alphaBetaSearch(state,game.player.getOpponent(),depth);
						System.out.println("Agent generated the move. Move = {"+move[0]+","+move[1]+"} - {"+move[2]+","+move[3]+"}");
						bw.write("Agent generated the move. Move = {"+move[0]+","+move[1]+"} - {"+move[2]+","+move[3]+"} \n");

						long stop=new Date().getTime();
						System.out.println("Moves generated in " + (stop-start) +" ms" );
						bw.write("Moves generated in " + (stop-start) +" ms \n" );
						totalAgentTime+=stop-start;

						if(move!=null)
						{
							System.out.println("New State ");
							stateAfterAgentMove=game.getLoaNextState(origState,new int[]{move[0],move[1]}, new int[]{move[2],move[3]},game.player.getOpponent());
							stateAfterAgentMove.printBoard();
							game.gameState=stateAfterAgentMove;
							printBoardOnFile(stateAfterAgentMove,bw);

						}
						if(game.piecesContiguous(stateAfterAgentMove,W))
						{
							agentWins++;
							agentMoves+=noOfMoves;
							System.out.println("Agent has won !!!");
							System.out.println("Total number of moves : " + noOfMoves);
							bw.write("Agent has won !!! \n");
							bw.write("Total number of moves : " + noOfMoves+"\n");
							long averageAgtMoveTime=totalAgentTime/noOfMoves;
							bw.write("Average time per move by agent : " + averageAgtMoveTime+"\n");
							abw.write("Game "+ noF +" won by Agent in " + noOfMoves +" moves \n");
							printBoardOnFile(stateAfterAgentMove,bw);
							abw.flush();
							bw.flush();
							bw.close();
							break;
						}
						if(stateAfterAgentMove!=null)
						{
							possibleActions = game.loaMovesPossible(stateAfterAgentMove, game.player);
						}
						noOfMoves++;
						if(bw!=null)
							bw.flush();
					}while(possibleActions!=null && possibleActions.length>0);
					System.out.println("Total number of moves : " + noOfMoves);

					sc.close();
				}catch(Exception e)
				{
					System.out.println("Incorrect input format");
					e.printStackTrace();
				}
			}
			abw.write("Total number of times Agent won : " + agentWins + " in moves " + agentMoves+"\n");
			abw.write("Total number of times Random won : " + randomWins + " in moves " + randomMoves+"\n");
			abw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * @param state				the state of the board to print
	 * @param bw				the bufferred reader which will do the print
	 * @throws IOException 
	 */
	private static void printBoardOnFile(State state, BufferedWriter bw) throws IOException 
	{

		int sz=state.board.length;
		StringBuilder sb= new StringBuilder();
		sb.append("    0 1 2 3 4 5 6 7");

		for(int i=0;i<sz;i++)
		{
			sb= new StringBuilder();
			sb.append(i+"-> ");
			for(int j=0;j<sz;j++)
			{
				if(state.board[i][j]==W)
					sb.append(W);
				if(state.board[i][j]==B)
					sb.append(B);
				if(state.board[i][j]==EMPTY)
					sb.append(EMPTY);
				if(j<sz-1)
					sb.append(' ');
			}
			bw.append(sb.toString()+"\n");
		}
	}
}
