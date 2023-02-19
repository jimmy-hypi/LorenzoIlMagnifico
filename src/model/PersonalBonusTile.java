package model;

import model.effect.InstantResourcesEffect;

import java.io.Serializable;

/**
 * PersonalBonusTile component of the game
 */
public class PersonalBonusTile implements Serializable {
    private static final long serialVersionUID = 5558558088929570115L;
    InstantResourcesEffect firstInstantResourceChest;
    InstantResourcesEffect secondInstantResourceChest;

    public PersonalBonusTile(InstantResourcesEffect firstEffect, InstantResourcesEffect secondEffect){
        this.firstInstantResourceChest = firstEffect;
        this.secondInstantResourceChest = secondEffect;
    }
    @Override
    public String toString() {
        return firstInstantResourceChest.toString() + " everytime you activate a production and "
                + secondInstantResourceChest.toString() + " everytime you a activate a harvest" ;
    }
}
