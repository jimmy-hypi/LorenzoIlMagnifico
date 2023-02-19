package model.area;

import constant.BoardConstants;
import constant.CardConstants;
import model.Period;
import model.excommunicationtile.ExcommunicationTile;
import model.excommunicationtile.ExcommunicationTilesCreator;
import model.resource.VictoryPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

/**
 * This class represents the church component of the game
 */
public class Church implements Serializable {
    private static final long serialVersionUID = 2245918610305460800L;
    private ExcommunicationTile excommunicationFirst;
    private ExcommunicationTile excommunicationSecond;
    private ExcommunicationTile excommunicationThird;
    private VictoryPoint[] victoryPoints;

    public Church(){
        Random rnd = new Random();
        ExcommunicationTile[] tiles = new ExcommunicationTile[CardConstants.EXCOMMUNICATION_TILES];
        try {
            tiles = ExcommunicationTilesCreator.createExcommunicationTiles(CardConstants.EXCOMMUNICATION_TILES);
        } catch (IOException e) {
            System.out.println("Error in retrieving excommunication tiles from file");
            e.printStackTrace();
        }
        this.excommunicationFirst = tiles[rnd.nextInt(CardConstants.EXCOMMUNICATION_TILES / Period.values().length)];
        this.excommunicationSecond = tiles[rnd.nextInt(CardConstants.EXCOMMUNICATION_TILES / Period.values().length)
                + CardConstants.EXCOMMUNICATION_TILES / Period.values().length];
        this.excommunicationThird = tiles[rnd.nextInt(CardConstants.EXCOMMUNICATION_TILES / Period.values().length)
                + 2 * CardConstants.EXCOMMUNICATION_TILES / Period.values().length];
        // We're not gonna need the tiles anymore after choosing the 3 tiles of
        // the match
        tiles = null;

        victoryPoints = new VictoryPoint[BoardConstants.CHURCHSLOTS];
        try {
            this.victoryPoints = BoardInitializer.churchBonuses();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExcommunicationTile getExcommunicationFirst() {
        return excommunicationFirst;
    }

    public ExcommunicationTile getExcommunicationSecond() {
        return excommunicationSecond;
    }

    public ExcommunicationTile getExcommunicationThird() {
        return excommunicationThird;
    }

    public ExcommunicationTile getExcommunicationTile(Period p){
        if(p==Period.FIRST)
            return this.excommunicationFirst;
        if(p==Period.SECOND)
            return this.excommunicationSecond;
        if(p==Period.THIRD)
            return this.excommunicationThird;

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°The Church°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");
        builder.append("The church contains the following Excommunication tiles:\n\n");
        builder.append(excommunicationFirst.toString() + "\n");
        builder.append(excommunicationSecond.toString() + "\n");
        builder.append(excommunicationThird.toString() + "\n\n");
        builder.append("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°\n\n");
        return builder.toString();
    }

    public VictoryPoint[] getVictoryPoints() {
        return victoryPoints;
    }
}
