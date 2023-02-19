package model.card;

import model.Period;
import model.effect.Effect;
import model.resource.ResourceChest;

/**
 * A card of type Character
 */
public class CharacterCard extends DevelopmentCard {
	//we have to pass some null values to immediate or permanent effect of some of these cards because some of them don't have both
	private static final long serialVersionUID = 8075237121381367847L;

	public CharacterCard(int id, String name, Period period, ResourceChest cost, Effect immediateEffect,
			Effect permanentEffect) {
		super(id, name, period, cost, immediateEffect, permanentEffect);
		this.cardType=CardType.CHARACTER;
	}

	@Override
	public String toString() {
    	StringBuilder string = new StringBuilder();
    	string.append(super.toString() + "\nPermanent effect: ");
    	if(this.permanentEffect!=null)
    		string.append(permanentEffect.toString());
    	
    	return string.toString();
	}
	@Override
	public int getActivationCost() {
		return -1;
	}
}
