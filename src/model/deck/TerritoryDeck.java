package model.deck;

import model.card.TerritoryCard;

/**
 * A deck of territory cards
 */
public class TerritoryDeck extends Deck<TerritoryCard> {
	private static final long serialVersionUID = -5081732306033008553L;

	public TerritoryDeck(String filePath, int deckLength) {
		try {
			cards = DeckCreator.createTerritoryCardDeck(filePath, deckLength);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("No territory cards without an harvest effect allowed");
		}
	}

	@Override
	public int length() {
		return this.cards.length;
	}
}
