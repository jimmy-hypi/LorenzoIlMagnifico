package command.toclient;

import client.ClientCommandHandler;
import model.Player;

/**
 * The command to notify that another player has changed its status, this message
 * brings only information that every player can see from the game board
 *
 */
public class OpponentStatusChangeCommand extends ServerToClientCommand {
	private static final long serialVersionUID = 3016399299916946675L;
	Player maskedPlayer;

	public OpponentStatusChangeCommand(Player maskedClone) {
		this.maskedPlayer=maskedClone;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}

	public Player getMaskedPlayer() {
		return maskedPlayer;
	}
}
