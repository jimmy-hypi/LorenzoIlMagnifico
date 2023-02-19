package view;

import client.ClientController;
import view.cli.CommandLineInterface;
import view.gui.GraphicalUserInterface;

/**
 * A factory for creating UserInterface objects.
 */
public class UserInterfaceFactory {
    /**
     * Gets the user interface based on a choioce (as integer)
     */
    public static UserInterface getUserInterface(int choice, ClientController gameController){
        switch(choice){
            case 1: return new CommandLineInterface(gameController);
            default: return new GraphicalUserInterface(gameController);
        }
    }
}
