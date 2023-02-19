package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 *  This class represents the images of the markers for the faithPoint of each player
 */
public class FaithPointMarkerDisk extends JPanel {
    private static final long serialVersionUID = 3087444235128620320L;
    private String src; //red,blue,green,yellow
    private Image img;
    static int wCount = 0;
    private static final double WIDTH_PERC = 0.06824146981627296587926509186352;
    private static final double HEIGHT_PERC = 0.73333333333333333333333333333333;
    private final static double wDIM_PERC = 0.02624671916010498687664041994751;
    private final static double hDIM_PERC = 0.01851851851851851851851851851852;
    int widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
    int heightRel = (int) (BoardPanel.dimension.getHeight()*HEIGHT_PERC);
    private final static double slotRelDim = 0.03937007874015748031496062992126;

    public FaithPointMarkerDisk(String color){
        src = color;
        try {
            this.img = ImageIO.read(getClass().getResource("/"+src+"Disc.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBounds(widthRel , heightRel, (int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
        setFaithPointMarkers();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img.getScaledInstance((int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()), 0), 0, 0, this);
    }

    public void setFaithPointMarkers(){
        this.setBounds(widthRel , heightRel, (int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
    }

    public void setFaithPointsAmount(double val){
        Random random = new Random();
        int n = random.nextInt(50) + 100;

        if(val>=15)
            val=18.7;

        widthRel = (int) ((BoardPanel.dimension.getWidth() + n)*WIDTH_PERC);
        if(val <=2){
            widthRel = (int) (widthRel + val*slotRelDim*(BoardPanel.dimension.getWidth()));
            this.setFaithPointMarkers();
            widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
        }
        else if(val > 6){
            widthRel = (int)(widthRel + 8.5*slotRelDim*(BoardPanel.dimension.getWidth()));
            widthRel = (int)(widthRel + (val-8.5)*slotRelDim*BoardPanel.dimension.getWidth());
            this.setFaithPointMarkers();
            widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
        } else if(val == 3){
            widthRel = (int)(widthRel + 3.5*slotRelDim*(BoardPanel.dimension.getWidth()));
            this.setFaithPointMarkers();
            widthRel = (int) ((BoardPanel.dimension.getWidth() + n)*WIDTH_PERC);
        } else if(val == 4){
            widthRel = (int)(widthRel + 5.2*slotRelDim*(BoardPanel.dimension.getWidth()));
            this.setFaithPointMarkers();
            widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
        } else {
            widthRel = (int)(widthRel + 7*slotRelDim*(BoardPanel.dimension.getWidth()));
            this.setFaithPointMarkers();
            widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
        }
    }

}
