package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Tile to originate click events and display the available recruit's portrait
 *
 * @author Amandeep Gill
 */
public class RecruitTile {
    private static int radius = 30;
    private static int imgSize = 40;
    private static Color BLNK = Color.WHITESMOKE;
    private static Color SELD = Color.BLUE;

    private final Group group;
    private final Circle circle;

    private ImageView unitPortrait;
    private Unit unit;
    private boolean selected;

    public RecruitTile(int centerX, int centerY) {
        this.selected = false;

        this.unitPortrait = new ImageView();
        this.unitPortrait.setX(centerX - imgSize / 2);
        this.unitPortrait.setY(centerY - imgSize / 2);

        this.circle = new Circle(centerX, centerY, radius, BLNK);

        this.group = new Group(circle, unitPortrait);
    }

    public void setSelectionCallback(RecruitSelectionCallback callback) {
        this.circle.setOnMouseClicked(event -> {
            if (unit != null) {
                callback.select(unit);
                if (selected) {
                    circle.setFill(BLNK);
                } else {
                    circle.setFill(SELD);
                }
                selected = !selected;
            }
            event.consume();
        });
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        if (unit != null) {
            unitPortrait.setImage(new Image(MainWindow.class.
                    getResourceAsStream("/art/unit/" + unit.getTypeString() + ".png"),
                    imgSize, imgSize, true, true
            ));
        } else {
            unitPortrait.setImage(null);
        }
    }

    public Group getGroup() {
        return group;
    }

    interface RecruitSelectionCallback {
        void select(Unit unit);
    }
}
