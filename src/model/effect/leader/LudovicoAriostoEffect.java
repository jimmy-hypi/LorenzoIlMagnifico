package model.effect.leader;

import model.Player;
import model.effect.Effect;

/**
 * The Class LudovicoAriostoEffect.
 * This class represents the effect of the leader card with the same name
 */
public class LudovicoAriostoEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = -2968684924820150729L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("You can place your familiars in action spaces already taken.");
		return builder.toString();
	}

	@Override
	public void applyEffect(Player p) {}

	@Override
	public void disapplyEffect(Player p) {}
}
