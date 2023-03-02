/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.uno;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    private int player;      //Current player
    private String[] playerNames;  //Names of players

    private Deck deck;
    private ArrayList<ArrayList<Card>> playerHand;
    private ArrayList<Card> stockPile;

    private Card.colour validClr;
    private Card.value validVal;

    boolean direction;

    public Game(String[] names) {
        deck = new Deck();
        deck.shuffle();
        stockPile = new ArrayList<Card>();

        playerNames = names;
        player = 0;
        direction = false;

        playerHand = new ArrayList<ArrayList<Card>>();

    for (int i=0;i<names.length;i++){
    ArrayList<Card> hand = new ArrayList<Card>(Arrays.asList(deck.drawCard(7)));
    playerHand.add(hand);
    }
    }

    public void start(Game game) {
        Card card = deck.drawCard();
        validClr = card.getColour();
        validVal = card.getValue();

        if (card.getValue() == Card.value.wild) {
            start(game);
        }
        if (card.getValue() == Card.value.wildFour || card.getValue() == Card.value.drawTwo) {
            start(game);
        }
        if (card.getValue() == Card.value.skip) {
            JLabel msg = new JLabel(playerNames[player] + " was skipped!");
            msg.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, msg);

            if (direction == false) {
                player = (player + 1) % playerNames.length;
            }
            else if (direction == true) {
                player = (player - 1) % playerNames.length;
                if (player == -1) {
                    player = playerNames.length - 1;
                }
            }
        }
        if (card.getValue() == Card.value.reverse) {
            JLabel msg = new JLabel(playerNames[player] + " The game direction changed!");
            msg.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, msg);
            direction ^= true;
            player = playerNames.length - 1;
        }

        stockPile.add(card);

    }

    public Card getTopCard() {
        return new Card(validClr,validVal);
    }

    public ImageIcon getTopCardImage(){
        return new ImageIcon(validClr+"_"+validVal+".png");
    }
    
    public String getTopCardName(){
        return new String(validClr+"_"+validVal+".png");
    }

    public boolean gameOver(){
        for(String player : this.playerNames) {
            if(hasEmptyHand(player)){
                return true;
            }
        }
        return false;
    }

    public String getCurrentPlayer(){
        return this.playerNames[this.player];
    }

    public String getPreviousPlayer(int i){
        int index=this.player - i;
        if (index ==-1){
            index=playerNames.length-1;
        }
        return this.playerNames[index];
    }

    public String[] getPlayers(){
        return playerNames;
    }

    public ArrayList<Card> getPlayerHand(String name){
        int index = Arrays.asList(playerNames).indexOf(name);
        return playerHand.get(index);
    }

    public int getPlayerHandSize(String name){
        return getPlayerHand(name).size();
    }

    public Card getPlayerCard(String name, int choice){
        ArrayList<Card> hand=getPlayerHand(name);
        return hand.get(choice);
    }

    public boolean hasEmptyHand(String name){
        return getPlayerHand(name).isEmpty();
    }

    public boolean validCardPlay(Card card){
        return card.getColour()==validClr||card.getValue()==validVal;
    }

    public void checkPlayerTurn(String name) throws InvalidPlayerTurnException {
        if(this.playerNames[this.player] != name){
            throw new InvalidPlayerTurnException("It is not "+ name + " 's turn" , name);
        }
    }

    public void submitDraw(String name) throws InvalidPlayerTurnException {
        checkPlayerTurn(name);

        if (deck.emptyDeck()) {
            deck.replaceDeckWith(stockPile);
            deck.shuffle();
        }
        getPlayerHand(name).add(deck.drawCard());
        if (direction==false){
            player=(player+1)%playerNames.length;
        }
        else if (direction==true){
            player=(player-1)%playerNames.length;
            if(player==-1){
                player=playerNames.length-1;
            }
        }
    }

    public void setCardColour(Card.colour clr){
        validClr=clr;
    }

    public void submitCard (String name, Card card, Card.colour declareColour)
        throws InvalidColourSubmissionException, InvalidValueSubmissionException, InvalidPlayerTurnException {
        checkPlayerTurn(name);

        ArrayList<Card> plHand = getPlayerHand(name);

        if(!validCardPlay(card)){
            if(card.getColour()==Card.colour.wild) {
                validClr=card.getColour();
                validVal=card.getValue();
            }
            if (card.getColour()!=validClr){
                JLabel message = new JLabel("Invalid player move, expected colour: "+validClr+" but got colour "+card.getColour());
                message.setFont(new Font("Arial",Font.BOLD,48));
                JOptionPane.showMessageDialog(null,message);
                throw new InvalidColourSubmissionException("Invalid player move, expected colour: "+validClr+" but got colour "+card.getColour(),card.getColour(),validClr);
            }
            else if(card.getValue()!=validVal) {
                JLabel message2 = new JLabel("Invalid player move, expected value: "+validVal+" but got colour" + card.getValue());
                message2.setFont(new Font("Arial",Font.BOLD,48));
                JOptionPane.showMessageDialog(null,message2);
                throw new InvalidValueSubmissionException("Invalid player move, expected value: "+validVal+" but got colour" + card.getValue(),card.getValue(),validVal);
            }

        }

        plHand.remove(card);

        if(hasEmptyHand(this.playerNames[player])) {
            JLabel message = new JLabel(this.playerNames[player]+"won the game! Thank you for playing!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null,message);
            System.exit(0);
        }

        validClr=card.getColour();
        validVal=card.getValue();
        stockPile.add(card);

        if(direction==false) {
            player = (player + 1) % playerNames.length;
        }
        else if(direction==true){
            player=(player-1)%playerNames.length;
            if (player==-1){
                player=playerNames.length-1;
            }
        }

        if(card.getValue()==Card.value.drawTwo){
            name=playerNames[player];
            getPlayerHand(name).add(deck.drawCard());
            getPlayerHand(name).add(deck.drawCard());
            JLabel message = new JLabel(name + " drew 2 cards!");
        }

        if(card.getValue()==Card.value.wildFour){
            name=playerNames[player];
            getPlayerHand(name).add(deck.drawCard());
            getPlayerHand(name).add(deck.drawCard());
            getPlayerHand(name).add(deck.drawCard());
            getPlayerHand(name).add(deck.drawCard());
            JLabel message = new JLabel(name + " drew 4 cards!");
        }

        if (card.getValue()==Card.value.skip) {
            JLabel message = new JLabel(playerNames[player] + " was skipped!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null,message);
            if(direction==false){
                player = (player+1)%playerNames.length;
            }
            else if(direction==true){
                player = (player-1) % playerNames.length;
                if(player==-1){
                    player = playerNames.length-1;
                }
            }
        }

        if (card.getValue() == Card.value.reverse) {
            JLabel message = new JLabel(name + " changed the game direction!");
            message.setFont(new Font("Arial",Font.BOLD,48));
            JOptionPane.showMessageDialog(null,message);

            direction ^= true;
            if (direction==true){
                player=(player-2)%playerNames.length;
                if (player == -1){
                    player = playerNames.length - 1;
                }
                if (player == -2) {
                    player = playerNames.length - 2;
                }
            }
            else if (direction == false) {
                player = (player + 2) % playerNames.length;
            }
        }





    }

}

class InvalidPlayerTurnException extends Exception {
    String playerName;

    public InvalidPlayerTurnException(String message, String name) {
        super(message);
        playerName=name;
    }
    public String getName(){
        return playerName;
    }
}

class InvalidColourSubmissionException extends Exception {
    private Card.colour expected;
    private Card.colour actual;

    public InvalidColourSubmissionException(String message, Card.colour actual, Card.colour expected){
        this.actual=actual;
        this.expected=expected;
    }
}

class InvalidValueSubmissionException extends Exception {
    private Card.value expected;
    private Card.value actual;

    public InvalidValueSubmissionException(String message, Card.value actual, Card.value expected){
        this.actual=actual;
        this.expected=expected;
    }
}

