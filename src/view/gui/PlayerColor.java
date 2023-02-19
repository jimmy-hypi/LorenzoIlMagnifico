package view.gui;

import javax.swing.*;
import java.awt.*;

public class PlayerColor extends JLabel {
    private static final long serialVersionUID = 1L;
    private String color;


    public PlayerColor(String color, int rightPanelWidth) {
        this.color = color;

        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setBackground(Color.ORANGE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.TOP);

        ImageIcon imageIcon = new ImageIcon(JResource.class.getResource("/" + color + "Disc.png"));
        Image image = imageIcon.getImage();
        double w = image.getWidth(null);
        double h = image.getHeight(null);
        double ratio = w / h;

        Image newimg = image.getScaledInstance((int) (rightPanelWidth / 14), (int) (rightPanelWidth / 14 / ratio),
                java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        setIcon(imageIcon);
    }
}
