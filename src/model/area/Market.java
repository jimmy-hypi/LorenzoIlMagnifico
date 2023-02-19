package model.area;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import model.effect.CouncilPrivilegeEffect;
import model.effect.InstantResourcesEffect;
import model.resource.ResourceChest;

/**
 * This class represents the market area, this is made of four different single action spaces
 * where you can place one familiar to get a specific resource bonus
 */
public class Market implements Serializable {
	private static final long serialVersionUID = -5295807604863580003L;
	private Map<String,SingleActionSpace> market; // the market slot are ordered from the left to the right
	private int playersInTheMatch;

	public Market(int playersInTheMatch){
		ResourceChest resourceFirstMarket = new ResourceChest(5,0,0,0,0,0,0);
		ResourceChest resourceSecondMarket = new ResourceChest(0,0,0,5,0,0,0);
		ResourceChest resourceThirdMarket = new ResourceChest(2,0,0,0,0,0,3);
		
		market = new LinkedHashMap<String,SingleActionSpace>();
		
		market.put("1",new SingleActionSpace(1,new InstantResourcesEffect(resourceFirstMarket)));
		market.put("2", new SingleActionSpace(1, new InstantResourcesEffect(resourceSecondMarket)));
		if(playersInTheMatch == 4){
		market.put("3",new SingleActionSpace(1, new InstantResourcesEffect(resourceThirdMarket)));
		market.put("4", new SingleActionSpace(1, new CouncilPrivilegeEffect(2)));
		}
		this.playersInTheMatch = playersInTheMatch;
	}

	public SingleActionSpace getMarketActionSpace(String name) {
		return this.market.get(name);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°The Market°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");
		if(market.get("1")!= null)
			builder.append("Cost: " + market.get("1").getActionValueRequired() + "\n\n");
		for(SingleActionSpace marketSlot: market.values()){
			if(marketSlot != null){
				if(marketSlot.getEffect()!=null)
					builder.append(marketSlot.getEffect().toString() + "\n");
				if(marketSlot.isOccupied())
					builder.append(marketSlot.toString() + "\n");
			}
		}
		builder.append("\n°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");

		return builder.toString();
	}
}
