package view.gui;


import model.card.CardType;

import javax.swing.*;
import java.awt.*;

/**
 * The player personal board, it extends JFrame, and it's the only "external" frame
 * in the GUI
 */
public class PersonalBoard extends JFrame {
    private static final long serialVersionUID = 1L;
    private int numBuilding;
    private int numVenture;
    private int numTerritory;
    private int numCharacter;
    private PersonalBoardPanel personalBoardPanel;

    public PersonalBoard() {
        setUndecorated(true);
        setResizable(false);

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(MyFrame.class.getResource("/MJMLogoTransparent.png")));

        personalBoardPanel = new PersonalBoardPanel();
        getContentPane().add(personalBoardPanel, BorderLayout.CENTER);
        setSize(personalBoardPanel.getPreferredSize());
        setPreferredSize(personalBoardPanel.getPreferredSize());
    }

    public int getNumBuilding() {
        return numBuilding;
    }

    public void setNumBuilding(int numBuilding) {
        this.numBuilding = numBuilding;
    }

    public int getNumVenture() {
        return numVenture;
    }

    public void setNumVenture(int numVenture) {
        this.numVenture = numVenture;
    }

    public int getNumTerritory() {
        return numTerritory;
    }

    public void setNumTerritory(int numTerritory) {
        this.numTerritory = numTerritory;
    }

    public int getNumCharacter() {
        return numCharacter;
    }

    public void setNumCharacter(int numCharacter) {
        this.numCharacter = numCharacter;
    }

    public int getRightNum(CardType cardType) {
        switch (cardType) {
            case BUILDING:
                return getNumBuilding();
            case VENTURE:
                return getNumVenture();
            case TERRITORY:
                return getNumTerritory();
            case CHARACTER:
                return getNumCharacter();
            default:
                break;
        }
        return 0;
    }

    public void incrementRightNum(CardType cardType) {
        switch (cardType) {
            case BUILDING:
                this.setNumBuilding(getNumBuilding()+1);
                break;
            case VENTURE:
                this.setNumVenture(getNumVenture()+1);
                break;
            case TERRITORY:
                this.setNumTerritory(getNumTerritory()+1);
                break;
            case CHARACTER:
                this.setNumCharacter(getNumCharacter()+1);
                break;
            default:
                break;
        }
    }

    public PersonalBoardPanel getPersonalBoardPanel() {
        return personalBoardPanel;
    }
}