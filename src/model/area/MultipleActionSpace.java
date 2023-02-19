package model.area;

import java.util.ArrayList;
import java.util.HashSet;

import model.Color;
import model.FamilyMember;
import model.Player;
import model.effect.Effect;

/**
 *  This class is the generic multiple action space, the one that characterizes the council palace
 *  and the big production and harvest areas. This class is different from the single action space
 *  because it can store more than one familiar per time.
 */
public class MultipleActionSpace extends ActionSpace {
	private static final long serialVersionUID = -3421639191735530503L;
	protected ArrayList<FamilyMember> members;

	public MultipleActionSpace(int actionValueRequired, Effect effect) {
		super(actionValueRequired, effect);
		members = new ArrayList<FamilyMember>();
	}

	@Override
	public boolean isOccupied() {
		return members.size() > 0;
	}

	private boolean checkAvailability(FamilyMember familyMember){
		
		boolean available = true;
		
		for(FamilyMember member : members){
			if(member.getPlayer() == familyMember.getPlayer() && familyMember.getDice().getColor() != Color.NEUTRAL && member.getColor()!=Color.NEUTRAL){
				available = false;
				break;
			}
		}
		return available || familyMember.getDice().getColor() == Color.NEUTRAL;
	}

	public boolean isOccupable(FamilyMember familyMember, int paidServants, int industrialActionVariation) {
		return familyMember.getActionValue() + paidServants + industrialActionVariation> this.actionValueRequired && checkAvailability(familyMember);
	}

	public ArrayList<FamilyMember> occupiedByMember() {	
		return members;
	}

	public HashSet<Player> occupiedByPlayer() {
		HashSet<Player> forInterface = new HashSet<Player>();
		
		for(FamilyMember member : members){
			forInterface.add(member.getPlayer());
		}
		
		return forInterface;
	}

	@Override
	public void setFamilyMember(FamilyMember familyMember) {
			this.members.add(familyMember);
	}

	public ArrayList<FamilyMember> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<FamilyMember> members) {
		this.members = members;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Members here:\n");
		for(FamilyMember f : members){
			builder.append(f.toString());
			builder.append("familiar of the ");
			builder.append(f.getPlayer().getColor());
			builder.append(" player\n");
		}
		return builder.toString();
	}

	@Override
	public boolean isOccupable(FamilyMember familyMember) {
		return false;
	}
}
