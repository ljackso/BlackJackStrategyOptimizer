package cards;

public class Card {
	
	private int _value;
	private int _suit;
	private boolean _isAce;
	
	public Card(int value, int suit){
		setValue(value);
		setSuit(suit);
	}
	
	public void setValue(int value){
		if(value == 1){
			_isAce = true;
		} else {
			_isAce = false;
		}		
		_value = value;
	}
	
	public void setSuit(int suit){
		_suit = suit;
	}
	
	public int getValue(){
		return _value;
	}
	
	public int getSuit(){
		return _suit;
	}
	
	public boolean isAce(){
		return _isAce;
	}
}
