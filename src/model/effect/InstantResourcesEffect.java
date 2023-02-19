package model.effect;

import model.Player;
import model.effect.leader.Disapplyable;
import model.resource.ResourceChest;

/**
 * This class implements the InstantResourceEffect that gives a certain amount
 * of a certain resource to a player.
 */
public class InstantResourcesEffect extends Effect implements Disapplyable{
	private static final long serialVersionUID = -6430388980538173536L;
	private ResourceChest effectResourceChest;
	private CouncilPrivilegeEffect councilPrivilegeEffect;
	
	/**
	 * class constructor.
	 *
	 * @param effectResourceChest it contains all the resources that a player gets as the
	 *                            result of the effect.
	 */
	public InstantResourcesEffect(ResourceChest effectResourceChest){
		this.effectResourceChest = effectResourceChest;
	}	
	
	/**
	 * Instantiates a new instant resources effect.
	 *
	 * @param effectResourceChest the effect resource chest
	 * @param councilPrivilegeEffect the council privilege effect
	 */
	public InstantResourcesEffect(ResourceChest effectResourceChest,CouncilPrivilegeEffect councilPrivilegeEffect){
		this.effectResourceChest = effectResourceChest;
		this.councilPrivilegeEffect=councilPrivilegeEffect;
	}	

	public void applyEffect(Player p) {
		p.addResources(effectResourceChest);
		if(councilPrivilegeEffect!=null){
			councilPrivilegeEffect.applyEffect(p);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("You gain:");
		builder.append(effectResourceChest.toString());
		
		return builder.toString();
	}

	@Override
	public void disapplyEffect(Player p) {}
}
