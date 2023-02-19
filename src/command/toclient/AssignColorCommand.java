package command.toclient;

import client.ClientCommandHandler;

/**
 * Server assigns the color to a player through this command
 */
public class AssignColorCommand extends ServerToClientCommand {
    private static final long serialVersionUID = 7572217227580448284L;
    private String color;

    public AssignColorCommand(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);
    }

}
