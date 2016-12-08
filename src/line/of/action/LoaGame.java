package line.of.action;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;


/*
 * For the whole project in case of any array like move[]
 * <p> The first element move[0] will correspond to row
 * <p> The second element move[1] will correspond to column
 */

public class LoaGame 
{
	public static int depth;
	public static final char W='X',B='O',EMPTY='_';
	State gameState;
	Player player;
	static int defaultDepth=3;
    static int nodesExpanded=0;
	static int size=8;
	int directions [][] = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
	int [][] visitedNodes;
	static boolean debugging=false;
	static int delay;
	
	public LoaGame()
	{
		gameState= new State();
		depth=defaultDepth;
		initializeBoard();
		gameState.printBoard();
		player= new Player(B);
		player.setOtherPlayerName(W);
		visitedNodes= new int[size][size]; // for each location on board. 0=not visited,1=in stack,2=visited   
	}
	/*
	 * Initialize the board
	 */
	void initializeBoard()
	{
		for(int i=1;i<7;i++)
		{
			gameState.board[0][i]=B;
			gameState.board[7][i]=B;
			gameState.board[i][0]=W;
			gameState.board[i][7]=W;
		}
	}
	
	private int[] loaCheckLeftHorizontal(State state, Player player, int row,int column) 
	{

		int countCoins=0;
		for(int i=0;i<8;i++)
		{
			if(state.board[row][i]!=EMPTY)
				countCoins++;
		}
		for(int i=column;i>column-countCoins;i--)
		{
			if(i<0 ||(state.board[row][i]==player.getOtherPlayerName()))
				return null;
		}
		if((column-countCoins<0) || state.board[row][column-countCoins]==player.name)
			return null;
		return new int[]{row,column-countCoins};
		
	}
	/**
	 * @param currentState
	 * @param player2
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] loaCheckRightHorizontal(State state, Player player, int row,int column) 
	{
		int countCoins=0;
		for(int i=0;i<8;i++)
		{
			if(state.board[row][i]!=EMPTY)
				countCoins++;
		}
		for(int i=column;i<column+countCoins;i++)
		{
			if(i>7 ||(state.board[row][i]==player.getOtherPlayerName()))
				return null;
		}
		if((column+countCoins>7) || state.board[row][column+countCoins]==player.name)
			return null;
		return new int[]{row,column+countCoins};
	}
	/**
	 * @param currentState
	 * @param player2
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] loaCheckUpMove(State state, Player player, int row,int column) 
	{
		int countCoins=0;
		for(int i=0;i<8;i++)
		{
			if(state.board[row][i]!=EMPTY)
				countCoins++;
		}
		for(int i=column;i>column-countCoins;i--)
		{
			if(i<0 ||(state.board[row][i]==player.getOtherPlayerName()))
				return null;
		}
		if((column-countCoins<0) || state.board[row][column-countCoins]==player.name)
			return null;
		return new int[]{row,column-countCoins};
	
	}
	/**
	 * @param currentState
	 * @param player2
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] loaCheckDownMove(State state, Player player, int row,int column) 
	{
		int countCoins=0;
		for(int i=0;i<8;i++)
		{
			if(state.board[i][column]!=EMPTY)
				countCoins++;
		}
		for(int i=row;i<row+countCoins;i++)
		{
			if(i>7 ||(state.board[i][column]==player.getOtherPlayerName()))
				return null;
		}
		if((row+countCoins>7) || state.board[row+countCoins][column]==player.name)
			return null;
		return new int[]{row+countCoins,column};
	
	}
	/**
	 * @param currentState
	 * @param player2
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] loaCheckNorthEastMove(State state, Player player, int row,int column) 
	{
		int countCoins=0;
		if(row+column <= 7)
		{
			int startR=row+column,startC=0;
			for(int k=startR;k>=0 && startC<8;k--,startC++)
			{
				if(state.board[k][startC]!=EMPTY)
				{
					countCoins++;
				}
					
			}
			
		}
		else
		{
			int startC=(row+column)-7,startR=7;
			for(int k=startC;k<8 && startR>=0;k++,startR--)
			{
				if(state.board[startR][k]!=EMPTY)
				{
					countCoins++;
				}
					
			}
		}
		int startR=row,startC=column;
		for(int k=startR;k>startR-countCoins;k--,startC++)
		{
			if(k<0 || startC>7 ||(state.board[k][startC]==player.getOtherPlayerName()))
				return null;
				
		}
		if((row-countCoins<0) || (column+countCoins>7) || state.board[row-countCoins][column+countCoins]==player.name)
			return null;
		return new int[]{row-countCoins,column+countCoins}; 
	
	}
	/**
	 * @param currentState
	 * @param player2
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] loaCheckNorthWestMove(State state, Player player, int row,int column) 
	{
		int countCoins=0;
		if(row>=column)
		{
			int startR=row-column,startC=0;
			for(int k=startR;k<8 && startC<8;k++,startC++)
			{
				if(state.board[k][startC]!=EMPTY)
				{
					countCoins++;
				}
					
			}
			
		}
		else
		{
			int startC=column-row,startR=0;
			for(int k=startC;k<8 && startR<8 ;k++,startR++)
			{
				if(state.board[startR][k]!=EMPTY)
				{
					countCoins++;
				}
					
			}
		}
		// TO-DO
		int startR=row,startC=column;
		for(int k=startR;k>startR-countCoins;k--,startC--)
		{
			if(k<0 || startC<0 ||(state.board[k][startC]==player.getOtherPlayerName()))
				return null;
				
		}
		if((row-countCoins<0) || (column-countCoins<0) || state.board[row-countCoins][column-countCoins]==player.name)
			return null;
		return new int[]{row-countCoins,column-countCoins}; 
	
	}
	/**
	 * @param currentState
	 * @param player2
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] loaCheckSouthEastMove(State state, Player player, int row,int column) 
	{
		int countCoins=0;
		if(row>=column)
		{
			int startR=row-column,startC=0;
			for(int k=startR;k<8 && startC<8;k++,startC++)
			{
				if(state.board[k][startC]!=EMPTY)
				{
					countCoins++;
				}
					
			}
			
		}
		else
		{
			int startC=column-row,startR=0;
			for(int k=startC;k<8 && startR<8;k++,startR++)
			{
				if(state.board[startR][k]!=EMPTY)
				{
					countCoins++;
				}
					
			}
		}
		// TO-DO
		int startR=row,startC=column;
		for(int k=startR;k<startR+countCoins;k++,startC++)
		{
			if(k>7 || startC>7 ||(state.board[k][startC]==player.getOtherPlayerName()))
				return null;
				
		}
		if((row+countCoins>7) || (column+countCoins>7) || state.board[row+countCoins][column+countCoins]==player.name)
			return null;
		return new int[]{row+countCoins,column+countCoins}; 
	
	}
	/**
	 * @param currentState
	 * @param player2
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] loaCheckSouthWestMove(State state, Player player, int row,int column) 
	{
		int countCoins=0;
		if(row+column<=7)
		{
			int startR=row+column,startC=0;
			for(int k=startR;k>=0 && startC<8;k--,startC++)
			{
				if(state.board[k][startC]!=EMPTY)
				{
					countCoins++;
				}
					
			}
			
		}
		else
		{
			int startC=(row+column)-7,startR=7;
			for(int k=startC;k<8 && startR>=0;k++,startR--)
			{
				if(state.board[startR][k]!=EMPTY)
				{
					countCoins++;
				}
					
			}
		}
		// TO-DO
		int startR=row,startC=column;
		for(int k=startR;k<startR+countCoins;k++,startC--)
		{
			if(k>7 || startC<0 ||(state.board[k][startC]==player.getOtherPlayerName()))
				return null;
				
		}
		if((row+countCoins>7) || (column-countCoins<0) || state.board[row+countCoins][column-countCoins]==player.name)
			return null;
		return new int[]{row+countCoins,column-countCoins}; 
	
	}
	
	
	private void loaAddMove(ArrayList<int[]> legalMoves, int[] startMove, int[] endMove) 
	{
		if(startMove==null || endMove==null)
		{
			return;
		}
		String toCheck=String.valueOf(startMove[0])+String.valueOf(startMove[1])+String.valueOf(endMove[0])+String.valueOf(endMove[1]);
		for(int i=0;i<legalMoves.size();i++)
		{
			String check=String.valueOf(legalMoves.get(i)[0])+String.valueOf(legalMoves.get(i)[1])+String.valueOf(legalMoves.get(i)[2])+String.valueOf(legalMoves.get(i)[3]);
			if(check.equals(toCheck))
				return;
		}
		int startR=startMove[0];
		int startC=startMove[1];
		int endR=endMove[0];
		int endC=endMove[1];
		int []nextMove={startR,startC,endR,endC};
		legalMoves.add(nextMove);
		//System.out.println("Added move {" + nextMove[0]+","+ nextMove[1]+"} to {"+ nextMove[2] +","+ nextMove[3]+"}");
	}
	
	public int[] alphaBetaSearch(State currentState, Player player, int maxDepth)
	{
		try{
			State nextState=new State(currentState);
			Integer alpha=Integer.MIN_VALUE;
			Integer beta=Integer.MAX_VALUE;
			int depth=0;

			ArrayList<SearchNode> nodesBucket= new ArrayList<SearchNode>();
			int[][] loaPossibleActions = loaMovesPossible(nextState, player);
			if(isGameOver(currentState))
			{
				System.out.println("Terminal state reached ");
				return null;	
			}
			if(loaPossibleActions.length>0)
			{
				for(int[]move : loaPossibleActions)
				{
					State newState= getLoaNextState(nextState, new int[]{move[0], move[1]}, new int[]{move[2],move[3]},player);
					nodesExpanded++;
					int val = minValue(newState,move,player.getOpponent(),depth+1,maxDepth,alpha,beta);
					SearchNode node= new SearchNode(move,val);
					nodesBucket.add(node);
					sortBucketNodesForMax(nodesBucket);
					alpha = nodesBucket.get(0).getValue();
				}
			}
			if(nodesBucket.size()>0)
			{
				sortBucketNodesForMax(nodesBucket);
				System.out.println("Moves to take : " + nodesBucket.get(0).toString());
				return nodesBucket.get(0).getMove();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @param currentState
	 * @return
	 */
	public boolean isGameOver(State currentState) 
	{
		return piecesContiguous(currentState,W) || piecesContiguous(currentState,B);
	}
	
