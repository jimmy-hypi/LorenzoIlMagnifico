package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class represents a general leader card, it displays it and gives the
 * functionalities offered by a JButton
 */
public class JLeaderCard extends JButton implements MouseListener {
    private static final long serialVersionUID = 1L;
    private transient Toolkit toolkit = Toolkit.getDefaultToolkit();
    ImageIcon icon;
    private String path;
    private String leaderName;
    private Dimension screenDimension;
    private Image zoomedImage;
    private Image img;

    public JLeaderCard(String leaderName) {

        this.leaderName=leaderName;

        screenDimension = toolkit.getScreenSize();

        img = null;

        path = "/"+leaderName+".jpg";

        try {
            zoomedImage = ImageIO.read(getClass().getResource(path));

        } catch (Exception ex) {
            System.out.println(ex);
        }
        zoomedImage = zoomedImage.getScaledInstance(screenDimension.width/8,screenDimension.width/8*500/300, Image.SCALE_SMOOTH);
        img = zoomedImage.getScaledInstance(screenDimension.width/10,screenDimension.width/10*500/300, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(img));

        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setIcon(new ImageIcon(zoomedImage));
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setIcon(new ImageIcon(img));
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    public String getLeaderName() {
        return leaderName;
    }
}
