package model.excommunicationtile;

import model.Player;
import model.effect.Effect;

/**
 * This effect when applied only sets a value for the servantsDivider field in bonus of a player.
 */
public class SetServantsDividerEffect extends Effect {
	private static final long serialVersionUID = -6964246372125181142L;
	private int divider;
	
	/**
	 * Instantiates a new sets the servants divider effect.
	 *
	 * @param divider the divider
	 */
	public SetServantsDividerEffect(int divider) {
		this.divider = divider;
	}

	@Override
	public void applyEffect(Player player) {
		player.getBonuses().setServantsDivider(divider);
	}

	@Override
	public String toString() {
		return "You have to spend 2 servants to raise the value of your action of 1";
	}
}
