package model.area;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.card.CardType;
import model.card.DevelopmentCard;
import model.deck.Deck;
import model.effect.InstantResourcesEffect;
import model.resource.Resource;
import model.resource.ResourceChest;
import model.area.Floor;

/**
 * This class represents the four towers in the game: TERRITORY,CHARACTER,BUILDING,VENTURE.
 * Each tower has a dynamic number of floors and a related deck, with a method to fill the floors
 * with cards of the deck according to the Periods
 */
public class Tower implements Serializable {
	private static final long serialVersionUID = 5976389942566908672L;
	private List<Floor> floors;
	private CardType cardType;
	private Deck<? extends DevelopmentCard> deck;
	private int currentCard = 0; // index of the card on the top of the deck
	private int actionSpaceCost = 1;

	 public Tower(CardType cardType, Deck<? extends DevelopmentCard> deck,ArrayList<Resource> bonuses){ 
		 floors=new ArrayList<Floor>();
		 this.cardType = cardType;
		 this.deck = deck;
			
		 ResourceChest r;

		 for(int i = 0; i < deck.length() / 6; i++){
			 r = new ResourceChest();
			 r.addResource(bonuses.get(i));
			 floors.add(new Floor(null,this,actionSpaceCost,new InstantResourcesEffect(r)));
			 actionSpaceCost = actionSpaceCost + 2;
		 }
	}

 	/**
 	 * This method places one Card in each floor, starting by the card currently on the top of it.
 	 */
 	public void changeCards(){
 		for(int i=0; i < deck.length()/6; i++){
 			floors.get(i).setCard(deck.getCard(currentCard)); 
 			currentCard++;
 		}
 	}

 	/**
	  * This Method builds an Array with the cards in the tower and returns it.
	  *
	  * @return the cards
	  */
 	public ArrayList<DevelopmentCard> getCards(){
 		ArrayList<DevelopmentCard> towerCards = new ArrayList<DevelopmentCard>();
 		
 		for(Floor f : floors){
 			towerCards.add(f.card);
 		}
 		
 		return towerCards;
 	}

	 public List<Floor> getFloors() {
		return floors;
	}

	public Floor getFloor(int index) {
		return floors.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(cardType.toString().toUpperCase() + " CARDS:\n");
		for(int i = 0; i<floors.size(); i++){
			builder.append("\n********************\n");
			switch(i){
			case 0:
				builder.append("First");
				break;
			case 1: 
				builder.append("Second");
				break;
			case 2:
				builder.append("Third");
				break;
			case 3:
				builder.append("Fourth");
				break;
			case 4: 	//This case probably won't be used 
				builder.append("Fifth");
				break;
			default:
				builder.append("A");
			}

			builder.append(" floor\nAction value required: " + getFloor(i).getActionSpace().getActionValueRequired());
			if(getFloor(i).getActionSpace().getEffect()!=null);
				builder.append("\nBonus:\n\t" + getFloor(i).getActionSpace().getEffect().toString());
			builder.append("\nCard:\n\n");
			if(getFloor(i).getCard()!=null)
				builder.append(getFloor(i).getCard().toString());
			else{
				if(getFloor(i).getActionSpace().isOccupied())
					builder.append("\tThis card has already been taken by player with color " + getFloor(i).getActionSpace().getFamilyMember().getPlayer().getColor());	
			}
			builder.append("\n********************\n\n");
		}
		builder.append("\n\n");
		return builder.toString();
	}
}
