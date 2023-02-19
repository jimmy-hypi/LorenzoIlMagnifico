package model.effect.leader;

import model.Player;
import model.effect.Effect;

/**
 * The Class LorenzoDeMediciEffect.
 * This class represents the effect of the leader card with the same name
 */
public class LorenzoDeMediciEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 6786349977460940485L;

	@Override
	public void applyEffect(Player p) {}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Copy the ability of a leader card played by any other player."
				+ "Once you choose one ability, it can't be changed");
		return builder.toString();
	}

	@Override
	public void disapplyEffect(Player p) {}
}
