package model;

import model.card.CardType;
import model.card.DevelopmentCard;
import model.card.LeaderCard;
import model.resource.ResourceChest;
import model.resource.ResourceType;
import server.observers.MatchObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the player class, everyone in the match is linked with an instance of this class
 */
public class Player implements Serializable {
    private static final long serialVersionUID = -7163473671124501218L;
    private String name;
    private String color;
    private HashMap<Color, FamilyMember> familyMembers;
    private ResourceChest resources;
    private HashMap<CardType, ArrayList<DevelopmentCard>> decks;
    private Bonus bonuses;
    private transient MatchObserver observer;
    private int councilPrivilege;
    private HashMap<String, LeaderCard> leaderCards;
    private boolean isExcommunicatedFirst;
    private boolean isExcommunicatedSecond;
    private boolean isExcommunicatedThird;

    public Player(String name, String color){
        familyMembers = new HashMap<>();
        decks = new HashMap<>();

        resources = new ResourceChest();

        for(int i = 0; i < Color.values().length; i++){
            decks.put(CardType.values()[i], new ArrayList<DevelopmentCard>());
        }
        leaderCards = new HashMap<String, LeaderCard>();

        this.name = name;
        this.color = color;

        this.bonuses = new Bonus();
    }

    /**
     * This method adds a cart of a generic Type to the correct Deck of the player.
     * @param card the card
     */
    public void addCard(DevelopmentCard card){
        this.getDeckOfType(card.getCardType()).add(card);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public ResourceChest getResourceChest() {
        return this.resources;
    }

    public ArrayList<DevelopmentCard> getDeckOfType(CardType cardType){
        return decks.get(cardType);
    }

    public Bonus getBonuses() {
        return bonuses;
    }

    public void setBonuses(Bonus bonuses) {
        this.bonuses = bonuses;
    }

    public void setResources(ResourceChest resources) {
        this.resources = resources;
    }

    public HashMap<Color, FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public FamilyMember getFamilyMember(String color){
        return this.familyMembers.get(Color.valueOf(Color.class, color.toUpperCase()));
    }

    public FamilyMember getFamilyMember(Color color){
        return this.familyMembers.get(color);
    }

    public void setFamilyMembers(HashMap<Color, FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("PLAYER:\n" + this.getName() + "\n" + "Color : " + this.getColor() +
                "\n" + "Resources : " + this.getResourceChest().toString() + "\n" + "Cards taken : \n\nTERRITORY CARDS:\n"
                + this.getDeckOfType(CardType.TERRITORY).toString() + "\nCHARACTER CARDS:\n"
                + this.getDeckOfType(CardType.CHARACTER).toString() + "\nBUILDING CARDS:\n"
                + this.getDeckOfType(CardType.BUILDING).toString() + "\nVENTURE CARDS:\n"
                + this.getDeckOfType(CardType.VENTURE).toString() );

        string.append("\n\nFamily members:\n");
        for(FamilyMember mem : this.getFamilyMembers().values()){
            string.append(mem.toString() + "\n");
        }

        if(!leaderCards.isEmpty()){
            string.append("Leader cards : \n");
            for(int i = 0; i < this.leaderCards.values().toArray().length; i++){
                string.append(leaderCards.values().toArray()[i].toString());
            }
        }

        string.append(leaderCards.size() + "questo è il size");

        return string.toString();
    }

    /**
     * This clone method returns a visible to all players Player.
     *
     * @return the masked player
     */
    public Player maskedClone() {

        Player maskedPlayer=new Player(this.name, this.color);
        ResourceChest maskedRC=new ResourceChest();

        maskedPlayer.setExcommunicatedFirst(this.isExcommunicatedFirst);
        maskedPlayer.setExcommunicatedSecond(this.isExcommunicatedSecond);
        maskedPlayer.setExcommunicatedThird(this.isExcommunicatedThird);

        maskedRC.addResource(this.getResourceChest().getResourceInChest(ResourceType.MILITARYPOINT));
        maskedRC.addResource(this.getResourceChest().getResourceInChest(ResourceType.FAITHPOINT));
        maskedRC.addResource(this.getResourceChest().getResourceInChest(ResourceType.VICTORYPOINT));

        maskedPlayer.setResources(maskedRC);

        return maskedPlayer;
    }

    public void addObserver(MatchObserver observer) {
        this.observer=observer;
    }

    public void addResources(ResourceChest resourceChest){
        this.resources.addChest(resourceChest);

        if(observer!=null)
            this.observer.notifyPlayerStatusChange(this);
    }

    public void subResources(ResourceChest resourceChest){
        this.resources.subChest(resourceChest);
        if(observer!=null)
            this.observer.notifyPlayerStatusChange(this);
    }

    public int getCouncilPrivilege() {
        return councilPrivilege;
    }

    public void setCouncilPrivilege(int councilPrivilege) {
        this.councilPrivilege = councilPrivilege;
    }

    public Map<String,LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(HashMap<String,LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public void removeLeaderCard(String leaderName) {
        this.leaderCards.remove((String)leaderName);
        if(observer!=null)
            this.observer.notifyPlayerStatusChange(this);
    }

    public void addLeaderCards(LeaderCard leaderCard){
        this.leaderCards.put(leaderCard.getName(), leaderCard);
        if(observer!=null)
            this.observer.notifyPlayerStatusChange(this);
    }

    public void activateLeaderCard(String name){
        this.leaderCards.get(name).setActivationState(true);
        this.leaderCards.get(name).getSpecialEffect().applyEffect(this);
        if(observer!=null)
            this.observer.notifyPlayerStatusChange(this);
    }

    public void payFaithPoint(){
        ResourceChest rc = new ResourceChest();
        rc.addResource(this.getResourceChest().getResourceInChest(ResourceType.FAITHPOINT));
        this.subResources(rc);
        this.observer.notifyPlayerStatusChange(this);
    }

    public void addFamilyMembers(){
        for(int i = 0; i < Color.values().length; i++){
            familyMembers.put(Color.values()[i], new FamilyMember(Dice.values()[i],this));
        }
    }

    public void removeFamilyMember(Color color){
        this.familyMembers.remove(color);
        if(this.observer!=null) //I need this control to let the test run.
            this.observer.notifyPlayerStatusChange(this);
    }

    public void resetPrivileges() {
        this.councilPrivilege=0;
    }

    public void refreshFamilyMemberValues() {
        for(Map.Entry<Color, FamilyMember > entry : familyMembers.entrySet()) {
            Color key = entry.getKey();
            FamilyMember fam = entry.getValue();

            fam.refreshDiceValue();
        }
    }

    public boolean isExcommunicatedFirst() {
        return isExcommunicatedFirst;
    }

    public boolean isExcommunicatedSecond() {
        return isExcommunicatedSecond;
    }

    public boolean isExcommunicatedThird() {
        return isExcommunicatedThird;
    }

    public void setExcommunicatedFirst(boolean isExcommunicatedFirst) {
        this.isExcommunicatedFirst = isExcommunicatedFirst;
    }

    public void setExcommunicatedSecond(boolean isExcommunicatedSecond) {
        this.isExcommunicatedSecond = isExcommunicatedSecond;
    }

    public void setExcommunicatedThird(boolean isExcommunicatedThird) {
        this.isExcommunicatedThird = isExcommunicatedThird;
    }

    public MatchObserver getObserver() {
        return observer;
    }
}
