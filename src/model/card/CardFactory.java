package model.card;

import exception.CardTypeException;
import model.Period;
import model.effect.Effect;
import model.resource.ResourceChest;

/**
 * A factory to create development Cards based on the Type
 */
public class CardFactory {              
	private static final int TERRITORY=1;
	private static final int BUILDING=2;
	private static final int CHARACTER=3;
	private static final int VENTURE=4;

	public static DevelopmentCard getCard(int code, int id, String name, Period period, ResourceChest cost, Effect immediateEffect, Effect permanentEffect, int harvestActivationCost, int productionActivationCost){
		switch(code){
			case TERRITORY:
				return new TerritoryCard(id, name, period, immediateEffect, permanentEffect, harvestActivationCost);
			case BUILDING:
				return new BuildingCard(id, name, period, cost, immediateEffect, permanentEffect, productionActivationCost);
			case CHARACTER:
				return new CharacterCard(id, name, period, cost, immediateEffect, permanentEffect);
			case VENTURE:
				return new VentureCard(id, name, period, cost, immediateEffect, permanentEffect);
			case 0:
				return null;
				default:
					throw new CardTypeException();
		}
	}
}
