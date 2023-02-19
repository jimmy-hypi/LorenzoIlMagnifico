package model.deck;

import java.io.IOException;

import model.card.BuildingCard;

/**
 * A deck of building cards
 */
public class BuildingDeck extends Deck<BuildingCard> {
	private static final long serialVersionUID = 626392870621207325L;

	public BuildingDeck(String filePath, int deckLength) throws IOException {
		cards=DeckCreator.createBuildingCardDeck(filePath, deckLength);
	}

	@Override
	public int length() {
		return this.cards.length;
	}

	public void printCardInfo(int i){
		System.out.println(this.cards[i].getId());
	}
}
