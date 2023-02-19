package view.gui;

import model.card.LeaderCard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeadersPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private List<JLeaderCard> leaders;

    public LeadersPanel() {
        leaders=new ArrayList<>();
    }

    public void addLeaderCards(Map<String, LeaderCard> leaderCards){
        ArrayList<LeaderCard> leaderArray = new ArrayList<LeaderCard>(leaderCards.values());

        for(LeaderCard leader: leaderArray){
            JLeaderCard jLeaderCard=new JLeaderCard(leader.getName());
            jLeaderCard.setToolTipText(leader.toString());
            leaders.add(jLeaderCard);
            add(jLeaderCard);
        }
    }

    public void refreshLeaderCards(Map<String,LeaderCard> leaderCards, GamePanel gamePanel){
        ArrayList<LeaderCard> leaderArray = new ArrayList<LeaderCard>(leaderCards.values());

        leaders.clear();
        this.removeAll();
        this.addLeaderCardsWithListener(leaderArray, gamePanel);
    }

    /**
     * Are leader cards.
     *
     * @param size the number of leader cards
     * @return true if the number of leader cards in the panel is equal to size
     */
    public boolean areLeaderCards(int size) {
        return leaders.size()==size;
    }

    public void addLeaderCards(ArrayList<LeaderCard> leaderCards) {
        for(LeaderCard leader: leaderCards){
            JLeaderCard jLeaderCard=new JLeaderCard(leader.getName());
            jLeaderCard.setToolTipText(leader.toString());
            leaders.add(jLeaderCard);
            add(jLeaderCard);
        }
    }

    public void addLeaderCardsWithListener(ArrayList<LeaderCard> leaderCards, GamePanel actionListener) {
        for(LeaderCard leader: leaderCards){
            JLeaderCard jLeaderCard=new JLeaderCard(leader.getName());
            jLeaderCard.setToolTipText(leader.toString());
            leaders.add(jLeaderCard);
            add(jLeaderCard);
            jLeaderCard.addActionListener(actionListener);
        }
    }

    public void deleteLeaders() {
        this.removeAll();
    }
}
