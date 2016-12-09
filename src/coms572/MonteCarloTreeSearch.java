package coms572;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class MonteCarloTreeSearch {
	private MCTSNode root;
	private int turns = 300;
	PrintWriter writer;

	public MonteCarloTreeSearch(PrintWriter writer) {
		this.writer = writer;
		root = null;
	}

	public MonteCarloTreeSearch(MCTSNode root){
		this.root = root;
	}

	public int getHashCode(MCTSNode node){
		int result = 1;
		String finalHashString = "";
		ArrayList<int[]> machineCo = node.getBoard().getCoordinates(Constants.machine);
		ArrayList<int[]> userCo = node.getBoard().getCoordinates(Constants.user);
		Collections.sort(machineCo,new Comparator<int[]>() {
			public int compare(int[] p1, int[] p2){
				if(p1[0] == p2[0])
					return p1[1] = p2[1];
				return p1[0] - p2[0];
			}
		});

		Collections.sort(userCo,new Comparator<int[]>() {
			public int compare(int[] p1, int[] p2){
				if(p1[0] == p2[0])
					return p1[1] = p2[1];
				return p1[0] - p2[0];
			}
		});

		String machineC = listToString(machineCo);
		String userC = listToString(userCo);
		finalHashString += "machine"+machineC+"user"+userC;
		result = finalHashString.hashCode();
		return result;
	}

	public String listToString(ArrayList<int[]> list){
		String result = "";
		for (int[] pos : list) {
			result += pos[0];
			result += pos[1];
		}
		return result;
	}

	public void addNodeAfterSimulation(MCTSNode node){
		boolean win = runSimulation(node.getBoard().cloneBoard());
		if(win){
			node.setVisitCount(node.getVisitCount()+1);
			node.setNodeVal(node.getNodeVal()+1);
		}
		else if(turns <= 0){}
		else{
			node.setNodeVal(node.getNodeVal()-1);
		}
		addNodeToTree(node);
	}

	public void addNodeToTree(MCTSNode node){
		if(root == null){
			root = node;
			root.setParentNode(null);
			return;
		}
		MCTSNode parent = findParent(node);
		Set<MCTSNode> children = parent.getChildren();
		children.add(node);
	}

	public MCTSNode findParent(MCTSNode node){
		MCTSNode parent = node.getParentNode();
		MCTSNode temp = root;
		Set<MCTSNode> children;
		Queue<MCTSNode> q = new LinkedList<>();
		q.add(root);
		Set<MCTSNode> visited = new HashSet<>();
		while(!q.isEmpty()){
			temp = q.poll();
			if(!visited.contains(temp)){
				//System.out.println(temp);
				children = temp.getChildren();
				if(children.contains(parent)){
					return temp;
				}
			}
			visited.add(temp);
		}
		return null;
	}

	public MCTSNode findNode(MCTSNode node){
		MCTSNode parent = findParent(node);
		if(!node.equalNode(root) || parent == null){
			return null;
		}
		MCTSNode temp = root;
		Set<MCTSNode> children;
		Queue<MCTSNode> q = new LinkedList<>();
		q.add(root);
		Set<MCTSNode> visited = new HashSet<>();
		while(!q.isEmpty()){
			temp = q.poll();
			if(!visited.contains(temp)){
				children = temp.getChildren();
				for (MCTSNode mctsNode : children) {
					if(mctsNode.equalNode(node))
						return mctsNode;
				}
			}
			visited.add(temp);
		}
		return null;
	}

	private boolean runSimulation(Board b) {
		if (turns <= 0) {
			return false;
		}
		Board forCheckMove = b.cloneBoard();
		Move currMove = forCheckMove.getRandomMove();
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
	public Move getMCTSMove(MCTSNode node){
		MCTSNode mcnode = findNode(node);
		boolean win = false;
		int winVal = 0;
		if(mcnode == null){
			AIAgent ai = new AIAgent(node.getBoard().cloneBoard(),writer);
			Move move = ai.getAgentMove();
			node.setMove(move);
			if(ai.terminalWin)
				winVal = node.getMachineWin() + 1;
			else
				winVal = node.getMachineWin() - 1;
			node.setMachineWin(winVal);
			node.setVisitCount(node.getVisitCount()+1);
			node.calculateVal();
			addNodeAfterSimulation(node);
		}
		else{
			node.setVisitCount(node.getVisitCount()+1);
			node.calculateVal();
			return mcnode.getMove();
		}
		return mcnode.getMove();
	}

}
