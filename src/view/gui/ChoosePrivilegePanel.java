package view.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * The Panel that lets the player choose the resources given by a council privilege
 */
public class ChoosePrivilegePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public ChoosePrivilegePanel(int resourceWidth, GamePanel listener) {
        setMaximumSize(new Dimension(resourceWidth, 32767));

        setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        setForeground(UIManager.getColor("ArrowButton.disabledText"));

        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 100));

        JResource woodStone = new JResource("woodstone", resourceWidth);
        woodStone.setAmount(1);
        woodStone.setName("0");
        add(woodStone);
        woodStone.addMouseListener(listener);

        JResource servant = new JResource("servant", resourceWidth);
        servant.setAmount(2);
        add(servant);
        servant.setName("1");
        servant.addMouseListener(listener);

        JResource coin = new JResource("coin", resourceWidth);
        coin.setAmount(2);
        add(coin);
        coin.setName("2");
        coin.addMouseListener(listener);

        JResource military = new JResource("military", resourceWidth);
        military.setAmount(2);
        add(military);
        military.setName("3");
        military.addMouseListener(listener);

        JResource faith = new JResource("faith", resourceWidth);
        faith.setAmount(1);
        add(faith);
        faith.setName("4");
        faith.addMouseListener(listener);
    }
}