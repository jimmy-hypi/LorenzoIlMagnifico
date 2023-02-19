package model.card;

import model.Period;
import model.effect.Effect;
import model.resource.ResourceChest;

/**
 * A card of type Territory
 */
public class TerritoryCard extends DevelopmentCard {
	private static final long serialVersionUID = 1065227820762540803L;
	private int harvestActivationCost;

	public TerritoryCard(int id, String name, Period period, Effect immediateEffect,
		Effect permanentEffect,int harvestActivationCost) {
		super(id, name, period, new ResourceChest(), immediateEffect, permanentEffect);
		this.cardType=CardType.TERRITORY;
		this.harvestActivationCost=harvestActivationCost;
	}

    /**
     * Can activate harvest with a certain production value.
     *
     * @param harvestValue the harvest value
     * @return true, if the harvest effect of this card can be activated with the given production Value
     */
    public boolean canActivateHarvestWith(int harvestValue){
    	return harvestValue>harvestActivationCost;
		
	}

    @Override
    public String toString() {
    	StringBuilder string = new StringBuilder();
    	string.append(super.toString() + "\nHarvest cost: " + harvestActivationCost + "\nHarvest effect: ");
    	if(this.permanentEffect!=null)
    		string.append(permanentEffect.toString());
    	string.append("\n\n");
    	return string.toString();
    }
	@Override
	public int getActivationCost() {
		return this.harvestActivationCost;
	}
}
