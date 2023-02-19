package model.effect;


import model.Player;
import model.effect.leader.Disapplyable;

/**
 * The Class InstantHarvestActionEffect.
 */
public class InstantHarvestActionEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 7562317170470979450L;
	private int harvestActionValue;

	public InstantHarvestActionEffect(int harvestActionValue) {
		this.harvestActionValue = harvestActionValue;
	}

	@Override
	public void applyEffect(Player p) {
		//TODO
		/*
		for(TerritoryCard c : card.getPlayer().getTerritoryDeck()){
			if(c.getPermanentEffect() instanceof HarvestEffect && c.canActivateHarvestWith(harvestActionValue + c.getPlayer().getHarvestModification())){
				new ProductionEffect(c.getPermanentEffect()).applyEffect();
			}
		}
		*/
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("You can activate a harvest with an action value of ");
		builder.append(harvestActionValue);
		return builder.toString();
	}

	@Override
	public void disapplyEffect(Player p) {}
}
