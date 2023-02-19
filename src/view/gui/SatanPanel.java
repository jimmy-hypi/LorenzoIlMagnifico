package view.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;

public class SatanPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField txtChooseThePlayer;
    private Image img;
    private int resourceWidth;

    public SatanPanel(int resourceWidth, GamePanel listener) {
        this.resourceWidth = resourceWidth;

        setMaximumSize(new Dimension(resourceWidth, 32767));

        setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        setForeground(UIManager.getColor("ArrowButton.disabledText"));

        LayoutManager lay = new FlowLayout(FlowLayout.CENTER, 40, 100);
        setLayout(lay);

        try {
            img = ImageIO.read(this.getClass().getResource("/satan.jpg"));
        } catch (IOException e) {}

        txtChooseThePlayer = new JTextField();
        txtChooseThePlayer.setFont(new Font("Tahoma", Font.PLAIN, 20));
        txtChooseThePlayer.setEditable(false);
        txtChooseThePlayer.setText("Choose the player:");
        add(txtChooseThePlayer);
        txtChooseThePlayer.setColumns(10);

        PlayerColor red = new PlayerColor("red", resourceWidth);
        red.setName("Red");
        add(red);
        red.addMouseListener(listener);

        PlayerColor green = new PlayerColor("green", resourceWidth);
        add(green);
        green.setName("Green");
        green.addMouseListener(listener);

        PlayerColor blue = new PlayerColor("blue", resourceWidth);
        add(blue);
        blue.setName("Blue");
        blue.addMouseListener(listener);

        PlayerColor yellow = new PlayerColor("yellow", resourceWidth);
        add(yellow);
        yellow.setName("Yellow");
        yellow.addMouseListener(listener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img.getScaledInstance(resourceWidth, 32767, 0), 0, 0, this);
    }
}