package model.area;

import java.util.ArrayList;
import java.util.HashSet;

import model.FamilyMember;
import model.Player;

/**
 * The Interface Occupable exposes some methods to every object of the game that can be occupied
 * by a Family Member
 */
public interface Occupable {
	public boolean isOccupied();
	
	/**
	 * Checks if is occupable.
	 *
	 * @param familyMember the family member
	 * @return true if it's occupable by the familyMember passed. N.B (we're not considering here
	 * the fact that more family members of the same players can't stay in the same tower, it can't be done
	 * at this level)
	 */
	public boolean isOccupable(FamilyMember familyMember);

	public ArrayList<FamilyMember> occupiedByMember();

	public HashSet<Player> occupiedByPlayer();
}