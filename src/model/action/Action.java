package model.action;

import exception.NotApplicableException;
import model.FamilyMember;
import model.Player;

/**
 * Abstract class to generalize an action of the game
 */
public abstract class Action {
    protected Player player;
    protected FamilyMember familyMember;

    public Action(FamilyMember familyMember) {
        if(familyMember!=null)
            this.player = familyMember.getPlayer();
        this.familyMember = familyMember;
    }

    public abstract void apply() throws NotApplicableException;

    public abstract boolean isApplicable();

    public Player getPlayer() {
        return this.player;
    }
}
