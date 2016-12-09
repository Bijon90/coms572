package coms572;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

	public AIAgent(Board board){
		this.board = board;
		try{
			this.writer = new PrintWriter(new File("C:\\output.txt"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public AIAgent(Board board, PrintWriter writer){
		this.board = board;
		this.writer = writer;
	}

	public Move getMonteCarloMove(){
		ArrayList<Move> moves = board.legalMoves();
		if(moves.size() <= 0)
			return null;
		Move resultMove = findBestMove(this.board, 2, Double.MAX_VALUE);
		boolean win = false;
		Collections.shuffle(moves);
		Board simBoard;
		for (Move move : moves) {
			simBoard = board.cloneBoard();
			simBoard.makeMove(move);
			win = simBoard.piecesContiguous(Constants.machine);
			if (win) {
				//System.out.println("Move results in win for machine");
				//writer.println("Move results in win for machine");
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

	private boolean runSimulation(Board b) {
		if (turns <= 0) {
			return false;
		}
		Board forCheckMove = b.cloneBoard();
		Move currMove = guessBestMove(forCheckMove);
		b.makeMove(currMove);
		turns --;
		if(b.piecesContiguous(Constants.machine)){
			return true;
		}
		else if(b.piecesContiguous(Constants.user)){
			return false;
		}
		else
			runSimulation(b);
		return false;
	}

	public Player getNextPlayer(){
		if(board.getCurrPlayer() == Constants.user)
			return Constants.machine;
		else
			return Constants.user;
	}

	/** Return best move DEPTH steps ahead on board START of turn SIDE
	 * using CUTOFF to prune. */
	Move findBestMove(Board start, int depth, double cutoff) {
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
		Move bestFar = null;
		Board copy;
		ArrayList<Move> lMoves = start.legalMoves();
		for (Move move : lMoves) {
			copy = start.cloneBoard();
			Move response = guessBestMove(copy);
			if (-_map.get(response) > value) {
				value = -_map.get(response);
				bestFar = move;
				_map.put(bestFar, value);
				if (value >= cutoff) {
					break;
				}
			}
		}
		return bestFar;
	}

	/** Return best move at depth 0 on BOARD of SIDE. */
	private Move guessBestMove(Board board) {
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

	/** Return evaluation of BOARD of turn SIDE. */
	private double eval(Board board) {
		char curr = board.getCurrPlayer().getOpponent();
		int[] com = com(board,curr);
		int colCom = com[1];
		int rowCom = com[0];
		int count = com[2];
		int distFromCom = distFromCOM(board, colCom, rowCom, curr);
		int empSq = distFromCom - minDist(count, colCom, rowCom);
		int central = centralize(board,curr);
		double evalSide = 1.0 / empSq + 1.0 / central;
		char opp = board.getCurrPlayer().get_marker();
		int[] comOp = com(board, opp);
		int colComOp = comOp[1];
		int rowComOp = comOp[0];
		int countOp = comOp[2];
		int distFromComOp = distFromCOM(board, colComOp, rowComOp, opp);
		int empSqOp = distFromComOp - minDist(countOp, colComOp, rowComOp);
		int centralOp = centralize(board, opp);
		double evalOp = 1.0 / empSqOp + 1.0 / centralOp;
		return evalSide - evalOp;
	}

	/** Return the center of mass of SIDE on BOARD. */
	private int[] com(Board board, char pl) {
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
	 * Assigns values to piece on BOARD of SIDE depending on how far it is from
	 * center. Further pieces get less numbers. Return sum of the pieces
	 */
	private int centralize(Board board, char pl) {
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
	 * Return sum of distance between the center of mass and all SIDE pieces
	 * from COMX, COMY on BOARD.
	 */
	private int distFromCOM(Board board, int comX, int comY, char pl) {
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
	 * Return the minimum number of squares COUNT number of pieces could be from
	 * COMX, COMY.
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
