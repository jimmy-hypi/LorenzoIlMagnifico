package model.effect.leader;

import model.FamilyMember;
import model.Player;
import model.effect.Effect;

/**
 * The Class LudovicoIlMoroEffect.
 * This class represents the effect of the leader card with the same name
 */
public class LudovicoIlMoroEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = -2211372359567972605L;

	@Override
	public void applyEffect(Player p) {
		for(FamilyMember member : p.getFamilyMembers().values()){
			member.setActionValueImposition(5);
		}
	}

	public void disapplyEffect(Player p){
		for(FamilyMember member : p.getFamilyMembers().values()){
			member.setActionValueImposition(0);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Your familiars, excepts for the neutral one, have an action value of 5."
				+ "The related dice value is not relevant. You can also increase the action "
				+ "value by spending servants or using character cards effects");
		return builder.toString();
	}
}
