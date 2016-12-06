package coms572;

public class Player {
	private String _name;
	private char _marker;
	private GamePlay _game;
	
	Player(){}
	
    Player(char marker, String name) {
        _marker = marker;
        //_game = game;
        _name = name;
    }
    
    Player(char marker, GamePlay game) {
        _marker = marker;
        _game = game;
    }
    
    Player(char marker, GamePlay game, String name){
    	_marker = marker;
    	_name = name;
    	_game = game;
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

	public GamePlay get_game() {
		return _game;
	}

	public void set_game(GamePlay _game) {
		this._game = _game;
	}
	
	public char getOpponent(){
		if(this._marker == 'X')
			return 'O';
		else
			return 'X';	
	}
	
	public Player getNextPlayer(){
		return this._name.equals("user")? Constants.machine : Constants.user;
	}
}
