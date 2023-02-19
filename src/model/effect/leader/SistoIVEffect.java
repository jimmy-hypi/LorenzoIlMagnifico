package model.effect.leader;

import model.Player;
import model.effect.Effect;

/**
 * The Class SistoIVEffect.
 * This class represents the effect of the leader card with the same name
 */
public class SistoIVEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 3980112957279298700L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("At the end of each period, if you support the church, you gain additional"
				+ "5 victory points ");
		return builder.toString();
	}

	@Override
	public void applyEffect(Player p) {
		p.getBonuses().setChurchSupportBonus(5);
	}

	@Override
	public void disapplyEffect(Player p) {}
}
