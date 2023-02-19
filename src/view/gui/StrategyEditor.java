package view.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This Panel is a simple text editor the player can use to write notes about the
 * status of the other players that he's now allowed to freely see in our version
 * of the game, this decision was made to make the game more challenging and to
 * rewards the player who better use this feature and are constantly monitoring
 * the actions of other players.
 */
public class StrategyEditor extends JPanel {
    private static final long serialVersionUID = 1L;
    public StrategyEditor() {
        setLayout(new BorderLayout(0, 0));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        textArea.setBackground(new Color(204, 153, 0));
        textArea.setFont(new Font("Baskerville Old Face", Font.PLAIN, 20));

        JScrollPane scrPane = new JScrollPane(textArea);
        Border border = BorderFactory.createLoweredBevelBorder();
        scrPane.setBorder(border);

        JScrollBar vertical = scrPane.getVerticalScrollBar();
        vertical.setPreferredSize(new Dimension(0, 0));

        JLabel lblNewLabel = new JLabel("Strategy Editor: you can write notes to help you remember the status of other players");
        lblNewLabel.setFont(new Font("Rockwell", Font.PLAIN, 18));
        lblNewLabel.setBackground(Color.ORANGE);
        scrPane.setColumnHeaderView(lblNewLabel);

        add(scrPane, BorderLayout.CENTER);
    }
}