package command.toserver;

import server.ServerCommandHandler;

/**
 * This command was needed to treat in a different way the production and its relative
 * resource exchange choices.
 */
public class ProductionCommand extends ClientToServerCommand {
	private static final long serialVersionUID = -1936330748404906687L;
	private String familyMember;
	private int paidServants;
	private int actionSpace;

	public ProductionCommand(String familyMember, int paidServants, int actionSpace){
		this.familyMember = familyMember;
		this.paidServants = paidServants;
		this.actionSpace = actionSpace;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
	}

	public String getFamilyMember(){
		return familyMember;
	}

	public int getPaidServants() {
		return paidServants;
	}

	public int getActionSpace() {
		return actionSpace;
	}
}
