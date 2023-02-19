package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The panel that lets the user choose what he wants to do when it's his turn.
 */
public class ChooseAction extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton action;
    private JButton activate;
    private JButton discard;
    private GamePanel gamePanel;

    public ChooseAction(GamePanel gamePanel) {

        this.gamePanel=gamePanel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(222, 184, 135));
        add(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));

        action = new JButton("");
        action.setPressedIcon(new ImageIcon(ChooseAction.class.getResource("/actionbtnpressed.png")));
        action.setBorderPainted(false);
        action.setContentAreaFilled(false);
        action.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel_2.add(action);
        action.setIcon(new ImageIcon(ChooseAction.class.getResource("/actionbtn.png")));

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(222, 184, 135));
        add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        activate = new JButton("");
        activate.setBackground(new Color(222, 184, 135));
        activate.setBorderPainted(false);
        activate.setContentAreaFilled(false);
        activate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activate.setPressedIcon(new ImageIcon(ChooseAction.class.getResource("/activatebtnpressed.png")));
        activate.setIcon(new ImageIcon(ChooseAction.class.getResource("/activatebtn.png")));
        panel_1.add(activate);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(222, 184, 135));
        add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        discard = new JButton("");
        discard.setPressedIcon(new ImageIcon(ChooseAction.class.getResource("/discardbtnpressed.png")));
        discard.setBorderPainted(false);
        discard.setContentAreaFilled(false);
        discard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        discard.setIcon(new ImageIcon(ChooseAction.class.getResource("/discardbtn.png")));
        panel.add(discard);

        action.addActionListener(this);
        activate.addActionListener(this);
        discard.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.action) {
            gamePanel.notifyActionClick();

        } else if (e.getSource() == this.activate) {
            gamePanel.notifyActivateClick();

        } else if (e.getSource() == this.discard) {
            gamePanel.notifyDiscardClick();
        }
    }
}
