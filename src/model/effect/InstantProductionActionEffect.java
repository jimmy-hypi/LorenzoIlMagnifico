package model.effect;


import model.Player;
import model.effect.leader.Disapplyable;


/**
 * The Class InstantProductionActionEffect.
 */
public class InstantProductionActionEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 3257137657687177488L;
	int productionActionValue;

	public InstantProductionActionEffect(int productionActionValue){
		this.productionActionValue = productionActionValue;
	}

	@Override
	public void applyEffect(Player p) {
		//TODO
		/*
		for(BuildingCard c : card.getPlayer().getBuildingDeck()){
			if(c.getPermanentEffect() instanceof ProductionEffect && c.canActivateProductionWith(productionActionValue + c.getPlayer().getProductionModification())){
				new ProductionEffect(c.getPermanentEffect()).applyEffect();
			}
		} */
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("You can activate a production with an action value of ");
		builder.append(productionActionValue);
		return builder.toString();
	}

	@Override
	public void disapplyEffect(Player p) {}
}
