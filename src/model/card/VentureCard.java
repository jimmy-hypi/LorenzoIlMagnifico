package model.card;

import model.Period;
import model.effect.Effect;
import model.resource.ResourceChest;


/**
 * A card of type Venture
 */
public class VentureCard extends DevelopmentCard {
	private static final long serialVersionUID = -3369173763705272528L;

	public VentureCard(int id, String name, Period period, ResourceChest cost, Effect immediateEffect,
			Effect permanentEffect) {
		super(id, name, period, cost, immediateEffect, permanentEffect);
		this.cardType=CardType.VENTURE;
	}

	public ResourceChest selectCost(ResourceChest cost, ResourceChest alternativeCost, int choice){
		switch(choice){
			case 2: return alternativeCost;
			default: return cost;
		}
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
