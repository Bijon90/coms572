package coms572;

import java.util.ArrayList;
import java.util.Random;

public class MonteCarloTreeSearch {
	private Board board;
	
	public MonteCarloTreeSearch(Board board){
		this.board = board;
	}
	
	public Move getMonteCarloMove(){
		Move resultMove = new Move();
		ArrayList<Move> moves = board.legalMoves();
		return resultMove;
	}
	
	public Player setNextPlayer(){
		if(board.getCurrPlayer() == Constants.user)
			return Constants.machine;
		else
			return Constants.user;
	}
	
	/** Run a game SIMULATION of N turns and return true iff
     *  the game is winnable by THIS within N turns. The first
     *  move is FIRSTMOVE. Returns false if the game cannot be won
     *  or the opponent wins first.*/
    private boolean runSimulation(GameSimulation simulation, Move firstMove) {
        Player player = board.getCurrPlayer();
        Player opponent = setNextPlayer();
        simulation.makeMove(player, firstMove);
        if (simulation.simulationWon(player)) {
            return true;
        }
        simulation.makeRandomMove(opponent);
        if (simulation.simulationWon(opponent)) {
            return false;
        }
        ArrayList<Move> legalMoves = simulation.legalMoves(player);
        for (Move move : legalMoves) {
            GameSimulation simCopy = new GameSimulation(
                    simulation.getConfig(), simulation.getTurns());
            boolean win = runSimulation(simCopy, move);
            if (win) {
                return true;
            }
        }
        return false;
    }

    /** A class representing a game simulation. This class is
     *  controlled externally by a MachinePlayer.
     *  @author Nick Holt */
    private class GameSimulation {
        /** Start a new simulation, using
         *  the initial CONFIG, with only
         *  TURNS number of turns allowed by each side. */
        GameSimulation(char[][] config, int turns) {
            _turns = turns;
            _config = config;
        }

        /** Perform a MOVE by SIDE. */
        void makeMove(Player side, Move move) {
            MutableBoard simBoard = new MutableBoard(_config, side);
            simBoard.makeMove(move);
            _config = simBoard.getConfigCopy();
        }
        
        /** Returns true iff this game simulation has been won
         *  by SIDE. */
        boolean simulationWon(Player side) {
            Board simboard = new Board(_config, side);
            return simboard.piecesContiguous(side);
        }
        
        /** Returns a list of SIDE's legal moves. */
        ArrayList<Move> legalMoves(Player side) {
            Board simBoard = new Board(_config, side);
            return simBoard.legalMoves();
        }
        
        /** Perform a random move by SIDE.
         *  Does nothing if no legal move can be made. */
        void makeRandomMove(Player side) {
            GamePlay game = get_game();
            Random randomsource = game.getRandomSource();
            ArrayList<Move> legalMoves = legalMoves(side);
            if (legalMoves.size() != 0) {
                Move move = legalMoves.get(
                        (int) (randomsource.nextDouble()
                        * legalMoves.size() - 1));
                makeMove(side, move);
            }
        }
    }
}
