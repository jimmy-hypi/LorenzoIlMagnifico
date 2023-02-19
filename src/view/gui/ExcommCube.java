package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class represent the image of excommunication dices
 */
public class ExcommCube extends JPanel {
    private static final long serialVersionUID = 1L;
    private int period;
    private String player;
    private Image img;
    private int widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
    private int heightRel = (int) (BoardPanel.dimension.getHeight()*HEIGHT_PERC);
    private static final double WIDTH_PERC = 0.2028503937007874015748031496063;
    private static final double HEIGHT_PERC = 0.61559259259259259259259259259259;
    private final static double wDIM_PERC = 0.02624671916010498687664041994751;
    private final static double hDIM_PERC = 0.02351851851851851851851851851852;
    private final static double offset = 0.075;

    public ExcommCube(int period,String player){
        this.period = period;
        this.player = player;
        try {
            this.img = ImageIO.read(getClass().getResource("/" + player + "Cube.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);

        /* The height rel. */
        heightRel = (int) (BoardPanel.dimension.getHeight()*HEIGHT_PERC);
        switch(player){

            case "Yellow" : widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
                break;
            case "Red" : widthRel =(int) (BoardPanel.dimension.getWidth()*WIDTH_PERC*1.1);
                break;
            case "Blue" : heightRel = (int) (BoardPanel.dimension.getHeight()*HEIGHT_PERC*1.03);
                break;
            case "Green" : heightRel = (int) (BoardPanel.dimension.getHeight()*HEIGHT_PERC*1.03);
                widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC*1.1);
                break;
            default :
                break;

        }

        this.setBounds((int) (widthRel + (period-1)*offset*BoardPanel.dimension.getWidth()), heightRel, (int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(img.getScaledInstance((int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()), 0), 0, 0, this);
    }
}
