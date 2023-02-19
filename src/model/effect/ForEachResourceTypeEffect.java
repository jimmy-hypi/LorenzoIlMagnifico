package model.effect;

import model.Player;
import model.resource.Resource;
import model.resource.ResourceChest;

/**
 * This effect gives n resources for each m other resources.
 */
public class ForEachResourceTypeEffect extends Effect{
	private static final long serialVersionUID = 4487719962938874499L;
	private Resource givenResource;
	private Resource toStringResource;  //This is used by the toString method to store a copy of the original givenResource that will be modified
	private Resource foreachResource;

	public ForEachResourceTypeEffect(Resource givenResource, Resource foreachResource){
		this.givenResource = givenResource;
		this.toStringResource = givenResource;
		this.foreachResource = foreachResource;
	}

	private Resource calculateResource(Player player){
		int leftFactor = givenResource.getAmount();
		//based on the dynamic type of the foreacheResource it will set the right
		//factor to the "int casted" result of the division between the amount of
		//the resource held by the player and the amount of the passed resource
		int rightFactor = (int) player.getResourceChest().getResourceInChest(foreachResource).getAmount() / foreachResource.getAmount();
		
		this.givenResource.setAmount(leftFactor * rightFactor);
		return givenResource;
	}

	public void applyEffect(Player player) {
		calculateResource(player);
		ResourceChest rs=new ResourceChest();
		rs.addResource(givenResource);
		player.addResources(rs);
	}

	@Override
	public String toString() {
		return toStringResource.toString() + " for each" + foreachResource.toString();
	}
}
