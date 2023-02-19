package model.deck;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import constant.CardConstants;
import constant.FileConstants;
import model.card.LeaderCard;

/**
 * The Class LeaderDeck.
 */
public class LeaderDeck implements Serializable  {
	
	private static final long serialVersionUID = 6540351317616183197L;
	private LeaderCard[] cards;

	public LeaderDeck() throws IOException{
		cards = DeckCreator.createLeaderCardDeck(FileConstants.LEADERCARDS, CardConstants.LEADER_DECK_LENGTH);
	}

	public void shuffleDeck() {
		Random rnd = new Random();

		for (int i = cards.length; i > 1; i--)
			swap(cards, i - 1, rnd.nextInt(i));

	}

	private static void swap(Object[] arr, int i, int j) {
		Object tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	public int length(){
		return this.cards.length;
	}

	public void printCardInfo(int i){
		System.out.println(this.cards[i].getName());
	}

	public LeaderCard getCard(int i) {
		return this.cards[i];
	}

	public LeaderCard getCard(String name){
		for(LeaderCard c : this.cards){
			if(c.getName().equals(name)){
				return c;
			}
		}
		return null;
	}

	public ArrayList<ArrayList<LeaderCard>> getStartingLeaderSets(int numberOfPlayers){
		this.shuffleDeck();
		int a=1;
		int i=0;
	
		List<ArrayList<LeaderCard>> box = new ArrayList<ArrayList<LeaderCard>>();
		for(int j = 0; j<numberOfPlayers; j++){
			List<LeaderCard> cards = new ArrayList<LeaderCard>();
			for(; i < 4*a; i++){
				cards.add(this.getCard(i));
			}
			box.add((ArrayList<LeaderCard>) cards);
			a++;
		}
		return (ArrayList<ArrayList<LeaderCard>>) box;
	}

}
