package engine;

import player.Player;
import cards.Deck;

// To use for debugging if needed
//import test.Utility;


public class MainGameLoop {
	
	private static Player _player;
	private static Player _house;
	
	private static Deck _deck;
	
	private static int _pot;
	
	public static void main(String[] args) {
		
		_player = new Player(500);
		_house = new Player(1000);
		
		_deck = new Deck();
		_pot = 0;
	
		//Game Loop
		_deck.shuffle();
		for(int l = 0; l < 10; l++){
			
			intialHandDeal();
			
			//Place function here that defines strategy after initial draw to attempt to win hand; 
			playStratergyOne();
			
			endHand();
		}
		
		System.out.println("-----------------------------------------");
		System.out.println("TOTALS");
		System.out.println("house : " + _house.seeReserve());
		System.out.println("player : " + _player.seeReserve());
		System.out.println("-----------------------------------------");
		
	}
	
	//Same process each time
	private static void intialHandDeal(){
		
		_pot += _player.placeBet(20);
		_pot += _house.placeBet(20);
				
		_house.hit(_deck.dealCard());
		_player.hit(_deck.dealCard());
	
		_house.hit(_deck.dealCard());
		_player.hit(_deck.dealCard());
	}
	
	//Again is the same process each time
	private static void endHand(){
		
		if((_house.getHandValue() >= _player.getHandValue()) || _player.isBust()){
			_house.recieveWinnings(_pot);
			System.out.println("h");
		} else {
			_player.recieveWinnings(_pot);
			System.out.println("p");
		}
		
		_deck.takeBackCards(_player.returnHand());
		_deck.takeBackCards(_house.returnHand());
		_pot = 0;	
	}
	
	//Basic Strategy where player just asks for cards till over
	private static void playStratergyOne(){
		while(_player.getHandValue() <= 12){
			_player.hit(_deck.dealCard());
			System.out.println("player hit");
		}		
	}	

}
