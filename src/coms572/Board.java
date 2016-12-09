package coms572;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Board{
	
	private ArrayList<Move> moves;
	private char[][] board;
	private Player currPlayer;
	
	public Board(char[][] initialContents, Player player) {
        board = initialContents;
        currPlayer = player;
        this.moves = new ArrayList<>();
    }

	/** 
	 * A new board in the standard initial position. 
	 */
    public Board() {
    	char[][] initboard = {
    			{ Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.EMPTY, Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.EMPTY, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.WHITE,  Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.WHITE, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.WHITE,  Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.WHITE, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.WHITE,  Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.WHITE, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.WHITE,  Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.WHITE, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.WHITE,  Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.WHITE, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.WHITE,  Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.WHITE, Constants.BUFFER  },
    	        { Constants.BUFFER, Constants.EMPTY, Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.BLACK,  Constants.EMPTY, Constants.BUFFER },
    	        { Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER, Constants.BUFFER  }
    	    };
    	this.setBoard(initboard);
    	this.setCurrPlayer(Constants.user);
        this.moves = new ArrayList<>();
    }
	
    public Board(Board board) {
    	this.board = board.getBoard();
    	this.currPlayer = board.getCurrPlayer();
    	this.moves = new ArrayList<>();
    }
    
    public ArrayList<Move> getMoves() {
		return moves;
	}

	public void setMoves(ArrayList<Move> moves) {
		this.moves = moves;
	}

	public char[][] getBoard() {
		return board;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public Player getCurrPlayer() {
		return currPlayer;
	}

	public void setCurrPlayer(Player currPlayer) {
		this.currPlayer = currPlayer;
	}
	
	/** 
	 *  Return the contents of column C, row R, where 1 <= C,R <= 8,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. 
     */
    public char getPiece(int r, int c){
    	return board[r][c];
    }
    
    /** 
     * @return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c and r each are a digit from 1-8). 
     */
    public char getPiece(String pos){
    	if(isValidPos(pos))
    		return board[(pos.charAt(1) - '0')][pos.charAt(0)-'0'];
    	return '.';
    }
    
    /** 
     * @return the column number (a value in the range 1-8) for SQ.
     *  SQ is in the form cr 
     */
    public int col(String sq) {
        return Integer.parseInt(sq.substring(0));
    }

    /** 
     * @return the row number (a value in the range 1-8) for SQ.
     *  SQ is in the form cr 
     */
    public int row(String sq) {
        return Integer.parseInt(sq.substring(1));
    }
	
    /** 
     * @return true iff move is legal for the player currently on move. 
     */
    public boolean isLegal(Move move) {
        if (move.getC0() > 8 || move.getR0() > 8 
        		|| move.getC1() > 8 || move.getR1() > 8
                || move.getC0() < 1 || move.getR0() < 1
                || move.getC1() < 1 || move.getR1() < 1) {
            return false;
        }
        if (!legalDirectionCheck(move)) {
            return false;
        }
        char originMarker = board[move.getR0()][move.getC0()];
        if (originMarker == currPlayer.getOpponent() || originMarker == '\0') {
            return false;
        }
        ArrayList<ArrayList<Character>> pieces
            = getLinePieces(move.getC0(), move.getR0(), move.getC1(), move.getR1());
        ArrayList<Character> to = pieces.get(0), fro = pieces.get(1);
        int count = 0, dist = -1;
        boolean passed = false;
        for (Character piece : to) {
            dist++;
            if (dist == move.getMoveLength()) {
                if (piece == currPlayer.get_marker()) {
                    return false;
                }
                passed = true;
            }
            if (!passed) {
                if (piece == currPlayer.getOpponent()) {
                    return false;
                }
                if (piece != Constants.EMPTY) {
                    count++;
                }
            } else {
                if (piece != Constants.EMPTY) {
                    count++;
                } else {
                    continue;
                }
            }
        }
        return isLegal2(fro, count, move.getMoveLength());
    }
    
    /**
     * @param fro
     * @param count
     * @param moveLength
     * @return true if move is legal
     */
    private boolean isLegal2(ArrayList<Character> fro, int count, int moveLength) {
        for (Character piece : fro) {
            if (piece != Constants.EMPTY) {
                count++;
            } else {
                continue;
            }
        }
        if (count != moveLength) {
            return false;
        } else {
            return true;
        }
    }
	
    /** 
     * Sub method to get past style check. Check's move's direction and returns true if it is valid. 
     */
    private boolean legalDirectionCheck(Move move) {
        int cd = Math.abs(move.getC1() - move.getC0());
        int cr = Math.abs(move.getR1() - move.getR0());
        if (cd != 0 && cr != 0 && cd != cr) {
            return false;
        }
        return true;
    }

    /** 
     * @return an ArrayList of all legal moves for current player. 
     */
    public ArrayList<Move> legalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        int x = 0, y = 0;
        for (char[] row : board) {
            for (char piece : row) {
                if (piece == currPlayer.get_marker()) {
                    int c = x; int r = y;
                    for (int d = 0; d <= 7; d++) {
                        ArrayList<ArrayList<Character>> line = getLinePieces(c, r, d);
                        int distance = 0;
                        for (Character neighbor : line.get(0)) {
                            if (neighbor != Constants.EMPTY) {
                                distance++;
                            }
                        }
                        for (Character neighbor : line.get(1)) {
                            if (neighbor != Constants.EMPTY) {
                                distance++;
                            }
                        }
                        Move move = new Move(c, r, moveC(c, d, distance), moveR(r, d, distance));
                        if (isLegal(move)) {
                            legalMoves.add(move);
                        }
                    }
                }
                x++;
            }
            x = 0;
            y++;
        }
        return legalMoves;
    }
    
	public boolean isValidPos(String pos) {
		int x = pos.charAt(1) - '0';
		int y = pos.charAt(0) - '0';
		if((x >= 1 && x <=8)&&(y >= 1 && y <=8))
			return true;
		return false;
	}
    
    /**
     * @param c
     * @param d
     * @param distance
     * @return a new row based on original column c, direction d, and distance.
     */
	public int moveC(int c, int d, int distance) {
        return c + Constants.UNIT_VECTORS[d][0] * distance;
    }

    /**
     * @param r
     * @param d
     * @param distance
     * @return a new row based on original row r, direction d, and distance.
     */
	public int moveR(int r, int d, int distance) {
        return r + Constants.UNIT_VECTORS[d][1] * distance;
    }
    
    /**
     * A utility method that takes a starting position p =(c, r) and
     * a direction d
     * @param c
     * @param r
     * @param d
     * @return an ArrayList of two ArrayLists in two opposite directions d and -d
     */
    public ArrayList<ArrayList<Character>> getLinePieces(int c, int r, int d) {
        int colUnit = Constants.UNIT_VECTORS[d][0], rowUnit = Constants.UNIT_VECTORS[d][1], 
        	col = c + colUnit, row = r + rowUnit;
        ArrayList<Character> l0 = new ArrayList<>(), l1 = new ArrayList<>();
        l0.add(getPiece(r, c));
        for (Character target = getPiece(row, col);
             target != Constants.BUFFER;
             col += colUnit, row += rowUnit, target = getPiece(row, col)) {
            l0.add(target);
        }
        col = c - colUnit; row = r - rowUnit;
        for (Character target = getPiece(row, col);
             target != Constants.BUFFER;
             col -= colUnit, row -= rowUnit, target = getPiece(row, col)) {
            l1.add(target);
        }
        ArrayList<ArrayList<Character>> result = new ArrayList<ArrayList<Character>>();
        result.add(l0); result.add(l1);
        return result;
    }
    
    /**
     * A utility method that takes a starting position (c0, r0) and end position (c1, r1) 
     * @param c0
     * @param r0
     * @param c1
     * @param r1
     * @return an ArrayList of two ArrayLists as per getLinePieces(int c, int r, int d)
     */
    public ArrayList<ArrayList<Character>> getLinePieces(int c0, int r0, int c1, int r1) {
        Move temp =  new Move(c0, r0, c1, r1);
        int cd = (c1 - c0) / temp.getMoveLength(), 
        	rd = (r1 - r0) / temp.getMoveLength(), 
        	d = 0;
        if (cd == 0) {
            if (rd == -1) {
                d = 4;
            } else if (rd == 1) {
                d = 0;
            } else {
                assert false;
            }
        } else if (cd == 1) {
            if (rd == -1) {
                d = 3;
            } else if (rd == 0) {
                d = 2;
            } else if (rd == 1) {
                d = 1;
            } else {
                assert false;
            }
        } else if (cd == -1) {
            if (rd == -1) {
                d = 5;
            } else if (rd == 0) {
                d = 6;
            } else if (rd == 1) {
                d = 7;
            } else {
                assert false;
            }
        } else {
            assert false;
        }
        return getLinePieces(c0, r0, d);
    }

    /**
     * @param player 
     *  A utility method that returns an ArrayList of coordinates in the form {x, y} 
     *  of player's pieces on the board. 
     */
    public ArrayList<int[]> getCoordinates(Player player) {
        ArrayList<int[]> result = new ArrayList<int[]>();
        int y = 0;
        for (char[] row : board) {
            int x = 0;
            for (char piece: row) {
                if (piece != player.getOpponent()
                        && piece != Constants.EMPTY
                        && piece != Constants.BUFFER) {
                    int[] temp = new int[2];
                    temp[0] = x; temp[1] = y;
                    result.add(temp);
                }
                x++;
            }
            y++;
        }
        return result;
    }
    
    /** 
     * @return true iff the game is currently over.  
     * A game is over if either or both the players have all his pieces contiguous. 
     */
    public boolean gameOver() {
        return piecesContiguous(getCurrPlayer()) || piecesContiguous(currPlayer.getNextPlayer());
    }

    /** 
     * @return true iff player's pieces are contiguous. 
     */
    public boolean piecesContiguous(Player player) {
        ArrayList<int[]> remaining = getCoordinates(player);
        if (remaining.size() <= 1) {
            return true;
        }
        ArrayList<int[]> grouped = new ArrayList<int[]>();
        grouped.add(remaining.get(0)); 
        remaining.remove(0);
        boolean connected, allAway;
        while (remaining.size() != 0) {
            allAway = true;
            for (int[] piece : remaining) {
                connected = isConnected(grouped, piece);
                if (connected) {
                    grouped.add(piece); 
                    remaining.remove(piece);
                    allAway = false;
                    break;
                }
            }
            if (allAway) {
                return false;
            }
        }
        return true;
    }

    /** 
     * @return the total number of moves that have been made and not retracted 
     */
    public int movesMade() {
        return moves.size();
    }

    /** 
     * @returns move #k used to reach the current position, where
     *  0 <= k < movesMade().  Does not include retracted moves. 
     */
    public Move getMove(int k) {
        return moves.get(k);
    }

    /** 
     * Adds a move to moves made on board till now. 
     */
    public void addMove(Move move) {
        moves.add(move);
    }

    /** 
     * Removes the last move from MOVES. 
     */
    public void removeMove() {
        moves.remove(moves.size() - 1);
    }

    /**
     * @param pieces
     * @param piece
     * @return true if pieces are all connected and false otherwise.
     */
    public boolean isConnected(ArrayList<int[]> pieces, int[] piece) {
        for (int[] target : pieces) {
            int xd = Math.abs(piece[0] - target[0]),
                yd = Math.abs(piece[1] - target[1]);
            if (xd <= 1 && yd <= 1) {
                return true;
            }
        }
        return false;
    }
    
    /** 
     * @return a copy of my current configuration. 
     */
    public char[][] getConfigCopy() {
        char[][] result = new char[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;

    }
    
    /**
     * Prints board to Standard Output
     */
    public void printBoard() {
    	System.out.print("  ");
    	for(int i = 0;i<10;i++){ System.out.print(i+" ");}
    	System.out.println();
    	int j = 0;
        for (char[] row : board) {
        	System.out.print(j++ + " ");
            for (Character piece : row) {
                System.out.print(piece+" ");
            }
            System.out.println();
        }
    }
    
    /**
     * @return cloned board from board's current configuration
     */
    public Board cloneBoard(){
    	Board clonedBoard = new Board();
    	clonedBoard.setBoard(this.getConfigCopy());
    	clonedBoard.setCurrPlayer(this.getCurrPlayer());
    	clonedBoard.setMoves(this.getMoves());
    	ArrayList<Move> copiedMoves = new ArrayList<>();
    	copiedMoves.addAll(getMoves());
    	clonedBoard.setMoves(copiedMoves);
    	return clonedBoard;
    }
    
    /**
     * @param writer
     * Prints board to file
     */
    public void printBoard(PrintWriter writer) {
    	writer.print("  ");
    	for(int i = 0;i<10;i++){ 
    		writer.print(i+" ");
    	}
    	writer.println();
    	int j = 0;
        for (char[] row : board) {
        	writer.print(j++ + " ");
            for (Character piece : row) {
                writer.print(piece+" ");
            }
            writer.println();
        }
    }
    
    /**
     * @param move
     * Applies move to board's current configuration
     */
    public void makeMove(Move move){
    	if(isLegal(move)){
    		if(board[move.getR0()][move.getC0()] == getCurrPlayer().get_marker() &&
    		   board[move.getR1()][move.getC1()] != getCurrPlayer().get_marker()){
    			if(board[move.getR1()][move.getC1()] == getCurrPlayer().getOpponent())
    				move.set_capture(true);
    			board[move.getR1()][move.getC1()] = getCurrPlayer().get_marker();
    			board[move.getR0()][move.getC0()] = Constants.EMPTY;
    			addMove(move);
    			setCurrPlayer(currPlayer.getNextPlayer());
    		}
    	}
    	else{
    		return;
    	}
    }
    
    /**
     * Undo last move made on the board
     */
    public void retract() {
    	this.setCurrPlayer(currPlayer.getNextPlayer());
    	char[][] config = getBoard();
        Move move = getMove(movesMade() - 1);
        Player player = getCurrPlayer();
        char curr, op;
        removeMove();
        if (player.getOpponent() == Constants.BLACK) {
            curr = Constants.WHITE;
            op = Constants.BLACK;
        } else {
        	curr = Constants.BLACK;
            op = Constants.WHITE;
        }
        if (!move.is_capture()) {
            op = Constants.EMPTY;
        }
        config[move.getR1()][move.getC1()] = op;
        config[move.getR0()][move.getC0()] = curr;
    }
    
    /**
     * @return a random move from list of available legal moves
     */
    public Move getRandomMove(){
    	ArrayList<Move> moves = this.legalMoves();
		Random rndmGenerator = new Random();
		int index = rndmGenerator.nextInt(moves.size());
		return moves.get(index);
	}
    
    /**
     * @param b1
     * @return true or false whether b1 equal current board
     */
    public boolean equalsB(Board b1){
    	if(b1.getCurrPlayer() != this.getCurrPlayer())
    		return false;
    	char[][] b1Board = b1.getBoard();
    	for (int i = 0; i < b1Board.length; i++) {
			for (int j = 0; j < b1Board[0].length; j++) {
				if(b1Board[i][j] != this.board[i][j])
					return false;
			}
		}
    	return true;
    }
}
