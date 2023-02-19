package model.deck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import model.LeaderCardRequirement;
import model.Period;
import model.card.BuildingCard;
import model.card.CardType;
import model.card.CharacterCard;
import model.card.LeaderCard;
import model.card.TerritoryCard;
import model.card.VentureCard;
import model.effect.AtomicExchangeEffect;
import model.effect.CharacterImmediateEffect;
import model.effect.CouncilPrivilegeEffect;
import model.effect.Effect;
import model.effect.ForEachResourceTypeEffect;
import model.effect.ForEachTypeCardEffect;
import model.effect.HarvestBonusEffect;
import model.effect.HarvestEffect;
import model.effect.InstantHarvestActionEffect;
import model.effect.InstantProductionActionEffect;
import model.effect.InstantResourcesEffect;
import model.effect.MultipleEffect;
import model.effect.NoEffect;
import model.effect.NoFloorBonusEffect;
import model.effect.ProductionBonusEffect;
import model.effect.ProductionEffect;
import model.effect.RaiseValueWithDiscountEffect;
import model.effect.ResourcesExchangeEffect;
import model.effect.TakeCardEffect;
import model.effect.leader.CesareBorgiaEffect;
import model.effect.leader.Disapplyable;
import model.effect.leader.FedericoDaMontefeltroEffect;
import model.effect.leader.FilippoBrunelleschiEffect;
import model.effect.leader.LorenzoDeMediciEffect;
import model.effect.leader.LucreziaBorgiaEffect;
import model.effect.leader.LudovicoAriostoEffect;
import model.effect.leader.LudovicoIlMoroEffect;
import model.effect.leader.PicoDellaMirandolaEffect;
import model.effect.leader.SantaRitaEffect;
import model.effect.leader.SigismondoMalatestaEffect;
import model.effect.leader.SistoIVEffect;
import model.resource.Coin;
import model.resource.MilitaryPoint;
import model.resource.Resource;
import model.resource.ResourceChest;
import model.resource.ResourceFactory;
import model.resource.ResourceType;
import model.resource.VentureCostResourceChest;
import model.resource.VictoryPoint;

/**
 * The Class that creates the decks of different types according to the rules
 */
public class DeckCreator {
	private static BufferedReader buffReader;
	private static String lineRead;

	private DeckCreator(){
		
	}
	
