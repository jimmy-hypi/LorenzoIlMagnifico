package model.excommunicationtile;

import model.Player;
import model.card.CardType;
import model.effect.Effect;

/**
 * Excommunication effect: you don't get the final victory points from the "cardType"
 * Card Type
 */
public class SetNoCardTypeFinalPointsEffect extends Effect {
	private static final long serialVersionUID = 3114860811278517273L;
	private CardType cardType;
	public SetNoCardTypeFinalPointsEffect(CardType cardType) {
		this.cardType = cardType;
	}

	@Override
	public void applyEffect(Player player) {
		player.getBonuses().setNoCardTypeFinalPoints(cardType);
	}

	@Override
	public String toString() {
		return "You don't get the final points relative to the "+cardType.toString().toLowerCase()+" cards";
	}
}
