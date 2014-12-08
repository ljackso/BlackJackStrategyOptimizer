package test;

import java.util.Random;

import cards.Card;
import cards.Deck;

public class Utility {

	public static void showDeck(Deck d) {
		Card[] cards = d.getCards();
		showCards(cards);
	}

	public static void showCards(Card[] cards) {
		for (int l = 0; l < cards.length; l++) {
			if (cards[l] != null) {
				showCard(cards[l]);
			} else {
				System.out.println("No Card");
			}
		}
	}

	public static void showCard(Card c) {
		System.out.println("suit : " + c.getSuit() + " value : " + c.getValue()
				+ " type : " + c.getType());
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

}
