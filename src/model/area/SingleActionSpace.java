package model.area;

import java.util.ArrayList;
import java.util.HashSet;

import model.FamilyMember;
import model.Player;
import model.effect.Effect;

/**
 * This class is the generic single action space that you can find in floors, markets and in small
 * production and harvest area. This action space can store only one familiar per time.
 */
public class SingleActionSpace extends ActionSpace{
	private static final long serialVersionUID = 9069295031617624276L;
	private FamilyMember familyMember;

	public SingleActionSpace(int actionValueRequired, Effect effect) {
		super(actionValueRequired, effect);
	}

	@Override
	public boolean isOccupied() {
		return familyMember!=null;
	}

	@Override
	public ArrayList<FamilyMember> occupiedByMember() {
		ArrayList<FamilyMember> forInterface = new ArrayList<FamilyMember>();
		forInterface.add(familyMember);
		return forInterface;
	}

	@Override
	public HashSet<Player> occupiedByPlayer() {
		HashSet<Player> forInterface = new HashSet<Player>();
		forInterface.add(familyMember.getPlayer());
		return forInterface;
	}

	public FamilyMember getFamilyMember() {
		return familyMember;
	}

	public void setFamilyMember(FamilyMember familyMember) {
			this.familyMember = familyMember;
	}

	public boolean isOccupable(FamilyMember familyMember, int paidServants, int industrialValueVariation) {
		return !isOccupied() && familyMember.getActionValue() + paidServants + industrialValueVariation>= this.actionValueRequired;
	}

	@Override
	public boolean isOccupable(FamilyMember familyMember) {
		return !isOccupied();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(familyMember != null){
			builder.append("The area is occupied by the ");
			builder.append(familyMember.toString());
			builder.append(" family member of the ");
			builder.append(familyMember.getPlayer().getColor());
			builder.append(" player\n");
		}
		return builder.toString();
	}
}