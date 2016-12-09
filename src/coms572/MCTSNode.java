package coms572;

import java.util.ArrayList;

public class MCTSNode {
	private MCTSNode parentNode;
	private ArrayList<MCTSNode> children;
	private double nodeValMachine;
	private double nodeValUser;
	private int visitCountMachine;
	private int visitCountUser;
	private double nodeVal;
	private Board board;
	
	public MCTSNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(MCTSNode parentNode) {
		this.parentNode = parentNode;
	}
	public ArrayList<MCTSNode> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<MCTSNode> children) {
		this.children = children;
	}
	public double getNodeValMachine() {
		return nodeValMachine;
	}
	public void setNodeValMachine(double nodeValMachine) {
		this.nodeValMachine = nodeValMachine;
	}
	public double getNodeValUser() {
		return nodeValUser;
	}
	public void setNodeValUser(double nodeValUser) {
		this.nodeValUser = nodeValUser;
	}
	public int getVisitCountMachine() {
		return visitCountMachine;
	}
	public void setVisitCountMachine(int visitCountMachine) {
		this.visitCountMachine = visitCountMachine;
	}
	public int getVisitCountUser() {
		return visitCountUser;
	}
	public void setVisitCountUser(int visitCountUser) {
		this.visitCountUser = visitCountUser;
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
	
	public boolean equalNode(MCTSNode node1){
		return this.board.equalsB(node1.board);
	}
	
}