	/**
	 * @param currentState 
	 * @param w2
	 * @return
	 */
	private boolean piecesContiguous(State currentState, char w) 
	{
		Player pl= new Player(w);
		ArrayList<int[]> remaining = getCoordinates(currentState,pl);
        if (remaining.size() <= 1) 
        {
            return true;
        }
        ArrayList<int[]> grouped = new ArrayList<int[]>();
        grouped.add(remaining.get(0)); remaining.remove(0);
        boolean connected, allAway;
        
        while (remaining.size() != 0) 
        {
            allAway = true;
            for (int[] piece : remaining) 
            {
                connected = isConnected(grouped, piece);
                if (connected) 
                {
                    grouped.add(piece); remaining.remove(piece);
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
	
	private static boolean isConnected(ArrayList<int[]> grouped, int[] piece) 
    {
        for (int[] groupedCoin : grouped) 
        {
            int xd = Math.abs(piece[0] - groupedCoin[0]), yd = Math.abs(piece[1] - groupedCoin[1]);
            if (xd <= 1 && yd <= 1) {
                return true;
            }
        }
        return false;
    }
	/**
	 * @param currentState 
	 * @param pl
	 * @return
	 */
	private ArrayList<int[]> getCoordinates(State currentState, Player pl) 
	{
		ArrayList<int[]> coor= new ArrayList<int[]>();
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(currentState.board[i][j]==pl.name)
					{
					coor.add(new int[]{i,j});
					}
					
			}
		}
		return coor;
	}
	/**
	 * @param nextState
	 * @param player2
	 * @return
	 */
	int[][] loaMovesPossible(State currentState, Player player) 
	{
		ArrayList<int[]> movesPossible = new ArrayList<int[]>();
		for (int i = 0; i < currentState.board.length; i++) 
		{
			for (int j = 0; j < currentState.board[i].length; j++) 
			{
				if (currentState.board[i][j] == player.name) 
				{
					if (j > 0) 
					{
						// get legal moves on left side horizontally
						int[] leftMove = loaCheckLeftHorizontal(currentState, player,i,j);
						loaAddMove(movesPossible, new int[]{i,j},leftMove);
					}

					if (j < 7) 
					{
						// get legal moves on right side horizontally
						int[] rightMove = loaCheckRightHorizontal(currentState, player,i,j );
						loaAddMove(movesPossible, new int[]{i,j},rightMove);
					}

					if (i > 0) 
					{
						// get legal moves in up direction
						int[] upMove = loaCheckUpMove(currentState, player, i, j);
						loaAddMove(movesPossible , new int[]{i,j} , upMove);
					}

					if (i < 7) 
					{
						// get legal moves in down direction
						int[] downMove = loaCheckDownMove(currentState, player, i, j);
						loaAddMove(movesPossible,new int[]{i,j}, downMove);
					}
					
					if(i > 0 && j < 7)
					{
						// get legal moves in north-east direction
						int[] neMove=loaCheckNorthEastMove(currentState, player, i, j);
						loaAddMove(movesPossible,new int[]{i,j},neMove);
					}
					
					if(i>0 && j > 0)
					{
						// get legal moves in north-west direction
						int nwMove[]= loaCheckNorthWestMove(currentState, player, i, j);
						loaAddMove(movesPossible,new int[]{i,j}, nwMove);
					}
					
					if(i<7 && j<7)
					{
						// get legal moves in south-east direction
						int seMove[]= loaCheckSouthEastMove(currentState, player , i ,j);
						loaAddMove(movesPossible, new int[]{i,j}, seMove);
					}
					
					if(i< 7 && j > 0)
					{
						//get legal moves in south-west direction
						int swMove[] = loaCheckSouthWestMove(currentState, player , i , j);
						loaAddMove(movesPossible,new int[]{i,j}, swMove);
						
					}

				}
			}
		}
		int [][] result= new int[movesPossible.size()][2];
		for(int i=0;i<movesPossible.size();i++)
		{
			result[i]=movesPossible.get(i);
		}
		return result;
	}
	private int maxValue(State state, int[] prevMove, Player currPlayer, int depth,int maxDepth, Integer alpha, Integer beta) 
	{

		ArrayList<SearchNode> nodesBucket= new ArrayList<SearchNode>();
		try{
			if(debugging) System.out.println("After maxvalue is called ");
			State nextState=new State(state);
			if(depth==maxDepth || isGameOver(nextState))
			{
				int value=evaluate(nextState, currPlayer);
				/*
				 * Use an improved version of utility function in which you can more weightage is given to corner and sides
				 */
				//int value=getUtilityValueImproved(nextState,currPlayer);
				if(debugging) System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ max Depth reached");
				if(debugging) System.out.println("MaxValue : " + value);
				return value;
			}
			int[][] possibleActions = loaMovesPossible(nextState, currPlayer);
			if(possibleActions.length>0)
			{
				for(int[]move : possibleActions)
				{
					State newState= getLoaNextState(nextState, new int[]{move[0],move[1]}, new int[]{move[2], move[3]},currPlayer);
					nodesExpanded++;
					int val = minValue(newState,move,currPlayer.getOpponent(),depth+1,maxDepth,alpha,beta);
					SearchNode node= new SearchNode(move,val);
					nodesBucket.add(node);
					sortBucketNodesForMax(nodesBucket);
					int maxVal=nodesBucket.get(0).getValue();
					if(maxVal>=beta)
					{
						return maxVal;
					}
					if(maxVal> alpha)
					{
						alpha=maxVal;
					}
					nextState= new State(state);
				}
			}
			sortBucketNodesForMax(nodesBucket);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return nodesBucket.get(0).getValue();
	}
	
	private int minValue(State state, int[] prevMove, Player currPlayer, int depth,int maxDepth, Integer alpha, Integer beta) 
	{
		ArrayList<SearchNode> nodesBucket= new ArrayList<SearchNode>();
		try{
			if(debugging) System.out.println("After minValue is called");
			State nextState=new State(state);
			if(depth==maxDepth || isGameOver(nextState))
			{
				int value=evaluate(nextState, currPlayer.getOpponent());
				if(debugging) System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ min Depth reached ");
				if(debugging) System.out.println("Minvalue : " + value);
				return value;
			}
			int[][] possibleActions = loaMovesPossible(nextState, currPlayer);
			if(possibleActions.length>0)
			{
				for(int[]move : possibleActions)
				{
					State newState= getLoaNextState(nextState, new int[]{move[0],move[1]}, new int[]{move[2], move[3]},currPlayer);
					nodesExpanded++;
					int val = maxValue(newState,move,currPlayer.getOpponent(),depth+1,maxDepth,alpha,beta);
					SearchNode node= new SearchNode(move,val);
					nodesBucket.add(node);
					sortBucketNodesForMin(nodesBucket);
					int minVal=nodesBucket.get(0).getValue();
					if(minVal<alpha)
					{
						return minVal;
					}
					if(minVal<beta)
					{
						beta=minVal;
					}
					nextState= new State(state);
				}
			}
			sortBucketNodesForMin(nodesBucket);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return nodesBucket.get(0).getValue();
	}
	private void sortBucketNodesForMax(ArrayList<SearchNode> nodesBucket) 
	{
		Collections.sort(nodesBucket, new Comparator<SearchNode>() 
				{
			@Override
			public int compare(SearchNode n1, SearchNode n2) 
			{
				if (n1.getValue()!=n2.getValue()) 
				{
					// order highest to lowest
					return (n2.getValue()-n1.getValue());
				} 
				else 
				{
					// if value is same, pick the smallest row number
					int row1 = Math.abs(n1.getMove()[2]- 4 );
					int col1 = Math.abs(n1.getMove()[3]-4 );
					
					int row2 = Math.abs(n2.getMove()[2]- 4);
					int col2 = Math.abs(n2.getMove()[3]- 4);
					if(row1+col1 != row2+col2)
					{
						//return (row2+col2 - row1- col1);
						return (row1+col1 - row2 - col2);
					}
					else if (row1 != row2) 
					{
						return row2 - row1;
					} 
					else 
					{
						return col2 - col1;
					}
				}
			}
				});

	}
	
	private void sortBucketNodesForMin(ArrayList<SearchNode> nodesBucket) 
	{
		Collections.sort(nodesBucket, new Comparator<SearchNode>() 
		{
			@Override
			public int compare(SearchNode n1, SearchNode n2) 
			{
				if (n1.getValue() != n2.getValue()) 
				{
					return (n1.getValue() - n2.getValue());
				} 
				else 
				{
					// if value is same, pick the smallest row number
					int row1 = Math.abs(n1.getMove()[2]- 4 );
					int col1 = Math.abs(n1.getMove()[3]-4 );
					
					int row2 = Math.abs(n2.getMove()[2]- 4);
					int col2 = Math.abs(n2.getMove()[3]- 4);
					if(row1+col1 != row2+col2)
					{
						return (row2+col2 - row1- col1);
					}
					else if (row1 != row2) 
					{
						return row1 - row2;
					} 
					else 
					{
						return col1 - col2;
					}
				}
			}
		});
	}
	
	/*public static void main(String args[])
	{
		LoaGame game= new LoaGame();
		game.gameState.board[0][2]=EMPTY;
		game.gameState.board[2][4]=B;
		game.gameState.printBoard();
		State newState= game.getLoaNextState(game.gameState, new int[]{2,7}, new int[]{2,4},new Player(W));
		newState.printBoard();
		
		
	}*/
	/**
	 * @param gameState
	 * @param moveStart
	 * @param moveStart 
	 * @param moveStart
	 * @return 
	 */
	State getLoaNextState(State gameState, int[] moveStart, int[] moveEnd, Player player) 
	{
		if(!isValidMove(gameState,moveStart,moveEnd,player))
		{
			if(debugging)
				System.out.println("Move = {"+moveStart[0]+","+moveStart[1]+"} - {"+moveEnd[0]+","+moveEnd[1]+"} is invalid");
			return null;
		}
		State nextState = new State(gameState);
		int r=moveStart[0];
		int c=moveStart[1];
		int fr=moveEnd[0];
		int fc=moveEnd[1];
		nextState.board[fr][fc]=player.name;
		nextState.board[r][c]=EMPTY;
		//nextState.printBoard();
		return nextState;
	}
	/**
	 * @param gameState
	 * @param moveStart
	 * @param moveEnd
	 * @param player2
	 * @return
	 */
	private boolean isValidMove(State gameState, int[] moveStart,int[] moveEnd, Player player) 
	{
		int r=moveStart[0];
		int c=moveStart[1];
		int fr=moveEnd[0];
		int fc=moveEnd[1];
		
		if(gameState.board[fr][fc]==player.name)
		{
			return false;
		}
		
		// check left move
		if(r==fr && fc<c)
		{
			int noCoin=0;
			for(int j=0;j<8;j++)
			{
				if(gameState.board[r][j]!=EMPTY)
				{
					noCoin++;
				}
					
			}
			if(noCoin!=Math.abs(c-fc))
				return false;
			for(int i=c;i>fc;i--)
			{
				if(gameState.board[r][i]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		
		// check right move
		if(r==fr && fc>c)
		{
			int noCoin=0;
			for(int j=0;j<8;j++)
			{
				if(gameState.board[r][j]!=EMPTY)
				{
					noCoin++;
				}
					
			}
			if(noCoin!=Math.abs(fc-c))
				return false;
			for(int i=c;i<fc;i++)
			{
				if(gameState.board[r][i]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		
		// check north move
		if(c==fc && fr<r)
		{
			int noCoin=0;
			for(int j=0;j<8;j++)
			{
				if(gameState.board[j][c]!=EMPTY)
				{
					noCoin++;
				}
					
			}
			if(noCoin!=Math.abs(r-fr))
				return false;
			for(int i=fr;i<r;i++)
			{
				if(gameState.board[i][c]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		
		// check south move
		if(c==fc && r<fr)
		{
			int noCoin=0;
			for(int j=0;j<8;j++)
			{
				if(gameState.board[j][c]!=EMPTY)
				{
					noCoin++;
				}
					
			}
			if(noCoin!=Math.abs(fr-r))
				return false;
			for(int i=r;i<fr;i++)
			{
				if(gameState.board[i][c]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		
		// check south-east move
		if(r<fr && c<fc && (fr-r == fc-c))
		{
			int noCoin=0;
			if(r>=c)
			{
				int startR=r-c,startC=0;
				for(int k=startR;k<8 && startC<8;k++,startC++)
				{
					if(gameState.board[k][startC]!=EMPTY)
					{
						noCoin++;
					}
						
				}
				
			}
			else
			{
				int startC=c-r,startR=0;
				for(int k=startC;k<8 && startR<8;k++,startR++)
				{
					if(gameState.board[startR][k]!=EMPTY)
					{
						noCoin++;
					}
						
				}
			}
			if(noCoin!=Math.abs(r-fr))
				return false;
			
			for(int i=r,j=c;i<fr;i++,j++)
			{
				if(gameState.board[i][j]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		
		// check north-east move
		if(r>fr && c<fc && (r-fr == fc-c))
		{
			int noCoin=0;
			if(r+c <= 7)
			{
				int startR=r+c,startC=0;
				for(int k=startR;k>=0 && startC<8;k--,startC++)
				{
					if(gameState.board[k][startC]!=EMPTY)
					{
						noCoin++;
					}
						
				}
				
			}
			else
			{
				int startC=(r+c)-7,startR=7;
				for(int k=startC;k<8 && startR>=0;k++,startR--)
				{
					if(gameState.board[startR][k]!=EMPTY)
					{
						noCoin++;
					}
						
				}
			}
			if(noCoin!=Math.abs(r-fr))
				return false;
			for(int i=r,j=c;i>fr;i--,j++)
			{
				if(gameState.board[i][j]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		
		// check north-west move
		if(r>fr && c>fc && (r-fr == c-fc))
		{
			int noCoin=0;
			if(r>=c)
			{
				int startR=r-c,startC=0;
				for(int k=startR;k<8 && startC<8;k++,startC++)
				{
					if(gameState.board[k][startC]!=EMPTY)
					{
						noCoin++;
					}
						
				}
				
			}
			else
			{
				int startC=c-r,startR=0;
				for(int k=startC;k<8 && startR<8;k++,startR++)
				{
					if(gameState.board[startR][k]!=EMPTY)
					{
						noCoin++;
					}
						
				}
			}
			if(noCoin!=Math.abs((r-fr)))
				return false;
			for(int i=r,j=c;i>fr;i--,j--)
			{
				if(gameState.board[i][j]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		
		// check south-west move
		if(r<fr && c>fc && (fr-r == c-fc))
		{
			int noCoin=0;
			if(r+c<=7)
			{
				int startR=r+c,startC=0;
				for(int k=startR;k>=0 && startC<8;k--,startC++)
				{
					if(gameState.board[k][startC]!=EMPTY)
					{
						noCoin++;
					}
						
				}
				
			}
			else
			{
				int startC=(r+c)-7,startR=7;
				for(int k=startC;k<8 && startR>=0;k++,startR--)
				{
					if(gameState.board[startR][k]!=EMPTY)
					{
						noCoin++;
					}
						
				}
			}
			if(noCoin!=Math.abs(r-fr))
				return false;
			for(int i=r,j=c;i<fr;i++,j--)
			{
				if(gameState.board[i][j]==player.getOpponent().name)
					return false;
			}
			return true;
		}
		return false;
	}
	// the heuristic will compute the size of the biggest group of checkers
	// returns a value beteween 1 and -1:
	// 1 means I win
	// -1 means I loose
	public int evaluate(State gameState,Player player)  // evaluates the current board
   {
	   int plyrCount=0,oppPlCount=0;
       for(int i=0; i<size; i++)
       {
           for(int j=0;j<size; j++)
           {
        	   visitedNodes[i][j] = 0 ;
        	   if(gameState.board[i][j]==player.name)
        	   {
        		   plyrCount++;
        	   }
        	   else if(gameState.board[i][j]==player.getOtherPlayerName())
        	   {
        		   oppPlCount++;
        	   }
           }
       }
       
       int bstMe = 0;                 // size of the largest group of my color
       int bstOther = 0;              // size of the largest group of the enemy's color
       
       // now we will run several DFS's to get the size of each group of checkers
       for(int i=0; i<size; i++)
       {
           for(int j=0;j<size; j++)
           {
               if(visitedNodes[i][j]==0)
               {
                   if(gameState.board[i][j]==player.name)
                   {
                       int h = dfs(i,j,gameState,player);
                       if(h > bstMe)
                           bstMe = h;
                   }
                   else if(gameState.board[i][j]==player.getOpponent().name)
                   {
                       int h = dfs(i,j,gameState,player.getOpponent());
                       if(h > bstOther)
                           bstOther = h;
                   }
               }
           }
       }
       //return 0f;
       
       // first check if I win or I loose
      
       if(bstMe == plyrCount)
    	   return 1;
       if(bstOther == oppPlCount) 
    	   return -1;
       
       // compute the relative size of the largest groups
       float bestMePct = (float)bstMe / (float)plyrCount;
       float bestOtherPct = (float)bstOther / (float)oppPlCount;
       
       // now compute the relative percentage compared with the other player
       float totPct = bestMePct + bestOtherPct;        
       Float myPct = (bestMePct / totPct)*2.f-1.f; // range [-1,1]
       //Float myPct = (bestMePct / totPct)*100;
       return myPct.intValue();
       //return player == whoami ? myPct : -myPct;
       
   }
	int dfs(int i, int j, State gameState, Player pl)
    {
        Stack<int[]> s = new Stack<>();
        int r = 0;
        
        s.push(new int[]{i,j});
        visitedNodes[i][j] = 1;
        
        while(!s.empty())
        {
            int[] p = s.pop();
            i = p[0];
            j = p[1];
            visitedNodes[i][j] = 2;
            r++;
            
            for(int d=0;d<8;d++)
            {
                int row = i + directions[d][0];
                int col = j + directions[d][1];
                // check the position is valid
                if(row<0 || row>=size || col<0 || col>=size) continue;
                
                // check another checker of the same player
                if(visitedNodes[row][col] == 0 && gameState.board[row][col] == pl.name)
                {
                	visitedNodes[row][col] = 1;
                    s.push(new int[]{row,col});
                }
            }            
        }        
        return r;
    }
	
	
}
