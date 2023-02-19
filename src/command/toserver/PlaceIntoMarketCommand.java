package command.toserver;


import exception.NotApplicableException;
import server.ServerCommandHandler;

/**
 * the command used to place a pawn into one of the market areas
 */
public class PlaceIntoMarketCommand extends ClientToServerCommand{

	private static final long serialVersionUID = 5666906861277528755L;
	private String familyMember; // orange,black,white,neutral
	private String actionSpace; // Left to right: 1-2-3-4
	private int paidServants;

	public PlaceIntoMarketCommand(String familyMember, String actionSpace, int paidServants){
		this.familyMember = familyMember;
		this.actionSpace = actionSpace;
		this.paidServants = paidServants;
	}

	@Override
	public void processCommand(ServerCommandHandler serverCommandHandler) {
		try {
			serverCommandHandler.applyCommand(this);
		} catch (NotApplicableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFamilyMember() {
		return familyMember;
	}

	public String getActionSpace() {
		return actionSpace;
	}

	public int getPaidServants() {
		return paidServants;
	}
}
