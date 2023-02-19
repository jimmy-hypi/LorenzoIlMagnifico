package model.effect;

import model.Player;

/**
 * The Class NoFloorBonusEffect if applied to a player, the player doesn't get Tower's
 * floors bonuses when he takes a card
 */
public class NoFloorBonusEffect extends Effect {
	private static final long serialVersionUID = 6422690715195253118L;

	@Override
	public void applyEffect(Player p) {
		p.getBonuses().setNoFloorBonus(true);
	}

	@Override
	public String toString() {
		return "You can't take any bonus from a tower floor";
	}
}
