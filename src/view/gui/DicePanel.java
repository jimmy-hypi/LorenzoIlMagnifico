package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class represents the dice images on the boardPanel
 */
public class DicePanel extends JPanel {
    private static final long serialVersionUID = -543922157239004995L;
    private int value;
    private String diceColor;
    private Image img;
    int widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
    int heightRel = (int) (BoardPanel.dimension.getHeight()*HEIGHT_PERC);
    private static final double WIDTH_PERC =  0.52149606299212598425196850393701;
    private static final double HEIGHT_PERC = 0.90592592592592592592592592592593;
    private final static double wDIM_PERC = 0.06561679790026246719160104986877;
    private final static double hDIM_PERC = 0.0462962962962962962962962962963;

    public DicePanel(int value,String diceColor){
        this.value = value;
        this.diceColor = diceColor;
        try {
            this.img = ImageIO.read(getClass().getResource("/" + value + diceColor + ".png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        switch(diceColor){
            case "black" :
                break;
            case "white" : widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC*1.17);
                break;
            case "orange" : widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC*1.34);
                break;
            default :
                break;

        }
        this.setBounds(widthRel , heightRel, (int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);

        widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(img.getScaledInstance((int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()), 0), 0, 0, this);
    }
}
