package view.gui;


import model.card.CardType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class represents a general development card, it displays it and gives the
 * functionalities offered by a JButton
 */
public class JDevelopmentCard extends JButton implements MouseListener {
    private static final long serialVersionUID = 1L;
    private CardType cardType;
    private int id;
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();
    private int cardWidth;
    private int cardHeight;
    private int applicationPointX;
    private int applicationPointY;
    private int cardNumber;
    private int personalH;
    private int personalW;
    private final double ratioCardW = 0.076486406;
    private final double ratioSpaceW = 0.00657305;
    private Dimension screenDimension;
    private final double ratioHighBorder=0.025;
    private final double ratioSecondHighBorder=0.464;
    private final double ratioLeftBorder=0.006;
    private final double ratioSecondLeftBorder=0.505;
    ImageIcon icon;
    private String path;
    private Image zoomedImage;
    private int h;
    private int w;
    private Image img;
    private double personalRatio=2.789166667;

    public JDevelopmentCard(CardType cardType, int id, int cardNumber) {
        screenDimension = toolkit.getScreenSize();

        personalW=screenDimension.width;
        personalH=(int)(personalW/personalRatio);

        img = null;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
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

        cardWidth=newW;
        cardHeight=newH;

        img = zoomedImage.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(img));

        calculateApplicationPoint();


        setBounds(applicationPointX, applicationPointY, newW, newH);

        this.addMouseListener(this);
    }

    private void calculateApplicationPoint() {
        setInitialApplicationPoint();
        applicationPointX+=cardNumber*(cardWidth+ratioSpaceW*personalW);
    }

    private void setInitialApplicationPoint() {
        switch (cardType) {
            case VENTURE:
                applicationPointX=(int)(ratioSecondLeftBorder*personalW);
                applicationPointY=(int)(ratioHighBorder*personalH);
                break;
            case TERRITORY:
                applicationPointX=(int)(ratioLeftBorder*personalW);
                applicationPointY=(int)(ratioSecondHighBorder*personalH);
                break;
            case CHARACTER:
                applicationPointX=(int)(ratioSecondLeftBorder*personalW);
                applicationPointY=(int)(ratioSecondHighBorder*personalH);
                break;
            default:
                applicationPointX=(int)(ratioLeftBorder*personalW);
                applicationPointY=(int)(ratioHighBorder*personalH);
                break;
        }
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

}