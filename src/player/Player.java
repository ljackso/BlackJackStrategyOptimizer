package player;

import cards.Card; 

public class Player {
	
	private Card[] _hand;
	private int _reserve;	//amount of money left
		
	
	public Player(int cash){
		_reserve = cash;
		_hand = new Card[2];
	}
	
	public void hit(Card c){
		
		if(_hand[1] != null){
			
			Card[] oldHand = _hand;
			int oldLength = _hand.length;
			
			_hand = new Card[oldLength + 1];
			
			for(int l = 0; l < (_hand.length - 1); l++){
				_hand[l] = oldHand[l];
			}
			
			_hand[oldLength] = c;
			
		} else if(_hand[0] == null) {
			_hand[0] = c;
		} else {
			_hand[1] = c;
		}
		
	}
	
	public Card[] returnHand(){
		
		Card[] hand = _hand;
		_hand = new Card[2];
		return hand;
	}
	
	public Card[] seeHand(){
		return _hand;
	}
	
	public int getHandValue(){
		
		int sum = 0;
		
		for(int l = 0; l < _hand.length; l++){
			sum += _hand[l].getValue();
		}
		
		return sum;
	}
	
	public boolean isBust(){
		if(getHandValue() > 21){
			return true;
		}
		return false;
	}
	
	//Returns bet as well because in my head will look smother later on 
	public int placeBet(int bet){
		
		_reserve -= bet;
		
		return bet;
	}
	
	public void recieveWinnings(int won){
		_reserve += won;
	}
	
	public int seeReserve(){
		return _reserve;
	}
	
}
