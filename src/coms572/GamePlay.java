package coms572;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlay {
	
	public static final char BLACK = 'X', WHITE = 'O', EMPTY = '*', BUFFER = '.';
	
    /** The official game board. */
    private MutableBoard _board = new MutableBoard();

    /** The white player. */
    private MachinePlayer _wp;

    /** The black player. */
    private HumanPlayer _bp;

    /** A source of random numbers, primed to deliver the same sequence in
     *  any Game with the same seed value. */
    private Random _randomSource;

    /** The number of human players. */
    private int _numHuman;

    /** The side of the player (assuming there is one AI). */
    private Player _side0;

    /** The random generator seed. */
    private long _seed;

    /** True if the game has a time limit. */
    private boolean hasTimeLimit = false;

    /** The maximum total time for all moves. */
    private int _time;

    /** The time black has taken thus far. */
    private int _bTime = 0;

    /** The time white has taken thus far. */
    private int _wTime = 0;

    /** True iff AI are activated. */
    private boolean aiActive = false;

    /** The game's primary input stream. */
    private Scanner inp;

    /** True if the game is over. */
    private boolean gameComplete = false;

    /** Maximum value represtable by an int. */
    private static final int MAX_INT = 2147483647;

    /** A number. */
    private static final int THOUSAND = 1000;
    
    public GamePlay(){}

    /** A new Game between NUMHUMAN humans and 2-NUMHUMAN AIs.  SIDE0
     *  indicates which side the first player (known as ``you'') is
     *  playing.  SEED is a random seed for random-number generation.
     *  TIME is the time limit each side has to make its moves (in seconds).
     *  A TIME value of <=0 means there is no time limit.  A SEED value <= 0
     *  means to use a randomly seeded generator.
     */
    public GamePlay(int numHuman, Player side0, long seed, int time) {
        if (seed <= 0) {
            _randomSource = new Random();
        } else {
            _randomSource = new Random(seed);
        }
        _numHuman = numHuman;
        _side0 = side0;
        _seed = seed;
        if (time <= 0) {
            hasTimeLimit = false;
            _time = MAX_INT;
        } else {
            hasTimeLimit = true;
            _time = time;
        }
    }

    /** Return the current board. */
    MutableBoard getBoard() {
        return _board;
    }

    /** Return a move from the terminal.  Processes any intervening commands
     *  as well. A move is a string of the form "c0r0-c1r1",
     *  "s", "p", "q", or "#". */
    String getMove() {
        MutableBoard board = getBoard();
        System.out.printf("%s's command > ", board.getCurrPlayer());
        System.out.flush();
        Pattern pat = Pattern.compile("\\s*([a-z][\\d]-[a-z]"
                                      + "[\\d])\\s*[\\S\\s]*");
        String line = inp.nextLine();
        Matcher mat = pat.matcher(line);
        if (mat.matches()) {
            return mat.group(1);
        } else {
            if (line.equals("")) {
                return getMove();
            }
            String command = line.substring(0, 1);
            if (command.equals("s")) {
                System.out.print(board);
            } else if (command.equals("p")) {
                activateAI();
                //if (_board.getCurrPlayer().get_marker() == WHITE
                //        && _wp instanceof MachinePlayer) {
                if (_board.getCurrPlayer().get_marker() == WHITE){
                    return _wp.makeMove().toString();
                } else if (_board.getCurrPlayer().get_marker() == BLACK){
                        //&& _bp instanceof MachinePlayer) {
                    return _bp.makeMove().toString();
                }
            } else if (command.equals("q")) {
                System.out.println("Game terminated.");
                System.exit(1);
            } else if (command.equals("t")) {
                if (hasTimeLimit()) {
                    System.out.printf("%s has %d seconds left\n", board.getCurrPlayer()
                            , timeRemaining(board.getCurrPlayer().get_marker()));
                } else {
                    System.out.println("No time limit was set.");
                }
            }
            return getMove();
        }
    }

    /** Play this game, printing any transcript and other results.
     *  White moves first.*/
    public void play() {
        inp = new Scanner(System.in);
        _wp = new MachinePlayer(WHITE, this);
        _bp = new HumanPlayer(BLACK, this);
        while (hasTime(_wp) || hasTime(_bp)) {
            _board = new MutableBoard(_board.getBoard(), _wp);
            _board.makeMove(_wp.makeMove());
            if (_board.piecesContiguous(_wp)) {
                inp.close();
                System.out.println("White wins.");
                System.exit(1);
                gameComplete = true;
            }
            _board = new MutableBoard(_board.getConfigCopy(), _bp);
            _board.makeMove(_bp.makeMove());
            if (_board.piecesContiguous(_bp)) {
                inp.close();
                System.out.println("Black wins.");
                System.exit(1);
                gameComplete = true;
            }
        }
        if (!hasTime(_wp)) {
            System.out.println("White has run out of time.");
            gameComplete = true;
        } else {
            assert !hasTime(_bp);
            System.out.println("Black has run out of time.");
            gameComplete = true;
        }
    }

    /** Play this game, printing any transcript and other results.
     *  Black moves first.*/
    public void alternativePlay() {
        inp = new Scanner(System.in);
        _wp = new MachinePlayer(WHITE, this);
        _bp = new HumanPlayer(BLACK, this);
        //Stopwatch stopwatch = new Stopwatch();
        double stopwatch, timeStart, timeEnd;
        int maxMoves = 50;
        while (hasTime(_wp) || hasTime(_bp)) {
            _board = new MutableBoard(_board.getConfigCopy(), _bp);
            timeStart = System.currentTimeMillis();
            _board.makeMove(_bp.makeMove());
            timeEnd = System.currentTimeMillis();
            addTime(_bp, (int) (timeEnd - timeStart) / THOUSAND);
            if (_board.piecesContiguous(_bp)) {
                inp.close();
                System.out.println("Black wins.");
                System.exit(1);
                gameComplete = true;
            }
            _board = new MutableBoard(_board.getConfigCopy(), _wp);
            timeStart = System.currentTimeMillis();
            _board.makeMove(_wp.makeMove());
            timeEnd = System.currentTimeMillis();
            addTime(_wp, (int) (timeEnd - timeStart) / THOUSAND);
            if (_board.piecesContiguous(_wp)) {
                inp.close();
                System.out.println("White wins.");
                System.exit(1);
                gameComplete = true;
            }
        }
        if (!hasTime(_wp)) {
            inp.close();
            System.out.println("White has run out of time.");
            System.exit(1);
            gameComplete = true;
        } else {
            assert !hasTime(_bp);
            inp.close();
            System.out.println("Black has run out of time.");
            System.exit(1);
            gameComplete = true;
        }
    }

    /** Return time remaining for SIDE (in seconds).
     *  Assumes a time limit has been set.*/
    int timeRemaining(char currMarker) {
        if (currMarker == WHITE) {
            return _time - _wTime;
        } else {
            assert currMarker == BLACK;
            return _time - _bTime;
        }
    }

    /** Returns true iff SIDE has time remaining. */
    private boolean hasTime(Player side) {
        if (hasTimeLimit) {
            return timeRemaining(side.get_marker()) > 0;
        }
        return true;
    }

    /** Return the random number generator for this game. */
    Random getRandomSource() {
        return _randomSource;
    }

    /** Return an integer from my random source. */
    public double getRandom() {
        return _randomSource.nextDouble();
    }

    /** Add TIME to SIDE's total. */
    public void addTime(Player side, int time) {
        assert side.get_marker() == BLACK || side.get_marker() == WHITE;
        if (side.get_marker() == BLACK) {
            _bTime += 1;
        } else {
            _wTime += 1;
        }
    }

    /** Return SIDE's time taken thus far. */
    public int getTime(Player side) {
        assert side.get_marker() == BLACK || side.get_marker() == WHITE;
        if (side.get_marker() == BLACK) {
            return _bTime;
        } else {
            return _wTime;
        }
    }

    /** Activate the game's AI. */
    public void activateAI() {
        aiActive = true;
        if (_numHuman == 0) {
            _wp = new MachinePlayer(WHITE, this);
            _bp = new HumanPlayer(BLACK, this);
        } else if (_numHuman == 1) {
            if (_side0 == _wp) {
                _bp = new HumanPlayer(BLACK, this);
            } else {
                assert _side0 == _bp;
                _wp = new MachinePlayer(WHITE, this);
            }
        }
    }

    /** Return true iff AI are active. */
    public boolean aiActive() {
        return aiActive;
    }

    /** Sets the white player to PLAYER. */
    public void setWhite(MachinePlayer player) {
        _wp = player;
    }

    /** Sets the black player to PLAYER. */
    public void setBlack(HumanPlayer player) {
        _bp = player;
    }

    /** Returns true if this game has a time limit. */
    public boolean hasTimeLimit() {
        return hasTimeLimit;
    }
}
