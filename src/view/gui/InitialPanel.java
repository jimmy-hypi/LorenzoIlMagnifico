package view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * The Initial image that the players sees while waiting for the game to start
 */
public class InitialPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Dimension screenDimension;
    private Image img;

    public InitialPanel(String image) {
        super(new GridBagLayout());

        screenDimension = toolkit.getScreenSize();

        try {
            img = ImageIO.read(this.getClass().getResource(image));
        } catch (IOException e) {
        }
        img = img.getScaledInstance(screenDimension.width, screenDimension.height,
                Image.SCALE_SMOOTH);
        screenDimension.setSize(screenDimension.width, screenDimension.height);
        setPreferredSize(screenDimension);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(img, 0, 0, this);
    }
}