	/**
	 * Creates the building card deck from the file, see template FileTemplateBuildingsCardV1.xlsx
	 * @param filePath the file path
	 * @param deckLength the deck length
	 * @return the building card[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BuildingCard[] createBuildingCardDeck(String filePath, int deckLength) throws IOException {
		
		int id;
		String name;
		Period period; 
		ResourceChest cost;
		ResourceChest instantChest;
		Effect immediateEffect;
		Effect permanentEffect;  //this should be a ProductionEffect, but we still have to create the class
		ProductionEffect productionEffect;
		int productionActivationCost;
		int cardIndex=0;

		BuildingCard[] deck = new BuildingCard[deckLength];

		buffReader = new BufferedReader(new FileReader(filePath));
		lineRead = buffReader.readLine();  //line 1  	//The lineRead variable stores the first line of a card and uses it to check the while condition
		while (lineRead!=null) {
			id=Integer.parseInt(lineRead);
			name= buffReader.readLine();    //line 2
			period=Period.values()[Integer.parseInt(buffReader.readLine())-1];  //line 3
			cost=new ResourceChest(Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),0,0,0);
			instantChest=new ResourceChest(0,0,0,0,Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()),0);  //lines 8-9
			immediateEffect=new InstantResourcesEffect(instantChest);
			productionActivationCost=Integer.parseInt(buffReader.readLine());  //line 10
			permanentEffect=calculateProductionEffectFromFile();  //lines 11 to 38
			productionEffect=new ProductionEffect(permanentEffect);
			deck[cardIndex]=new BuildingCard(id,name,period,cost,immediateEffect,productionEffect,productionActivationCost);
			cardIndex++;
			lineRead = buffReader.readLine();   //line 1
		}

		buffReader.close();
		return deck;
	}

	/**
	 * Calculates the production effect from file. (It's only for building cards)
	 *
	 * @return the effect
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static Effect calculateProductionEffectFromFile() throws IOException {
		int privilege;
		int mPoint;
		int vPoint;
		int coin;
		int cardType;
		CardType cType;
		ResourceChest instantChest;
		CouncilPrivilegeEffect privilegeEffect;
		ForEachTypeCardEffect forEachEffect;

		AtomicExchangeEffect atomicExchange1;
		AtomicExchangeEffect atomicExchange2;
		
		privilege=Integer.parseInt(buffReader.readLine());   //lines 11-12-13-14
		mPoint=Integer.parseInt(buffReader.readLine());
		vPoint=Integer.parseInt(buffReader.readLine());
		coin=Integer.parseInt(buffReader.readLine());
		
		if(privilege==0&&mPoint==0&&vPoint==0&&coin==0){
			coin=Integer.parseInt(buffReader.readLine());  //lines 15-16
			cardType=Integer.parseInt(buffReader.readLine());
			if(coin==0&&cardType==0){
				vPoint=Integer.parseInt(buffReader.readLine());    //lines 17-18
				cardType=Integer.parseInt(buffReader.readLine());
				if(coin==0&&vPoint==0){  
					atomicExchange1=calculateAtomicExchangeFromFile();   // lines 19 to 28
					atomicExchange2=calculateAtomicExchangeFromFile();  //lines 29 to 38
					
					return new ResourcesExchangeEffect(atomicExchange1,atomicExchange2);
				}
				else {
					cType=CardType.values()[cardType-1];
					forEachEffect=new ForEachTypeCardEffect(new VictoryPoint(vPoint), cType);
					for(int i=19;i<39;i++)buffReader.readLine();
					return forEachEffect;
				}
			}
			else {
				cType=CardType.values()[cardType-1];
				forEachEffect=new ForEachTypeCardEffect(new Coin(coin), cType);
				for(int i=17;i<39;i++)buffReader.readLine();
				return forEachEffect;
			}
		}
		else {
			privilegeEffect=new CouncilPrivilegeEffect(privilege);
			instantChest=new ResourceChest(coin,0,0,0,0,vPoint,mPoint);
			for(int i=15;i<39;i++)buffReader.readLine();
			return new InstantResourcesEffect(instantChest,privilegeEffect);  //instant needs a constructor with the possibility to have a privilege effect
		}
	}



	/**
	 * Calculates the atomic exchange effect from file. The atomic effect is needed to instantiate
	 * the exchange effect. (only for building cards)
	 *
	 * @return the atomic exchange effect
	 * @throws IOException Signals that an I/O exception has occurred.
	 * 
	 */
	private static AtomicExchangeEffect calculateAtomicExchangeFromFile() throws IOException {
		int numberOfResource;
		
		numberOfResource=Integer.parseInt(buffReader.readLine()); //19 or 29
		if(numberOfResource==0){  //it means it has no atomicExchangeEffect
			for(int i=0;i<9;i++)buffReader.readLine();  //line 20 to 28
			return null;
		}
		else{ //it means it has an atomic exchange effect

			int resourceId;
			Resource resourceOut1;
			Resource resourceOut2;
			Resource resourceOut3;  //resources to give
			Resource resourceIn1;
			Resource resourceIn2;     //resources to get
			ResourceChest resourcesOut=new ResourceChest(0,0,0,0,0,0,0);
			ResourceChest resourcesIn=new ResourceChest(0,0,0,0,0,0,0);
			
			resourceId=Integer.parseInt(buffReader.readLine());  //line 20 or 31
			if(resourceId!=0){
			resourceOut1=ResourceFactory.getResource(ResourceType.values()[resourceId-1],numberOfResource);
			resourcesOut.addResource(resourceOut1);}
			
			numberOfResource=Integer.parseInt(buffReader.readLine());  //line 21
			resourceId=Integer.parseInt(buffReader.readLine());  //line 22
			
			if(resourceId!=0){
			resourceOut2=ResourceFactory.getResource(ResourceType.values()[resourceId-1],numberOfResource);
			resourcesOut.addResource(resourceOut2);}
			
			numberOfResource=Integer.parseInt(buffReader.readLine());  // line 23
			resourceId=Integer.parseInt(buffReader.readLine());   //line24
			
			if(resourceId!=0){
			resourceOut3=ResourceFactory.getResource(ResourceType.values()[resourceId-1],numberOfResource);
			resourcesOut.addResource(resourceOut3);}
			
			numberOfResource=Integer.parseInt(buffReader.readLine());  //line25
			resourceId=Integer.parseInt(buffReader.readLine());     //line 26
			
			if(resourceId!=0){
			resourceIn1=ResourceFactory.getResource(ResourceType.values()[resourceId-1],numberOfResource);
			resourcesIn.addResource(resourceIn1);}

			numberOfResource=Integer.parseInt(buffReader.readLine());   //line 27
			resourceId=Integer.parseInt(buffReader.readLine());   //line 28s
			
			if(resourceId!=0){
			resourceIn2=ResourceFactory.getResource(ResourceType.values()[resourceId-1],numberOfResource);
			resourcesIn.addResource(resourceIn2);}

			return new AtomicExchangeEffect(resourcesOut,resourcesIn);
			
		}
	}

