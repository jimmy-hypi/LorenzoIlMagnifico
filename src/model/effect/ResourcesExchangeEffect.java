package model.effect;

import model.Player;

/**
 * The Class ResourcesExchangeEffect represents a type of effect which can be found only in
 * a Building Card and can only be activated when the player activates a production
 * The exchange gives the player the possibility to pay a certain amount of up to three
 * resources type in order to receive a certain amount of up to two resources type
 * This effect also includes the possibility to have a XOR choice on the exchange .
 */

public class ResourcesExchangeEffect extends Effect {
	private static final long serialVersionUID = 2825814236559993863L;
	private AtomicExchangeEffect normalExchangeEffect;
	private AtomicExchangeEffect alternativeExchangeEffect;

	public ResourcesExchangeEffect(AtomicExchangeEffect normalExchangeEffect,
			AtomicExchangeEffect alternativeExchangeEffect) {
		super();
		this.normalExchangeEffect = normalExchangeEffect;
		this.alternativeExchangeEffect = alternativeExchangeEffect;
	}

	private void applyEffect(AtomicExchangeEffect chosenExchangeEffect,Player player){
		chosenExchangeEffect.applyEffect(player);
	}

	public boolean hasAlternativeEffect(){
		return alternativeExchangeEffect!=null;
	}

	public String getNormalEffectToString(){
		return this.normalExchangeEffect.toString();
	}

	public String getAlternativeEffectToString(){
		return this.alternativeExchangeEffect.toString();
	}
	
	
	/**
	 * method to apply the chosen exchange alternative, if choice is 1,it applies the normal effect
	 * 	(the first in the card image) otherwise it applies the second one
	 * 	we might change the 1 and 2 notation to a better one like using constants or an Enumeration.
	 *
	 * @param choice the choice
	 * @param player the player
	 */
	public void applyEffect(int choice, Player player) {
		if(choice==1){
			applyEffect(normalExchangeEffect,player);
		}
		else if(choice==2){
			applyEffect(alternativeExchangeEffect,player);
		}
	}

	@Override
	public void applyEffect(Player player) {
		applyEffect(normalExchangeEffect,player);
	}

	@Override
	public String toString() {
		if(alternativeExchangeEffect!=null)
		return normalExchangeEffect.toString()+
				" or"+alternativeExchangeEffect.toString();
		else return normalExchangeEffect.toString();
	}
}
