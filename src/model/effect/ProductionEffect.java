package model.effect;

import exception.NotAnExchangeEffectException;
import model.Player;
import model.effect.leader.Disapplyable;

/**
 * This class represents the production effect of a building card, it differs from other effects because its actual effect
 * is applied only when a production on the card is activated.
 */
public class ProductionEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = -4899398635817616168L;
	/**
	 * actualEffect is the the effect to be applied when the production on the card is activated.
	 * actualEffect at runtime will have only three possible dynamic types: ResourceExchangeEffect, InstantResourceEffect
	 * or ForEachCardTypeEffect
	 */
	private Effect actualEffect;

	public ProductionEffect(Effect actualEffect) {
		super();
		this.actualEffect = actualEffect;
	}

	@Override
	public void applyEffect(Player p) {
		actualEffect.applyEffect(p);
	}

	@Override
	public String toString() {
		return actualEffect.toString();
	}

	public boolean isResourcesExchangeEffect(){
		if(this.actualEffect instanceof ResourcesExchangeEffect)
			return true;
		else
			return false;
	}

	public ResourcesExchangeEffect getResourcesExchangeEffect(){
		if(this.actualEffect instanceof ResourcesExchangeEffect)
			return (ResourcesExchangeEffect)this.actualEffect;			
		else throw new NotAnExchangeEffectException();
	}

	@Override
	public void disapplyEffect(Player p) {}
}

