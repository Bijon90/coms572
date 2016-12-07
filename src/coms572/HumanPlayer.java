package coms572;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HumanPlayer extends Player {

    /** A HumanPlayer that plays the SIDE pieces in GAME.  It uses
     *  GAME.getMove() as a source of moves.  */
    HumanPlayer(char marker, GamePlay game) {
        super(marker, game);
    }

    public Move makeMove() {
        GamePlay game = get_game();
        String move = game.getMove();
        ArrayList<Move> legalMoves = game.getBoard().legalMoves();
        if (legalMoves.size() == 0) {
            System.out.printf("Player %s has no legal moves. %s wins.\n"
                    , this.get_marker(), this.getOpponent());
            System.out.println(game.getBoard());
            System.exit(1);
        }
        Pattern pat = Pattern.compile("([a-z][\\d])-([a-z][\\d])");
        Matcher mat = pat.matcher(move);
        mat.matches();
        Move newMove = new Move(Board.col(mat.group(1))
                , Board.row(mat.group(1))
                , Board.col(mat.group(2))
                , Board.row(mat.group(2)));
        if (game.getBoard().isLegal(newMove)) {
            return newMove;
        } else {
            System.out.println("Illegal move. Try again.");
            return makeMove();
        }
    }
}
