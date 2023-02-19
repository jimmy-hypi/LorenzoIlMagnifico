package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * This class represents the markers to show the victory points amount of each player
 */
public class VictoryPointMarkerDisk extends JPanel {
    private static final long serialVersionUID = -2138063191515420324L;
    private String src;
    private Image img;
    private transient Toolkit tool = Toolkit.getDefaultToolkit();
    static int wCount = 0;
    private final static double WIDTH_PERC = 0.01912335958005249343832020997375;
    private final static double HEIGHT_PERC = 0.00562962962962962962962962962963;
    private final static double wDIM_PERC = 0.02624671916010498687664041994751;
    private final static double hDIM_PERC = 0.01851851851851851851851851851852;
    int heightRel = (int) (tool.getScreenSize().height * HEIGHT_PERC);
    int widthRel = (int) (BoardPanel.dimension.getWidth() * WIDTH_PERC);
    private final static double wOffset = 0.04593175853018372703412073490814;
    private final static double hOffset = 0.03194444444444444444444444444444;

    public VictoryPointMarkerDisk(String color) {
        src = color;
        try {
            this.img = ImageIO.read(getClass().getResource("/" + src + "Disc.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBounds(widthRel + wCount, heightRel, (int) (wDIM_PERC * BoardPanel.dimension.getWidth()), (int) (hDIM_PERC * BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
        setVictoryPointMarkers();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(img.getScaledInstance((int) (wDIM_PERC * BoardPanel.dimension.getWidth()), (int) (hDIM_PERC * BoardPanel.dimension.getHeight()), 0), 0, 0, this);
    }

    private void setVictoryPointMarkers() {
        this.setBounds(widthRel, heightRel, (int) (wDIM_PERC * BoardPanel.dimension.getWidth()), (int) (hDIM_PERC * BoardPanel.dimension.getHeight()));
        this.setVisible(true);
        this.setOpaque(false);
    }

    public void setVictoryPointsAmount(int amount) {
        Random random = new Random();
        int n = random.nextInt(50) + 100;

        amount = amount % 100;
        widthRel = (int) ((BoardPanel.dimension.getWidth() + n) * WIDTH_PERC);
        if (amount <= 20) {

            widthRel = (int) (widthRel + amount * wOffset * (BoardPanel.dimension.getWidth()));
            setVictoryPointMarkers();

        } else if (amount > 20 && amount <= 50) {
            widthRel = (int) (widthRel + 20.5 * wOffset * (BoardPanel.dimension.getWidth()));
            heightRel = (int) (heightRel + (amount - 20) * hOffset * BoardPanel.dimension.getHeight());
            setVictoryPointMarkers();

        } else if (amount > 70 && amount <= 100) {
            widthRel = (int) (0.1 * (BoardPanel.dimension.getWidth()) * WIDTH_PERC);
            heightRel = (int) (30.2 * hOffset * BoardPanel.dimension.getHeight());
            heightRel = (int) (heightRel - (amount - 70) * hOffset * BoardPanel.dimension.getHeight());
            setVictoryPointMarkers();

        } else {
            widthRel = (int) (widthRel + 20 * wOffset * (BoardPanel.dimension.getWidth()));
            heightRel = (int) (30.5 * hOffset * BoardPanel.dimension.getHeight());
            widthRel = (int) (widthRel - (amount - 50) * wOffset * BoardPanel.dimension.getWidth());
            setVictoryPointMarkers();

        }
        heightRel = (int) (tool.getScreenSize().height * HEIGHT_PERC);
        widthRel = (int) (BoardPanel.dimension.getWidth() * WIDTH_PERC);
    }
}