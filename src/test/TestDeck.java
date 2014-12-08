package test;

import cards.Deck;
import cards.Card;

public class TestDeck {

	private static Deck d1;

	public static void main(String[] args) {
		testCountSimulate();
	}
	
	public static void testCountSimulate(){
		d1 = new Deck(1);
		Utility.showDeck(d1);
		System.out.println("---------------------------------------------");
		
		d1.simulateCount(-3);
		Utility.showDeck(d1);
		System.out.println("---------------------------------------------");
	}

	public static void testShuffleNGenerate() {
		// tests newly generated deck
		d1 = new Deck(2);
		Utility.showDeck(d1);
		System.out.println("---------------------------------------------");

		// tests shuffle
		System.out.println("EVERYDAY I'M SHUFFLING");
		d1.shuffle();
		Utility.showDeck(d1);
		System.out.println("---------------------------------------------");

		// tests Deal
		System.out.println("Dealing ");
		Card[] testDealCards = new Card[4];
		testDealCards[0] = d1.dealCard();
		testDealCards[1] = d1.dealCard();
		testDealCards[2] = d1.dealCard();
		testDealCards[3] = d1.dealCard();
		Utility.showCard(testDealCards[0]);
		Utility.showCard(testDealCards[1]);
		Utility.showCard(testDealCards[2]);
		Utility.showCard(testDealCards[3]);
		System.out.println("Deck");
		Utility.showDeck(d1);
		System.out.println("Putting Cards Back");
		d1.takeBackCards(testDealCards);
		System.out.println("Deck");
		Utility.showDeck(d1);
	}

}
