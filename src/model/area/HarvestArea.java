package model.area;

import java.util.List;

import model.Player;
import model.card.CardType;
import model.card.DevelopmentCard;
import model.effect.Effect;
import model.effect.HarvestBonusEffect;

/**
 * The Class representing the HarvestArea component of the game.
 */
public class HarvestArea extends IndustrialArea{
	private static final long serialVersionUID = 813593501545744521L;
	Effect personalEffect;

	public HarvestArea(){
		super();
		//The "MALUS" constant is defined in IndustrialArea
		this.multipleSlot = new MultipleActionSpace(SLOT_COST, new HarvestBonusEffect(MALUS));
		
		
	}

	@Override
	public List<DevelopmentCard> getPlayerCards(Player player) {
			
		return player.getDeckOfType(CardType.TERRITORY);
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("\n°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°The Harvest Area°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");
		builder.append(super.toString());
		builder.append("\n°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");

		
		return builder.toString();
		
	}

	@Override
	public CardType getAssociatedCardType() {
		return CardType.TERRITORY;
	}
}
