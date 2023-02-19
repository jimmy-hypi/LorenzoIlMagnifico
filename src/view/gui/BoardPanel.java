package view.gui;

import constant.ImagesConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This JPanel extension is the left-side of the frame, the one containing the board image with
 * all his components, at the moment of the creation the board image is scaled maintaining the
 * proportions to adapt it to any kind of screen, components put into this BoardPanel must set
 * the bounds for themselves and scale their dimensions to create a responsive UI.
 */
public class BoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image img;
    static Dimension dimension;
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();
    private int imgHeight;
    private int imgWidth;

    public BoardPanel(){
        super(new GridBagLayout());

        dimension = toolkit.getScreenSize();
        try {
            img = ImageIO.read(this.getClass().getResource(ImagesConstants.BOARD));
        } catch (IOException  e) {}
        imgHeight = img.getHeight(null);
        imgWidth = img.getWidth(null);
        img = img.getScaledInstance(dimension.height*imgWidth/imgHeight, dimension.height, Image.SCALE_SMOOTH);
        dimension.setSize(dimension.height*imgWidth/imgHeight, dimension.height);
        setPreferredSize(dimension);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }

    public Dimension getDimension() {return dimension;}
}
