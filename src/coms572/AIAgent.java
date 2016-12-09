package coms572;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Bijon
 *
 */
public class AIAgent {
	private Board board;
	private int turns = 2000;
	PrintWriter writer;

	/** Links a move to its value. */
	private HashMap<Move, Double> _map = new HashMap<Move, Double>();
	/** Worst possible move value. */
	private static final double WORST = -100;
	/** Column center of board. */
	private static final double COMCOL = 4.5;
	/** Row center of board. */
	private static final double COMROW = 4.5;
	public boolean terminalWin = false;

	
	/**
	 * Constructor
	 * @param board
	 */
	public AIAgent(Board board){
		this.board = board;
		try{
			this.writer = new PrintWriter(new File("C:\\Users\\Bijon\\workspace\\LineOfAction\\LOA_Output.txt","UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Constructor
	 * @param board
	 * @param writer
	 */
	public AIAgent(Board board, PrintWriter writer){
		this.board = board;
		this.writer = writer;
	}

	/**
	 * Gets agent move by running a simulation of the game driven by evaluation function
	 * @return selected move
	 */
	public Move getAgentMove(){
		ArrayList<Move> moves = board.legalMoves();
		if(moves.size() <= 0)
			return null;
		Move resultMove = guessBestMove(this.board);
		boolean win = false;
		Collections.shuffle(moves);
		Board simBoard;
		for (Move move : moves) {
			simBoard = board.cloneBoard();
			simBoard.makeMove(move);
			win = simBoard.piecesContiguous(Constants.machine);
			if (win) {
				terminalWin = win;
				return move;
			} else {
				if(simBoard.piecesContiguous(Constants.user)){
					simBoard.retract();
					continue;
				}
				else{
					turns = 500;
					win = runSimulation(simBoard.cloneBoard());
					if(win){
						terminalWin = win;
						System.out.println("Move can result in win for machine");
						writer.println("Move can result in win for machine");
						return move;
					}
					else 
						continue;
				}
			}
		}
		return resultMove;
	}

	/**
	 * Runs a simulation of the game from current configuration of the board
	 * @param b
	 * @return true or false depending on whether agent wins or loses
	 */
	private boolean runSimulation(Board b) {
		//If turns exceeds the pre-defined maximum limit, 
		//simulation ends and random move is returned
		if (turns <= 0) {
			return false;
		}
		Board forCheckMove = b.cloneBoard();
		Move currMove = guessBestMove(forCheckMove);
		//Move currMove = b.getRandomMove();	//When moves are chosen randomly during simulation
		b.makeMove(currMove);
		turns --;
		if(b.piecesContiguous(Constants.machine)){
			terminalWin = true;
			return true;
		}
		else if(b.piecesContiguous(Constants.user)){
			terminalWin = false;
			return false;
		}
		else
			runSimulation(b);
		return false;
	}

	/**
	 * @return opponent
	 */
	public Player getNextPlayer(){
		if(board.getCurrPlayer() == Constants.user)
			return Constants.machine;
		else
			return Constants.user;
	}

	/**
	 * @param start
	 * @param depth
	 * @param cutoff 
	 * @return best move going until DEPTH steps ahead on board and usning CUTOFF to prune 
	 */
	public Move findBestMove(Board start, int depth, double cutoff) {
		Player curPlayer = start.getCurrPlayer();
		if (start.piecesContiguous(curPlayer)) {
			_map.put(null, Double.MAX_VALUE);
			return null;
		} else if (start.piecesContiguous(curPlayer.getNextPlayer())) {
			_map.put(null, WORST);
			return null;
		} else if (depth == 0) {
			Move move = guessBestMove(start);
			return move;
		}
		double value = WORST;
		Move bestSoFar = null;
		Board copy;
		ArrayList<Move> lMoves = start.legalMoves();
		for (Move move : lMoves) {
			copy = start.cloneBoard();
			Move response = guessBestMove(copy);
			if (-_map.get(response) > value) {
				value = -_map.get(response);
				bestSoFar = move;
				_map.put(bestSoFar, value);
				if (value >= cutoff) {
					break;
				}
			}
		}
		return bestSoFar;
	}


	/**
	 * @param board
	 * @return guess best move on board for current player.
	 */
	public Move guessBestMove(Board board) {
		Move bestFar = null;
		double val = WORST;
		ArrayList<Move> currMoves = board.legalMoves();
		for (Move move : currMoves) {
			board.makeMove(move);
			double eval = eval(board);
			if (eval > val) {
				bestFar = move;
				val = eval;
			}
			board.retract();
		}
		_map.put(bestFar, val);
		return bestFar;
	}

	/** 
	 * @param board
	 * @return evaluation value of board for current player. 
	 */
	public double eval(Board board) {
		char curr = board.getCurrPlayer().getOpponent();
		int[] com = findCOM(board,curr);
		int colCom = com[1];
		int rowCom = com[0];
		int count = com[2];
		int distFromCom = distFromCOM(board, colCom, rowCom, curr);
		int empSq = distFromCom - minDist(count, colCom, rowCom);
		int central = centralize(board,curr);
		double evalSide = 1.0 / empSq + 1.0 / central;
		char opp = board.getCurrPlayer().get_marker();
		int[] comOp = findCOM(board, opp);
		int colComOp = comOp[1];
		int rowComOp = comOp[0];
		int countOp = comOp[2];
		int distFromComOp = distFromCOM(board, colComOp, rowComOp, opp);
		int empSqOp = distFromComOp - minDist(countOp, colComOp, rowComOp);
		int centralOp = centralize(board, opp);
		double evalOp = 1.0 / empSqOp + 1.0 / centralOp;
		return evalSide - evalOp;
	}

	/** 
	 * @param board, pl
	 * @return the center of mass for the pieces of player with marker pl. 
	 */
	public int[] findCOM(Board board, char pl) {
		int[] com = new int[] {0, 0, 0};
		int count = 0;
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board.getPiece(i, j) == pl) {
					com[0] += i;
					com[1] += j;
					count++;
				}
			}
		}
		com[0] /= count;
		com[1] /= count;
		com[2] = count;
		return com;
	}

	/**
	 * @param board
	 * @param pl
	 * @return centralization values to pieces of player with marker pl on board 
	 * depending on how far it is from the center of the board. 
	 * Further pieces get less numbers. Return sum of the value for all the pieces
	 */
	public int centralize(Board board, char pl) {
		int sum = 0;
		int min = 0;
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board.getPiece(i, j) == pl) {
					int colDif = (int) Math.abs(COMCOL - i);
					int rowDif = (int) Math.abs(COMROW - j);
					min = (int) Math.min(colDif, rowDif);
					sum += rowDif + colDif - min;
				}
			}
		}
		return sum;
	}

	/**
	 * @param board
	 * @param comX
	 * @param comY
	 * @param pl
	 * @return sum of distances between the center of mass and all pieces from COMX, COMY on board for pl
	 */
	public int distFromCOM(Board board, int comX, int comY, char pl) {
		int sum = 0;
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				if (board.getPiece(i, j) == pl) {
					int xDif = Math.abs(i - comX);
					int yDif = Math.abs(j - comY);
					int min = Math.min(xDif, yDif);
					sum += xDif + yDif - min;
				}
			}
		}
		return sum;
	}

	/**
	 * @param count
	 * @param comX
	 * @param comY
	 * @return the minimum number of distance in terms of squares for all pieces from COMX, COMY
	 */
	public int minDist(int count, int comX, int comY) {
		count -= 1;
		if ((comX == 1 && comY == 1) || (comX == 1 && comY == 8)
				|| (comX == 8 && comY == 1)
				|| (comX == 8 && comY == 8)) {
			return count;
		} else if (comX == 0 || comY == 0) {
			if (count <= 5) {
				return count;
			} else {
				return 5 + 2 * (count - 5);
			}
		} else {
			if (count <= 8) {
				return count;
			} else {
				return 8 + 2 * (count - 8);
			}
		}
	}
}
