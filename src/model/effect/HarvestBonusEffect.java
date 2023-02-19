package model.effect;

import model.Player;

/**
 * The Class HarvestBonusEffect.
 */
public class HarvestBonusEffect extends Effect {
	private static final long serialVersionUID = -2823865542154838135L;
	int value;

	public HarvestBonusEffect(int value){
	this.value = value;	
	}

	@Override
	public void applyEffect(Player p) {
		p.getBonuses().setHarvestVariation(value);
	}

	@Override
	public String toString() {
		if(value >= 0)
			return "You gain a + " + value + " to your harvest value";
		else
			return "You gain a " + value + " to your harvest value";
	}
}
