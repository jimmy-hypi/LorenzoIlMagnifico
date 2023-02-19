package model.deck;

import java.io.IOException;

import model.card.VentureCard;

/**
 * A deck of venture cards
 */
public class VentureDeck extends Deck<VentureCard> {

	private static final long serialVersionUID = -2801964840693027773L;

	public VentureDeck(String filePath, int deckLength) throws IOException {
		cards=DeckCreator.createVentureCardDeck(filePath, deckLength);
	}

	@Override
	public int length() {
		return this.cards.length;
	}

	public void printCardInfo(int i){
		System.out.println(this.cards[i].getId());
	}
}
