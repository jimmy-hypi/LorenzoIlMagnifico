package model.card;

import model.Period;
import model.effect.Effect;
import model.effect.ProductionEffect;
import model.effect.ResourcesExchangeEffect;
import model.resource.ResourceChest;

/**
 * A card of type Building
 */
public class BuildingCard extends DevelopmentCard {
	private static final long serialVersionUID = -6226375170446454135L;
	private int productionActivationCost;

	/**
	 * Instantiates a new building card.
	 *
	 * @param id the id
	 * @param name the name
	 * @param period the period
	 * @param cost the cost
	 * @param immediateEffect the immediate effect
	 * @param permanentEffect the permanent effect
	 * @param productionActivationCost the production activation cost
	 */
	public BuildingCard(int id, String name, Period period, ResourceChest cost, Effect immediateEffect,
			Effect permanentEffect, int productionActivationCost) {

		super(id, name, period, cost, immediateEffect, permanentEffect);
		this.productionActivationCost = productionActivationCost;
		this.cardType=CardType.BUILDING;
	}

	/**
	 * Can activate production with a certain production value.
	 *
	 * @param productionValue the production value
	 * @return true, if the production effect of this card can be activated with the given production Value
	 */
	public boolean canActivateProductionWith(int productionValue){
    	return productionValue>productionActivationCost;
	}

	@Override
	public String toString() {	
    	StringBuilder string = new StringBuilder();
    	string.append(super.toString() + "\nProduction cost: " + productionActivationCost + "\nProduction effect: ");
    	if(this.permanentEffect!=null)
    		string.append(permanentEffect.toString());
    	string.append("\n");
    	
    	return string.toString();
	}

	@Override
	public int getActivationCost() {
		return this.productionActivationCost;
	}

	public boolean hasProductionChoice(){
		ProductionEffect productionEffect=
		(ProductionEffect)this.getPermanentEffect();
		if(productionEffect.isResourcesExchangeEffect()){
			if(productionEffect.getResourcesExchangeEffect().hasAlternativeEffect())
				return true;
		}
		return false;
	}

	public String[] getProductionChoice(){
		String[] choices = null;
		
		ProductionEffect productionEffect=
		(ProductionEffect)this.getPermanentEffect();
		if(productionEffect.isResourcesExchangeEffect()){
			ResourcesExchangeEffect resourcesExchangeEffect=productionEffect.getResourcesExchangeEffect();
			choices=new String[2];
			choices[0]=resourcesExchangeEffect.getNormalEffectToString();
			choices[1]=resourcesExchangeEffect.getAlternativeEffectToString();
		}
		return choices;
	}
	
	
}
