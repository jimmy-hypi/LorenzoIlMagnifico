package model.area;

import model.FamilyMember;
import model.effect.Effect;

import java.io.Serializable;

/**
 * This class represents the space that can receive one or more family member.
 */
public abstract class ActionSpace implements Occupable, Serializable {
    private static final long serialVersionUID = -3896782904317576765L;
    protected int actionValueRequired;
    protected Effect effect;

    public abstract void setFamilyMember(FamilyMember familyMember);

    protected ActionSpace(int actionValueRequired, Effect effect){
        this.actionValueRequired = actionValueRequired;
        this.effect = effect;
    }

    public Effect getEffect(){
        return effect;
    }

    public int getActionValueRequired(){
        return actionValueRequired;
    }
}
