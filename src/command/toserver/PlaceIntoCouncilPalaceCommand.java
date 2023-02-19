package command.toserver;

import server.ServerCommandHandler;

/**
 * the Client uses this command to place a pawn in the council palace
 */
public class PlaceIntoCouncilPalaceCommand extends ClientToServerCommand {
	private static final long serialVersionUID = -364724017878574034L;
	private String familyMember; //the color
	private int paidServants;

	public PlaceIntoCouncilPalaceCommand(String familyMember, int paidServants){
		this.familyMember = familyMember; 
		this.paidServants = paidServants;
	}
	
	public void processCommand(ServerCommandHandler serverCommandHandler) {
		serverCommandHandler.applyCommand(this);
		
	}

	public String getFamilyMember() {
		return familyMember;
	}

	public int getPaidServants() {
		return paidServants;
	}
}
