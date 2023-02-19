package model.action;

import exception.NotApplicableException;
import model.FamilyMember;
import model.area.ActionSpace;
import model.area.IndustrialArea;
import model.area.MultipleActionSpace;
import model.area.SingleActionSpace;
import model.card.BuildingCard;
import model.card.DevelopmentCard;
import model.effect.ProductionEffect;
import model.effect.ResourcesExchangeEffect;
import model.resource.Servant;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the Industrial action
 */
public class IndustrialAction extends Action {
    IndustrialArea industrialArea;
    ArrayList<? extends DevelopmentCard> playerCards;
    ActionSpace actionSpace;
    int paidServants;

    public IndustrialAction(FamilyMember familyMember, IndustrialArea industrialArea, ActionSpace actionSpace, int paidServants){
        super(familyMember);
        this.industrialArea = industrialArea;
        this.actionSpace = actionSpace;
        this.paidServants = paidServants;
    }

    @Override
    public void apply() throws NotApplicableException {
        if (actionSpace.getEffect() != null) {
            actionSpace.getEffect().applyEffect(player);
        }
        if (canBePlaced()) {
            actionSpace.setFamilyMember(familyMember);
            this.player.removeFamilyMember(familyMember.getColor());
            this.player.getResourceChest().subResource(new Servant(paidServants));
            for (DevelopmentCard card : industrialArea.getPlayerCards(this.player)) {
            }
            for (DevelopmentCard card : industrialArea.getPlayerCards(this.player)) {
                if (isApplicable(card)) {
                    card.getPermanentEffect().applyEffect(this.player);
                } else
                    System.out.println("CannotActivate card effect");
            }
        } else {
            throw new NotApplicableException("You cannot put your member here!");
        }
    }

    public void apply(List<Integer> choices) throws NotApplicableException {
        if (canBePlaced()) {
            int index = 0;
            actionSpace.setFamilyMember(familyMember);
            this.player.removeFamilyMember(familyMember.getColor());
            this.player.getResourceChest().subResource(new Servant(paidServants));
            if (actionSpace.getEffect() != null) {
                actionSpace.getEffect().applyEffect(player);
            }
            for (DevelopmentCard card : industrialArea.getPlayerCards(this.player)) {
                if (isApplicable(card)) {
                    if ((card instanceof BuildingCard) && ((BuildingCard) card).hasProductionChoice()) { // Se

                        if (card.getPermanentEffect() instanceof ResourcesExchangeEffect)
                            System.out.println("permanenteffect è un resourceexchangeeffect");

                        try {
                            ProductionEffect p = (ProductionEffect) (card.getPermanentEffect());
                            p.getResourcesExchangeEffect().applyEffect(choices.get(index), player);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        card.getPermanentEffect().applyEffect(this.player);
                    }
                } else
                    System.out.println("CannotActivate card effect");
            }
        } else {
            throw new NotApplicableException("You cannot put your member here!");
        }
    }
    @Override
    public boolean isApplicable() {
        return false;
    }

    public boolean isApplicable(DevelopmentCard card) {
        return (this.familyMember.getActionValue()
                + this.getPlayer().getBonuses().getActivationVariation(card.getCardType())
                + paidServants >= card.getActivationCost());
    }

    private boolean canBePlaced(){
        if(actionSpace instanceof MultipleActionSpace){
            return ((MultipleActionSpace) actionSpace).isOccupable(familyMember, paidServants, player.getBonuses().getActivationVariation(industrialArea.getAssociatedCardType()));
        }
        else if(actionSpace instanceof SingleActionSpace){
            return ((SingleActionSpace) actionSpace).isOccupable(familyMember, paidServants, player.getBonuses().getActivationVariation(industrialArea.getAssociatedCardType()));
        }
        return actionSpace.isOccupable(this.familyMember);
    }
}
