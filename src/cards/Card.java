package cards;

public class Card {
	
	private int _value;
	private int _suit;
	private Type _type;
	
	public enum Type{
		NUMBER, JACK, QUEEN, KING, ACE;
	}	
	
	public Card(int value, int suit){
		setValue(value);
		setSuit(suit);
	}
	
	public void setValue(int value){
		if(value == 1){
			_type = Type.ACE;
		} else {
			_type = Type.NUMBER;
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
		return (_type == Type.ACE);
	}
	
	public void setAceValue(boolean high){
		if(isAce()){
			if(high){
				_value = 11;
			} else {
				_value = 1;
			}
		}
	}
}
