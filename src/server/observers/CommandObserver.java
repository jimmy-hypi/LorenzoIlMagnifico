package server.observers;

import command.toserver.ClientToServerCommand;

public interface CommandObserver {

    public void notifyNewCommand(ClientToServerCommand command);
}
