package model.effect;

import model.Player;
import model.effect.leader.Disapplyable;

/**
 * This class implements the harvest effect that gives only resources,
 * hence a resource chest. The harvest effect is the same as an instantResourcesEffect
 * except for the fact that it has a condition that triggers it.
 */
public class HarvestEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 1643184690128815176L;
	Effect instantEffect;

	public HarvestEffect(Effect instantEffect){
		this.instantEffect=instantEffect;
	
	}

	public void applyEffect(Player p) {
		instantEffect.applyEffect(p);
	}

	@Override
	public String toString() {
		return instantEffect.toString();
	}

	@Override
	public void disapplyEffect(Player p) {}
}
