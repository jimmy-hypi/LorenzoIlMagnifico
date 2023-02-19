package command.toclient;

import client.ClientCommandHandler;

/**
 * Server notifies the client they authenticated correctly
 */
public class AuthenticatedCorrectlyCommand extends ServerToClientCommand {

    private static final long serialVersionUID = 2881110350664317902L;

    private String username;

    public AuthenticatedCorrectlyCommand(String username) {
        this.username=username;
    }

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);
    }

    public String getUsername() {
        return username;
    }



}
