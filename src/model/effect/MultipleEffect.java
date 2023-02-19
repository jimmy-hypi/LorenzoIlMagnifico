package model.effect;

import model.Player;

/**
 * This effect takes two effects and apply them in a row.
 */
public class MultipleEffect extends Effect {
	private static final long serialVersionUID = 7018409853938720869L;
	Effect firstEffect;
	Effect secondEffect;

	public MultipleEffect(Effect firstEffect, Effect secondEffect){
		this.firstEffect = firstEffect;
		this.secondEffect = secondEffect;
	}

	public void applyEffect(Player p) {
		firstEffect.applyEffect(p);
		secondEffect.applyEffect(p);
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(firstEffect.toString() + "\n");
		string.append("\t\t  and " + secondEffect.toString());
		return string.toString();
	}
}
