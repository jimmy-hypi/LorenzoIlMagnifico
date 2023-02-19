package network;

import network.rmi.ClientRMIInterface;

/**
 * Factory pattern to generate NetworkInterface implementors.
 */
public class NetworkInterfaceFactory {
    public static NetworkInterface getNetworkInterface(int choice){
        switch(choice){
            case 2: return new ClientRMIInterface();
            default: return new ClientSocketInterface();
        }
    }
}
