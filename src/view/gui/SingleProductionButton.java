package view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Button to place a family member in the single production area.
 */
public class SingleProductionButton extends JButton {
    private static final long serialVersionUID = 1L;
    private int widthRel = (int) (BoardPanel.dimension.getWidth()*wFIRST_SLOT);
    private int heightRel = (int) (BoardPanel.dimension.getHeight()*hFIRST_SLOT);
    private final static double wFIRST_SLOT = 0.17716535433070866141732283464567;
    private final static double hFIRST_SLOT = 0.08333333333333333333333333333333;
    private final static double wProduction = 0.06561679790026246719160104986877;
    private final static double hProduction = 0.81018518518518518518518518518519;
    private final static double wDIM_PERC = 0.07874015748031496062992125984252;
    private final static double hDIM_PERC = 0.05555555555555555555555555555556;

    public SingleProductionButton() {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
        setBorder(new RoundedBorder(25));
        setName("");
        setPosition();
    }

    private void setPosition() {
        widthRel = (int)(BoardPanel.dimension.getWidth()*wProduction);
        heightRel = (int)(BoardPanel.dimension.getHeight()*hProduction);
        setBounds(widthRel,heightRel,(int) (BoardPanel.dimension.getWidth()*this.wDIM_PERC),(int) (BoardPanel.dimension.getHeight()*this.hDIM_PERC));
    }
}
