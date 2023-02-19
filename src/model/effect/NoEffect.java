package model.effect;

import model.Player;

/**
 * The Class NoEffect was needed in order not to check if a card's effect is null
 * before applying it
 */
public class NoEffect extends Effect {
	private static final long serialVersionUID = 7758404621897411258L;

	/* 
	 * It's being left blank on purpose
	 */
	@Override
	public void applyEffect(Player player) {}

	@Override
	public String toString() {
		return "No effect";
	}
}
