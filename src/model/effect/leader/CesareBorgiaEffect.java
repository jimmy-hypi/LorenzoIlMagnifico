package model.effect.leader;

import model.Player;
import model.effect.Effect;

/**
 * This class represents the effect of the leader card with the same name
 */
public class CesareBorgiaEffect extends Effect implements Disapplyable {
	private static final long serialVersionUID = -1765669019505493547L;

	@Override
	public void applyEffect(Player p) {
		p.getBonuses().setNoMilitaryPointsRequiredForTerritories(true);
	}

	public void disapplyEffect(Player p){
		p.getBonuses().setNoMilitaryPointsRequiredForTerritories(false);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("You can pick territory cards without having the military points required");
		return builder.toString();
	}
}
