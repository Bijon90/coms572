package coms572;

public class Player {
	//Name of the player
	private String _name;
	//Marker for the player
	private char _marker;
	
	Player(){}
	
    Player(char marker, String name) {
        _marker = marker;
        _name = name;
    }
    
	@Override
	public String toString() {
		return "Player [name=" + _name + ", marker=" + _marker+"]";
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public char get_marker() {
		return _marker;
	}

	public void set_marker(char _marker) {
		this._marker = _marker;
	}

	public char getOpponent(){
		if(this._marker == 'X')
			return 'O';
		else
			return 'X';	
	}
	
	/**
	 * @return oponent
	 */
	public Player getNextPlayer(){
		return this._name.equals("user")? Constants.machine : Constants.user;
	}
}
