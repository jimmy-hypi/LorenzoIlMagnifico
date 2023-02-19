package server;

import command.toclient.*;
import command.toserver.*;
import exception.NotApplicableException;
import model.FamilyMember;
import model.Match;
import model.Player;
import model.action.*;
import model.area.Floor;
import model.resource.Servant;
import server.controller.MatchHandler;
import server.observers.CommandObserver;

/**
 * Client to server commands handler:
 * Manages the client command requests.
 */
public class ServerCommandHandler implements CommandObserver {

    private MatchHandler handler;
    private Match match;

    public ServerCommandHandler(MatchHandler matchHandler, Match match) {
        this.handler = matchHandler;
        this.match = match;
    }

    public void applyCommand(PlaceIntoMarketCommand placeIntoMarketCommand) throws NotApplicableException {
        try {
            FamilyMember familyMember;
            familyMember = handler.getCurrentPlayer().getFamilyMember(placeIntoMarketCommand.getFamilyMember());

            try {
                if (familyMember == null) {
                    throw new NotApplicableException("You had alredy Used this family member");
                }
                handler.applyAction(new MarketAction(familyMember,
                        match.getBoard().getMarket().getMarketActionSpace(placeIntoMarketCommand.getActionSpace()),
                        placeIntoMarketCommand.getPaidServants()));
            } catch (NotApplicableException e) {
                handler.sendToCurrentPlayer(new InvalidActionCommand(e.getNotApplicableCode()));
                handler.sendToCurrentPlayer(new AskMoveCommand());
            }
        } catch (Exception e) {
            handler.sendToCurrentPlayer(new InvalidCommand());
            handler.sendToCurrentPlayer(new AskMoveCommand());
        }
    }

    public void applyCommand(PlaceIntoCouncilPalaceCommand placeIntoCouncilPalaceCommand) {
        try {
            FamilyMember familyMember = handler.getCurrentPlayer()
                    .getFamilyMember(placeIntoCouncilPalaceCommand.getFamilyMember());
            try {
                if (familyMember == null) {
                    throw new NotApplicableException("You had already Used this family member");
                }
                handler.applyAction(new CouncilPalaceAction(familyMember, match.getBoard().getCouncilPalace(),
                        placeIntoCouncilPalaceCommand.getPaidServants()));
            } catch (NotApplicableException e) {
                handler.sendToCurrentPlayer(new InvalidActionCommand(e.getNotApplicableCode()));
                handler.sendToCurrentPlayer(new AskMoveCommand());
            }
        } catch (Exception e) {
            handler.sendToCurrentPlayer(new InvalidCommand());
            handler.sendToCurrentPlayer(new AskMoveCommand());
        }
    }

