package com.tiberiuslabs.BattleChess.Gui;

import com.sun.javafx.geom.Vec2d;
import com.tiberiuslabs.BattleChess.Types.Highlight;
import com.tiberiuslabs.BattleChess.Types.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static java.lang.Math.*;

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
    private final Canvas canvas;
    private final Vec2d location;
    private int size;
    private final double[] xVertices;
    private final double[] yVertices;
    private SelectionListener selectionListener;
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
        this.size = size;
        location = new Vec2d(position.x() * size * 1.5 + 300, position.y() * size * 2 + position.x() * size + 300);

        highlight = Highlight.NONE;

        xVertices = new double[6];
        yVertices = new double[6];
        double theta = PI / 3;

        for (int i = 0; i < 6; i += 1) {
            xVertices[i] = size * cos(i * theta) + location.x;
            yVertices[i] = size * sin(i * theta) + location.y;
        }

        this.canvas = canvas;
        this.canvas.setOnMouseClicked(event -> {
            if (this.contains(event.getX(), event.getY())) {
                selectionListener.select(position);
                event.consume();
            }
        });
    }

    /**
     * Sets the listener callback for when selections are registered
     *
     * @param selectionListener the listener callback for when this Tile is selected
     */
    public void addSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    /**
     * Repaint the tile onto the screen, highlights the tile as necessary and displays the unit on the tile
     */
    public void repaint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(backgroundColor);
        switch (highlight) {
            case SELD:
                gc.setLineWidth(5);
                gc.setStroke(Color.BLUE);
                break;
            case THRT:
                gc.setLineWidth(5);
                gc.setStroke(Color.RED);
                break;
            case MOVE:
                gc.setLineWidth(5);
                gc.setStroke(Color.GREEN);
                break;
            case NONE:
                gc.setLineWidth(1);
                gc.setStroke(Color.BLACK);
                break;
        }

        gc.fillPolygon(xVertices, yVertices, 6);
        // TODO: display the unit portrait if it isn't null

        gc.save();
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
     * Checks that the given x,y coordinate is inside the polygon
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the x,y is no more than 2*size from any vertex, false otherwise
     */
    public boolean contains(double x, double y) {
        for (int i = 0; i < 6; i += 1) {
            if (location.distance(x, y) > 2 * size) {
                return false;
            }
        }
        return true;
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
     * Set the image that represents the unit that is on this tile
     *
     * @param unitPortrait the unit's portrait image
     */
    public void setUnitPortrait(Image unitPortrait) {
        this.unitPortrait = unitPortrait;
    }

    /**
     * Interface for the SelectionListener callback
     */
    interface SelectionListener {
        void select(Position position);
    }
}
