package model.excommunicationtile;

import java.util.List;

import model.Player;
import model.card.CardType;
import model.card.DevelopmentCard;
import model.effect.Effect;
import model.resource.ResourceChest;
import model.resource.VictoryPoint;

/**
 * Lose victoryPoints points for every wood or stone pictured in the costs of the cards you have
 * of the specified CardType type.
 */
public class LosePointsEveryWoodStoneEffect extends Effect {
	private static final long serialVersionUID = 5082995786213243247L;
	private VictoryPoint victoryPoint;
	private CardType cardType;
	public LosePointsEveryWoodStoneEffect(VictoryPoint victoryPoint,CardType cardType) {
		this.victoryPoint = victoryPoint;
		this.cardType=cardType;
	}

	@Override
	public void applyEffect(Player player) {
		int sum=0;
		List<DevelopmentCard> deck;
		deck=player.getDeckOfType(cardType);
		for(DevelopmentCard card:deck){
			sum+=card.getCost().getStoneAmount();
			sum+=card.getCost().getWoodAmount();
		}
		this.victoryPoint.setAmount(victoryPoint.getAmount()*sum);
		ResourceChest rc=new ResourceChest();
		rc.addResource(victoryPoint);
		player.subResources(rc);
	}

	@Override
	public String toString() {
		return "You Lose "+ victoryPoint.toString() + 
				" for every wood or stone pictured in the costs of the "+
				cardType.toString().toLowerCase()+" you own";
	}
}