    public void applyCommand(TakeCardCommand takeCardCommand) {
        try {
            try {
                Action action = calculateTakeCardAction(takeCardCommand);
                handler.applyAction(action);
            } catch (NotApplicableException e) {
                handler.sendToCurrentPlayer(new InvalidActionCommand(e.getNotApplicableCode()));
                handler.sendToCurrentPlayer(new AskMoveCommand());
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendToCurrentPlayer(new InvalidCommand());
            handler.sendToCurrentPlayer(new AskMoveCommand());
        }
    }

    public void applyCommand(ProductionCommand command) {
        try{
            handler.saveProductionParams(command);
            handler.sendToCurrentPlayer(new ChooseProductionExchangeEffectsCommand(match.getCurrentPlayerProductionChoices(
                    command.getFamilyMember(), command.getActionSpace(), command.getPaidServants())));
        }catch(Exception e){
            e.printStackTrace();
            handler.sendToCurrentPlayer(new InvalidCommand());
            handler.sendToCurrentPlayer(new AskMoveCommand());
        }
    }

    private Action calculateTakeCardAction(TakeCardCommand takeCardCommand) throws NotApplicableException {
        Player player = match.getCurrentPlayer();
        FamilyMember familyMember = player.getFamilyMember(takeCardCommand.getFamilyMember());
        if (familyMember == null) {
            throw new NotApplicableException("you don't have that family member");
        } else {
            Floor floor = match.getFloor(takeCardCommand.getCardType(), takeCardCommand.getFloor());

            return new TakeCardAction(familyMember, floor, new Servant(takeCardCommand.getPaidServants()));
        }
    }

    public void applyCommand(RequestClosureCommand requestClosureCommand) {
        handler.clientClosedTheGame(requestClosureCommand.getPlayerColor());
    }

    public void applyCommand(ChosenPrivilegeCommand chosenPrivilegeCommand) {
        handler.addPrivilegeResources(chosenPrivilegeCommand.getChoice());
    }

    @Override
    public void notifyNewCommand(ClientToServerCommand command) {
        command.processCommand(this);
    }

    public void applyCommand(DiscardLeaderCardCommand discardLeaderCardCommand) {
        handler.discardLeaderCard(discardLeaderCardCommand.getLeaderName());
    }

    public void applyCommand(ActivateLeaderCardCommand activateLeaderCardCommand) throws NotApplicableException {
        if(handler.getCurrentPlayer().getLeaderCards().containsKey(activateLeaderCardCommand.getLeaderName())
                && handler.isLeaderCardActivable(activateLeaderCardCommand.getLeaderName())){
            handler.getCurrentPlayer().activateLeaderCard(activateLeaderCardCommand.getLeaderName());
            handler.sendToCurrentPlayer(new AskMoveCommand());
        }
        else {
            handler.sendToCurrentPlayer(new InvalidActionCommand("You don't have that leader requirements to activate it!"));
            handler.sendToCurrentPlayer(new AskMoveCommand());
        }
    }
    public void applyCommand(ChurchSupportCommand churchSupportCommand) {
        handler.handleChurchSupportDecision(churchSupportCommand.getPlayerColor(), churchSupportCommand.getDecision());
    }
    public void applyCommand(SendCredentialsCommand command, ClientHandler clientHandler) {
        handler.handleCredentials(command.getUsername(), command.getPassword(), clientHandler);
    }

    public void applyCommand(ChosenLeaderCardCommand command) {
        handler.handleLeaderChoice(command.getName(), command.getPlayerColor());
    }

    public void applyCommand(PlayerMoveCommand playerMoveCommand, ClientHandler clientHandler) {
        // handler.handlePlayerMove(playerMoveCommand.getMove(), clientHandler);
    }

    public void applyCommand(FinishRoundCommand finishRoundCommand) {
        handler.finishRound();
    }

    public void applyCommand(InvalidInputCommand invalidInputCommand) {
        handler.handleInvalidCommand();
    }

    public void applyCommand(ProductionActivationCommand productionActivationCommand) {
        handler.handleProductionActivation(productionActivationCommand);
    }

    public void applyCommand(SendCredentialsCommand sendCredentialsCommand) {
        handler.handleAuthenticationRequest(sendCredentialsCommand.getUsername(),
                sendCredentialsCommand.getPassword(),sendCredentialsCommand.getPlayerColor());
    }

    public void applyCommand(ChatMessageClientCommand chatMessageClientCommand) {
        handler.sendToAllPlayers(new ChatMessageServerCommand(chatMessageClientCommand.getMessage()));
    }

    public void applyCommand(HarvestCommand harvestCommand) {
        try{
            FamilyMember member = handler.getCurrentPlayer().getFamilyMember(harvestCommand.getFamilyMember());

            if (harvestCommand.getActionSpace() == 1) {
                try {
                    handler.applyAction(new IndustrialAction(member, match.getBoard().getHarvestArea(),
                            match.getBoard().getHarvestArea().getSingleActionSpace(), harvestCommand.getPaidServants()));
                } catch (NotApplicableException e) {
                    handler.sendToCurrentPlayer(new InvalidActionCommand(e.getNotApplicableCode()));
                    handler.sendToCurrentPlayer(new AskMoveCommand());
                }
            } else {
                try {
                    handler.applyAction(new IndustrialAction(member, match.getBoard().getHarvestArea(),
                            match.getBoard().getHarvestArea().getMultipleActionSpace(), harvestCommand.getPaidServants()));
                } catch (NotApplicableException e) {
                    handler.sendToCurrentPlayer(new InvalidActionCommand(e.getNotApplicableCode()));
                    handler.sendToCurrentPlayer(new AskMoveCommand());
                }
            }
        }catch(Exception e){
            handler.sendToCurrentPlayer(new InvalidCommand());
            handler.sendToCurrentPlayer(new AskMoveCommand());
        }
    }


    public void applyCommand(SatanChoiceCommand satanChoice) {
        handler.handleSatanChoice(satanChoice.getColor());

    }


    public void applyCommand(ReconnectionAnswerCommand reconnectionAnswerCommand) {

    }
}
