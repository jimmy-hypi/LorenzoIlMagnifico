package model.excommunicationtile;

import java.util.ArrayList;
import java.util.Iterator;

import model.Player;
import model.effect.Effect;
import model.resource.Resource;
import model.resource.ResourceChest;

/**
 * The Class ResourceMalusEffect.
 */
public class ResourceMalusEffect extends Effect{
	private static final long serialVersionUID = -2781912671942166597L;
	ArrayList<Resource> resources;
	public ResourceMalusEffect(ArrayList<Resource> resources){
		this.resources=resources;
	}

	@Override
	public void applyEffect(Player player) {
		ResourceChest chest = new ResourceChest(0,0,0,0,0,0,0);
		for (Resource r : this.resources){
			chest.addResource(r);
		}
		player.getBonuses().setResourceMalus(resources);
	}

	@Override
	public String toString() {
		StringBuilder s=new StringBuilder();
		Iterator<Resource> iterator= resources.iterator();
		s.append("Every time you get ");
		
		while(iterator.hasNext()){
			s.append(iterator.next().getResourceType().toString().toLowerCase());
		if(iterator.hasNext())s.append(" or");
		}
	
		s.append(" from an action space or a development card you get ");
		iterator=resources.iterator();
		while(iterator.hasNext()){
			s.append(iterator.next().toString());
		if(iterator.hasNext())s.append(" and ");
		}
		s.append(" less");
		
		return s.toString();
	}
}
