package model.card;

import model.Period;
import model.effect.Effect;
import model.resource.ResourceChest;
import model.resource.VentureCostResourceChest;

/**
 * This class represents the abstract class of a generic Development Card.
 */
public abstract class DevelopmentCard extends Card {
	private static final long serialVersionUID = 2067083051151226893L;
	protected int id;
	protected Period period;
	protected ResourceChest cost;  //Territory cards are the only one without cost, they're going to have the attribute set to null
	protected Effect immediateEffect;
	protected Effect permanentEffect;
	protected CardType cardType;

	public DevelopmentCard(int id,String name,Period period,ResourceChest cost,Effect immediateEffect,Effect permanentEffect){
		super(name);
		this.id=id;
		this.period=period;
		this.cost=cost;
		this.immediateEffect=immediateEffect;
		this.permanentEffect=permanentEffect;
	}

	public  ResourceChest getCost() {
		return cost;
	}
	
	public VentureCostResourceChest getVentureCost(){
		return (VentureCostResourceChest) cost;
	}

	public void setCost(ResourceChest cost) {
		this.cost = cost;
	}

	public int getId() {
		return id;
	}

	public Period getPeriod() {
		return period;
	}

	public Effect getImmediateEffect() {
		return immediateEffect;
	}

	public Effect getPermanentEffect() {
		return permanentEffect;
	}

	public CardType getCardType() {
		return cardType;
	}

 	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();

		string.append("Name: " + name + "\nPeriod: " + period + "\nCost: ");

		if(this.cost!=null)
			string.append(cost.toString());
		string.append("\nImmediate effect: ");
		if(this.immediateEffect!=null)
			string.append(immediateEffect.toString());
		return string.toString();
	}

 	public abstract int getActivationCost();
}
