package model.excommunicationtile;

import model.Player;
import model.effect.Effect;
import model.resource.Resource;
import model.resource.VictoryPoint;

/**
 * You lose "victoryPoints" points for every "resource"
 *  you have at the end of the game.
 */
public class LosePointsBasedOnResourcesEffect extends Effect {
	private static final long serialVersionUID = 7541480330001366752L;
	private Resource resource;
	private VictoryPoint victoryPoints;

	public LosePointsBasedOnResourcesEffect(Resource resource, VictoryPoint victoryPoints) {
		this.resource = resource;
		this.victoryPoints = victoryPoints;
	}

	@Override
	public void applyEffect(Player player) {
		int multiplier;
		int amount;
		amount=player.getResourceChest().getResourceInChest(resource).getAmount();
		multiplier=amount/resource.getAmount();
		victoryPoints.setAmount(victoryPoints.getAmount()*multiplier);
		player.getResourceChest().subResource(victoryPoints);
	}

	@Override
	public String toString() {
		return "You lose "+ victoryPoints.toString()+ " for every "
				+ resource.toString()+" you have at the end of the game";
	}
}
