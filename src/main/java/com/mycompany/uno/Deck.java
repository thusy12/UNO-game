/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.uno;

import java.util.Random;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Deck {

	private Card[] cards;
	private int cardsInDeck;

	public Deck() {

		cards = new Card[108];
		reset();
	}

	public void reset() {

		Card.colour[] colours = Card.colour.values();
		cardsInDeck = 0;

		for (int i = 0; i < colours.length - 1; i++) {

			Card.colour colour = colours[i];
			// 1 zero card
			cards[cardsInDeck++] = new Card(colour, Card.value.getValue(0));
			// add 2*1-9 cards
			for (int j = 1; j < 10; j++) {
				cards[cardsInDeck++] = new Card(colour, Card.value.getValue(j));
				cards[cardsInDeck++] = new Card(colour, Card.value.getValue(j));
			}

			// add Draw Two, Skip and Reverse
			Card.value[] values = new Card.value[] { Card.value.drawTwo, Card.value.skip, Card.value.reverse };
			for (Card.value value : values) {
				cards[cardsInDeck++] = new Card(colour, value);
				cards[cardsInDeck++] = new Card(colour, value);
			}
		}
		// add wild cards
		Card.value[] values = new Card.value[] { Card.value.wild, Card.value.wildFour };
		for (Card.value value : values) {
			for (int i = 0; i < 4; i++) {
				cards[cardsInDeck++] = new Card(Card.colour.wild, value);
			}
		}

	}

	/// replace the deck with an array list

	public void replaceDeckWith(ArrayList<Card> cards) {
		this.cards = cards.toArray(new Card[cards.size()]);
		this.cardsInDeck = this.cards.length;
	}

	/// if zero cards in the deck

	public boolean emptyDeck() {
		return cardsInDeck == 0;
	}

	public void shuffle() {
		int n = cards.length;
		Random rand = new Random();

		for (int i = 0; i < cards.length; i++) {
			int rVal = i + rand.nextInt(n - i);
			Card rCard = cards[rVal];
			cards[rVal] = cards[i];
			cards[i] = rCard;
		}
	}

	public Card drawCard() throws IllegalArgumentException {
		if (emptyDeck()) {
			throw new IllegalArgumentException("Deck is empty!");
		}
		return cards[--cardsInDeck];
	}

	public ImageIcon drawCardImage() throws IllegalArgumentException {
		if (emptyDeck()) {
			throw new IllegalArgumentException("Deck is empty!");
		}
		return new ImageIcon(cards[--cardsInDeck].toString() + ".png");
	}

	public Card[] drawCard(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Must draw positive cards but tried to draw " + n + " cards.");

		}
		if (n > cardsInDeck) {
			throw new IllegalArgumentException("Can not draw " + n + "\nThere are only " + cardsInDeck + "cards.");
		}

		Card[] ret = new Card[n];

		for (int i = 0; i < n; i++) {
			ret[i] = cards[--cardsInDeck];
		}

		return ret;
	}

}

