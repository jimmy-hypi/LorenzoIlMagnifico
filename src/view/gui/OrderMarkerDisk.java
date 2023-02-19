package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class represents the images of the marker discs to show the order of in-game players
 */
public class OrderMarkerDisk extends JPanel {
    private static final long serialVersionUID = 8409734849829937305L;
    private String src;
    private Image img;
    private transient Toolkit tool = Toolkit.getDefaultToolkit();
    private final static double WIDTH_PERC = 0.76771653543307086614173228346457;
    private final static double HEIGHT_PERC = 0.5324074074;
    private final static double DELTA_PERC= 0.03703703704;
    static int Ordercounter = 0;
    private final static double wDIM_PERC = 0.04724409448818897637795275590551;
    private final static double hDIM_PERC = 0.03333333333333333333333333333333;
    private int widthRel;
    private int heightRel;
    private int delta;

    public OrderMarkerDisk(String color){
        this.src = color;

        setOrderMarkers();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(img.getScaledInstance((int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()), 0), 0, 0, this);
    }

    protected void setOrderMarkers(){
        this.heightRel = (int) (tool.getScreenSize().height*HEIGHT_PERC);
        this.widthRel = (int) (BoardPanel.dimension.getWidth()*WIDTH_PERC);
        this.delta = (int) (DELTA_PERC*tool.getScreenSize().height);
        try {
            this.img = ImageIO.read(getClass().getResource("/"+src+"Disc.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBounds(widthRel, heightRel + delta*Ordercounter, (int) (wDIM_PERC*BoardPanel.dimension.getWidth()),(int) (hDIM_PERC*BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
        Ordercounter++;
    }

    public String getSrc() {
        return src;
    }
}