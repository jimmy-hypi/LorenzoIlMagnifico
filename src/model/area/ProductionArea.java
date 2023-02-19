package model.area;

import java.util.List;

import model.Player;
import model.card.CardType;
import model.card.DevelopmentCard;
import model.effect.ProductionBonusEffect;
import model.area.IndustrialArea;
import model.area.MultipleActionSpace;

/**
 * The Class representing the production area of the game board.
 */
public class ProductionArea extends IndustrialArea {
	private static final long serialVersionUID = -3586015416509643408L;

	public ProductionArea(){
		super();
		//The "MALUS" costant is defined in IndustrialArea
		this.multipleSlot = new MultipleActionSpace(SLOT_COST, new ProductionBonusEffect(MALUS));
	}

	@Override
	public List<DevelopmentCard> getPlayerCards(Player player) {
		return player.getDeckOfType(CardType.BUILDING);
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
		return CardType.BUILDING;
	}
}
