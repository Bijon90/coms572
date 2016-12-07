package coms572;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
	Board board;
	Player player1, player2;
	private final int moveLimit = 300;
	PrintWriter writer;

	public Game() {
		try {
			writer = new PrintWriter("C:\\Users\\Bijon\\workspace\\LineOfAction\\LOA_Output.txt","UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		board=new Board();
		player1 = Constants.user;
		player2 = Constants.machine;
		board.setCurrPlayer(player2);
		startGame();
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
					}
					else{
						System.out.println(player2.get_name()+" wins!");
						writer.println(player2.get_name()+" wins!");
					}
					break;
				}
				else{
					System.out.println("Total moves made: "+board.movesMade());
					writer.println("Total moves made: "+board.movesMade());
					System.out.println("Game drawn!");
					writer.println("Game drawn!");
				}
				break;
			}
			setNextPlayer();
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
					MonteCarloTreeSearch mcTS = new MonteCarloTreeSearch(this.board.cloneBoard());
					Move mcMove = mcTS.getMonteCarloMove(); 
					double t2 = System.currentTimeMillis();
					myMove = mcMove == null ? getRandomMove(moves) : mcMove;
					System.out.println("Monte Carlo move: "+mcTS.getMonteCarloMove()+" \nTime taken to decide move: "+((t2-t1)/1000)+" seconds");
					writer.println("Monte Carlo move: "+mcTS.getMonteCarloMove()+" \nTime taken to decide move: "+((t2-t1)/1000)+" seconds");
					
				}
				if (board.isLegal(myMove)) {
					writer.println("Good Move");
					board.makeMove(myMove);
					board.printBoard(writer);
					board.printBoard();
					board.addMove(myMove);
					if(board.gameOver()){
						if(board.piecesContiguous(player1) && !board.piecesContiguous(player2)){
							System.out.println("Total moves made: "+board.movesMade());
							writer.println("Total moves made: "+board.movesMade());
							System.out.println("Game Over! "+player1.get_name()+" wins!");
							writer.println("Game Over! "+player1.get_name()+" wins!");
						}
						else if(board.piecesContiguous(player2) && !board.piecesContiguous(player1)){
							System.out.println("Total moves made: "+board.movesMade());
							writer.println("Total moves made: "+board.movesMade());
							System.out.println("Game Over! "+player2.get_name()+" wins!");
							writer.println("Game Over! "+player2.get_name()+" wins!");
						}
						else{
							System.out.println("Total moves made: "+board.movesMade());
							writer.println("Total moves made: "+board.movesMade());
							System.out.println("Game Drawn!");
							writer.println("Game Drawn!");
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
		Game g = new Game();
	}
}
