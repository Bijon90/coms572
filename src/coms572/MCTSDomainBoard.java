package coms572;

import java.util.List;

public interface MCTSDomainBoard<Move, Player extends MCTSDomainPLayer> {
    public boolean isTerminal();
    public Player getCurrentPlayer();
    public Player getPreviousPlayer();
    public int getNoOfLegalMoves();
    public List<Move> getLegalMoves();
    MCTSDomainBoard performMove(Move move);
    MCTSDomainBoard skipCurrentPlayer();
}
