package model.effect;


import java.io.Serializable;

import model.Player;

/**
 * The abstract class Effect, it only has one method: applyEffect
 */
public abstract class Effect implements Serializable {
	private static final long serialVersionUID = 2481175952685522509L;

	public abstract void applyEffect(Player player);
}