	/**
	 * Creates the territory card deck.
	 *
	 * @param filePath the file path
	 * @param deckLength the deck length
	 * @return the territory card[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TerritoryCard[] createTerritoryCardDeck(String filePath, int deckLength) throws IOException {

		int cardIndex=0;
		TerritoryCard[] deck = new TerritoryCard[deckLength];
		
		int id;
		String name;
		Period period;
		Effect immediateEffect;
		HarvestEffect harvestEffect;
		int harvestActivationCost;
		
		ResourceChest immediateResourceChest;
		ResourceChest harvestedResourceChest;
		int immediatePrivilegeAmount;
		int harvestedPrivilegeAmount;

		buffReader = new BufferedReader(new FileReader(filePath));	
		lineRead = buffReader.readLine();

		while (lineRead!=null) {
			
			id = Integer.parseInt(lineRead);
			
			name = buffReader.readLine();
			period = Period.values()[Integer.parseInt(buffReader.readLine())-1];
			
			immediateResourceChest = new ResourceChest(Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()), //Coins and wood
													   Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()), //Stones and servants
													   Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()), //Faith and victory
													   Integer.parseInt(buffReader.readLine()));										 //Military
			
			immediatePrivilegeAmount = Integer.parseInt(buffReader.readLine());
			harvestActivationCost = Integer.parseInt(buffReader.readLine());
			
			harvestedResourceChest = new ResourceChest(Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()), //Coins and wood
					                                   Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()), //Stones and servants
					                                   Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()), //Faith and victory
					                                   Integer.parseInt(buffReader.readLine()));										 //Military
			harvestedPrivilegeAmount = Integer.parseInt(buffReader.readLine());
			
			//Immediate effect initialization
			if(immediateResourceChest.isEmpty()){
				if(immediatePrivilegeAmount == 0)
					immediateEffect = new NoEffect();
				else{
					immediateEffect = new CouncilPrivilegeEffect(immediatePrivilegeAmount);
				}
			}
			else{
				if(immediatePrivilegeAmount == 0)
					immediateEffect = new InstantResourcesEffect(immediateResourceChest);
				else{
					immediateEffect = new MultipleEffect(new InstantResourcesEffect(immediateResourceChest), new CouncilPrivilegeEffect(immediatePrivilegeAmount));
				}
			}
			
			//Permanent effect initialization; it will be a harvest effect
			if(harvestedResourceChest.isEmpty()){
				if(harvestedPrivilegeAmount !=0)
					harvestEffect = new HarvestEffect(new CouncilPrivilegeEffect(harvestedPrivilegeAmount));
				else
					throw new IOException();   //It is impossible to have a territory card without a harvest effect
			}
			else{
				if(harvestedPrivilegeAmount == 0)
					harvestEffect = new HarvestEffect(new InstantResourcesEffect(harvestedResourceChest));
				else
					harvestEffect = new HarvestEffect(new MultipleEffect(new InstantResourcesEffect(harvestedResourceChest), new CouncilPrivilegeEffect(harvestedPrivilegeAmount)));
			}
			
			deck[cardIndex] = new TerritoryCard(id,name,period,immediateEffect,harvestEffect, harvestActivationCost);
			
			lineRead = buffReader.readLine();
			cardIndex++;
		}
		buffReader.close();
		return deck;
	}
	
	/**
	 * Creates the venture card deck.
	 *
	 * @param filePath the file path
	 * @param deckLength the deck length
	 * @return the venture card[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static VentureCard[] createVentureCardDeck(String filePath, int deckLength) throws IOException {

		int id;
		String name;
		Period period;
		
		ResourceChest cost;
		ResourceChest addings;
		ResourceChest victoryPointsChest;
		
		Effect immediateEffect;
		Effect permanentEffect;  //this should be a ProductionEffect, but we still have to create the class

		int coins;
		int faithPoints;
		int victoryPoints;
		int woods;
		int stones;
		int servants;
		int militaryPoints;
		int privilege;
		int productionBonus;
		int harvestBonus;
		int takeCardType;
		int takeCardCost;

		int index = 0;
		
		VentureCard[] deck = new VentureCard[deckLength];

		buffReader = new BufferedReader(new FileReader(filePath));
		lineRead = buffReader.readLine();
		
		while (lineRead!=null) {
			
		id = Integer.parseInt(lineRead);

		name = buffReader.readLine();
		
		period = Period.values()[Integer.parseInt(buffReader.readLine()) - 1];
		
		cost = new VentureCostResourceChest(Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()),
				Integer.parseInt(buffReader.readLine()), Integer.parseInt(buffReader.readLine()),
				0,0,0,Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()));

		coins = Integer.parseInt(buffReader.readLine());
		woods = Integer.parseInt(buffReader.readLine());
		stones = Integer.parseInt(buffReader.readLine());
		servants = Integer.parseInt(buffReader.readLine());
		faithPoints = Integer.parseInt(buffReader.readLine());
		victoryPoints = Integer.parseInt(buffReader.readLine());
		militaryPoints = Integer.parseInt(buffReader.readLine());
		
		addings = new ResourceChest(coins,woods,stones,servants,faithPoints,0,militaryPoints);
		
		privilege = Integer.parseInt(buffReader.readLine());
		productionBonus = Integer.parseInt(buffReader.readLine());
		harvestBonus = Integer.parseInt(buffReader.readLine());
		takeCardType = Integer.parseInt(buffReader.readLine());
		takeCardCost = Integer.parseInt(buffReader.readLine());

		victoryPointsChest = new ResourceChest(0,0,0,0,0,victoryPoints,0);
		
		if(privilege != 0){
			immediateEffect = new CouncilPrivilegeEffect(privilege);
		}else if (takeCardType != 0){
			immediateEffect = new TakeCardEffect(CardType.convertCardType(takeCardType), takeCardCost, new ResourceChest());
		} else if (productionBonus != 0){
			immediateEffect = new InstantProductionActionEffect(productionBonus);
		}else if (harvestBonus != 0){
			immediateEffect = new InstantHarvestActionEffect(harvestBonus);
		}else {
			immediateEffect = new InstantResourcesEffect(addings) ;
		}
		
		permanentEffect = new InstantResourcesEffect(victoryPointsChest);
		
		deck[index] = new VentureCard(id,name,period,cost,immediateEffect,permanentEffect);
		index++;
		
		lineRead = buffReader.readLine();   //line 1
		}
		buffReader.close();
	
		return deck;
	}
	
	
	/**
	 * Creates the character card deck.
	 *
	 * @param filePath the file path
	 * @param deckLength the deck length
	 * @return the character card[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static CharacterCard[] createCharacterCardDeck(String filePath, int deckLength) throws IOException {

		int cardIndex=0;
		
		//CharacterCard parameter
		int id;
		String name;
		Period period;
		ResourceChest moneyCost;  //Character cards cost only coins
		Effect immediateEffect = null;  //it should be impossible to have it still null after the immediateEffectAssignment
		Effect permanentEffect;
		
		
		//local variable for support purpose, they will read and store the value from the file
		//and then build the related object
		int valueAmount;
		CardType cardType;
		int extraCardValue;
		CardType extraCardType;
		boolean buildingCardsBonus;
		boolean characterCardsBonus;
		int discountedCoin;
		int discountedWood;
		int discountedStone;
		int discountedServant;
		int givenMilitaryPoints;
		int givenFaithPoints;
		boolean noFloorBonus;
		int raiseProductionValueAmount;
		int raiseHarvestValueAmount;
		int privilegeAmount; 
		int victoryPerMilitaryPointsAmount;
		int militaryPointsAmount;
		int victoryPointsPerCardAmount;
		CardType typeCard;
		int harvestActionValue; 
		int productionActionValue;
		
		ResourceChest discountChest;

		CharacterCard[] deck = new CharacterCard[deckLength];

		buffReader = new BufferedReader(new FileReader(filePath));
		lineRead = buffReader.readLine();
		
		while (lineRead!=null) {
			id = Integer.parseInt(lineRead); 
			
			name = buffReader.readLine();
			period = Period.values()[Integer.parseInt(buffReader.readLine())-1];
			moneyCost = new ResourceChest(Integer.parseInt(buffReader.readLine()), 0, 0, 0, 0, 0, 0);
			
			//Initializing the support variables; this section reads all the parameters
			//and saves them in local variables which are used to create the effect Objects later on
			//This approach helps the readability. I want to avoid methods that directly have 
			//other methods call as parameter making them hard to read
			valueAmount = Integer.parseInt(buffReader.readLine()); 
			cardType = CardType.values()[Integer.parseInt(buffReader.readLine())-1];
			extraCardValue = Integer.parseInt(buffReader.readLine()); 
			extraCardType = CardType.values()[Integer.parseInt(buffReader.readLine())-1]; 
			buildingCardsBonus = Boolean.parseBoolean(buffReader.readLine());  
			characterCardsBonus = Boolean.parseBoolean(buffReader.readLine());  
			discountedCoin = Integer.parseInt(buffReader.readLine());
			discountedWood = Integer.parseInt(buffReader.readLine());  		
			discountedStone = Integer.parseInt(buffReader.readLine()); 	
			discountedServant = Integer.parseInt(buffReader.readLine()); 
			givenMilitaryPoints = Integer.parseInt(buffReader.readLine());
			givenFaithPoints = Integer.parseInt(buffReader.readLine());	 
			noFloorBonus = Boolean.parseBoolean(buffReader.readLine());  
			raiseProductionValueAmount = Integer.parseInt(buffReader.readLine());
			raiseHarvestValueAmount = Integer.parseInt(buffReader.readLine());  
			privilegeAmount = Integer.parseInt(buffReader.readLine());  
			victoryPerMilitaryPointsAmount = Integer.parseInt(buffReader.readLine());
			militaryPointsAmount = Integer.parseInt(buffReader.readLine());
			victoryPointsPerCardAmount = Integer.parseInt(buffReader.readLine()); 
			typeCard = CardType.values()[Integer.parseInt(buffReader.readLine())-1]; 
			harvestActionValue = Integer.parseInt(buffReader.readLine()); 
			productionActionValue = Integer.parseInt(buffReader.readLine()); 
			
			//The following instructions just check the correctness of the two boolean 
			//depending on the type of card passed later on 
			if(cardType == CardType.TERRITORY || cardType == CardType.VENTURE){
				buildingCardsBonus = false;
				characterCardsBonus = false;
			}else if(cardType == CardType.CHARACTER){
				buildingCardsBonus = false;
			}else
				characterCardsBonus = false;

			//First I assign the permanent effect, there can't be more then one permanent effect per card
			if(valueAmount != 0){
				permanentEffect = new RaiseValueWithDiscountEffect(valueAmount, cardType, buildingCardsBonus, characterCardsBonus);	
			}
			else if(noFloorBonus){
				permanentEffect = new NoFloorBonusEffect();
			}else if(raiseProductionValueAmount != 0){
				permanentEffect = new HarvestBonusEffect(raiseProductionValueAmount);
			}else if(raiseHarvestValueAmount != 0){
				permanentEffect = new ProductionBonusEffect(raiseHarvestValueAmount);
			}else
				permanentEffect = new NoEffect();

			//Second, I assign the immediateEffect
			//First I set the "discountedResourceChest"
			discountChest = new ResourceChest(discountedCoin, discountedWood, discountedStone, discountedServant, 0, 0, 0);
			
			if(givenMilitaryPoints == 0){
				if(givenFaithPoints == 0){
					if(privilegeAmount == 0){
						if(extraCardValue == 0){
							if(victoryPerMilitaryPointsAmount == 0){
								if(victoryPointsPerCardAmount == 0)
									immediateEffect = new NoEffect();
								else	
									immediateEffect = new CharacterImmediateEffect(new ForEachTypeCardEffect(new VictoryPoint(victoryPointsPerCardAmount), typeCard));
							}
							else{
								immediateEffect = new CharacterImmediateEffect(new ForEachResourceTypeEffect(new VictoryPoint(victoryPerMilitaryPointsAmount), new MilitaryPoint(militaryPointsAmount)));			
							}
						}
						else{
							immediateEffect = new CharacterImmediateEffect(new TakeCardEffect(extraCardType, extraCardValue, discountChest));
						}
					}
					else{
											
						if(extraCardValue == 0){
							immediateEffect = new CharacterImmediateEffect(new CouncilPrivilegeEffect(privilegeAmount));
						}
						else{
							immediateEffect = new CharacterImmediateEffect(new MultipleEffect(new TakeCardEffect(extraCardType, extraCardValue, discountChest), new CouncilPrivilegeEffect(privilegeAmount)));
						}
					}	
				}
				else{
					if(extraCardValue == 0 && harvestActionValue == 0 && productionActionValue == 0){    //o mi dà solo punti fede
						immediateEffect = new CharacterImmediateEffect(new InstantResourcesEffect(new ResourceChest(0, 0, 0, 0, givenFaithPoints, 0, 0)));
					}
					if(extraCardValue != 0 && harvestActionValue == 0 && productionActionValue == 0){    //o mi dà punti fede e mi fa attivare un'altra carta
						immediateEffect = new CharacterImmediateEffect(new MultipleEffect(new TakeCardEffect(extraCardType, extraCardValue, discountChest), new InstantResourcesEffect(new ResourceChest(0, 0, 0, 0, givenFaithPoints, 0, 0))));
					}
					if(extraCardValue == 0 && harvestActionValue != 0 && productionActionValue == 0){    //o mi dà solo punti fede e mi fa attivare una raccolta
						immediateEffect = new CharacterImmediateEffect(new MultipleEffect(new InstantHarvestActionEffect(harvestActionValue), new InstantResourcesEffect(new ResourceChest(0, 0, 0, 0, givenFaithPoints, 0, 0))));
					}
					if(extraCardValue == 0 && harvestActionValue == 0 && productionActionValue != 0){    //o mi dà solo punti fede e mi fa attivare una produzione
						immediateEffect = new CharacterImmediateEffect(new MultipleEffect(new InstantProductionActionEffect(productionActionValue), new InstantResourcesEffect(new ResourceChest(0, 0, 0, 0, givenFaithPoints, 0, 0))));
					}
				}
			}
			else{
				if(extraCardValue == 0){
					immediateEffect = new CharacterImmediateEffect(new InstantResourcesEffect(new ResourceChest(0, 0, 0, 0, 0, 0, givenMilitaryPoints)));
				}
				if(extraCardValue != 0){
					immediateEffect = new CharacterImmediateEffect(new MultipleEffect(new TakeCardEffect(extraCardType, extraCardValue, discountChest), new InstantResourcesEffect(new ResourceChest(0, 0, 0, 0, 0, 0, givenMilitaryPoints))));
				}
			}
			
			deck[cardIndex] = new CharacterCard(id,name,period,moneyCost,immediateEffect,permanentEffect);

			lineRead = buffReader.readLine();
			cardIndex++;
		}
		
		buffReader.close();
		return deck;
	}
	
	
	

	/**
	 * Creates the leader card deck.
	 *
	 * @param filePath the file path
	 * @param deckLength the deck length
	 * @return the leader card[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static LeaderCard[] createLeaderCardDeck(String filePath, int deckLength) throws IOException {
		
		int index = 0;
		
		String name;
		
		ResourceChest requirements;
		int privilege;

		int territoryCardsRequired;
		int buildingCardsRequired;
		int characterCardsRequired;
		int ventureCardsRequired;
		int anyCardsRequired;
		
		LeaderCardRequirement totalRequirements;
		
		ResourceChest addings;
		
		int harvestActionValue;
		float productionActionValue;
		
		int ludovicoAriostoEffect;
		int filippoBrunelleschiEffect;
		int sigismondoMalatestaEffect;
		int ludovicoIlMoroEffect;
		int lorenzoDeMediciEffect;
		int sistoIVEffect;
		int cesareBorgiaEffect;
		int santaRitaEffect;
		int picoDellaMirandolaEffect;
		int lucreziaBorgiaEffect;
		int federicoDaMontefeltroEffect;
		
		Disapplyable specialEffect;

		LeaderCard[] deck = new LeaderCard[deckLength];

		buffReader = new BufferedReader(new FileReader(filePath));
		lineRead = buffReader.readLine();
		
		while (lineRead!=null) {
			name = lineRead;
			
			requirements = new ResourceChest(Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()));
			
			territoryCardsRequired = Integer.parseInt(buffReader.readLine());
			buildingCardsRequired = Integer.parseInt(buffReader.readLine());
			characterCardsRequired = Integer.parseInt(buffReader.readLine());
			ventureCardsRequired = Integer.parseInt(buffReader.readLine());
			anyCardsRequired = Integer.parseInt(buffReader.readLine());
			
			totalRequirements = new LeaderCardRequirement(requirements,territoryCardsRequired,buildingCardsRequired,
					characterCardsRequired,ventureCardsRequired,anyCardsRequired);
			
			addings = new ResourceChest(Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()),Integer.parseInt(buffReader.readLine()),
					Integer.parseInt(buffReader.readLine()));
			
			privilege = Integer.parseInt(buffReader.readLine());
			
			harvestActionValue = Integer.parseInt(buffReader.readLine());
			productionActionValue = Float.parseFloat(buffReader.readLine());
			
			ludovicoAriostoEffect = Integer.parseInt(buffReader.readLine());
			filippoBrunelleschiEffect = Integer.parseInt(buffReader.readLine());
			sigismondoMalatestaEffect = Integer.parseInt(buffReader.readLine());
			ludovicoIlMoroEffect = Integer.parseInt(buffReader.readLine());
			lorenzoDeMediciEffect = Integer.parseInt(buffReader.readLine());
			sistoIVEffect = Integer.parseInt(buffReader.readLine());
			cesareBorgiaEffect = Integer.parseInt(buffReader.readLine());
			santaRitaEffect = Integer.parseInt(buffReader.readLine());
			picoDellaMirandolaEffect = Integer.parseInt(buffReader.readLine());
			lucreziaBorgiaEffect = Integer.parseInt(buffReader.readLine());
			federicoDaMontefeltroEffect = Integer.parseInt(buffReader.readLine());
			
			if(harvestActionValue != 0){
			specialEffect = new InstantHarvestActionEffect(harvestActionValue);
			} else if(productionActionValue != 0.0)
			{
				specialEffect = new InstantProductionActionEffect((int)productionActionValue);
			}else if(ludovicoAriostoEffect == 1){
				specialEffect = new LudovicoAriostoEffect();
			}else if(filippoBrunelleschiEffect == 1){
				specialEffect = new FilippoBrunelleschiEffect();
			}else if(sigismondoMalatestaEffect == 1){
				specialEffect = new SigismondoMalatestaEffect();
			}else if(ludovicoIlMoroEffect == 1){
				specialEffect = new LudovicoIlMoroEffect();
			}else if(lorenzoDeMediciEffect == 1){
				specialEffect = new LorenzoDeMediciEffect();
			}else if(sistoIVEffect == 1){
				specialEffect = new SistoIVEffect();
			}else if(cesareBorgiaEffect == 1){
				specialEffect = new CesareBorgiaEffect();
			}else if(santaRitaEffect == 1){
				specialEffect = new SantaRitaEffect();
			}else if(picoDellaMirandolaEffect == 1){
				specialEffect = new PicoDellaMirandolaEffect();
			}else if(lucreziaBorgiaEffect == 1){
				specialEffect = new LucreziaBorgiaEffect();
			}else if(federicoDaMontefeltroEffect == 1){
				specialEffect = new FedericoDaMontefeltroEffect();
			}else if (privilege != 0){
				specialEffect = new CouncilPrivilegeEffect(privilege);
			}else{
				specialEffect = new InstantResourcesEffect(addings);
			}
			
			deck[index] = new LeaderCard(name,totalRequirements,specialEffect);

			index++;

			lineRead = buffReader.readLine();   //line 1
		}
		buffReader.close();
		return deck;
	}
}
