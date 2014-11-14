package test;

import player.Player;

public class TestPlayer {
	
	private static Player _playerOne;
	
	public static void main(String[] args) {
		
		_playerOne = new Player(1000);
		Utility.showCards(_playerOne.seeHand());
		
	}

}
