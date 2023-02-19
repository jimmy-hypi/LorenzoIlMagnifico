package model.action;

import exception.NotApplicableException;
import model.FamilyMember;
import model.area.SingleActionSpace;
import model.effect.InstantResourcesEffect;
import model.resource.Coin;
import model.resource.MilitaryPoint;
import model.resource.ResourceChest;
import model.resource.Servant;

/**
 * This class represents the placement in a market slot action
 */
public class MarketAction extends Action {
    private SingleActionSpace marketSpot;
    private int paidServants;

    public MarketAction(FamilyMember familyMember, SingleActionSpace marketSpot, int paidServants){
        super(familyMember);
        this.marketSpot = marketSpot;
        this.paidServants = paidServants;
    }
    @Override
    public void apply() throws NotApplicableException {
        if(isApplicable()){
            this.marketSpot.getEffect().applyEffect(familyMember.getPlayer());
            this.marketSpot.setFamilyMember(familyMember);
            familyMember.getPlayer().removeFamilyMember(familyMember.getColor());
            familyMember.getPlayer().getResourceChest().subResource(new Servant(paidServants));
            for(int i = 0; i<familyMember.getPlayer().getBonuses().getResourceMalus().size();i++){
                if(familyMember.getPlayer().getBonuses().getResourceMalus().get(i) instanceof Coin
                        && this.marketSpot.getEffect().equals(new InstantResourcesEffect(new
                        ResourceChest(5,0,0,0,0,0,0)))){
                    familyMember.getPlayer().getResourceChest().subResource(new Coin(1));
                }
                if(familyMember.getPlayer().getBonuses().getResourceMalus().get(i) instanceof Coin
                        && this.marketSpot.getEffect().equals(new InstantResourcesEffect(new
                        ResourceChest(0,0,0,5,0,0,0)))){
                    familyMember.getPlayer().getResourceChest().subResource(new Servant(1));
                }
                if(familyMember.getPlayer().getBonuses().getResourceMalus().get(i) instanceof Coin
                        && this.marketSpot.getEffect().equals(new InstantResourcesEffect(new
                        ResourceChest(2,0,0,0,0,0,3)))){
                    familyMember.getPlayer().getResourceChest().subResource(new Coin(1));
                    familyMember.getPlayer().getResourceChest().subResource(new MilitaryPoint(1));
                }
            }
        } else {
            throw new NotApplicableException("market action non si può fare");
        }
    }

    @Override
    public boolean isApplicable() {
        if(!this.canBePlaced() || player.getBonuses().isNoMarketActionActive()) {
            return false;
        }
        return true;
    }

    private boolean canBePlaced(){
        if(!marketSpot.isOccupable(familyMember) || (familyMember.getActionValue() + paidServants) < this.marketSpot.getActionValueRequired()){
            return false;
        }
        return true;
    }
}
