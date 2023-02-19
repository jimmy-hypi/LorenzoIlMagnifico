package model.action;

import exception.NotApplicableException;
import model.FamilyMember;
import model.area.CouncilPalace;
import model.resource.Coin;
import model.resource.Servant;

/**
 * Action related to family member placement in the council palace
 */
public class CouncilPalaceAction extends Action {
    private CouncilPalace councilPalace;
    int paidServants;

    public CouncilPalaceAction(FamilyMember familyMember, CouncilPalace councilPalace, int paidServants){
        super(familyMember);
        this.councilPalace = councilPalace;
        this.paidServants = paidServants;
    }
    @Override
    public void apply() throws NotApplicableException {
        if(isApplicable()){
            councilPalace.getMembers().add(familyMember);
            councilPalace.getEffect().applyEffect(familyMember.getPlayer());
            familyMember.getPlayer().removeFamilyMember(familyMember.getColor());
            familyMember.getPlayer().getResourceChest().subResource(new Servant(paidServants));
            for(int i = 0; i<familyMember.getPlayer().getBonuses().getResourceMalus().size();i++){
                if(familyMember.getPlayer().getBonuses().getResourceMalus().get(i) instanceof Coin){
                    familyMember.getPlayer().getResourceChest().subResource(new Coin(1));
                }
            }
        }
    }

    @Override
    public boolean isApplicable() {
        if((familyMember.getActionValue() + paidServants) < this.councilPalace.getActionValueRequired()){
            return false;
        }
        return true;
    }
}
