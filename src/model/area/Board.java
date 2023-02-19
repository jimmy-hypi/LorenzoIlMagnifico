package model.area;

import constant.CardConstants;
import model.Dice;
import model.card.CardType;
import model.card.DevelopmentCard;
import model.deck.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents the board with all the areas you can find in the physical board.
 * In its different areas you can place the family members and also it contains the cards and the excommunication tiles.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = -8471586996479354156L;
    private Map<CardType, Tower> towers;
    private Church church;
    private CouncilPalace councilPalace;
    private Market market;
    private HarvestArea harvestArea;
    private ProductionArea productionArea;
    private static ArrayList<Integer> militaryRequirementsForTerritories;
    private Deck<? extends DevelopmentCard> territoryCards;
    private Deck<? extends DevelopmentCard> buildingCards;
    private Deck<? extends DevelopmentCard> characterCards;
    private Deck<? extends DevelopmentCard> ventureCards;
    private int numberOfPlayers;
    private List<String> playerOrder;
    private Map<Dice, Integer> dices;

    public Board(int numberOfPlayers) throws FileNotFoundException, IOException {
        dices = new EnumMap<Dice, Integer>(Dice.class);

        dices.put(Dice.BLACK_DICE, Dice.BLACK_DICE.getUpperFaceValue());
        dices.put(Dice.ORANGE_DICE,Dice.ORANGE_DICE.getUpperFaceValue());
        dices.put(Dice.WHITE_DICE,Dice.WHITE_DICE.getUpperFaceValue());

        towers = new LinkedHashMap<>();

        militaryRequirementsForTerritories=BoardInitializer.playerBoardRequirementsForTerritory();

        territoryCards=new TerritoryDeck("res/files/fileterritorycards.txt",CardConstants.DECK_LENGTH);
        characterCards=new CharacterDeck("res/files/filecharactercards.txt",CardConstants.DECK_LENGTH);
        buildingCards=new BuildingDeck("res/files/filebuildingcards.txt",CardConstants.DECK_LENGTH);
        ventureCards=new VentureDeck("res/files/fileventurecards.txt",CardConstants.DECK_LENGTH);

        territoryCards.shuffleDeck();
        buildingCards.shuffleDeck();
        characterCards.shuffleDeck();
        ventureCards.shuffleDeck();

        for(int i = 0; i< CardConstants.DECK_LENGTH; i++)

        towers.put(CardType.TERRITORY,new Tower(CardType.TERRITORY, territoryCards,BoardInitializer.territoryBonuses()));
        towers.put(CardType.CHARACTER,new Tower(CardType.CHARACTER, characterCards,BoardInitializer.characterBonuses()));
        towers.put(CardType.BUILDING,new Tower(CardType.BUILDING, buildingCards,BoardInitializer.buildingBonuses()));
        towers.put(CardType.VENTURE,new Tower(CardType.VENTURE, ventureCards,BoardInitializer.ventureBonuses()));

        church = new Church();
        councilPalace = new CouncilPalace();
        market = new Market(numberOfPlayers); //this method should return the number of players in the match
        harvestArea = new HarvestArea();
        productionArea = new ProductionArea();

        this.numberOfPlayers = numberOfPlayers;

        this.playerOrder = new ArrayList<String>();
    }

    public Tower getTower(CardType cardType){
        return this.towers.get(cardType);
    }

    public void changeCardInTowers(){
        for(int i=0;i<CardType.values().length-1;i++){
            this.getTower(CardType.values()[i]).changeCards();
        }
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public CouncilPalace getCouncilPalace() {
        return councilPalace;
    }

    public void setCouncilPalace(CouncilPalace councilPalace) {
        this.councilPalace = councilPalace;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public IndustrialArea getHarvestArea() {
        return harvestArea;
    }

    public void setHarvestArea(HarvestArea harvestArea) {
        this.harvestArea = harvestArea;
    }

    public ProductionArea getProductionArea() {
        return productionArea;
    }

    public void setProductionArea(ProductionArea productionArea) {
        this.productionArea = productionArea;
    }

    public static ArrayList<Integer> getMilitaryRequirementsForTerritories() {
        return militaryRequirementsForTerritories;
    }

    public Floor getFloor(CardType cardType, int index){
        return towers.get(cardType).getFloor(index);

    }

    public void setPlayerOrder(ArrayList<String> playerOrder){
        this.playerOrder=playerOrder;
    }

    public void rollDices() {
        for(int i=0;i<Dice.values().length;i++)
            Dice.values()[i].roll();

        dices.put(Dice.BLACK_DICE, Dice.BLACK_DICE.getUpperFaceValue());
        dices.put(Dice.ORANGE_DICE,Dice.ORANGE_DICE.getUpperFaceValue());
        dices.put(Dice.WHITE_DICE,Dice.WHITE_DICE.getUpperFaceValue());
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("--------------------------------------------------THE BOARD-------------------------------------------------");
        builder.append("\n\n");
        builder.append("\t\t\t\tThis is your world, let's try to impose your family\n\n");
        builder.append("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°Development cards in the towers°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");
        for(Tower t: towers.values()){
            builder.append(t.toString());
        }
        builder.append("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");
        builder.append(church.toString());
        builder.append("\n");
        builder.append(councilPalace.toString());
        builder.append(market.toString());
        builder.append("\n");
        builder.append(harvestArea.toString());
        builder.append("\n");
        builder.append(productionArea.toString());

        builder.append("------------------------------------------------------------------------------------------------------------\n\n");

        builder.append("\nPLAY ORDER:\n");
        for(String p : playerOrder){
            builder.append(playerOrder.indexOf(p) + 1);
            builder.append(" - ");
            builder.append(p);
            builder.append("\n");
        }
        builder.append("\nDICES: \n");


        builder.append(Dice.BLACK_DICE.getColor().toString() + " dice: " + dices.get(Dice.BLACK_DICE) + "\n");
        builder.append(Dice.ORANGE_DICE.getColor().toString() + " dice: " + dices.get(Dice.ORANGE_DICE) + "\n");
        builder.append(Dice.WHITE_DICE.getColor().toString() + " dice: " + dices.get(Dice.WHITE_DICE) + "\n");

        return builder.toString();
    }

    public ArrayList<String> getPlayerOrder() {
        return (ArrayList<String>) playerOrder;
    }

    public Map<Dice, Integer> getDices() {
        return dices;
    }
}
