package view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A generic resource displaying image, name and amount.
 */
public class JResource extends JLabel {
    private static final long serialVersionUID = 1L;
    private int amount;
    private String resourceType;

    public JResource(String resourceType, int rightPanelWidth) {
        this.resourceType = resourceType;

        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setBackground(Color.ORANGE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.TOP);
        setToolTipText("This is your number of" + resourceType);
        if (resourceType.equals("woodstone")) {
            setText("stone: 1, wood: 1  ");
        } else if (resourceType.equals("military")) {
            setText("militaries: " + amount + "  ");
        } else {
            setText(resourceType + "s: " + amount + "  ");
        }

        setFont(new Font("SansSerif", Font.BOLD, 20));

        ImageIcon imageIcon = new ImageIcon(JResource.class.getResource("/" + resourceType + ".png"));
        Image image = imageIcon.getImage(); // transform it
        double w = image.getWidth(null);
        double h = image.getHeight(null);
        double ratio = w / h;

        Image newimg = image.getScaledInstance((int) (rightPanelWidth / 14), (int) (rightPanelWidth / 14 / ratio),
                java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg); // transform it back
        setIcon(imageIcon);
    }

    public void setAmount(int amount) {
        this.amount = amount;
        if (resourceType.equals("woodstone")) {
            setText("stone: 1, wood: 1  ");
        } else if (resourceType.equals("military")) {
            setText("militaries: " + amount + "  ");
        } else {
            setText(resourceType + "s: " + amount + "  ");
        }
    }
}
