package command.toclient;

import client.ClientCommandHandler;
import model.Player;

/**
 * This command has to be sent only to the client representing the same player as the one in the field.
 */
public class PlayerStatusChangeCommand extends ServerToClientCommand {
	private static final long serialVersionUID = 8622584838091071912L;
	private Player player;

	public PlayerStatusChangeCommand(Player player) {
		this.player=player;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}

	public Player getPlayer() {
		return player;
	}

}
