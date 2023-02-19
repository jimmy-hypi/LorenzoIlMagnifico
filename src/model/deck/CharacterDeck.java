package model.deck;

import java.io.IOException;

import model.card.CharacterCard;

/**
 * A deck of character cards
 */
public class CharacterDeck extends Deck<CharacterCard> {
	private static final long serialVersionUID = 6919342650647211328L;

	public CharacterDeck(String filePath, int deckLength) throws IOException {
		cards=DeckCreator.createCharacterCardDeck(filePath, deckLength);
	}

	@Override
	public int length() {
		return this.cards.length;
	}

	public void printCardInfo(int i){
		System.out.println(this.cards[i].getId());
	}
}
