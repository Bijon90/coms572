package line.of.action;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;


/*
 * For the whole project in case of any array like move[]
 * <p> The first element move[0] will correspond to row of source
 * <p> The second element move[1] will correspond to column of source
 * <p> The second element move[2] will correspond to column of destination
 * <p> The second element move[3] will correspond to column of destination
 */

public class LoaGame 
{
	/*
	 * some variables used by the 
	 * 
	 */
	public static int depth;
	public static final char W='X',B='O',EMPTY='_';
	State gameState;
	Player player;
	static int defaultDepth=3;
    static int nodesExpanded=0;
	static int size=8;
	int directions [][] = 
		{{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
	int [][] visitedNodes;
	static boolean debugging=false;
	static int delay;
	
	/*
	 * initialize the game variables
	 */
	public LoaGame() throws Exception
	{
		gameState= new State();
		depth=defaultDepth;
		initializeBoard();
		gameState.printBoard();
		player= new Player(B);
		player.setOtherPlayerName(W);
		// for each location on board. 0=not visited,1=in stack,2=visited   
		visitedNodes= new int[size][size]; 
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
	/**
	 * Checks if left horizontal move is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move				
	 */
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
	 * Checks if a right horizontal move is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move	
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
	 * Checks if a vertical up move is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move	
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
	 * Checks if a vertical down move is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move	
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
	 * Checks if a move in north-east direction is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move	
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
	 * Checks if a move in north-west direction is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move	
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
	 * Checks if a move in south-east direction is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move	
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
	 * Checks if a move in south-west direction is possible
	 * @param currentState 		state of the board
	 * @param player 			the player for whom to check
	 * @param row 				the row of the checker
	 * @param column			the column of the checker
	 * @return					if a move is possible, return the move	
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
	
	/**
	 * @param legalMoves 		the list of legal moves
	 * @param startMove			the starting location of move to add
	 * @param endMove			the endding location of move to add
	 */
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
	
	/**
	 * The main alpha-beta search
	 * @param currentState		the state of the board
	 * @param player			the player whose turn 
	 * @param maxDepth			the maximum depth of tree to analyze
	 * @return					the move to take by the agent
	 */
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
	 * @param currentState 		the current state of the board
	 * @return					whether the terminal state is reached
	 */
	public boolean isGameOver(State currentState) 
	{
		return piecesContiguous(currentState,W) || piecesContiguous(currentState,B);
	}
	
	/**
	 * @param currentState		the current state of the board 
	 * @param w					the player's whom to check
	 * @return					whether all the peices of the player is contiguous
	 */
	boolean piecesContiguous(State currentState, char w) 
	{
		Player pl= new Player(w);
		ArrayList<int[]> tilesToCheck = getCoordinates(currentState,pl);
        if (tilesToCheck.size() <= 1) 
        {
            return true;
        }
        ArrayList<int[]> alreadyContiguous = new ArrayList<int[]>();
        alreadyContiguous.add(tilesToCheck.get(0)); tilesToCheck.remove(0);
        boolean connected, isDisconnected;
        while (tilesToCheck.size() != 0) 
        {
            isDisconnected = true;
            for (int[] piece : tilesToCheck) 
            {
                connected = isConnected(alreadyContiguous, piece);
                if (connected) 
                {
                    alreadyContiguous.add(piece); tilesToCheck.remove(piece);
                    isDisconnected = false;
                    break;
                }
            }
            if (isDisconnected) 
            {
                return false;
            }
        }
        return true;
	}
	/**
	 * Check if a checker if connected to already connected group or not
	 * @param grouped		the connected group of tiles
	 * @param piece			the location of a single tile
	 * @return				whether the tile if connected or not 
	 */
	private static boolean isConnected(ArrayList<int[]> grouped, int[] piece) 
    {
        for (int[] groupedCoin : grouped) 
        {
            int distanceX = Math.abs(piece[0] - groupedCoin[0]), distanceY = Math.abs(piece[1] - groupedCoin[1]);
            if (distanceX <= 1 && distanceY <= 1) 
            {
                return true;
            }
        }
        return false;
    }
	/**
	 * @param currentState 	the state of the board
	 * @param pl			the player 
	 * @return				the list of coordinates of the player's tiles
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
	 * @param currentState		the	state of the board 
	 * @param player			the player for which we need to find the possible moves
	 * @return					the list of moves possible for the player
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
	/**
	 * 
	 * @param state 		the state of the board
	 * @param prevMove		the move of the player 
	 * @param currPlayer	the player
	 * @param depth			the current depth for alpha beta search
	 * @param maxDepth		the maximum depth of the search
	 * @param alpha			the value of alpha
	 * @param beta			the value of beta
	 * @return				the highest value for player
	 */
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
	
	/**
	 * 
	 * @param state 		the state of the board
	 * @param prevMove		the move of the player 
	 * @param currPlayer	the player
	 * @param depth			the current depth for alpha beta search
	 * @param maxDepth		the maximum depth of the search
	 * @param alpha			the value of alpha
	 * @param beta			the value of beta
	 * @return				the minimum value for opponent
	 */
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
	/**
	 * @param nodesBucket   	sort the nodes in the bucket of nodes for getting the maximum value
	 */
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
					// if value is same, give more preference if the final position if towards the center of the board
					int row1 = Math.abs(n1.getMove()[2]- 4 );
					int col1 = Math.abs(n1.getMove()[3]-4 );
					
					int row2 = Math.abs(n2.getMove()[2]- 4);
					int col2 = Math.abs(n2.getMove()[3]- 4);
					if(row1+col1 != row2+col2)
					{
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
	/**
	 * @param nodesBucket		sort the nodes in the bucket of nodes for getting the minimum value
	 */
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
					// if value is same, give more preference if the final position if towards the center of the board
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
	/**
	 * @param gameState		the state of the board
	 * @param moveStart		the starting position of the tile
	 * @param moveEnd		the ending position of the tile
	 * @param player		the player whose chance it is
	 * @return				the modified state of the board after this move is applied
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
	 * @param gameState		the state of the board
	 * @param moveStart		the starting position for the move
	 * @param moveEnd		the ending position for the move
	 * @param player		the player whose turn it is		
	 * @return				check if this move is a valid move
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
	/**
	 * @param gameState		the state of the game
	 * @param player		the player whose turn it is
	 * @return				evaluate the board and returns a value representing the boards's state in favor of player
	 */
	public int evaluate(State gameState,Player player)  // evaluates the current board
   {
	   int plyrCount=0,oppPlCount=0;
	   int playerClusterCount = 0,opponentClusterCount = 0;           
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
       // find the size of largest cluster for the player
       for(int i=0; i<size; i++)
       {
           for(int j=0;j<size; j++)
           {
               if(visitedNodes[i][j]==0)
               {
                   if(gameState.board[i][j]==player.name)
                   {
                       int ct = depthFirstSearch(i,j,gameState,player);
                       if(ct > playerClusterCount)
                           playerClusterCount = ct;
                   }
                   else if(gameState.board[i][j]==player.getOpponent().name)
                   {
                       int ct = depthFirstSearch(i,j,gameState,player.getOpponent());
                       if(ct > opponentClusterCount)
                           opponentClusterCount = ct;
                   }
               }
           }
       }
       if(playerClusterCount == plyrCount)
    	   return 1000;
       if(opponentClusterCount == oppPlCount) 
    	   return -1000;
       // find the relative size
       float playerPct = (float)playerClusterCount / (float)plyrCount;
       float oppoPct = (float)opponentClusterCount / (float)oppPlCount;
       // calculate the relative percentage compared to the opponent
       float totPct = playerPct + oppoPct;        
       Float myPct = (playerPct / totPct)*1000;
       Integer result=myPct.intValue();
       return result;
   }
	/**
	 * @param i 			the row to start the search
	 * @param j				the column to start the search
	 * @param gameState		the state of the game
	 * @param pl			the player whose checkers to check
	 * @return				the size of largest cluster for this player
	 */
	int depthFirstSearch(int i, int j, State gameState, Player pl)
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
                if(row<0 || row>=size || col<0 || col>=size) 
                	continue;
                // check if the position if of the same player
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
