package game;

import java.io.Serializable;

public class Player implements Serializable{
	
	String playerName;
	
	public Player(String playerName) {
		this.playerName = playerName;
	}
	
	public Player() {
		// TODO Auto-generated constructor stub
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerName(){
		return this.playerName;
	}
}
