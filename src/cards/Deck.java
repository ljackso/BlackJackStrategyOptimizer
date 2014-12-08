package cards;

import java.util.Random;

import test.Utility;
import cards.Card.Type;

public class Deck {

	private static int DECK_SIZE = 52;

	private Card[] _cards;
	private int _numDecks;

	// let cards[0] be the top of the deck and cards [51] be the bottom.
	public Deck(int numDecks) {
		_numDecks = numDecks;
		generateNewCards();
	}

	public void generateNewCards() {
		_cards = new Card[DECK_SIZE * _numDecks];
		int pos = 0;
		int val;
		int faceCount = 0;
		Type type = Type.NUMBER;
		// uses values for suits defined in cards.Suit
		for (int i = 0; i < _numDecks; i++) {
			for (int l = 0; l < 4; l++) {
				type = Type.NUMBER;
				faceCount = 0;
				for (int j = 1; j < 14; j++) {
					if (j >= 10) {
						val = 10;
						if (faceCount == 1) {
							type = Type.JACK;
						} else if (faceCount == 2) {
							type = Type.QUEEN;
						} else if (faceCount == 3) {
							type = Type.KING;
						}
						faceCount++;
					} else {
						val = j;
					}
					// Ace Type Assignment dealt with in Card class.
					_cards[pos] = new Card(val, l, type);
					pos++;
				}
				faceCount = 0;
			}
		}
	}

	// Implements Fisher-Yates shuffle
	public void shuffle() {
		Random rnd = new Random();
		for (int l = ((_numDecks * DECK_SIZE) - 1); l > 0; l--) {
			int index = rnd.nextInt(l + 1);
			Card j = _cards[index];
			_cards[index] = _cards[l];
			_cards[l] = j;
		}
	}

	public Card dealCard() {
		Card topCard = _cards[0];

		// Refactor Array to leave space at the bottom
		for (int l = 1; l < (DECK_SIZE * _numDecks); l++) {
			_cards[l - 1] = _cards[l];
		}
		// Set last card to null.
		_cards[(DECK_SIZE * _numDecks) - 1] = null;
		return topCard;
	}

	public void takeBackCard(Card newCard) {

		int lastEmptySlot = (DECK_SIZE * _numDecks) - 1;

		// Find last position
		for (int l = ((DECK_SIZE * _numDecks) - 1); l > 0; l--) {
			if (_cards[l] == null) {
				lastEmptySlot = l;
			} else {
				break;
			}
		}
		// put card at bottom of deck
		_cards[lastEmptySlot] = newCard;

	}

	public void takeBackCards(Card[] newCards) {

		for (int l = 0; l < newCards.length; l++) {
			takeBackCard(newCards[l]);
		}
	}

	public void simulateCount(int count) {

		shuffle();
		boolean neg;

		if (count < 0) {
			neg = true;
			count *= -1;
		} else {
			neg = false;
		}

		int cardsMoved = 0;
		int val;
		int suit;
		Card[] removedCards = new Card[count];

		shuffle();
		while (cardsMoved < count) {
			if (neg) {
				
				
			} else {
				val = Utility.randInt(2, 6);
				suit = Utility.randInt(0, 3);

				for (int l = 0; l < _cards.length; l++) {
					if (_cards[l] != null) {
						if (_cards[l].getValue() == val
								&& _cards[l].getSuit() == suit) {

							removedCards[cardsMoved] = _cards[l];
							_cards[l] = null;
							cardsMoved++;
							break;
						}
					}
				}
			}
		}

		// Squash Array (complete hack)
		Card[] newCards = new Card[DECK_SIZE * _numDecks];
		int indexDiff = 0;
		for (int l = 0; (l + indexDiff) < (newCards.length); l++) {
			while ((l + indexDiff) < (newCards.length - 1)
					&& _cards[l + indexDiff] == null) {
				indexDiff++;
			}
			newCards[l] = _cards[l + indexDiff];
		}

		// Put unused cards on the bottom
		int j = 0;
		for (int l = (newCards.length - count); l < newCards.length; l++) {
			newCards[l] = removedCards[j];
			j++;
		}
		_cards = newCards;

	}

	public Card[] getCards() {
		return _cards;
	}

}
