package model.excommunicationtile;

import model.Dice;
import model.FamilyMember;
import model.Player;
import model.effect.Effect;

/**
 * The Class ColoredFamiliarsVariationEffect.
 */
public class ColoredFamiliarsVariationEffect extends Effect{
	private static final long serialVersionUID = -3421157855376061112L;
	private int variation;

	public ColoredFamiliarsVariationEffect(int variation){
		this.variation = variation;
	}

	@Override
	public void applyEffect(Player player) {
		for(FamilyMember f : player.getFamilyMembers().values()){
			if(f.getDice() != Dice.NEUTRAL_DICE){
				f.addActionValueVariation(variation);
			}
		}
	}

	@Override
	public String toString() {
		return "Your colored family members' action variation is decreased by"+variation;
	}
}
