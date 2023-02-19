package model.effect;

import model.Player;
import model.card.CardType;

/**
 * The Class RaiseValueWithDiscountEffect.
 */
public class RaiseValueWithDiscountEffect extends Effect{
	private static final long serialVersionUID = 6683191125876080358L;
	private int raiseAmount;
	private CardType cardType;
	private boolean buildingCardsBonus;
	private boolean characterCardsBonus;
	
	
	/**
	 * Instantiates a new raise value with discount effect.
	 *
	 * @param raiseAmount the raise amount
	 * @param cardType the card type
	 * @param buildingCardsBonus the building cards bonus
	 * @param characterCardsBonus the character cards bonus
	 */
	public RaiseValueWithDiscountEffect(int raiseAmount, CardType cardType, boolean buildingCardsBonus, boolean characterCardsBonus){
		this.raiseAmount = raiseAmount;
		this.cardType = cardType;
		this.buildingCardsBonus = buildingCardsBonus;
		this.characterCardsBonus = characterCardsBonus;
	}

	public void applyEffect(Player p) {
		p.getBonuses().addCardTypeActionVariation(cardType, raiseAmount);

		if(buildingCardsBonus == true)
			p.getBonuses().setBuildingCardsDiscount(true);
		else if(characterCardsBonus == true)
			p.getBonuses().setCharacterCardsDiscount(true);
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		if(raiseAmount >= 0)
			string.append("You get a + " + raiseAmount + " value for a " + cardType.toString().toLowerCase() + " card");
		else
			string.append("You get a " + raiseAmount + " value for a " + cardType.toString().toLowerCase() + " card");
		
		if(buildingCardsBonus == true)
			string.append(" and you receive one wood and one stone discount on its cost");
		else if(characterCardsBonus == true)
			string.append(" and you receive one coin discount on its costs");

		return string.toString();
	}
}
