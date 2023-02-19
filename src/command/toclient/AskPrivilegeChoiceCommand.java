package command.toclient;

import client.ClientCommandHandler;
import model.resource.ResourceChest;

import java.util.ArrayList;

/**
 * This command asks the player what type of reward they want to get from a Privilege Choice
 */
public class AskPrivilegeChoiceCommand extends ServerToClientCommand {
    private static final long serialVersionUID = 7880502444669115290L;
    private int numberOfPrivilege;
    private ArrayList<ResourceChest> privilegeResources;

    public AskPrivilegeChoiceCommand(int numberOfPrivilege, ArrayList<ResourceChest> privilegeResources){
        this.numberOfPrivilege = numberOfPrivilege;
        this.privilegeResources = privilegeResources;
    }

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);

    }

    public int getNumberOfPrivilege() {
        return numberOfPrivilege;
    }

    public ArrayList<ResourceChest> getPrivilegeResources(){
        return privilegeResources;
    }

}
