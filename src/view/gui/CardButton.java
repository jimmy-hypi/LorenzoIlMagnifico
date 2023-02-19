package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A development card button that positions itself on the board based on the parameters passed
 * to the constructor.
 */
public class CardButton extends JButton implements MouseListener {
    private static final long serialVersionUID = 1L;
    private int tower;
    private int floor;
    private int id;
    private Dimension boardPanelPrefSize;
    private int cardWidth;
    private int cardHeight;
    private int applicationPointX;
    private int applicationPointY;
    private final double ratioCardW = 0.076486406;
    private final double ratio = 0.11875;
    private final double northBorder = 0.052083333;
    private final double leftBorder = 0.069813176;
    private final double floorSpace = 0.006944444;
    private final double towerSpace = 0.089029007;
    ImageIcon icon;
    private String path;
    private Image zoomedImage;
    private int h;
    private int w;
    private Image img;
    private Dimension screenDimension;
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();

    public CardButton(int id) {
        screenDimension = toolkit.getScreenSize();

        img = null;
        this.id = id;

        path = "/devcards_f_en_c_";
        path = path + id;
        path = path + ".png";

        try {
            zoomedImage = ImageIO.read(getClass().getResource(path));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        h = zoomedImage.getHeight(null);
        w = zoomedImage.getWidth(null);

        int newW = (int) (ratioCardW * screenDimension.width);
        int newH = newW * h / w;

        cardWidth = newW;
        cardHeight = newH;

        img = zoomedImage.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(img));

        this.addMouseListener(this);
    }

    public CardButton(Dimension boardPanelPrefSize, int tower, int floor, int id) {

        img = null;
        this.tower = tower;
        this.floor = floor;
        this.id = id;
        this.boardPanelPrefSize = boardPanelPrefSize;

        int cardCut;
        if ((GamePanel.screenDim.getWidth() / GamePanel.screenDim.getHeight()) < 1.6)
            cardCut = 15;
        else cardCut = 12;

        cardWidth = (int) (ratio * boardPanelPrefSize.width - cardCut);
        cardHeight = (int) (ratio * boardPanelPrefSize.height);

        calculateApplicationPoint();

        setBounds(applicationPointX, applicationPointY, cardWidth, cardHeight);

        path = "/devcards_f_en_c_";
        path = path + id;
        path = path + ".png";

        try {
            zoomedImage = ImageIO.read(getClass().getResource(path));

        } catch (Exception ex) {
            System.out.println(ex);
        }

        h = zoomedImage.getHeight(null);
        w = zoomedImage.getWidth(null);
        img = zoomedImage.getScaledInstance((int) (ratio * boardPanelPrefSize.width), (int) (ratio * boardPanelPrefSize.height),
                Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(img));
        this.addMouseListener(this);
    }

    private void calculateApplicationPoint() {
        int revertFloor = 3 - this.floor;
        applicationPointX = (int) (this.tower * towerSpace * boardPanelPrefSize.width +
                leftBorder * boardPanelPrefSize.width + this.tower * cardWidth);
        applicationPointY = (int) (revertFloor * floorSpace * boardPanelPrefSize.height +
                northBorder * boardPanelPrefSize.height + revertFloor * cardHeight);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.getParent().setComponentZOrder(this, 0);

        setBounds(applicationPointX, applicationPointY, w, h);
        setIcon(new ImageIcon(zoomedImage));
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        setBounds(applicationPointX, applicationPointY, cardWidth, cardHeight);
        setIcon(new ImageIcon(img));
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    public int getTower() {
        return tower;
    }

    public int getFloor() {
        return floor;
    }

    public int getId() {
        return id;
    }
}


