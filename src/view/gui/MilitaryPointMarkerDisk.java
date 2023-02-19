package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 *  This class represents the images of the markers for the military points of each player
 */
public class MilitaryPointMarkerDisk extends JPanel {
    private static final long serialVersionUID = 1L;
    private String src; //red,blue,green,yellow
    private Image img;
    private transient Toolkit tool = Toolkit.getDefaultToolkit();
    static int wCount = 0;
    private static double WIDTH_PERC = 0.86614173228346456692913385826772;
    private static double HEIGHT_PERC = 0.89814814814814814814814814814815;
    private final static double wDIM_PERC = 0.02624671916010498687664041994751;
    private final static double hDIM_PERC = 0.01851851851851851851851851851852;
    private int  heightRel = (int) (tool.getScreenSize().height*HEIGHT_PERC);
    private int  widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
    private final static double offset = 0.03333333333333333333333333333333;

    public MilitaryPointMarkerDisk(String color){
        src = color;
        try {
            this.img = ImageIO.read(getClass().getResource("/"+src+"Disc.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBounds(widthRel , heightRel , (int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
        setMilitaryPointMarkers();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img.getScaledInstance((int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()), 0), 0, 0, this);
    }

    private void setMilitaryPointMarkers(){
        this.setBounds(widthRel , heightRel , (int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
    }

    public void setMilitaryPointsAmount(int amount){
        Random random = new Random();
        int n = random.nextInt(30);
        if(amount>25){
            amount = 25;
        }
        widthRel = (int) ((BoardPanel.dimension.getWidth() + n)*WIDTH_PERC);
        heightRel = (int) (heightRel - amount*offset*(BoardPanel.dimension.getHeight()));
        setMilitaryPointMarkers();
        widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
        heightRel = (int) (tool.getScreenSize().height*HEIGHT_PERC);
    }
}
