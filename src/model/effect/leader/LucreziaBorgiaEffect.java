package model.effect.leader;

import model.FamilyMember;
import model.Player;
import model.effect.Effect;

/**
 * The Class LucreziaBorgiaEffect.
 * This class represents the effect of the leader card with the same name
 */
public class LucreziaBorgiaEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 9063070050082460745L;

	@Override
	public void applyEffect(Player p) {
		for(FamilyMember member : p.getFamilyMembers().values()){
			member.setActionValueVariation(2);
		}
	}

	public void disapplyEffect(Player p){
		for(FamilyMember member : p.getFamilyMembers().values()){
			member.setActionValueVariation(0);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Your familiars, excepts for the neutral one, have a +2 bonus to their"
				+ "action value. You can also increase the value by spending servants or by"
				+ "using character cards effects.");
		return builder.toString();
	}
}
