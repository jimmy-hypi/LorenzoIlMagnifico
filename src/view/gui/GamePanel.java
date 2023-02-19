package view.gui;

import model.Dice;
import model.FamilyMember;
import model.Player;
import model.area.Board;
import model.card.CardType;
import model.card.LeaderCard;
import model.resource.Resource;
import model.resource.ResourceType;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private Clip clip;
    private static final Color BACKGROUND_PANELS_COLOR = new Color(204, 153, 51);
    private static final Color CHAT_BACKGROUND_COLOR = new Color(245, 200, 86);
    protected static Dimension screenDim;
    /** Can be: draft, none, activate, discard. */
    private String leaderState = "draft";
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();
    private JTextField textField;
    private BoardPanel boardPanel;
    private JButton sendChat;
    private JButton showPersonalBoard;
    private JButton strategyEditorButton;
    private JButton quitGameButton;
    private JButton showLeaderCardsButton;
    private MarketButton firstMarket;
    private MarketButton secondMarket;
    private MarketButton thirdMarket;
    private MarketButton fourthMarket;
    private CouncilButton councilButton;
    private SingleHarvestButton singleHarvestButton;
    private SingleProductionButton singleProductionButton;
    private MultipleHarvestButton multipleHarvestButton;
    private MultipleProductionButton multipleProductionButton;
    private JToggleButton musicToggle;
    private JTextArea textArea;
    private PlayerResources playerResources;
    private final Font buttonsFont = new Font("SansSerif", Font.BOLD, 16);
    private java.util.List<CardButton> cards;
    private Container actionContentPane;
    private ActionPanel actionPanel;
    private ChooseAction chooseAction;
    private LeadersPanel leadersPanel;
    private StrategyEditor strategyEditor;
    private ProductionChoices productionChoices;
    private EndOrDiscardPanel endOrDiscardPanel;
    private ChoosePrivilegePanel choosePrivilegePanel;
    private ChooseExcommunicationPanel chooseExcommunicationPanel;
    private SatanPanel satanPanel;
    private LeadersPanel draftPanel;
    private AskAuthenticationPanel askAuthenticationPanel;
    private ArrayList<String> actionConstructor;
    private GraphicalUserInterface GUI;
    private Component currentActionPanel;
    private List<OrderMarkerDisk> orderMarkers;
    private Map<String, VictoryPointMarkerDisk> victoryMarkers;
    private Map<String, MilitaryPointMarkerDisk> militaryMarkers;
    private Map<String, FaithPointMarkerDisk> faithMarkers;
    private Map<String, FamilyMemberPawn> familiars;
    private Map<String, DicePanel> dices;
    private int currentNumberOfPrivilege;
    private ArrayList<String> chosenPrivileges;

    public GamePanel(String playerColor, int numPlayers) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        chosenPrivileges = new ArrayList<String>();

        cards = new ArrayList<CardButton>();

        screenDim = toolkit.getScreenSize();
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(new BorderLayout(0, 0));

        // BOARD
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.WEST);
        boardPanel.setLayout(null);
        // RIGHT SCROLLBAR
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(screenDim.width - boardPanel.getPreferredSize().width, 500));
        add(scrollPane, BorderLayout.EAST);
        // PANEL CONTAINED IN THE SCROLLBAR
        JPanel rightScrollbarContainer = new JPanel();
        rightScrollbarContainer
                .setMaximumSize(new Dimension(screenDim.width - boardPanel.getPreferredSize().width, 20000));
        rightScrollbarContainer
                .setPreferredSize(new Dimension(screenDim.width - boardPanel.getPreferredSize().width, 1000));
        scrollPane.setViewportView(rightScrollbarContainer);
        rightScrollbarContainer.setLayout(new BoxLayout(rightScrollbarContainer, BoxLayout.Y_AXIS));
        // CHAT INTERNALFRAME
        JInternalFrame internalFrame = new JInternalFrame("Chat");
        internalFrame.setBackground(SystemColor.controlHighlight);
        rightScrollbarContainer.add(internalFrame);
        internalFrame.setPreferredSize(
                new Dimension(screenDim.width - boardPanel.getPreferredSize().width, screenDim.height / 3));
        internalFrame.setMaximumSize(
                new Dimension(screenDim.width - boardPanel.getPreferredSize().width, screenDim.height / 3));
        // CHAT TEXTAREA
        textArea = new JTextArea();
        textArea.setMargin(new Insets(1, 1, 1, 1));
        textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        textArea.setBackground(CHAT_BACKGROUND_COLOR);
        textArea.setFont(new Font("Consolas", 0, 20));
        // SCROLLPANE FOR CHAT
        JScrollPane scrPane = new JScrollPane(textArea);
        Border border = BorderFactory.createLoweredBevelBorder();
        scrPane.setBorder(border);

        JScrollBar vertical = scrPane.getVerticalScrollBar();
        vertical.setPreferredSize(new Dimension(0, 0));

        internalFrame.getContentPane().add(scrPane, BorderLayout.CENTER);

        JPanel chatInputPanel = new JPanel();
        internalFrame.getContentPane().add(chatInputPanel, BorderLayout.SOUTH);
        chatInputPanel.setLayout(new BorderLayout(0, 0));
        // CHAT INPUT BUTTON
        sendChat = new JButton("Send");
        sendChat.setForeground(new Color(255, 255, 255));
        sendChat.setBackground(new Color(102, 51, 51));
        sendChat.setFont(buttonsFont);
        sendChat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendChat.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        chatInputPanel.add(sendChat, BorderLayout.EAST);
        // TEXTFIELD
        JPanel textFieldOuterPanel = new JPanel();
        textFieldOuterPanel.setBackground(new Color(160, 82, 45));
        chatInputPanel.add(textFieldOuterPanel, BorderLayout.CENTER);
        textFieldOuterPanel.setLayout(new BorderLayout(0, 0));

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, 26));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldOuterPanel.add(textField);
        textField.setColumns(50);
        // PLAYER RESOURCES PANEL
        if (!playerColor.equals("black")) {
            playerResources = new PlayerResources(screenDim.width - boardPanel.getPreferredSize().width, playerColor);
            rightScrollbarContainer.add(playerResources);
        }
        // ACTIONS INTERNAL FRAME
        JInternalFrame actionsInternalFrame = new JInternalFrame("Game actions");

        if (playerColor.equals("black"))
            actionsInternalFrame.setTitle("Satan panel");
        if (!playerColor.equals("black"))
            actionsInternalFrame.getContentPane().setBackground(new Color(160, 82, 45));
        else
            actionsInternalFrame.getContentPane().setBackground(new Color(0, 0, 0));

        actionsInternalFrame.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        actionsInternalFrame.setBounds(new Rectangle(0, 0, 500, 0));

        rightScrollbarContainer.add(actionsInternalFrame);
        actionsInternalFrame.getContentPane().setLayout(new BorderLayout(0, 0));

        actionContentPane = actionsInternalFrame.getContentPane();
        // BUTTONS: Market, council, production and harvest
        if (!playerColor.equals("black")) {
            firstMarket = new MarketButton(1);
            firstMarket.addActionListener(this);
            boardPanel.add(firstMarket);

            secondMarket = new MarketButton(2);
            secondMarket.addActionListener(this);
            boardPanel.add(secondMarket);

            if (numPlayers > 3) {
                thirdMarket = new MarketButton(3);
                thirdMarket.addActionListener(this);
                boardPanel.add(thirdMarket);

                fourthMarket = new MarketButton(4);
                fourthMarket.addActionListener(this);
                boardPanel.add(fourthMarket);
            }

            councilButton = new CouncilButton();
            councilButton.addActionListener(this);
            boardPanel.add(councilButton);

            singleHarvestButton = new SingleHarvestButton();
            singleHarvestButton.addActionListener(this);
            boardPanel.add(singleHarvestButton);

            singleProductionButton = new SingleProductionButton();
            singleProductionButton.addActionListener(this);
            boardPanel.add(singleProductionButton);

            if (numPlayers > 2) {
                multipleHarvestButton = new MultipleHarvestButton();
                multipleHarvestButton.addActionListener(this);
                boardPanel.add(multipleHarvestButton);

                multipleProductionButton = new MultipleProductionButton();
                multipleProductionButton.addActionListener(this);
                boardPanel.add(multipleProductionButton);
            }
        }
        // ACTION PANELS
        if (!playerColor.equals("black")) {
            actionPanel = new ActionPanel(this, playerColor);
            if (!playerColor.equals("black"))
                actionPanel.setBackground(BACKGROUND_PANELS_COLOR);
            else
                actionPanel.setBackground(new Color(0, 0, 0));
            actionPanel.setVisible(false);

            chooseAction = new ChooseAction(this);
            chooseAction.setBackground(BACKGROUND_PANELS_COLOR);
            chooseAction.setVisible(false);

            strategyEditor = new StrategyEditor();
            strategyEditor.setVisible(false);

            endOrDiscardPanel = new EndOrDiscardPanel(this);
            endOrDiscardPanel.setBackground(BACKGROUND_PANELS_COLOR);
            endOrDiscardPanel.setVisible(false);

            choosePrivilegePanel = new ChoosePrivilegePanel(screenDim.width - boardPanel.getPreferredSize().width,
                    this);
            choosePrivilegePanel.setBackground(BACKGROUND_PANELS_COLOR);
            choosePrivilegePanel.setVisible(false);

            chooseExcommunicationPanel = new ChooseExcommunicationPanel(this);
            chooseExcommunicationPanel.setBackground(BACKGROUND_PANELS_COLOR);
            chooseExcommunicationPanel.setVisible(false);

            leadersPanel = new LeadersPanel();
            leadersPanel.setBackground(BACKGROUND_PANELS_COLOR);
            leadersPanel.setVisible(false);

            draftPanel = new LeadersPanel();
            draftPanel.setBackground(BACKGROUND_PANELS_COLOR);
            draftPanel.setVisible(false);

            productionChoices = new ProductionChoices(this);
            productionChoices.setBackground(BACKGROUND_PANELS_COLOR);
            productionChoices.setVisible(false);

            askAuthenticationPanel = new AskAuthenticationPanel(this);
            askAuthenticationPanel.setBackground(BACKGROUND_PANELS_COLOR);
            askAuthenticationPanel.setVisible(false);
        }

        if (playerColor.equals("black")) {
            satanPanel = new SatanPanel(screenDim.width - boardPanel.getPreferredSize().width, this);
            satanPanel.setBackground(BACKGROUND_PANELS_COLOR);
            satanPanel.setVisible(false);
        }

        if (numPlayers == 5)
            this.placeSatanDisc();
        // FINAL BUTTONS PANEL
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(210, 180, 140));
        rightScrollbarContainer.add(buttonsPanel);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        if (!playerColor.equals("black")) {

            showLeaderCardsButton = new JButton("Show Leader Cards");
            showLeaderCardsButton.setFont(buttonsFont);
            showLeaderCardsButton.setForeground(new Color(255, 255, 255));
            showLeaderCardsButton.setBackground(new Color(102, 51, 51));
            showLeaderCardsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            showLeaderCardsButton.addActionListener(this);
            buttonsPanel.add(showLeaderCardsButton);

            showPersonalBoard = new JButton("Show Personal Board");
            showPersonalBoard.setFont(buttonsFont);
            showPersonalBoard.setBackground(new Color(102, 51, 51));
            showPersonalBoard.setForeground(new Color(255, 255, 255));
            showPersonalBoard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonsPanel.add(showPersonalBoard);

            strategyEditorButton = new JButton("Strategy editor");
            strategyEditorButton.setFont(buttonsFont);
            strategyEditorButton.setBackground(new Color(102, 51, 51));
            strategyEditorButton.setForeground(new Color(255, 255, 255));
            strategyEditorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            strategyEditorButton.addActionListener(this);
            buttonsPanel.add(strategyEditorButton);
        }

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("res/song.wav"));
            clip = AudioSystem.getClip();
            this.clip.open(audio);
        } catch (UnsupportedAudioFileException | IOException e1) {
            e1.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        musicToggle = new JToggleButton("Start music");
        musicToggle.setFont(new Font("SansSerif", Font.BOLD, 16));
        musicToggle.setBackground(new Color(102, 51, 51));
        musicToggle.setForeground(new Color(255, 255, 255));
        musicToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        musicToggle.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (musicToggle.isSelected()) {
                    musicToggle.setText("Stop music");
                    this.startMusic();
                } else {
                    musicToggle.setText("Start music");
                    this.stopMusic();
                }
            }
            private void stopMusic() {
                clip.stop();
            }
            private void startMusic() {
                clip.loop(1);
            }
        });
        buttonsPanel.add(musicToggle);

        quitGameButton = new JButton("Quit Game");
        quitGameButton.setFont(buttonsFont);
        quitGameButton.setBackground(new Color(20, 20, 0));
        quitGameButton.setForeground(new Color(255, 255, 255));
        quitGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        quitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.notifyCloseGame();
            }
        });
        buttonsPanel.add(quitGameButton);

        actionsInternalFrame.setVisible(true);
        internalFrame.setVisible(true);

        orderMarkers = new ArrayList<OrderMarkerDisk>();
        victoryMarkers = new HashMap<String, VictoryPointMarkerDisk>();
        militaryMarkers = new HashMap<String, MilitaryPointMarkerDisk>();
        faithMarkers = new HashMap<String, FaithPointMarkerDisk>();
        familiars = new HashMap<String, FamilyMemberPawn>();
        dices = new HashMap<String, DicePanel>();
        createDices();
    }

    public void addCard(int tower, int floor, int id, String descr) {
        if (tower == 1)
            tower = 2;
        else if (tower == 2)
            tower = 1;
        CardButton card = new CardButton(boardPanel.getPreferredSize(), tower, floor, id);
        card.addActionListener(this);
        card.setToolTipText(descr);
        boardPanel.add(card);
        cards.add(card);
    }

    public JButton getSendChat() {
        return sendChat;
    }

    public JButton getShowPersonalBoard() {
        return showPersonalBoard;
    }

    public String getAndDeleteChatInput() {
        String m = this.textField.getText();
        this.textField.setText("");
        return m;
    }

    public void addMessageToConsole(String message) {
        message = "\n" + message;
        this.textArea.append(message);
        this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
    }

    private void writeGameMessage(String string) {
        addMessageToConsole("\n<-GAME-> " + string + "\n");
    }

    public void setMarkerOrder(ArrayList<OrderMarkerDisk> markers) {
        for (OrderMarkerDisk m : markers) {
            paint(m.getGraphics());
            m.setAlignmentX((float) 782.23);
            m.setAlignmentY(792);
        }
    }

    public void setMarkerVictoryPoints() {}

    public void setMarkerMilitaryPoint() {}


    public void setChurchMarker() {}

    public void addResourceToPlayerStatus(Resource resourceInChest) {
        playerResources.refreshResource(resourceInChest);
    }

    public void addFamilyMembersToPlayerStatus(HashMap<model.Color, FamilyMember> familyMembers) {
        playerResources.refreshFamilyMembers(familyMembers);
    }

    public void removeCards() {
        cards.forEach(card -> boardPanel.remove(card));
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    private void showActionPanel(Component panel) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    setEveryoneInvisible(actionContentPane.getComponents());
                    actionContentPane.removeAll();
                    actionContentPane.add(panel);
                    actionContentPane.repaint();
                    panel.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setEveryoneInvisible(Component[] components) {
        for (int i = 0; i < components.length; i++)
            components[i].setVisible(false);
    }

    public void removeActionPanel() {
        this.actionContentPane.removeAll();
        actionContentPane.repaint();
    }

    public void showChooseAction() {
        this.currentActionPanel = chooseAction;
        this.showActionPanel(chooseAction);
    }

    public void setObserver(GraphicalUserInterface GUI) {
        this.GUI = GUI;
    }

    public void notifyActionClick() {
        writeGameMessage("Choose the family member and the amount of servants "
                + "you intend to use in this action, then press the area"
                + "you want to place your family member into");
        this.currentActionPanel = actionPanel;
        this.showActionPanel(actionPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof CardButton) {
            CardButton card = (CardButton) e.getSource();
            constructAction(card);
            this.GUI.notifyTakeCard(actionConstructor);

        } else if (e.getSource() == strategyEditorButton) {
            if (!strategyEditor.isVisible())
                showActionPanel(strategyEditor);
            else
                backToCurrentAction();

        } else if (e.getSource() instanceof MarketButton) {
            String market = ((MarketButton) e.getSource()).getName();
            constructAction(market);
            if (actionConstructor.size() == 4) {
                this.GUI.notifyMarketAction(actionConstructor);
            } else {
            }

        } else if (e.getSource() instanceof CouncilButton) {
            constructCouncilAction();
            this.GUI.notifyCouncilAction(actionConstructor);

        } else if (e.getSource() instanceof SingleHarvestButton) {
            addFamServToConstructor();
            actionConstructor.add("harvest");
            actionConstructor.add("1");
            this.GUI.notifyHarvest(actionConstructor);

        } else if (e.getSource() instanceof SingleProductionButton) {
            addFamServToConstructor();
            actionConstructor.add("production");
            actionConstructor.add("1");
            this.GUI.notifyProduction(actionConstructor);

        } else if (e.getSource() instanceof MultipleHarvestButton) {
            addFamServToConstructor();
            actionConstructor.add("harvest");
            actionConstructor.add("2");
            this.GUI.notifyHarvest(actionConstructor);

        } else if (e.getSource() instanceof MultipleProductionButton) {
            addFamServToConstructor();
            actionConstructor.add("production");
            actionConstructor.add("2");
            this.GUI.notifyProduction(actionConstructor);
        } else if (e.getSource() instanceof JLeaderCard) {
            JLeaderCard leader = (JLeaderCard) (e.getSource());
            this.removeActionPanel();
            leadersPanel.setVisible(false);

            if (leaderState.equals("draft")) {
                this.GUI.notifyChosenLeaderInDraft(leader.getLeaderName());
                draftPanel = new LeadersPanel();
                draftPanel.setBackground(BACKGROUND_PANELS_COLOR);
                draftPanel.setVisible(false);
            } else if (leaderState.equals("activate")) {
                this.GUI.notifyActivateLeader(leader.getLeaderName());
                leaderState = "none";
                leadersPanel.setVisible(false);

            } else if (leaderState.equals("discard")) {
                this.GUI.notifyDiscardLeader(leader.getLeaderName());
                leaderState = "none";
                leadersPanel.setVisible(false);

            }
        } else if (e.getSource() == showLeaderCardsButton) {
            if (!leadersPanel.isVisible()) {
                showActionPanel(leadersPanel);
            } else {
                backToCurrentAction();
            }
        }

    }

    private void addFamServToConstructor() {
        actionConstructor = new ArrayList<String>();
        String familyMember;
        String servants; // number

        familyMember = actionPanel.getFamilyMember();

        if (familyMember == "none") {
            invalidInputMessage("Select a family member");
            return;
        }

        servants = actionPanel.getServants();

        actionConstructor.add(familyMember);
        actionConstructor.add(servants);
    }

    private void constructCouncilAction() {
        addFamServToConstructor();

    }

    private void constructAction(String market) {
        actionConstructor = new ArrayList<String>();

        String familyMember;
        String servants; // number
        String actionSpace = market; // FIRST,SECOND,THIRD,FOURTH as the place
        // of the marker spot from the left to
        // the
        // right on the board
        familyMember = actionPanel.getFamilyMember();

        if (familyMember == "none") {
            invalidInputMessage("Select a family member");
            return;
        }

        servants = actionPanel.getServants();

        // I have to adapt the order because it was made that way in the
        // ClietController
        // order: 0-family member, 1-servants, 2-doesn't matter (actiontype),
        // 3-actionspace

        actionConstructor.add(familyMember);
        actionConstructor.add(servants);
        actionConstructor.add("market");
        actionConstructor.add(actionSpace);

    }

    private void backToCurrentAction() {
        if (currentActionPanel != null)
            showActionPanel(currentActionPanel);
        else {
            setEveryoneInvisible(this.actionContentPane.getComponents());
            this.actionContentPane.removeAll();
        }
    }

    private void constructAction(CardButton card) {
        // order; 0-member, 1-servants, 2-unused, 3-cardtype,4-floor
        actionConstructor = new ArrayList<String>();

        String familyMember;
        String floor; // number
        String servants; // number
        int tower;
        String cardType; // number
        familyMember = actionPanel.getFamilyMember();

        if (familyMember == "none") {
            invalidInputMessage("Select a family member");
            return;
        }

        floor = "" + card.getFloor();
        tower = card.getTower();

        if (tower == 1)
            tower = 2;
        else if (tower == 2)
            tower = 1;

        tower = tower + 1; // to use the conventions of CLI...

        servants = actionPanel.getServants();
        cardType = "" + tower; // scambio 2 torri centrali

        actionConstructor.add(familyMember);
        actionConstructor.add(servants);
        actionConstructor.add("takecard");
        actionConstructor.add(cardType);
        actionConstructor.add(floor);
    }

    private void invalidInputMessage(String string) {
        this.addMessageToConsole("--INVALID INPUT--: " + string);
    }

    public void notifyEndRound() {
        writeGameMessage("Your round has ended");
        this.removeActionPanel();
        GUI.notifyEndRound();
        currentActionPanel = null;
    }

    public void showEndOrDiscard() {
        this.currentActionPanel = endOrDiscardPanel;
        this.showActionPanel(endOrDiscardPanel);
    }

    public void showChoosePrivilege() {
        this.currentActionPanel = choosePrivilegePanel;
        this.showActionPanel(choosePrivilegePanel);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof JResource) {
            String chosenP = ((JResource) e.getSource()).getName();
            chosenPrivileges.add(chosenP);
            if (currentNumberOfPrivilege == chosenPrivileges.size()) {
                this.removeActionPanel();

                this.GUI.notifyChosenPrivilege(chosenP);
                chosenPrivileges.clear();
            }
        } else if (e.getSource() instanceof PlayerColor) {

            this.removeActionPanel();
            String playerColor = ((PlayerColor) e.getSource()).getName();
            this.GUI.notifySatanChoice(playerColor);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    public void createMarkers(Board board) {
        if (orderMarkers.isEmpty()) {
            for (int i = 0; i < board.getPlayerOrder().size(); i++) {
                orderMarkers.add(new OrderMarkerDisk(board.getPlayerOrder().get(i)));
                boardPanel.add(orderMarkers.get(i));
                victoryMarkers.put(board.getPlayerOrder().get(i),
                        new VictoryPointMarkerDisk(board.getPlayerOrder().get(i)));
                militaryMarkers.put(board.getPlayerOrder().get(i),
                        new MilitaryPointMarkerDisk(board.getPlayerOrder().get(i)));
                faithMarkers.put(board.getPlayerOrder().get(i),
                        new FaithPointMarkerDisk(board.getPlayerOrder().get(i)));
                boardPanel.add(victoryMarkers.get(board.getPlayerOrder().get(i)));
                boardPanel.add(militaryMarkers.get(board.getPlayerOrder().get(i)));
                boardPanel.add(faithMarkers.get(board.getPlayerOrder().get(i)));
            }
        }
    }

    /**
     * Place familiars.
     *
     * @param board
     *            the board
     */
    public void PlaceFamiliars(Board board) {

        if (board.getCouncilPalace().getMembers().size() != FamilyMemberPawn.councilCounter) {
            FamilyMember last = board.getCouncilPalace().getMembers()
                    .get(board.getCouncilPalace().getMembers().size() - 1);
            boardPanel.add(this.familiars.get(last.getColor().toString() + last.getPlayer().getColor()));
            this.familiars.get(last.getColor().toString() + last.getPlayer().getColor())
                    .PlaceFamiliarIntoCouncilPalace();
        }

        for (int i = 1; i <= board.getPlayerOrder().size(); i++) {
            if(board.getPlayerOrder().size() == 3 && i==3){
                break;
            }
            if (board.getMarket().getMarketActionSpace(String.valueOf(i)).getFamilyMember() != null) {

                boardPanel.add(this.familiars.get(
                        board.getMarket().getMarketActionSpace(String.valueOf(i)).getFamilyMember().getColor().toString()
                                + board.getMarket().getMarketActionSpace(String.valueOf(i)).getFamilyMember().getPlayer()
                                .getColor()));
                this.familiars.get(
                                board.getMarket().getMarketActionSpace(String.valueOf(i)).getFamilyMember().getColor().toString()
                                        + board.getMarket().getMarketActionSpace(String.valueOf(i)).getFamilyMember().getPlayer()
                                        .getColor())
                        .PlaceFamiliarIntoMarket(i);
            }
        }

        for (int i = 0; i < board.getHarvestArea().getMultipleActionSpace().getMembers().size(); i++) {
            boardPanel.add(this.familiars.get(
                    board.getHarvestArea().getMultipleActionSpace().getMembers().get(i).getColor().toString() + board
                            .getHarvestArea().getMultipleActionSpace().getMembers().get(i).getPlayer().getColor()));
            this.familiars.get(board.getHarvestArea().getMultipleActionSpace().getMembers().get(i).getColor().toString()
                            + board.getHarvestArea().getMultipleActionSpace().getMembers().get(i).getPlayer().getColor())
                    .PlaceFamiliarIntoHarvestArea("2");
        }

        if (board.getHarvestArea().getSingleActionSpace().getFamilyMember() != null) {
            boardPanel.add(this.familiars
                    .get(board.getHarvestArea().getSingleActionSpace().getFamilyMember().getColor().toString()
                            + board.getHarvestArea().getSingleActionSpace().getFamilyMember().getPlayer().getColor()));
            this.familiars
                    .get(board.getHarvestArea().getSingleActionSpace().getFamilyMember().getColor().toString()
                            + board.getHarvestArea().getSingleActionSpace().getFamilyMember().getPlayer().getColor())
                    .PlaceFamiliarIntoHarvestArea("1");

        }
        for (int i = 0; i < board.getProductionArea().getMultipleActionSpace().getMembers().size(); i++) {
            boardPanel.add(this.familiars.get(
                    board.getProductionArea().getMultipleActionSpace().getMembers().get(i).getColor().toString() + board
                            .getProductionArea().getMultipleActionSpace().getMembers().get(i).getPlayer().getColor()));
            this.familiars.get(
                            board.getProductionArea().getMultipleActionSpace().getMembers().get(i).getColor().toString() + board
                                    .getProductionArea().getMultipleActionSpace().getMembers().get(i).getPlayer().getColor())
                    .PlaceFamiliarIntoProductionArea("2");
        }

        if (board.getProductionArea().getSingleActionSpace().getFamilyMember() != null) {
            boardPanel.add(this.familiars.get(
                    board.getProductionArea().getSingleActionSpace().getFamilyMember().getColor().toString() + board
                            .getProductionArea().getSingleActionSpace().getFamilyMember().getPlayer().getColor()));
            familiars
                    .get(board.getProductionArea().getSingleActionSpace().getFamilyMember().getColor().toString()
                            + board.getProductionArea().getSingleActionSpace().getFamilyMember().getPlayer().getColor())
                    .PlaceFamiliarIntoProductionArea("1");
        }
        for (CardType c : CardType.values()) {
            if (c != CardType.ANY) {
                for (int i = 0; i < board.getTower(c).getFloors().size(); i++) {
                    if (board.getFloor(c, i).getActionSpace().getFamilyMember() != null) {

                        boardPanel.add(this.familiars.get(
                                board.getFloor(c, i).getActionSpace().getFamilyMember().getColor().toString() + board
                                        .getFloor(c, i).getActionSpace().getFamilyMember().getPlayer().getColor()));
                        this.familiars.get(board.getFloor(c, i).getActionSpace().getFamilyMember().getColor().toString()
                                        + board.getFloor(c, i).getActionSpace().getFamilyMember().getPlayer().getColor())
                                .PlaceFamiliarInTower(c.toString().toLowerCase(Locale.ROOT), i);
                    }
                }
            }
        }
    }

    public void setPointsMarkers(Player p) {

        victoryMarkers.get(p.getColor())
                .setVictoryPointsAmount(p.getResourceChest().getResourceInChest(ResourceType.VICTORYPOINT).getAmount());
        militaryMarkers.get(p.getColor()).setMilitaryPointsAmount(
                p.getResourceChest().getResourceInChest(ResourceType.MILITARYPOINT).getAmount());
        faithMarkers.get(p.getColor())
                .setFaithPointsAmount(p.getResourceChest().getResourceInChest(ResourceType.FAITHPOINT).getAmount());
    }

    public void updateOrder(Board board) {
        ArrayList<String> playerColor = board.getPlayerOrder();
        for (int i = 0; i < playerColor.size(); i++) {
            getOrderFromColor(playerColor.get(i)).setOrderMarkers();
            boardPanel.add(getOrderFromColor(playerColor.get(i)));
        }
    }

    private OrderMarkerDisk getOrderFromColor(String color) {
        for (int i = 0; i < orderMarkers.size(); i++) {
            if (orderMarkers.get(i).getSrc().equals(color)) {
                return orderMarkers.get(i);
            }
        }
        return null;
    }

    public void populateFamiliars(Board board) {
        if (familiars.isEmpty()) {
            for (String s : board.getPlayerOrder()) {
                for (Dice d : Dice.values()) {

                    familiars.put(d.getColor().toString() + s, new FamilyMemberPawn(d.getColor().toString(), s));
                }
            }
        }
    }

    public ArrayList<OrderMarkerDisk> getOrderMarkers() {
        return (ArrayList<OrderMarkerDisk>) orderMarkers;
    }

    public Map<String, VictoryPointMarkerDisk> getVictoryMarkers() {
        return victoryMarkers;
    }

    public Map<String, MilitaryPointMarkerDisk> getMilitaryMarkers() {
        return militaryMarkers;
    }

    public Map<String, FaithPointMarkerDisk> getFaithMarkers() {
        return faithMarkers;
    }

    public void showSupport(boolean showSupportDecision) {
        this.GUI.notifyExcommunicationChoice(showSupportDecision);
        currentActionPanel = null;
        this.removeActionPanel();

    }

    public void showExcommunicationPanel() {
        this.currentActionPanel = chooseExcommunicationPanel;
        this.showActionPanel(chooseExcommunicationPanel);
    }

    public void resetFamiliars() {
        for (FamilyMemberPawn f : familiars.values()) {
            if (!familiars.isEmpty())
                boardPanel.remove(f);
        }
    }

    public void refreshLeaders(Map<String, LeaderCard> leaderCards) {
        if (!leadersPanel.areLeaderCards(leaderCards.size())) {
            leadersPanel.refreshLeaderCards(leaderCards, this);
        }
    }

    public void showChooseLeaderDraft(ArrayList<LeaderCard> leaderCards) {
        this.removeActionPanel();
        draftPanel.addLeaderCardsWithListener(leaderCards, this);
        showActionPanel(draftPanel);
        this.currentActionPanel = draftPanel;
    }

    public void repaintResources() {
        playerResources.repaint();
    }

    public void repaintBoard() {
        boardPanel.repaint();
    }

    public boolean isContained(int id) {
        for (CardButton card : cards) {
            if (card.getId() == id)
                return true;
        }
        return false;
    }

    public void removeCard(int tower, int floor) {
        if (tower == 1)
            tower = 2;
        else if (tower == 2)
            tower = 1;

        for (CardButton card : cards) {
            if (card.getTower() == tower && card.getFloor() == floor) {
                boardPanel.remove(card);
            }
        }
    }

    public void notifyActivateClick() {
        leaderState = "activate";
        currentActionPanel = leadersPanel;
        showActionPanel(leadersPanel);
    }

    public void notifyDiscardClick() {
        leaderState = "discard";
        currentActionPanel = leadersPanel;
        showActionPanel(leadersPanel);
    }

    public void setLeaderState(String string) {
        leaderState = string;
    }

    public void setExcommTiles(Board board) {
        if (familiars.isEmpty()) {
            boardPanel.add(new ExcommTileFirstPeriod(board.getChurch().getExcommunicationFirst().getId(),
                    board.getChurch().getExcommunicationFirst().toString()));
            boardPanel.add(new ExcommTileSecondPeriod(board.getChurch().getExcommunicationSecond().getId(),
                    board.getChurch().getExcommunicationSecond().toString()));
            boardPanel.add(new ExcommTileThirdPeriod(board.getChurch().getExcommunicationThird().getId(),
                    board.getChurch().getExcommunicationThird().toString()));
        }
    }

    public void addExcommunicationCubes(Player p) {
        if (p.isExcommunicatedFirst()) {
            ExcommCube first = new ExcommCube(1, p.getColor());
            boardPanel.add(first);
            first.getParent().setComponentZOrder(first, 0);
        }

        if (p.isExcommunicatedSecond()) {
            ExcommCube second = new ExcommCube(2, p.getColor());
            boardPanel.add(second);
            second.getParent().setComponentZOrder(second, 0);
        }

        if (p.isExcommunicatedThird()) {
            ExcommCube third = new ExcommCube(3, p.getColor());
            boardPanel.add(third);
            third.getParent().setComponentZOrder(third, 0);
        }
    }

    public void playerDisconnected() {
        this.currentActionPanel = null;
        this.removeActionPanel();
    }

    public void createDices() {
        for (int i = 1; i < 7; i++) {
            dices.put(String.valueOf(i) + "black", new DicePanel(i, "black"));
            dices.put(String.valueOf(i) + "white", new DicePanel(i, "white"));
            dices.put(String.valueOf(i) + "orange", new DicePanel(i, "orange"));
        }
    }

    public void setDices(Board board) {
        if (board.getDices().get(Dice.BLACK_DICE) != 0 && board.getDices().get(Dice.WHITE_DICE) != 0
                && board.getDices().get(Dice.ORANGE_DICE) != 0) {
            DicePanel black = dices.get(String.valueOf(board.getDices().get(Dice.BLACK_DICE)) + "black");
            DicePanel white = dices.get(String.valueOf(board.getDices().get(Dice.WHITE_DICE)) + "white");
            DicePanel orange = dices.get(String.valueOf(board.getDices().get(Dice.ORANGE_DICE)) + "orange");

            boardPanel.add(black);
            boardPanel.add(orange);
            boardPanel.add(white);
        }
    }

    public void removeDicesAndMarkers() {
        for (int i = 0; i < orderMarkers.size(); i++) {
            boardPanel.remove(orderMarkers.get(i));
        }

        for (DicePanel d : dices.values()) {
            if (d.getParent() == boardPanel) {
                boardPanel.remove(d);
            }
        }

    }

    public void notifyActivateProduction(ArrayList<Integer> choices) {
        this.GUI.notifyActivateProduction(choices);
        productionChoices = new ProductionChoices(this);
        productionChoices.setBackground(BACKGROUND_PANELS_COLOR);
        productionChoices.setVisible(false);
    }

    public void showChooseProductionEffects(ArrayList<Integer> cardsIds) {
        productionChoices.addCards(cardsIds);
        currentActionPanel = productionChoices;
        this.showActionPanel(productionChoices);

    }

    public void showAskAuthentication() {
        currentActionPanel = askAuthenticationPanel;
        this.showActionPanel(askAuthenticationPanel);
    }

    public void notifyAuthenticateClick(String username, String password) {
        this.GUI.notifyAuthenticationRequest(username, password);
        this.currentActionPanel = null;
        this.removeActionPanel();
    }

    public void setUsername(String username) {
        this.askAuthenticationPanel = null; // freeing up memory by "invoking"
        // garbage collector
        playerResources.setUsername(username);
    }

    public void setCurrentNumberOfPrivilege(int numberOfPrivilege) {
        currentNumberOfPrivilege = numberOfPrivilege;
    }

    public void showSatanPanel() {
        showActionPanel(satanPanel);
    }

    public void placeSatanDisc() {
        VictoryPointMarkerDisk blackDisk = new VictoryPointMarkerDisk("black");
        boardPanel.add(blackDisk);
        blackDisk.setVictoryPointsAmount(99);
    }
}
