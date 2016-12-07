package coms572;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MonteCarloTreeSearch {
	private Board board;
	private int turns = 2000;

	public MonteCarloTreeSearch(Board board){
		this.board = board;
	}
	public Move getMonteCarloMove(){
		ArrayList<Move> moves = board.legalMoves();
		Random rndmGenerator = new Random();
		int index = rndmGenerator.nextInt(moves.size());
		Move resultMove = moves.get(index);
		boolean win = false;
		Collections.shuffle(moves);
		for (Move move : moves) {
			board.makeMove(move);
			board.addMove(move);
			win = board.piecesContiguous(Constants.machine);
			if (win) {
				System.out.println("Move results in win for machine");
				board.retract();
				return move;
			} else {
				board.retract();
			}
		}
		if (!win) {
			for (Move move: moves) {
				turns = 500;
				Board b = this.board.cloneBoard();
				win = runSimulation(b);
				if (win) {
					System.out.println("Move results in win for machine");
					return move;
				}
				if(turns <= 0)
					return resultMove;
			}
		}
		return resultMove;
	}
	
	private boolean runSimulation(Board b) {
		if (turns <= 0) {
			return false;
		}
		Player curr = b.getCurrPlayer();
		Player op = getNextPlayer();
		Board simBoard; 
		ArrayList<Move> moves = b.legalMoves();
		boolean win = false;
		turns--;
		for (Move move : moves) {
			simBoard = b.cloneBoard();
			simBoard.makeMove(move);
			win = simBoard.piecesContiguous(Constants.machine);
			if(win){
				return true;
			}
			else{
				simBoard.setCurrPlayer(getNextPlayer());
				simBoard.makeMove(simBoard.getRandomMove());
				if(simBoard.piecesContiguous(simBoard.getCurrPlayer()))
					return false;
			}
			runSimulation(simBoard);
		}
		return false;
	}

	public Player getNextPlayer(){
		if(board.getCurrPlayer() == Constants.user)
			return Constants.machine;
		else
			return Constants.user;
	}
}
