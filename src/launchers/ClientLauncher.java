package launchers;

import client.ClientCommandHandler;
import client.ClientController;
import network.NetworkInterface;
import network.NetworkInterfaceFactory;
import view.UserInterface;
import view.UserInterfaceFactory;

import java.util.Scanner;

/**
 * Client launcher:
 * Asks the user the type of connection and UI and instantiates respective objects and connects
 */
public class ClientLauncher {
    static Scanner i;

    public static void main(String[] args){

        NetworkInterface networkInterface;
        UserInterface userInterface;
        ClientCommandHandler handler;
        ClientController controller;

        i = new Scanner(System.in);
        System.out.println("Select the Connection mode: \n1 - Socket\n2 - RMI");

        int choice = i.nextInt();

        networkInterface = NetworkInterfaceFactory.getNetworkInterface(choice);
        System.out.println("Choose user interface: \n1 - Command Line Interface\n2 - Graphic User Interface");

        choice = i.nextInt();
        controller = new ClientController(networkInterface);
        userInterface = UserInterfaceFactory.getUserInterface(choice, controller);
        handler = new ClientCommandHandler(userInterface, networkInterface);

        controller.setUserInterface(userInterface);
        controller.setCommandHandler(handler);

        try {
            networkInterface.connect();
        } catch (Exception e) {
            System.err.println("Unable to connect to server");
            e.printStackTrace();
        }
        networkInterface.addCommandObserver(handler);
    }
}
