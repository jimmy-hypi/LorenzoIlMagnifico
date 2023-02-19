package view.gui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel that allows the construction of an action, letting the user choose
 * between the family members and the servants he wants to use in the action.
 */
public class ActionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private ButtonGroup familyGroup;
    private List<JRadioButton> radioButtons;
    JSlider slider;

    public ActionPanel(GamePanel gamePanel,String playerColor) {
        radioButtons = new ArrayList<JRadioButton>();

        setBackground(new Color(222, 184, 135));
        familyGroup = new ButtonGroup();

        URL neutral = ActionPanel.class.getResource("/"+playerColor+"neutralFamiliar.png");
        URL orange = ActionPanel.class.getResource("/"+playerColor+"orangeFamiliar.png");
        URL white = ActionPanel.class.getResource("/"+playerColor+"whiteFamiliar.png");
        URL black = ActionPanel.class.getResource("/"+playerColor+"blackFamiliar.png");

        String html = "<html><body><img src='" + neutral.toString() + "'width=160 height=160>";

        setLayout(new BorderLayout(0, 0));
        Font font = new Font("Serif", Font.ITALIC, 15);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(222, 184, 135));
        add(panel, BorderLayout.NORTH);

        JRadioButton neutralB = new JRadioButton(html);
        panel.add(neutralB);
        neutralB.setName("neutral");

        html = "<html><body><img src='" + white.toString() + "'width=160 height=160>";

        familyGroup.add(neutralB);
        radioButtons.add(neutralB);

        JRadioButton whiteB = new JRadioButton(html);
        panel.add(whiteB);
        familyGroup.add(whiteB);
        radioButtons.add(whiteB);
        whiteB.setName("white");

        html = "<html><body><img src='" + orange.toString() + "'width=160 height=160>";

        JRadioButton orangeB = new JRadioButton(html);
        panel.add(orangeB);
        familyGroup.add(orangeB);
        radioButtons.add(orangeB);
        orangeB.setName("orange");

        html = "<html><body><img src='" + black.toString() + "'width=160 height=160>";

        JRadioButton blackB = new JRadioButton(html);
        panel.add(blackB);
        familyGroup.add(blackB);
        radioButtons.add(blackB);
        blackB.setName("black");

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(222, 184, 135));
        add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(null);

        JLabel label = new JLabel("");
        label.setBounds(315, 0, 132, 132);
        panel_1.add(label);
        ImageIcon servant = new ImageIcon(ActionPanel.class.getResource("/servant.png"));

        Image img = servant.getImage();

        img = img.getScaledInstance(img.getWidth(null) / 3, img.getHeight(null) / 3, java.awt.Image.SCALE_SMOOTH);

        servant = new ImageIcon(img);

        label.setIcon(servant);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
        slider.setBounds(488, 0, 200, 71);
        panel_1.add(slider);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        slider.setFont(font);

    }

    public String getFamilyMember() {
        JRadioButton button = getSelectedRadioButton();
        if (button == null)
            return "none";
        else
            return button.getName();

    }

    public JRadioButton getSelectedRadioButton() {
        for (JRadioButton button : radioButtons) {
            if (button.isSelected())
                return button;
        }
        return null;
    }

    public String getServants() {
        return ""+slider.getValue();
    }
}