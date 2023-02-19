package command.toclient;

import client.ClientCommandHandler;
import model.Period;

/**
 * The Command to notify the Client that a turn has been initialized
 */
public class InitializeTurnCommand extends ServerToClientCommand {

	private static final long serialVersionUID = 1569847639899896757L;
	private Period period;
	private int turn;

	public InitializeTurnCommand(Period period, int turn) {
	
		this.period = period;
		this.turn = turn;
	}

	public Period getPeriod() {
		return period;
	}

	public int getTurn() {
		return turn;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}

}
