package coms572;

public class Constants {
	/**Markers*/
	public static final char BLACK = 'X', WHITE = 'O', EMPTY = '*', BUFFER = '.';
	
	/**User*/
	public static Player user = new Player(BLACK, new GamePlay(), "user");
	
	/**Machine*/
	public static Player machine = new Player(WHITE, new GamePlay(), "machine");
	
	/** The standard initial configuration for Lines of Action. */
    static final char[][] INITIAL_PIECES = {
        { BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER  },
        { BUFFER, EMPTY, BLACK,  BLACK,  BLACK,  BLACK,  BLACK,  BLACK,  EMPTY, BUFFER  },
        { BUFFER, WHITE,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE, BUFFER  },
        { BUFFER, WHITE,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE, BUFFER  },
        { BUFFER, WHITE,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE, BUFFER  },
        { BUFFER, WHITE,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE, BUFFER  },
        { BUFFER, WHITE,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE, BUFFER  },
        { BUFFER, WHITE,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE, BUFFER  },
        { BUFFER, EMPTY, BLACK,  BLACK,  BLACK,  BLACK,  BLACK,  BLACK,  EMPTY, BUFFER },
        { BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER, BUFFER  }
    };
    
    /** An array that maps direction to unit vectors.
    The index is the direction d. */
	public static final int[][] UNIT_VECTORS = {{0, 1}, {1, 1}, {1, 0}
    , {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
}
