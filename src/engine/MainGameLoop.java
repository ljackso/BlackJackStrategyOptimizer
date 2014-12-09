package engine;

import java.util.Random;

import player.Player;
import cards.Card;
import cards.Deck;

// To use for debugging if needed
//import test.Utility;

public class MainGameLoop {

	private static final boolean DEBUG = false;
	private static final int COUNT_RNG = 20;

	// Deck to be used
	private static Deck _deck;

	// Players info
	private static Player _player;
	private static Player _house;
	private static int _pot;

	// used for card counting and
	private static double _playerWin;
	private static int _count;

	public static void main(String[] args) {

		_player = new Player(50000);
		_house = new Player(100000);

		_deck = new Deck(1);
		_pot = 0;

		double[] countGames = new double[1 + (COUNT_RNG * 2)];
		double[] countWin = new double[1 + (COUNT_RNG * 2)];
		double[] countBlackJack = new double[1 + (COUNT_RNG * 2)];
		double[] countPush = new double[1 + (COUNT_RNG * 2)];
		
		// Game Loop
		_deck.shuffle();
		for (int count = 0; count < ((2 * COUNT_RNG) + 1); count++) {
			for (int l = 0; l < 100000; l++) {
				
				if (count != COUNT_RNG) {
					_deck.simulateCount(count - COUNT_RNG);
				} else {
					_deck.shuffle();
				}
				
				biddingStrategy();
				intialHandDeal();

				if (DEBUG) {
					System.out
							.println("-----------------------------------------");
					System.out.println("New Hand");
					System.out.println("h ||  p");
					System.out.println(_house.getCardFromHand(0).getValue()
							+ " || " + _player.getCardFromHand(0).getValue());
					System.out.println(_house.getCardFromHand(1).getValue()
							+ " || " + _player.getCardFromHand(1).getValue());
				}

				boolean houseHitOnSoft17 = true;
				basicStrategy(houseHitOnSoft17);
				houseStrategy(houseHitOnSoft17);
				endHand();

				int countIndex = count;
				if (countIndex < (1 + (2 * COUNT_RNG)) && countIndex > -1) {
					countGames[countIndex] += 1;
					if (playerWonHand()) {
						if (_playerWin == 0.5) {
							countPush[countIndex] += 1;
						} else if (_playerWin == 1) {
							countWin[countIndex] += 1;
						} else if (_playerWin == 2) {
							countBlackJack[countIndex] += 1;
						}
					}
				}
				returnCards();
			}
		}

		double probWin, probPush, probBlackJack, probLose, f, bestF, sum, bestSum;
		System.out.println("-----------------------------------------");
		for (int l = 0; l < countGames.length; l++) {
			int c = l - COUNT_RNG;
			probBlackJack = countBlackJack[l] / countGames[l];
			probWin = countWin[l] / countGames[l];
			probPush = countPush[l] / countGames[l];
			probLose = 1 - (probBlackJack + probWin + probPush);

			// This is where we can define our bidding function based on the
			// stats we have, just using a rough interpretation at the moment
			f = 0;
			sum = 0;
			bestSum = 0;
			bestF = 0;
			while (f < 1) {				
				sum = (probWin * Math.log(1 + f))
						+ (probBlackJack * Math
								.log(1 + (2 * f)))
						+ (probPush * Math.log(1))
						+ (probLose * Math.log(1 - f));

				if(sum > bestSum){
					bestSum = sum;
					bestF = f;
				}
				f += 0.001;
			}
			System.out.println(c + ", " + probBlackJack + ", " + probWin + ", "
					+ probPush + ", " + probLose + ", " + bestF);

		}
		System.out.println("-----------------------------------------");

	}

	// This is where we will use count to build bidding strategies
	private static void biddingStrategy() {
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

		if (_player.hasAce()) {
			if ((_player.getHandValueExcludingAce() + 11) <= 21) {
				for (int l = 0; l < _player.handSize(); l++) {
					if (_player.getCardFromHand(l).isAce()) {
						// true == set value too 11;
						_player.getCardFromHand(l).setAceValue(true);
						break;
					}
				}
			}
		}
		if (_house.hasAce()) {
			if ((_house.getHandValueExcludingAce() + 11) <= 21) {
				for (int l = 0; l < _house.handSize(); l++) {
					if (_house.getCardFromHand(l).isAce()) {
						// true == set value too 11;
						_house.getCardFromHand(l).setAceValue(true);
						break;
					}
				}
			}
		}

		if ((_house.getHandValue() > _player.getHandValue() && !_house.isBust())
				|| _player.isBust()
				|| (_house.hasBlackJack() && !_player.hasBlackJack())) {

			_playerWin = 0;
			if (DEBUG) {
				System.out.println("House Win");
			}
		} else if (_player.getHandValue() == _house.getHandValue()) {
			_playerWin = 0.5;
			if (DEBUG) {
				System.out.println("Push");
			}
		} else if (_player.hasBlackJack()) {
			_playerWin = 2;
			if (DEBUG) {
				System.out.println("Player BlackJack");
			}
		} else {
			_playerWin = 1;
			if (DEBUG) {
				System.out.println("Player Win");
			}
		}
	}

