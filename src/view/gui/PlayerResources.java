package view.gui;

import model.FamilyMember;
import model.resource.Resource;
import model.resource.ResourceType;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The panel showing the resources of the player, note that the points aren't shown here
 * because everyone can see them in the board panel
 */
public class PlayerResources extends JPanel {
    private static final long serialVersionUID = 1L;
    private Map<ResourceType,JResource> resources;
    private Map<model.Color,JLabel> familyMembers;
    private JPanel familyGridPanel;
    private JLabel lblPlayername;

    public PlayerResources(int resourceWidth,String playerColor) {
        setSize(new Dimension(resourceWidth, 1000));
        setMaximumSize(new Dimension(resourceWidth, 32767));

        familyMembers=new HashMap<model.Color,JLabel>();

        setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        setForeground(UIManager.getColor("ArrowButton.disabledText"));

        StyleSheet s = new StyleSheet();
        String rgb=playerColor.toLowerCase();
        Color c1 = s.stringToColor(rgb);
        int r=c1.getRed();
        int g=c1.getGreen();
        int b=c1.getBlue();

        if(r==0)r+=80;
        if(g==0)g+=60;
        if(b==0)b+=60;

        setBackground(new Color(r, g, b));

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        lblPlayername = new JLabel("Waiting for Authentication");
        lblPlayername.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(lblPlayername);

        resources=new HashMap<ResourceType,JResource>();

        JResource stone = new JResource("stone",resourceWidth);
        add(stone);
        resources.put(ResourceType.STONE, stone);

        JResource wood = new JResource("wood",resourceWidth);
        add(wood);
        resources.put(ResourceType.WOOD, wood);

        JResource coin = new JResource("coin",resourceWidth);
        add(coin);
        resources.put(ResourceType.COIN, coin);

        JResource servant = new JResource("servant",resourceWidth);
        add(servant);
        resources.put(ResourceType.SERVANT, servant);

        familyGridPanel = new JPanel();
        familyGridPanel.setBackground(new Color(r, g, b));
        add(familyGridPanel);
        familyGridPanel.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel neutralFam = new JLabel("");
        ImageIcon neutral = new ImageIcon(ActionPanel.class.getResource("/"+playerColor.toLowerCase()+"neutralFamiliar.png"));
        Image img = neutral.getImage();
        img = img.getScaledInstance(img.getWidth(null) / 4, img.getHeight(null) / 4, java.awt.Image.SCALE_SMOOTH);
        neutral = new ImageIcon(img);
        neutralFam.setIcon(neutral);
        familyMembers.put(model.Color.NEUTRAL,neutralFam);

        JLabel whiteFam = new JLabel("");
        ImageIcon white = new ImageIcon(ActionPanel.class.getResource("/"+playerColor.toLowerCase()+"whiteFamiliar.png"));
        img = white.getImage();
        img = img.getScaledInstance(img.getWidth(null) / 4, img.getHeight(null) / 4, java.awt.Image.SCALE_SMOOTH);
        white = new ImageIcon(img);
        whiteFam.setIcon(white);
        familyMembers.put(model.Color.WHITE,whiteFam);

        JLabel orangeFam = new JLabel("");
        ImageIcon orange = new ImageIcon(ActionPanel.class.getResource("/"+playerColor.toLowerCase()+"orangeFamiliar.png"));
        img = orange.getImage();
        img = img.getScaledInstance(img.getWidth(null) / 4, img.getHeight(null) / 4, java.awt.Image.SCALE_SMOOTH);
        orange = new ImageIcon(img);
        orangeFam.setIcon(orange);
        familyMembers.put(model.Color.ORANGE,orangeFam);

        JLabel blackFam = new JLabel("");
        ImageIcon black = new ImageIcon(ActionPanel.class.getResource("/"+playerColor.toLowerCase()+"blackFamiliar.png"));
        img = black.getImage();
        img = img.getScaledInstance(img.getWidth(null) / 4, img.getHeight(null) / 4, java.awt.Image.SCALE_SMOOTH);
        black = new ImageIcon(img);
        blackFam.setIcon(black);
        familyMembers.put(model.Color.BLACK,blackFam);

        addAllFamilyMembers();
    }
    public void refreshResource(Resource r){
        resources.get(r.getResourceType()).setAmount(r.getAmount());
    }
    public void removeAllFamilyMembers(){
        familyGridPanel.removeAll();
    }
    public void addAllFamilyMembers(){

        for(int i=0;i<model.Color.values().length;i++)
            familyGridPanel.add(familyMembers.get(model.Color.values()[i]));
    }
    public void refreshFamilyMembers(HashMap<model.Color, FamilyMember> families) {
        this.removeAllFamilyMembers();

        Iterator<model.Color> iterator = families.keySet().iterator();

        for (;iterator.hasNext();)
            familyGridPanel.add(familyMembers.get(iterator.next()));

    }

    public void setUsername(String name){
        lblPlayername.setText(name);
    }
}
