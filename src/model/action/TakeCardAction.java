package model.action;

import constant.CardConstants;
import exception.NotApplicableException;
import model.Color;
import model.FamilyMember;
import model.Player;
import model.area.Board;
import model.area.Floor;
import model.card.CardType;
import model.card.CharacterCard;
import model.card.DevelopmentCard;
import model.effect.Effect;
import model.resource.*;

import java.util.List;

/**
 * This class represents the action of placing a family member onto an action
 * space of a tower and taking the corresponding card, it throws a NotApplicableException with a field
 * that states why the action is not applicable .
 */
public class TakeCardAction extends Action {
    private DevelopmentCard card;
    private Servant paidServants;
    private Floor floor;
    private int actionValueVariation;
    private String notApplicableCode = "You can't apply this action";

    public TakeCardAction(FamilyMember familyMember, Floor floor, Servant paidServants){
        super(familyMember);

        this.card = floor.getCard();
        this.paidServants = paidServants;
        this.floor = floor;

        this.actionValueVariation = calculateActionValueVariation();
    }

    private int calculateActionValueVariation(){
        return this.player.getBonuses().getCardTypeActionVariation(this.card.getCardType());
    }
    @Override
    public void apply() throws NotApplicableException {
        if (this.isApplicable()) {
            player.addCard(card);
            floor.setCard(null);
            player.removeFamilyMember(familyMember.getColor());

            ResourceChest realCost;
            realCost = card.getCost().cloneChest();
            if (player.getBonuses().getCardCostCoinDiscount() != 0) {
                realCost.subResource(new Coin(player.getBonuses().getCardCostCoinDiscount()));
            }

            if (isSomeoneInTheTower())
                realCost.addResource(new Coin(3));

            realCost.addResource(paidServants);

            player.subResources(realCost);

            card.getImmediateEffect().applyEffect(familyMember.getPlayer());

            if (player.getBonuses().isDoubleResourcesFromCards())
                card.getImmediateEffect().applyEffect(familyMember.getPlayer());

            if(card instanceof CharacterCard){
                Effect permanent=card.getPermanentEffect();
                if(permanent!=null)
                    permanent.applyEffect(familyMember.getPlayer());
            }

            this.floor.getActionSpace().setFamilyMember(familyMember);

            this.floor.getActionSpace().getEffect().applyEffect(player);
        } else
            throw new NotApplicableException(notApplicableCode);
    }

    @Override
    public boolean isApplicable() {
        if(!validParams())
            return false;
        if(this.card.getCardType() == CardType.TERRITORY) {
            if(!player.getBonuses().isNoMilitaryPointsRequiredForTerritories()){
                try{
                    int len = player.getDeckOfType(card.getCardType()).size();
                } catch(Exception e){
                    e.printStackTrace();
                }

                if ((int) (Board.getMilitaryRequirementsForTerritories().get(player.getDeckOfType(card.getCardType()).size())) > player.getResourceChest().getResourceInChest(ResourceType.MILITARYPOINT).getAmount()){
                    this.notApplicableCode = "you don't have the required military points to take this card";
                    return false;
                }
            }
        }

        ResourceChest realCost;

        realCost = (ResourceChest) card.getCost().cloneChest();
        if (this.card.getCardType() == CardType.VENTURE){
            ResourceChest vCost = card.getVentureCost();
            int militaryCost = card.getVentureCost().getMilitaryPointsCost();
            int militaryRequired = card.getVentureCost().getRequiredMilitaryPoints();

            if(vCost.isEqualTo(new VentureCostResourceChest(0,0,0,0,0,0,0,militaryCost,militaryRequired))){
                if(this.familyMember.getPlayer().getResourceChest().getResourceInChest(ResourceType.MILITARYPOINT).getAmount() >= militaryRequired){
                    realCost.addChest(new ResourceChest(0,0,0,0,0,0,militaryCost));

                }
                else if(this.familyMember.getPlayer().getResourceChest().getResourceInChest(ResourceType.MILITARYPOINT).getAmount() < militaryRequired)
                {
                    this.notApplicableCode = "you don't have enough military points to take this card";
                    return false;
                }
            }
        }

        if (player.getBonuses().getCardCostCoinDiscount() != 0)
            realCost.subResource(new Coin(player.getBonuses().getCardCostCoinDiscount()));

        //adding 3 additional coins if someone else is occupying the tower
        if (isSomeoneInTheTower())
            realCost.addResource(new Coin(3));

        if (!player.getResourceChest().isGreaterEqualThan(realCost)) {
            this.notApplicableCode = "you don't have enough resources to take this card";
            return false;
        }
        // controlling if the player has space in the corresponding deck
        if (player.getDeckOfType(card.getCardType()).size() >= CardConstants.MAX_PERSONAL_DECK_SIZE) {
            this.notApplicableCode = "you don't have space in your personal board to take this card";
            return false;
        }

        return this.canBePlaced();
    }
    /**
     * Checks if is someone in the tower.
     *
     * @return true, if is someone in the tower
     */
    private boolean isSomeoneInTheTower(){
        List<Floor> floors;
        floors = this.floor.getTower().getFloors();
        for (Floor fl : floors) {
            if (fl.getActionSpace().isOccupied())
                return true;
        }
        return false;
    }

    private boolean validParams(){
        if (this.familyMember == null) {
            this.notApplicableCode = "you don't have this family member";
            return false;
        }
        if (this.floor == null) {
            this.notApplicableCode = "ERROR: floor is null";
            return false;
        }
        if (this.card == null) {
            this.notApplicableCode = "there is not a card on that floor";
            return false;
        }
        return true;
    }
    /**
     * Can be placed.
     *
     * @return true if this.familymember can be placed in this.actionspace
     */
    private boolean canBePlaced() {
        if (!(this.isActionValueEnough() && !floor.getActionSpace().isOccupied()
                && (familyMember.getDice().getColor() == Color.NEUTRAL
                || this.noSamePlayerMembers(familyMember.getPlayer())))) {
            this.notApplicableCode = "your family member can't be placed here because either the floor is occupied, your actionvalue isn't enough"
                    + "or another floor of this tower is occupied by a family member of your color";
            return false;
        }
        return true;
    }
    /**
     * No same player members.
     *
     * @param player the player
     * @return true if there are no other family members of the specified player
     *         in the tower
     */
    private boolean noSamePlayerMembers(Player player) {
        List<Floor> floors;
        floors = this.floor.getTower().getFloors();
        for (Floor fl : floors) {
            if (fl.getActionSpace().isOccupied() && fl != this.floor
                    && fl.getActionSpace().getFamilyMember().getPlayer() == player
                    && !(fl.getActionSpace().getFamilyMember().getColor()==Color.NEUTRAL))
                return false;
        }

        return true;
    }

    /**
     * controlling if the action value of the family member is enough to place
     * it in this action space.
     *
     * @return true, if is action value enough
     */
    private boolean isActionValueEnough() {
        // personal bonuses to add
        return (familyMember.getActionValue() + this.paidServants.getAmount() + this.actionValueVariation >= this.floor
                .getActionSpace().getActionValueRequired());
    }
}