	private static void returnCards() {

		// for (int l = 0; l < _house.handSize(); l++) {
		// countCard(_house.getCardFromHand(l));
		// }
		_house.getCardFromHand(0);
		for (int l = 0; l < _player.handSize(); l++) {
			countCard(_player.getCardFromHand(l));
		}
		_deck.takeBackCards(_player.returnHand());
		_deck.takeBackCards(_house.returnHand());
		_pot = 0;
	}

	// Uses simple Hi-Lo count
	private static void countCard(Card c) {
		if (c.getValue() == 10 || c.isAce()) {
			_count -= 1.0;
		} else if (c.getValue() <= 6 && c.getValue() >= 2) {
			_count += 1.0;
		}
	}

	// Might come in useful
	private static boolean playerWonHand() {
		return (_playerWin > 0);
	}

	// house strategy soft 17 == (Ace + 6)
	private static void houseStrategy(boolean hitOnSoft17) {

		int hits = 2;
		while (_house.getHandValue() < 17 && !_house.hasBlackJack()) {
			if (_house.hasAce()) {
				if (hitOnSoft17) {
					if (_house.getHandValueExcludingAce() <= 6) {
						_house.hit(_deck.dealCard());
						if (DEBUG) {
							System.out.println("House Hit : "
									+ _house.getCardFromHand(hits).getValue());

						}
					} else if ((_house.getHandValueExcludingAce() + 11) > 21
							&& _house.getHandValue() < 17) {
						_house.hit(_deck.dealCard());
						if (DEBUG) {
							System.out.println("House Hit : "
									+ _house.getCardFromHand(hits).getValue());
						}
					} else {
						return;
					}
				} else {
					if (_house.getHandValueExcludingAce() < 6) {
						_house.hit(_deck.dealCard());
						if (DEBUG) {
							System.out.println("House Hit : "
									+ _house.getCardFromHand(hits).getValue());

						}
					} else if ((_house.getHandValueExcludingAce() + 11) > 21
							&& _house.getHandValue() < 17) {
						_house.hit(_deck.dealCard());
						if (DEBUG) {
							System.out.println("House Hit : "
									+ _house.getCardFromHand(hits).getValue());

						}
					} else {
						return;
					}

				}
			} else {
				_house.hit(_deck.dealCard());
				if (DEBUG) {
					System.out.println("House Hit : "
							+ _house.getCardFromHand(hits).getValue());

				}

			}
			hits++;
		}
	}

	// PLAYER STRATERGIES
	// Implement Basic Strategy
	private static void basicStrategy(boolean hitOnSoft17) {

		Card houseCard = _house.getCardFromHand(0);
		int hits = 2;
		while (_player.getHandValue() <= 20 && !_player.hasBlackJack()) {
			int val = _player.getHandValue();
			if (!_player.hasAce()) {
				switch (val) {
				case 20:
				case 19:
				case 18:
				case 17:
					return;
				case 16:
				case 15:
				case 14:
				case 13:
					if (!houseCard.isAce() || houseCard.getValue() <= 7) {
						return;
					} else {
						_player.hit(_deck.dealCard());
						if (DEBUG) {
							System.out.println("Player Hit : "
									+ _player.getCardFromHand(hits).getValue());
						}
					}
					break;
				case 12:
					if (houseCard.getValue() <= 6 && houseCard.getValue() >= 4) {
						return;
					} else {
						_player.hit(_deck.dealCard());
						if (DEBUG) {
							System.out.println("Player Hit : "
									+ _player.getCardFromHand(hits).getValue());
						}
					}
					break;
				default:
					_player.hit(_deck.dealCard());
					if (DEBUG) {
						System.out.println("Player Hit : "
								+ _player.getCardFromHand(hits).getValue());
					}
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
					if (houseCard.getValue() == 9 || houseCard.getValue() == 10
							|| (hitOnSoft17 && houseCard.isAce())) {
						_player.hit(_deck.dealCard());
						if (DEBUG) {
							System.out.println("Player Hit : "
									+ _player.getCardFromHand(hits).getValue());
						}
					} else {
						return;
					}
					break;
				default:
					return;
				}
			}
			hits++;
		}
	}

	private static void simpleStrategy() {

		int hits = 2;
		while (_player.getHandValue() < 17 && !_player.hasBlackJack()) {
			if (_player.hasAce()) {
				if (_player.getHandValueExcludingAce() < 6) {
					_player.hit(_deck.dealCard());
					if (DEBUG) {
						System.out.println("Player Hit : "
								+ _player.getCardFromHand(hits).getValue());
					}
				} else if ((_player.getHandValueExcludingAce() + 11) > 21
						&& _player.getHandValue() < 17) {
					_player.hit(_deck.dealCard());
					if (DEBUG) {
						System.out.println("Player Hit : "
								+ _player.getCardFromHand(hits).getValue());
					}
				} else {
					return;
				}

			} else {
				_player.hit(_deck.dealCard());
				if (DEBUG) {
					System.out.println("Player Hit : "
							+ _player.getCardFromHand(hits).getValue());

				}
			}
			hits++;
		}

	}

}
