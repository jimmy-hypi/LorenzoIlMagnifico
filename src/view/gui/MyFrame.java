package view.gui;

import constant.ImagesConstants;
import model.Player;
import model.area.Board;
import model.area.Tower;
import model.card.CardType;
import model.card.LeaderCard;
import model.resource.ResourceType;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

/**
 * Main frame of the game.
 */
public class MyFrame extends JFrame {
    private String playerColor;
    private static final long serialVersionUID = 1L;
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Dimension screenDimension = new Dimension();
    private int height;
    private int width;
    private int toolBarHeight;
    private Image img;
    private GamePanel gamePanel;
    private Container content;
    private BoardPanel board;
    private InitialPanel initialPanel;

    public MyFrame() {
        super("Lorenzo Il Magnifico");

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(MyFrame.class.getResource("/MJMLogoTransparent.png")));
        // Setting the LookAndFeel theme
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        setUndecorated(true);
        setLayout(new BorderLayout());
        content = this.getContentPane();

        screenDimension = toolkit.getScreenSize();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        toolBarHeight = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).bottom;

        width = screenDimension.width;
        height = screenDimension.height - toolBarHeight;

        addInitialPanel(ImagesConstants.INITIAL_IMAGE);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void addInitialPanel(String initialImage) {
        initialPanel = new InitialPanel(initialImage);

        content.add(initialPanel, BorderLayout.CENTER);
        initialPanel.setLayout(null);
    }

    public void removeInitialImage() {
        remove(initialPanel);
    }

    public void initializeGameFrame(int numPlayers){
        try {
            gamePanel = new GamePanel(playerColor,numPlayers);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        setContentPane(gamePanel);
    }

    public void refreshBoard(Board board) {
        addCards(board);
    }

    private void addCards(Board board) {
        for (int j = 0; j < CardType.values().length; j++) {
            CardType c = CardType.values()[j];
            if (c != CardType.ANY) {
                Tower t = board.getTower(c);
                for (int i = 0; i < t.getFloors().size(); i++) {
                    if (t.getFloor(i).getCard() != null) {
                        int id = t.getFloor(i).getCard().getId();
                        String descr = t.getFloor(i).getCard().toString();
                        gamePanel.addCard(j, i, id, descr);
                    } else{
                        gamePanel.removeCard(j,i);
                    }
                }
            }
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void refreshPlayerStatus(Player p) {
        gamePanel.addResourceToPlayerStatus(p.getResourceChest().getResourceInChest(ResourceType.COIN));
        gamePanel.addResourceToPlayerStatus(p.getResourceChest().getResourceInChest(ResourceType.WOOD));
        gamePanel.addResourceToPlayerStatus(p.getResourceChest().getResourceInChest(ResourceType.STONE));
        gamePanel.addResourceToPlayerStatus(p.getResourceChest().getResourceInChest(ResourceType.SERVANT));

        gamePanel.addFamilyMembersToPlayerStatus(p.getFamilyMembers());
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public void addOrderMarkerDisks(Player[] players){
        for(int i = 0; i < players.length; i++){
            gamePanel.add(new OrderMarkerDisk(players[i].getColor()));
        }
    }

    public void showChooseAction() {
        gamePanel.showChooseAction();
    }

    public void showEndOrDiscard() {
        gamePanel.showEndOrDiscard();
    }

    public void showPrivilegeChoice() {
        gamePanel.showChoosePrivilege();
    }

    public void showExcommunicationPanel() {
        gamePanel.showExcommunicationPanel();
    }

    public void refreshLeaderCards(Map<String, LeaderCard> leaderCards) {
        gamePanel.refreshLeaders(leaderCards);
    }
}
