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

	/*public Move getMonteCarloMove(){
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
                resultMove = move;
                win = true;
                break;
            } else {
                board.retract();
            }
        }
        if (!win) {
            for (Move move: moves) {
                //int turns = 200;
                Board b = this.board.cloneBoard();
                GameSimulation simulation = new GameSimulation(b);
                win = runSimulation(simulation, move);
                if (win) {
                	System.out.println("Move results in win for machine");
                    resultMove = move;
                    break;
                }
                if(turns <= 0)
                	break;
            }
        }
		return resultMove;
	}
	 */
	public Player getNextPlayer(){
		if(board.getCurrPlayer() == Constants.user)
			return Constants.machine;
		else
			return Constants.user;
	}

	/** Run a game SIMULATION of N turns and return true iff
	 *  the game is winnable by THIS within N turns. The first
	 *  move is FIRSTMOVE. Returns false if the game cannot be won
	 *  or the opponent wins first.*/
	/*private boolean runSimulation(GameSimulation simulation, Move firstMove) {
		if (turns <= 0) {
			return false;
		}
		Player curr = simulation.simBoard.getCurrPlayer();
		Player op = getNextPlayer(); 
		simulation.makeMove(firstMove);
		//simulation.simBoard.addMove(firstMove);
		if (simulation.simulationWon(Constants.machine)) {
			System.out.println("Move results in win for machine");
			return true;
		}
		simulation.makeRandomMove(op);
		if (simulation.simulationWon(Constants.user)) {
			return false;
		}
		//simulation.decreaseTurns();
		turns--;
		ArrayList<Move> legalMoves = simulation.legalMoves();
		for (Move move : legalMoves) {
			GameSimulation simCopy = new GameSimulation(
					simulation.getBoard().cloneBoard());
			boolean win = runSimulation(simCopy, move);
			if (win) {
				System.out.println("Move results in win for machine");
				return true;
			}
		}
		return false;
	}*/

	/** A class representing a game simulation. This class is
	 *  controlled externally by a MachinePlayer.
	 */
	private class GameSimulation {
		/** The amount of turns left in this game. */
		private int _turns;
		private Board simBoard;
		//private char[][] _config;
		/** The simulation's game board. *//*
		 */

		/** Start a new simulation, using
		 *  the initial CONFIG, with only
		 *  TURNS number of turns allowed by each side. */
		/*GameSimulation(char[][] config, int turns) {
            _turns = turns;
            _config = config;
            this.simBoard = new Board(_config, )
        }*/

		/*GameSimulation() {
			this.simBoard = new Board(board);
			_turns = 500;
		}
		 */
		GameSimulation(Board board) {
			this.simBoard = new Board(board);
			//_turns = turns;
		}

		/** Perform a MOVE by SIDE. */
		/*void makeMove(Player player, Move move) {
            MutableBoard simBoard = new MutableBoard(_config, player);
            simBoard.makeMove(move);
            _config = simBoard.getConfigCopy();
        }*/

		void makeMove(Move move) {
			this.simBoard.makeMove(move);
			//this._config = simBoard.getConfigCopy();
		}

		/** Returns true iff this game simulation has been won
		 *  by SIDE. */
		boolean simulationWon(Player player) {
			return this.simBoard.piecesContiguous(player);
		}

		/** Returns a list of SIDE's legal moves. */
		ArrayList<Move> legalMoves() {
			Board simBoard = new Board(this.simBoard);
			return simBoard.legalMoves();
		}

		/** Perform a random move by SIDE.
		 *  Does nothing if no legal move can be made. */
		void makeRandomMove(Player player) {
			Random rndmGenerator = new Random();

			ArrayList<Move> legalMoves = legalMoves();
			if (legalMoves.size() != 0) {
				int index = rndmGenerator.nextInt(legalMoves.size());
				Move move = legalMoves.get(index);
				makeMove(move);
			}
		}

		/** Decrease the number of turns allowed by one. */
		void decreaseTurns() {
			_turns -= 1;
		}

		/** Returns true if this simulation has turns left. */
		boolean hasTurns() {
			return _turns > 0;
		}

		/** Returns this simulation's number of turns. */
		int getTurns() {
			return _turns;
		}

		/*public char[][] get_config() {
			return _config;
		}

		public void set_config(char[][] _config) {
			this._config = _config;
		}*/


		public Board getBoard() {
			return board;
		}

		public void setBoard(Board board) {
			this.simBoard = board;
		}

	}
}
