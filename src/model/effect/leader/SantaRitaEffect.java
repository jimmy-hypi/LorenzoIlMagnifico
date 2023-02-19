package model.effect.leader;

import model.Player;
import model.effect.Effect;

/**
 * The Class SantaRitaEffect.
 * This class represents the effect of the leader card with the same name
 *
 * @author matteo
 */
public class SantaRitaEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 3175571380689500083L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Everytime you gain coins, woods, stones or servants from a development card"
				+ "effect, it is duplicate");
		return builder.toString();
	}
	@Override
	public void applyEffect(Player p) {
		p.getBonuses().setDoubleResourcesFromCards(true);
	}
	public void disapplyEffect(Player p){
		p.getBonuses().setDoubleResourcesFromCards(false);
	}
}
