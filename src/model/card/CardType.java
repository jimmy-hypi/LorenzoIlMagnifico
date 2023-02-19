package model.card;

/**
 * The Enum CardType representing the type of a development card
 */
public enum CardType {
	TERRITORY,
	BUILDING,
	CHARACTER,
	VENTURE,
	ANY; // Mirko: I modified the order that HAS to stay like this (conventions)

	/**
	 * Convert card type.
	 *
	 * @param cardType
	 *            the card type
	 * @return the card type
	 */
	public static CardType convertCardType(int cardType) {
		switch (cardType) {
		case 1:
			return TERRITORY;
		case 2:
			return BUILDING;
		case 3:
			return CHARACTER;
		case 4:
			return VENTURE;
		default:
			return ANY;
		}
	}
}
