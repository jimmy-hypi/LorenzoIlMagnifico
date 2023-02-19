package command.toclient;

import client.ClientCommandHandler;
import model.area.Board;

/**
 * This is the command used to process the board changes by refreshing it
 */
public class RefreshBoardCommand extends ServerToClientCommand {
	private static final long serialVersionUID = 5062730484649123660L;
	private Board board;

	public RefreshBoardCommand(Board board) {
		super();
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}

}
