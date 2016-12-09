package coms572;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game2 {
	private Board board;
	private Player player1, player2;
	private final int moveLimit = 300;
	PrintWriter writer;
	public ArrayList<Double> times = new ArrayList<>();
	public double maxTime = Double.MIN_VALUE, minTime = Double.MAX_VALUE, totalTime = 0.0, time = 0.0;
	private int i;
	public double avgTime;
	public String result;
	private MonteCarloTreeSearch mcts;
	private MCTS<Board, Move, Player> mc;

	public Game2(int i) {
		try {
			writer = new PrintWriter("C:\\Users\\Bijon\\workspace\\LineOfAction\\LOA_Output"+i+".txt","UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		board=new Board();
		player1 = Constants.user;
		player2 = Constants.machine;
		board.setCurrPlayer(player1);
		this.i =i;
		mcts = new MonteCarloTreeSearch(writer);
	}
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void startGame() {
		System.out.println("Play Line of Action! Start Game! "+player1.get_name()+" vs. "+player2.get_name());
		writer.println("Play Line of Action! Start Game! "+player1.get_name()+" vs. "+player2.get_name());
		//Scanner in = new Scanner(System.in);
		board.printBoard(writer);
		while (true) {
			if(board.movesMade() >= moveLimit){
				if(board.gameOver()){
					if(board.piecesContiguous(player1)){
						System.out.println(player1.get_name()+" wins!");
						writer.println(player1.get_name()+" wins!");
						result = player1.get_name();
					}
					else if(board.piecesContiguous(player2)){
						System.out.println(player2.get_name()+" wins!");
						writer.println(player2.get_name()+" wins!");
						result = player2.get_name();
					}
					break;
				}
				else{
					System.out.println("Total moves made: "+board.movesMade());
					writer.println("Total moves made: "+board.movesMade());
					System.out.println("Game drawn!");
					writer.println("Game drawn!");
					result = "Draw!";
				}
				break;
			}
			ArrayList<Move> moves = board.legalMoves();
			if (moves.size() > 0) {
				System.out.println("It is player " + board.getCurrPlayer().get_name()+ "'s move.");
				writer.println("It is player " + board.getCurrPlayer().get_name()+ "'s move.");
				System.out.println("Where would you like to move?");
				writer.println("Where would you like to move?");
				/*System.out.println("From Column: ");
				int c0 = in.nextInt();
				System.out.println("From row: ");
				int r0 = in.nextInt();
				System.out.println("To Column: ");
				int c1 = in.nextInt();
				System.out.println("To row: ");
				int r1 = in.nextInt();

				System.out.println("Enter move (c0r0-c1r1): ")
				String moveString = sc.nextLine();
				int c0 = moveString.charAt(0) - '0';
				int r0 = moveString.charAt(1) - '0';
				int c1 = moveString.charAt(3) - '0';
				int r1 = moveString.charAt(4) - '0';

				Move move = new Movee(c0,r0,c1,r1);
				 */
				Move myMove;
				if(board.getCurrPlayer() == player1)
					myMove = getRandomMove(moves);
				else{
					double t1 = System.currentTimeMillis();
					AIAgent ai = new AIAgent(this.board.cloneBoard(), writer);
					MCTSNode node = new MCTSNode(this.board);
					Move mcMove = mcts.getMCTSMove(node);
					double t2 = System.currentTimeMillis();
					myMove = mcMove == null ? getRandomMove(moves) : mcMove;
					time = (t2-t1)/1000;
					times.add(time);
					maxTime = Math.max(maxTime, time);
					minTime = Math.min(minTime, time);
					totalTime += time;
					System.out.println("Agent move: "+mcMove+" \nTime taken to decide move: "+((t2-t1)/1000)+" seconds");
					writer.println("Agent move: "+mcMove+" \nTime taken to decide move: "+((t2-t1)/1000)+" seconds");
					
				}
				if (board.isLegal(myMove)) {
					writer.println("Good Move");
					board.makeMove(myMove);
					board.printBoard(writer);
					board.printBoard();
					if(board.gameOver()){
						if(board.piecesContiguous(player1) && !board.piecesContiguous(player2)){
							System.out.println("Total moves made: "+board.movesMade());
							writer.println("Total moves made: "+board.movesMade());
							System.out.println("Game Over! "+player1.get_name()+" wins!");
							writer.println("Game Over! "+player1.get_name()+" wins!");
							result = player1.get_name();
						}
						else if(board.piecesContiguous(player2) && !board.piecesContiguous(player1)){
							System.out.println("Total moves made: "+board.movesMade());
							writer.println("Total moves made: "+board.movesMade());
							System.out.println("Game Over! "+player2.get_name()+" wins!");
							writer.println("Game Over! "+player2.get_name()+" wins!");
							result = player2.get_name();
						}
						else{
							System.out.println("Total moves made: "+board.movesMade());
							writer.println("Total moves made: "+board.movesMade());
							System.out.println("Game Drawn!");
							writer.println("Game Drawn!");
							result = "Draw!";
						}
						break;
					}
				} else {
					System.out.println("Move Invalid");
					writer.println("Move Invalid");
				}
			} else {
				System.out.println("You have no where to go.");
				writer.println("You have no where to go.");
				setNextPlayer();
			}
		}
		avgTime = totalTime / times.size();
		writer.println("Maximum time taken for a move: "+maxTime +" seconds");
		writer.println("Minimum time taken for a move: "+minTime +" seconds");
		writer.println("Average time taken per move: "+avgTime +" seconds");
		writer.println("Total time taken for all moves: "+totalTime +" seconds");
		writer.close();
	}
	public void setNextPlayer(){
		if(board.getCurrPlayer() == player1)
			board.setCurrPlayer(player2);
		else
			board.setCurrPlayer(player1);
	}

	public Move getRandomMove(ArrayList<Move> moves){
		Random rndmGenerator = new Random();
		int index = rndmGenerator.nextInt(moves.size());
		return moves.get(index);
	}
	
	public static void main(String args[]) {
		Game2 g = new Game2(0);
		g.startGame();
	}

}
