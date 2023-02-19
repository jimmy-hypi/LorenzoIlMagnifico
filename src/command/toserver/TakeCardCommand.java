package command.toserver;

import model.card.CardType;
import server.ServerCommandHandler;

/**
 * the client uses this command to take a card from the game board and to place a pawn in the related floor
 */
public class TakeCardCommand extends ClientToServerCommand {
	private static final long serialVersionUID = -2549395717779803669L;
	private String familyMember;
	private int floor;
	private CardType cardType;
	private int paidServants;

	public TakeCardCommand(String familyMember, int floor, int paidServants, CardType cardType) {
		this.familyMember = familyMember;
		this.floor = floor;
		this.paidServants = paidServants;
		this.cardType = cardType;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);

	}

	public String getFamilyMember() {
		return familyMember;
	}

	public int getFloor() {
		return floor;
	}

	public int getPaidServants() {
		return paidServants;
	}

	public CardType getCardType() {
		return cardType;
	}
	
}
