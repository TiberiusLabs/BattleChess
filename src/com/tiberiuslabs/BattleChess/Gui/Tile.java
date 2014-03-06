package com.tiberiuslabs.BattleChess.Gui;

import com.sun.javafx.geom.Vec2f;
import com.tiberiuslabs.BattleChess.Types.Highlight;
import com.tiberiuslabs.BattleChess.Types.Position;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Holds interface state of the tile
 * <ul>
 * <li>what color tile it should be
 * <li>which unit portrait should be displayed, if any
 * <li>whether/how it is highlighted
 * </ul>
 * Also is the origin of the mouse click events
 *
 * @author Amandeep Gill
 */
public class Tile {
    private final Position position;
    private final Color backgroundColor;
    private final Polygon polygon;
    private Image unitPortrait;
    private Highlight highlight;

    /**
     * Instantiate the Tile with the given size and position
     *
     * @param position        the x,y position to draw the tile on the screen
     * @param size            the length of the hexagon's edges
     * @param backgroundColor the background image of the tile
     * @param canvas          the canvas that the tile is drawn on
     */
    public Tile(Position position, int size, Color backgroundColor, Canvas canvas) {
        this.position = position;
        this.backgroundColor = backgroundColor;
        this.unitPortrait = null;
        Vec2f location = new Vec2f((float) (position.x() * size * 1.5 + 300), position.y() * size * 2 + position.x() * size + 300);

        double[] vertices = new double[12];
        double theta = Math.PI / 6;

        for (int i = 0; i < 12; i += 2) {
            vertices[i] = size * Math.cos((i / 2) * theta) + location.x;
            vertices[i+1] = size * Math.sin((i / 2) * theta) + location.y;
        }

        polygon = new Polygon(vertices);
    }

    /**
     * Sets the listener callback for when selections are registered
     *
     * @param selectionListener the listener callback for when this Tile is selected
     */
    public void addSelectionListener(SelectionListener selectionListener) {
        polygon.setOnMouseClicked(mouseEvent -> {
            selectionListener.select(position);
        });
    }

    public void repaint() {
        polygon.setFill(backgroundColor);
        switch (highlight) {
            case SELD:
                polygon.setStrokeWidth(5);
                polygon.setStroke(Color.BLUE);
                break;
            case THRT:
                polygon.setStrokeWidth(5);
                polygon.setStroke(Color.RED);
                break;
            case MOVE:
                polygon.setStrokeWidth(5);
                polygon.setStroke(Color.GREEN);
                break;
            case NONE:
                polygon.setStrokeWidth(1);
                polygon.setStroke(Color.BLACK);
                break;
        }
    }

    /**
     * Set the image that represents the unit that is on this tile
     *
     * @param unitPortrait the unit's portrait image
     */
    public void setUnitPortrait(Image unitPortrait) {
        this.unitPortrait = unitPortrait;
    }

    /**
     * Set the highlighting for the Tile
     *
     * @param highlight the enumerated highlight type
     * @see com.tiberiuslabs.BattleChess.Types.Highlight
     */
    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }

    /**
     * Gets the position of the tile
     *
     * @return a Position object holding the (x,y) board coordinate of the Tile
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the image to display on the Tile
     *
     * @return an Image object referencing either the unit that is on the tile, or the default image
     */
    public Image getUnitPortrait() {
        return unitPortrait;
    }

    /**
     * Gets the current highlighting on the Tile
     *
     * @return the enumerated highlight type
     * @see com.tiberiuslabs.BattleChess.Types.Highlight
     */
    public Highlight getHighlight() {
        return highlight;
    }

    /**
     * Interface for the SelectionListener callback
     */
    interface SelectionListener {
        void select(Position position);
    }
}
