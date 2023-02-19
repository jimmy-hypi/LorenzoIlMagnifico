package model.card;

import java.io.Serializable;

import model.LeaderCardRequirement;
import model.effect.leader.Disapplyable;

/**
 * The Class representing leader cards of the game.
 */
public class LeaderCard extends Card implements Serializable{
	private static final long serialVersionUID = -678840188923192984L;
	private LeaderCardRequirement requirement;
	private Disapplyable specialEffect;
	private boolean activationState;

	public LeaderCard(String name, LeaderCardRequirement requirement, Disapplyable specialEffect){
		super(name);
		this.requirement = requirement;
		this.specialEffect = specialEffect;
	}

	public LeaderCardRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(LeaderCardRequirement requirement) {
		this.requirement = requirement;
	}

	public Disapplyable getSpecialEffect() {
		return specialEffect;
	}

	public void setSpecialEffect(Disapplyable specialEffect) {
		this.specialEffect = specialEffect;
	}
	
	public boolean isActivationState() {
		return activationState;
	}

	public void setActivationState(boolean activationState) {
		this.activationState = activationState;
	}

	@Override
	public String toString() {
		return "[name=" + super.getName()
		+ " \nRequirements=" + requirement.toString() + " \nSpecialEffect=" + specialEffect.toString() + "]";
	}
}
