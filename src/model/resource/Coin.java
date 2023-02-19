package model.resource;

/**
 * The Class Coin.
 */
public class Coin extends Resource {
	private static final long serialVersionUID = 4990887197340403545L;

	public Coin(int amount) {
		super(ResourceType.COIN, amount);
	}

	@Override
	public String toString() {
		if (this.getAmount() == 1)
			return super.toString() + " coin";
		else
			return super.toString() + " coins";
	}
}
