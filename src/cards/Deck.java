package cards;

import java.util.Random;

public class Deck {
	
	private Card[] _cards = new Card[52]; 
	
	// let cards[0] be the top of the deck and cards [51] be the bottom.	
	public Deck(){
		generateNewCards();		
	}
	
	public void generateNewCards(){
		
		_cards = new Card[52];
		int pos = 0;
		int val;
		
		//uses values for suits defined in cards.Suit
		for(int l = 0; l< 4; l++){
			for(int j = 1; j < 14; j++){
				
				if(j > 10){
					val = 10;
				} else {
					val = j;
				}
				_cards[pos] = new Card(val,l);
				pos++;
			}
		}		
	}
	
	//Implements Fisher-Yates shuffle
	public void shuffle(){
		
		Random rnd = new Random();
		
		for (int l = 51; l > 0; l--){
			
			int index = rnd.nextInt(l + 1);
			Card j = _cards[index];
		    _cards[index] = _cards[l];
		    _cards[l] = j;
		}
	}
	
	public Card dealCard() {
		
		Card topCard = _cards[0];
		
		//Refactor Array to leave space at the bottom
		for(int l = 1; l < 52; l ++){
			_cards[l-1] = _cards[l];
		}
		//Set last card to null. 
		_cards[51] = null;
		
		return topCard;
	}
	
	public void takeBackCard(Card newCard){
		
		int lastEmptySlot = 51;
		
		//Find last position
		for(int l= 51; l> 0;l--){
			if(_cards[l] == null){
				lastEmptySlot = l;
			} else {
				break;
			}	
		}
		
		// put card at bottom of deck
		_cards[lastEmptySlot] = newCard;
		
	}
	
	public void takeBackCards(Card[] newCards){
		
		for(int l = 0; l < newCards.length; l++){
			takeBackCard(newCards[l]);
		}
	}
	
	public Card[] getCards() {
		return _cards;
	}
	
	

}
