package model;

import java.io.Serializable;
import java.util.Random;

/**
 * Dice Abstract Data Type
 */
public enum Dice implements Serializable {
    /** The orange dice. */
    ORANGE_DICE(Color.ORANGE,5),
    /** The black dice. */
    BLACK_DICE(Color.BLACK,0),
    /** The white dice. */
    WHITE_DICE(Color.WHITE,0),
    /** The neutral dice. */
    NEUTRAL_DICE(Color.NEUTRAL,0);

    private Color color;
    private int displayedFace;

    private Dice(Color color, int val){
        this.color = color;
        this.displayedFace = val;
    }
    public void roll(){
        Random random = new Random();
        if(this.color != Color.NEUTRAL)
            this.displayedFace = random.nextInt(6)+1;
    }
    public int getUpperFaceValue(){
        return displayedFace;
    }
    public int getRandomFaceValue(){
        this.roll();
        return this.getUpperFaceValue();
    }
    public Color getColor(){
        return color;
    }
}
