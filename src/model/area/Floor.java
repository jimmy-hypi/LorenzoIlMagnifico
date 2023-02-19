package model.area;


import java.io.Serializable;

import model.card.DevelopmentCard;
import model.effect.InstantResourcesEffect;

/**
 * This class represents the floors in the towers, the related action spaces have specific bonuses
 * taken from file and here you can place the familiar
 */
public class Floor implements Serializable {
	private static final long serialVersionUID = 5822825459286872128L;
	DevelopmentCard card;
	SingleActionSpace actionSpace;
	Tower tower;

	public Floor(DevelopmentCard card, Tower tower, int actionSpaceCost, InstantResourcesEffect instantResourcesEffect){
		this.card = card;
		this.tower = tower;
		this.actionSpace = new SingleActionSpace(actionSpaceCost,instantResourcesEffect);
	}

	public DevelopmentCard getCard() {
		return card;
	}

	public void setCard(DevelopmentCard card) {
		this.card = card;
	}

	public SingleActionSpace getActionSpace() {
		return actionSpace;
	}

	public Tower getTower() {
		return tower;
	}
}
