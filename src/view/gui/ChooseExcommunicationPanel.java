package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Panel that lets the player choose if he wants to be excommunicated or support the church
 */
public class ChooseExcommunicationPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton showSupport;
    private JButton beExcommunicated;
    private GamePanel gamePanel;

    public ChooseExcommunicationPanel(GamePanel gamePanel) {
        this.gamePanel=gamePanel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(222, 184, 135));
        add(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));

        showSupport = new JButton("");
        showSupport.setPressedIcon(new ImageIcon(ChooseAction.class.getResource("/showsupportbtnpressed.png")));
        showSupport.setBorderPainted(false);
        showSupport.setContentAreaFilled(false);
        showSupport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel_2.add(showSupport);
        showSupport.setIcon(new ImageIcon(ChooseAction.class.getResource("/showsupportbtn.png")));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(222, 184, 135));
        add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        beExcommunicated = new JButton("");
        beExcommunicated.setPressedIcon(new ImageIcon(ChooseAction.class.getResource("/beexcommunicatedbtnpressed.png")));
        beExcommunicated.setBorderPainted(false);
        beExcommunicated.setContentAreaFilled(false);
        beExcommunicated.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        beExcommunicated.setIcon(new ImageIcon(ChooseAction.class.getResource("/beexcommunicatedbtn.png")));
        panel.add(beExcommunicated);

        showSupport.addActionListener(this);
        beExcommunicated.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.showSupport) {
            gamePanel.showSupport(true);
        } else if (e.getSource() == this.beExcommunicated) {
            gamePanel.showSupport(false);
        }
    }
}