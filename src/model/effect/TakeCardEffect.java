package model.effect;

import model.Player;
import model.card.CardType;
import model.resource.ResourceChest;

/**
 * The Class TakeCardEffect.
 */
public class TakeCardEffect extends Effect{
	private static final long serialVersionUID = -7178424807692191463L;
	private CardType cardType;
	private int cardValue;
	private ResourceChest discountChest;
	
	
	/**
	 * Instantiates a new take card effect.
	 *
	 * @param card the card
	 * @param value the value
	 * @param discountChest this parameter can be an empty ResourceChest if there is no discount
	 */
	public TakeCardEffect(CardType card, int value, ResourceChest discountChest){
		cardType = card;
		cardValue = value;
		this.discountChest = discountChest;
	}

	@Override
	public void applyEffect(Player p) {}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Take a ");
		builder.append(cardType.toString().toLowerCase());
		builder.append(" card, from an action space with an action value of ");
		builder.append(cardValue);
		builder.append(" and you receive the following discount:" + discountChest.toString());
		return builder.toString();
	}
}
