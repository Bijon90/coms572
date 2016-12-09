package coms572;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class MCTSNode {
	private MCTSNode parentNode;
	private Set<MCTSNode> children;
	private int visitCount;
	private int machineWin;

	private double nodeVal;
	private Board board;
	private Move move;

	public MCTSNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(MCTSNode parentNode) {
		this.parentNode = parentNode;
	}
	public Set<MCTSNode> getChildren() {
		return children;
	}
	public void setChildren(Set<MCTSNode> children) {
		this.children = children;
	}
	public double getNodeVal() {
		return nodeVal;
	}
	public void setNodeVal(double nodeVal) {
		this.nodeVal = nodeVal;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}

	public Move getMove() {
		return move;
	}
	public void setMove(Move move) {
		this.move = move;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	
	public int getMachineWin() {
		return machineWin;
	}
	public void setMachineWin(int machineWin) {
		this.machineWin = machineWin;
	}


	public MCTSNode(){
		this.parentNode = null;
		this.children = new HashSet<>();
		this.nodeVal = Double.MIN_VALUE;
		this.visitCount = 0;
		this.board = new Board();
		this.move = new Move();
		this.machineWin = 0;
	}

	public MCTSNode(Board b){
		this.parentNode = null;
		this.children = new HashSet<>();
		this.nodeVal = Double.MIN_VALUE;
		this.board = b.cloneBoard();
		this.visitCount = 0;
		this.machineWin = 0;
		this.move = new Move();
	}

	public MCTSNode(MCTSNode parentNode, HashSet<MCTSNode> children, double nodeValMachine, double nodeValUser,
			int visitCountMachine, int visitCountUser, double nodeVal, Board board) {
		this.parentNode = parentNode;
		this.children = children;
		this.visitCount = visitCountMachine;
		this.nodeVal = nodeVal;
		this.board = board;
		this.machineWin = 0;
		this.move = new Move();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		long temp;
		temp = Double.doubleToLongBits(nodeVal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((parentNode == null) ? 0 : parentNode.hashCode());
		result = prime * result + visitCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCTSNode other = (MCTSNode) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (Double.doubleToLongBits(nodeVal) != Double.doubleToLongBits(other.nodeVal))
			return false;
		if (parentNode == null) {
			if (other.parentNode != null)
				return false;
		} else if (!parentNode.equals(other.parentNode))
			return false;
		return true;
	}

	public MCTSNode cloneNode(){
		MCTSNode copy = new MCTSNode();
		copy.setBoard(getBoard().cloneBoard());
		copy.setChildren(new HashSet<>(getChildren()));
		copy.setNodeVal(getNodeVal());
		copy.setVisitCount(getVisitCount());
		copy.setParentNode(getParentNode());
		return copy;
	}

	public boolean equalNode(MCTSNode node1){
		return this.board.equalsB(node1.board);
	}
	
	public void calculateVal(){
		double val = this.getMachineWin()/this.getVisitCount();
		this.setNodeVal(val);
	}

	public MCTSNode applyMove(Move move) {
		Board b = this.getBoard().cloneBoard();
		b.makeMove(move);
		MCTSNode resultNode = this.cloneNode();
		resultNode.setParentNode(this);
		resultNode.setVisitCount(getVisitCount()+1);
		return resultNode;
	}

}
