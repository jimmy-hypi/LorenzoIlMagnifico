package model.excommunicationtile;

import model.Player;
import model.effect.Effect;

/**
 * If applied to a player, this effect doesn't allow the user to play when it's his turn
 */
public class SetSkipRoundEffect extends Effect {
	private static final long serialVersionUID = -2678249263612511472L;

	@Override
	public void applyEffect(Player player) {
		player.getBonuses().setSkipRoundActive(true);
	}

	@Override
	public String toString() {
		return "When it's your turn you skip the turn and play after everyone"; 
	}
}
