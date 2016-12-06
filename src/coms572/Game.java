package coms572;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
	Board board;
	Player player1, player2;
	private final int moveLimit = 500;

	public Game() {
		board=new Board();
		player1 = Constants.user;
		player2 = Constants.machine;
		board.setCurrPlayer(player2);
		startGame();
	}

	public void startGame() {
		//Scanner in = new Scanner(System.in);
		board.printBoard();
		while (true) {
			/*if(board.movesMade() >= moveLimit){
				if(board.gameOver()){
					System.out.println(board.getCurrPlayer().get_name()+" wins!");
				}
				else
					System.out.println("Game drawn!");
				break;
			}*/
			setNextPlayer();
			ArrayList<Move> moves = board.legalMoves();
			if (moves.size() > 0) {
				System.out.println("It is player " + board.getCurrPlayer().get_name()+ "'s move.");
				System.out.println("Where would you like to move?:");
				/*System.out.println("X: ");
				int x = in.nextInt();
				System.out.println("Y: ");
				int y = in.nextInt();*/
				Move myMove = getRandomMove(moves);
				if (board.isLegal(myMove)) {
					System.out.println("Good Move");
					board.makeMove(myMove);
					board.printBoard();
					board.addMove(myMove);
					if(board.gameOver()){
						System.out.println("Game Over! "+board.getCurrPlayer().get_name()+" wins!");
						break;
					}
					//doMinimaxMove(player.getOpposite());
				} else {
					System.out.println("Move Invalid");
				}
			} else {
				System.out.println("You have no where to go.");
				setNextPlayer();
			}
			/*MutableBoard board2 = new MutableBoard(board);
			Game game1 = new Game();
			game1.setNextPlayer();
			if (board.legalMoves().size() == 0
					&& board2.legalMoves().size() == 0){
				System.out.println("Game Draw!");
				break;
			}*/
		}
		//int[] results = board.getBoardScore();
		/*System.out.println("Player 1 had: " + results[1]
				+ " points and Player 2 had: " + results[2]);*/
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

	/*	public void doMinimaxMove(Player myPlayer){
		Minimax max = new Minimax(myPlayer, board);
		board.doMove(max.doMiniMax(), myPlayer);
	}*/
	
	public static void main(String args[]) {
		Game g = new Game();
	}
}
