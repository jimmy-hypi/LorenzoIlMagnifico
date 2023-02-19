package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * The panel containing the image of the personal board of the player, it has the layout manager
 * set to null so that components (Development Cards) can be scaled and positioned in the
 * correct position based on the screen dimension
 */
public class PersonalBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image img;
    private Dimension dimension;
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();

    public PersonalBoardPanel() {
        super(null);

        dimension = toolkit.getScreenSize();

        try {
            img = ImageIO.read(this.getClass().getResource(
                    "/completepersonalboardmirkosmall.jpg"));
        } catch (IOException e) {}
        int imgHeight=img.getHeight(null);
        int imgWidth=img.getWidth(null);
        img = img.getScaledInstance(dimension.width, dimension.width*imgHeight/imgWidth,
                Image.SCALE_SMOOTH);
        setPreferredSize(new Dimension(dimension.width,dimension.width*imgHeight/imgWidth));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }

}