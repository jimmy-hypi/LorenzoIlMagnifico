package command.toclient;

import client.ClientCommandHandler;

/**
 * The command that asks the player if they want to pay coins to avoid excommunication.
 */
public class AskForExcommunicationPaymentCommand extends ServerToClientCommand {
    private static final long serialVersionUID = 2282290893400038423L;
    private String excommunicationEffect;
    public AskForExcommunicationPaymentCommand(String excommunicationEffect) {
        this.excommunicationEffect = excommunicationEffect;
    }
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);
    }
    public String getExcommunicationEffect() {
        return excommunicationEffect;
    }
}
