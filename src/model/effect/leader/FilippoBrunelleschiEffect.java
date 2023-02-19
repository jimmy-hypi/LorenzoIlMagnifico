package model.effect.leader;

import model.Player;
import model.effect.Effect;

/**
 * The Class FilippoBrunelleschiEffect.
 * This class represents the effect of the leader card with the same name
 */
public class FilippoBrunelleschiEffect extends Effect implements Disapplyable {
	private static final long serialVersionUID = 770128159487460222L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("You can place one familiar in a tower already occupied without paying 3 coins");
		return builder.toString();
	}

	@Override
	public void applyEffect(Player p) {
		p.getBonuses().setDiscountOccupiedTower(true);
	}

	public void disapplyEffect(Player p){
		p.getBonuses().setDiscountOccupiedTower(false);
	}
}
