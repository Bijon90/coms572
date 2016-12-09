package coms572;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.rits.cloning.Cloner;

public class MCTSTreeNode<Board extends MCTSDomainBoard<Move, Player>, Move, Player extends MCTSDomainPLayer> {

    private final MCTSTreeNode<Board, Move, Player> parentNode;
    private final Move incomingAction;
    private final Board representedState;
    private int visitCount;
    private double totalReward;
    private List<MCTSTreeNode<Board, Move, Player>> childNodes;
    private final Cloner cloner;

    protected MCTSTreeNode(Board representedState, Cloner cloner) {
        this(null, null, representedState, cloner);
    }

    private MCTSTreeNode(MCTSTreeNode<Board, Move, Player> parentNode, Move incomingAction, Board representedState, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.childNodes = new ArrayList<>();
        this.cloner = cloner;
    }

    protected MCTSTreeNode<Board, Move, Player> getParentNode() {
        return parentNode;
    }

    protected Move getIncomingAction() {
        return incomingAction;
    }

    protected int getVisitCount() {
        return visitCount;
    }

    protected int getParentsVisitCount() {
        return parentNode.getVisitCount();
    }

    protected List<MCTSTreeNode<Board, Move, Player>> getChildNodes() {
        return childNodes;
    }

    protected boolean hasChildNodes() {
        return childNodes.size() > 0;
    }

    protected boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    protected Player getRepresentedStatesPreviousAgent() {
        return representedState.getPreviousPlayer();
    }

    protected boolean representedStatesCurrentAgentHasAvailableActions() {
        return representedState.getNoOfLegalMoves() > 0;
    }

    protected boolean isFullyExpanded() {
        return representedState.getNoOfLegalMoves() == childNodes.size();
    }

    protected boolean hasUnvisitedChild () {
        return childNodes.stream().anyMatch(MCTSTreeNode::isUnvisited);
    }

    private boolean isUnvisited() {
        return visitCount == 0;
    }

    protected MCTSTreeNode<Board, Move, Player> addNewChildWithoutAction() {
        Board childNodeState = getDeepCloneOfRepresentedState();
        childNodeState.skipCurrentPlayer();
        return appendNewChildInstance(childNodeState, null);
    }

    protected MCTSTreeNode<Board, Move, Player> addNewChildFromAction(Move action) {
        if (!isUntriedAction(action))
            throw new IllegalArgumentException("Error: invalid action passed as function parameter");
        else
            return addNewChildFromUntriedAction(action);
    }

    private boolean isUntriedAction(Move action) {
        return getUntriedActionsForCurrentAgent().contains(action);
    }

    protected List<Move> getUntriedActionsForCurrentAgent() {
        List<Move> availableActions = representedState.getLegalMoves();
        List<Move> untriedActions = new ArrayList<>(availableActions);
        List<Move> triedActions = getTriedActionsForCurrentAgent();
        untriedActions.removeAll(triedActions);
        return untriedActions;
    }

    private List<Move> getTriedActionsForCurrentAgent() {
        return childNodes.stream()
                .map(MCTSTreeNode::getIncomingAction)
                .collect(Collectors.toList());
    }

    private MCTSTreeNode<Board, Move, Player> addNewChildFromUntriedAction(Move incomingAction) {
        Board childNodeState = getNewStateFromAction(incomingAction);
        return appendNewChildInstance(childNodeState, incomingAction);
    }

    private Board getNewStateFromAction(Move action) {
        Board representedStateClone = getDeepCloneOfRepresentedState();
        representedStateClone.performMove(action);
        return representedStateClone;
    }

    protected Board getDeepCloneOfRepresentedState() {
        return cloner.deepClone(representedState);
    }

    private MCTSTreeNode<Board, Move, Player> appendNewChildInstance(
            Board representedState, Move incomingAction) {
        MCTSTreeNode<Board, Move, Player> childNode = new MCTSTreeNode<>(
                this, incomingAction, representedState, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    protected void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }

    protected double getDomainTheoreticValue() {
        return totalReward / visitCount;
    }
}
