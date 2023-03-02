/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.uno;

public class Card {

	enum colour {
		blue, green, red, yellow, wild;

		private static final colour[] colours = colour.values();

		static colour getColour(int i) {
			return colour.colours[i];
		}
	}

	enum value {
		zero, one, two, three, four, five, six, seven, eight, nine, skip, reverse, drawTwo, wild, wildFour;

		private static final value[] values = value.values();

		static value getValue(int i) {
			return value.values[i];
		}
	}

	private final colour cardColour;
	private final value cardValue;

	public Card(colour c, value v) {
		cardColour = c;
		cardValue = v;
	}

	public colour getColour() {
		return cardColour;
	}

	public value getValue() {
		return cardValue;
	}

	public String toString() {
		return cardColour + "_" + cardValue;
	}

}
