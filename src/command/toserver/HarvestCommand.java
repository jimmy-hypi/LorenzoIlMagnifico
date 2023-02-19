package command.toserver;

import server.ServerCommandHandler;

/**
 * This class manages the client decision to make a harvest action
 */
public class HarvestCommand extends ClientToServerCommand {
	private static final long serialVersionUID = 7415867579709431260L;
	private String familyMember;
	private int paidServants;
	private int actionSpace;

	public HarvestCommand(String familyMember, int paidServants, int actionSpace){
		this.familyMember = familyMember;
		this.paidServants = paidServants;
		this.actionSpace = actionSpace;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
	}

	public String getFamilyMember() {
		return familyMember;
	}

	public int getPaidServants() {
		return paidServants;
	}

	public int getActionSpace() {
		return actionSpace;
	}
}
