package model.excommunicationtile;

import model.Player;
import model.effect.Effect;

/**
 * If activated, doesn't let the player position a family member into the market anymore
 */
public class SetNoMarketActionEffect extends Effect {
	private static final long serialVersionUID = -6954023005307137525L;
	@Override
	public void applyEffect(Player player) {
		player.getBonuses().setNoMarketActionActive(true);
	}
	@Override
	public String toString() {
		return "You can't place family members in the market anymore";
	}
}
