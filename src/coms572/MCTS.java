package coms572;

import java.util.Collections;
import java.util.List;

import com.rits.cloning.Cloner;

public class MCTS<Board extends MCTSDomainBoard<Move, Player>, Move, Player extends MCTSDomainPLayer<Board>> {

	    private static final double NO_EXPLORATION = 0;

	    private final int numberOfIterations;
	    private double explorationParameter;
	    private final Cloner cloner;

	    public static<Board extends MCTSDomainBoard<Move, Player>, Move, Player extends MCTSDomainPLayer<Board>>
	        MCTS<Board, Move, Player> initializeIterations(int numberOfIterations) {
	            Cloner cloner = new Cloner();
	            return new MCTS<>(numberOfIterations, cloner);
	    }

	    private MCTS(int numberOfIterations, Cloner cloner) {
	        this.numberOfIterations = numberOfIterations;
	        this.cloner = cloner;
	    }

	    public void dontClone(final Class<?>... classes) {
	        cloner.dontClone(classes);
	    }

	    public Move uctSearchWithExploration(Board state, double explorationParameter) {
	        setExplorationForSearch(explorationParameter);
	        MCTSTreeNode<Board, Move, Player> rootNode = new MCTSTreeNode<>(state, cloner);
	        for (int i = 0; i < numberOfIterations; i++) {
	            performMctsIteration(rootNode, state.getCurrentPlayer());
	        }
	        return getNodesMostPromisingAction(rootNode);
	    }

	    private void setExplorationForSearch(double explorationParameter) {
	        this.explorationParameter = explorationParameter;
	    }

	    private void performMctsIteration(MCTSTreeNode<Board, Move, Player> rootNode, Player agentInvoking) {
	        MCTSTreeNode<Board, Move, Player> selectedChildNode = treePolicy(rootNode);
	        Board terminalState = getTerminalStateFromDefaultPolicy(selectedChildNode, agentInvoking);
	        backPropagate(selectedChildNode, terminalState);
	    }

	    private MCTSTreeNode<Board, Move, Player> treePolicy(MCTSTreeNode<Board, Move, Player> node) {
	        while (!node.representsTerminalState()) {
	            if (!node.representedStatesCurrentAgentHasAvailableActions())
	                return expandWithoutAction(node);
	            else if (!node.isFullyExpanded())
	                return expandWithAction(node);
	            else
	                node = getNodesBestChild(node);
	        }
	        return node;
	    }


	    private MCTSTreeNode<Board, Move, Player> expandWithoutAction(MCTSTreeNode<Board, Move, Player> node) {
	        return node.addNewChildWithoutAction();
	    }

	    private MCTSTreeNode<Board, Move, Player> expandWithAction(MCTSTreeNode<Board, Move, Player> node) {
	        Move randomUntriedAction = getRandomActionFromNodesUntriedActions(node);
	        return node.addNewChildFromAction(randomUntriedAction);
	    }

	    private Move getRandomActionFromNodesUntriedActions(MCTSTreeNode<Board, Move, Player> node) {
	        List<Move> untriedActions = node.getUntriedActionsForCurrentAgent();
	        Collections.shuffle(untriedActions);
	        return untriedActions.get(0);
	    }

	    private MCTSTreeNode<Board, Move, Player> getNodesBestChild(MCTSTreeNode<Board, Move, Player> node) {
	        validateBestChildComputable(node);
	        return getNodesBestChildConfidentlyWithExploration(node, explorationParameter);
	    }

	    private void validateBestChildComputable(MCTSTreeNode<Board, Move, Player> node) {
	        if (!node.hasChildNodes())
	            throw new UnsupportedOperationException("Error: operation not supported if child nodes empty");
	        else if (!node.isFullyExpanded())
	            throw new UnsupportedOperationException("Error: operation not supported if node not fully expanded");
	        else if (node.hasUnvisitedChild())
	            throw new UnsupportedOperationException(
	                    "Error: operation not supported if node contains an unvisited child");
	    }

	    private Move getNodesMostPromisingAction(MCTSTreeNode<Board, Move, Player> node) {
	        validateBestChildComputable(node);
	        MCTSTreeNode<Board, Move, Player> bestChildWithoutExploration =
	                getNodesBestChildConfidentlyWithExploration(node, NO_EXPLORATION);
	        return bestChildWithoutExploration.getIncomingAction();
	    }

	    private MCTSTreeNode<Board, Move, Player> getNodesBestChildConfidentlyWithExploration(
	            MCTSTreeNode<Board, Move, Player> node, double explorationParameter) {
	        return node.getChildNodes().stream()
	                .max((node1, node2) -> Double.compare(
	                        calculateUctValue(node1, explorationParameter),
	                        calculateUctValue(node2, explorationParameter))).get();
	    }

	    private double calculateUctValue(MCTSTreeNode<Board, Move, Player> node, double explorationParameter) {
	        return node.getDomainTheoreticValue()
	                + explorationParameter
	                * (Math.sqrt((2 * Math.log(node.getParentsVisitCount())) / node.getVisitCount()));
	    }

	    private Board getTerminalStateFromDefaultPolicy(
	            MCTSTreeNode<Board, Move, Player> node, Player agentInvoking) {
	        Board nodesStateClone = node.getDeepCloneOfRepresentedState();
	        return agentInvoking.performSimulationFromState(nodesStateClone);
	    }

	    private void backPropagate(MCTSTreeNode<Board, Move, Player> node, Board terminalState) {
	        while (node != null) {
	            updateNodesDomainTheoreticValue(node, terminalState);
	            node = node.getParentNode();
	        }
	    }

	    private void updateNodesDomainTheoreticValue(MCTSTreeNode<Board, Move, Player> node, Board terminalState) {
	        // violation of the law of demeter
	        Player parentsStatesCurrentAgent = node.getRepresentedStatesPreviousAgent();
	        double reward = parentsStatesCurrentAgent.getValueFromTState(terminalState);
	        node.updateDomainTheoreticValue(reward);
	    }
}
