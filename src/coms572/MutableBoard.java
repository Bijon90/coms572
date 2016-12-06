package coms572;

class MutableBoard extends Board {

    /** A MutableBoard whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 9x9,
     *  including the buffer zones..
     */
    MutableBoard(char[][] initialContents, Player player) {
        super(initialContents, player);
    }

    /** A new board in the standard initial position. */
    MutableBoard() {
        super();
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    MutableBoard(Board board) {
        super(board);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        addMove(move);
        char[][] config = getBoard();
        Player player = getCurrPlayer();
        if (config[move.getR1()][move.getC1()] == player.getNextPlayer().get_marker()) {
            move.set_capture(true);
        }
        if (player.getNextPlayer().get_marker() == Constants.BLACK) {
            config[move.getR1()][move.getC1()] = Constants.WHITE;
        } else if (player.getNextPlayer().get_marker() == Constants.WHITE) {
            config[move.getR1()][move.getC1()] = Constants.BLACK;
        } else {
            assert false;
        }
        config[move.getR0()][move.getC0()] = Constants.EMPTY;
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        char[][] config = getBoard();
        Move move = getMove(movesMade() - 1);
        Player player = getCurrPlayer();
        char me, target;
        removeMove();
        if (player.getNextPlayer().get_marker() == Constants.BLACK) {
            me = Constants.WHITE;
            target = Constants.BLACK;
        } else {
            assert player.getNextPlayer().get_marker() == Constants.WHITE;
            me = Constants.BLACK;
            target = Constants.WHITE;
        }
        if (!move.is_capture()) {
            target = Constants.EMPTY;
        }
        config[move.getR1()][move.getC1()] = target;
        config[move.getR0()][move.getC0()] = me;
    }
}
