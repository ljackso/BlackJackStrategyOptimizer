package engine;

import player.Player;
import cards.Card;
import cards.Deck;

// To use for debugging if needed
//import test.Utility;

public class MainGameLoop {

	private static Player _player;
	private static Player _house;

	private static Deck _deck;

	private static int _pot;
	
	private static int _count;
	private static int _shuffleOn;

	public static void main(String[] args) {

		_player = new Player(500);
		_house = new Player(1000);

		_deck = new Deck();
		_pot = 0;
		
		_count = 0;
		_shuffleOn = 10;
		
		//Game Loop
		_deck.shuffle();
		for (int l = 0; l < 100000; l++) {
						
			
			biddingStrategy();
			
			intialHandDeal();

			// Place function here that defines strategy after initial draw to
			// attempt to win hand;
			playStrategyOne();
			
			endHand();
		}

		System.out.println("-----------------------------------------");
		System.out.println("TOTALS");
		System.out.println("house : " + _house.seeReserve());
		System.out.println("player : " + _player.seeReserve());
		System.out.println("-----------------------------------------");

	}
	
	//This is where we will use count to build bidding stratergies
	private static void biddingStrategy(){
		_pot += _player.placeBet(20);
		_pot += _house.placeBet(20);
	}	
	
	// Same process each time
	private static void intialHandDeal() {

		_house.hit(_deck.dealCard());
		_player.hit(_deck.dealCard());

		_house.hit(_deck.dealCard());
		_player.hit(_deck.dealCard());
	}
	
	// Again is the same process each time
	private static void endHand() {
		
		if(_player.hasAce()){
			if((_player.getHandValueExcludingAce() + 11) <= 21){
				for(int l =0; l < _player.handSize(); l ++){
					if(_player.getCardFromHand(l).isAce()){
						//true == set value too 11;
						_player.getCardFromHand(l).setAceValue(true);
						break;
					}
				}
			}
		}
		
		if(_house.hasAce()){
			if((_house.getHandValueExcludingAce() + 11) <= 21){
				for(int l =0; l < _house.handSize(); l ++){
					if(_house.getCardFromHand(l).isAce()){
						//true == set value too 11;
						_player.getCardFromHand(l).setAceValue(true);
						break;
					}
				}
			}
		}	
		
		if ((_house.getHandValue() >= _player.getHandValue())
				|| _player.isBust()) {
			_house.recieveWinnings(_pot);
			System.out.println("h");
		} else {
			_player.recieveWinnings(_pot);
			System.out.println("p");
		}		
		
		countCard(_house.getCardFromHand(0));
		for(int l = 0; l < _player.handSize(); l ++){	
			countCard(_player.getCardFromHand(l));		
		}
		
		_deck.takeBackCards(_player.returnHand());
		_deck.takeBackCards(_house.returnHand());
		_pot = 0;
	}

	private static void countCard(Card c){
		if(c.getValue() >= 8 || c.isAce()){
			_count -= 1;
		} else if(c.getValue() <= 5){
			_count += 1;
		}
	}
	
	// Simple Strategy where player just asks for cards till over
	private static void playStrategyOne() {
		while (_player.getHandValue() <= 12) {
			_player.hit(_deck.dealCard());
		}
	}
	
	//house strategy
	//Is messy but left it like this as it follows my trail of thought will change later
	private static boolean houseStrategy(boolean hitOnSoft17){
		
		boolean stand = false;
		if(_house.getHandValue()  <= 17){
			if(_house.hasAce()){
				if(hitOnSoft17){			
					if((_house.getHandValueExcludingAce() + 11) <= 17){
						_house.hit(_deck.dealCard());
					} else if(_house.getHandValue() <= 17) {
						_house.hit(_deck.dealCard());
					} else {
						stand = true; 
					}
				} else {
					if((_house.getHandValueExcludingAce() + 11) < 17){
						_house.hit(_deck.dealCard());
					} else if(_house.getHandValue() <= 17) {
						_house.hit(_deck.dealCard());
					} else {
						stand = true; 
					}
				}
			} else{
				_house.hit(_deck.dealCard());
			}
		} else {
			stand = true;
		}
		return stand;	
	}

	//Implement Basic Strategy
	private static boolean basicStrategy() {

		Card houseCard = _house.getCardFromHand(0);				
		boolean stand = false;
		if(_player.getHandValue() <= 20) {
			int val = _player.getHandValue();			
			if (!_player.hasAce()) {				
				switch (val) {
				case 20:
				case 19:
				case 18:
				case 17:
					stand = true;
					break;
				case 16:
				case 15:
				case 14:
				case 13:
					if(!houseCard.isAce() || houseCard.getValue() <= 7){
						_player.hit(_deck.dealCard());
					} else {
						stand = true;
					}					
					break;
				case 12:
					if(houseCard.getValue() <= 6 && houseCard.getValue() >= 4){
						stand = true;
					} else {
						_player.hit(_deck.dealCard());
					}					
					break;
				default:
					_player.hit(_deck.dealCard());
					break;
				}				
			} else {
				val = _player.getHandValueExcludingAce();
				switch (val) {
				case 2:
				case 3:
				case 4: 
				case 5:
				case 6:
					_player.hit(_deck.dealCard());
					break;
				case 7:
					if(houseCard.getValue() <= 8 && houseCard.getValue() >= 2){
						stand = true;
					} else {
						_player.hit(_deck.dealCard());
					}
					break;					
				case 8:
				case 9:
					_player.hit(_deck.dealCard());
					break;
				default:
					stand = true;
					break;
				}
			}
		}
		return stand;
	}
	
}
