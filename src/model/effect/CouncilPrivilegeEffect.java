package model.effect;

import model.Player;
import model.effect.leader.Disapplyable;

/**
 * The Class CouncilPrivilegeEffect.
 */
public class CouncilPrivilegeEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = 2209211330966096453L;
	int privilegeAmount;

	public CouncilPrivilegeEffect(int privilegeAmount) {
		this.privilegeAmount = privilegeAmount;
	}

	public void applyEffect(Player p){
		p.setCouncilPrivilege(privilegeAmount);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("You can take " + privilegeAmount + " privileges");
		return builder.toString();
	}

	@Override
	public void disapplyEffect(Player p) {
	}
}
