package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Highlight;
import com.tiberiuslabs.BattleChess.Types.Position;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;
import org.lwjgl.util.vector.Vector2f;

/**
 * Holds interface state of the tile
 * <ul>
 * <li>what color tile it should be
 * <li>which unit portrait should be displayed, if any
 * <li>whether/how it is highlighted
 * </ul>
 *  Also is the origin of the mouse click events
 * @author Amandeep Gill
 */
public class Tile {
    private final Vector2f location;
    private final Vector2f[] hexTile;
    private final Image backgroundImage;
    private Image unitPortrait;
    private Highlight highlight;

    /**
     * Instantiate the Tile with the given size and location
     * @param location          the x,y location to draw the tile on the screen
     * @param size              the length of the hexagon's edges
     * @param backgroundImage   the background image of the tile
     */
    public Tile(Vector2f location, int size, Image backgroundImage) {
        this.location = location;
        this.backgroundImage = backgroundImage;
        this.unitPortrait = null;

        hexTile = new Vector2f[6];
        double theta = Math.PI/6;

        for (int i = 0; i < 10; i += 2) {
            float x = (float)(size * Math.cos((i/2)*theta) + location.getX());
            float y = (float)(size * Math.sin((i/2)*theta) + location.getY());
            hexTile[i] = new Vector2f(x,y);
        }
    }

    /**
     * Get the background image for this tile
     * @return  an Image containing the background picture
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Set the image that represents the unit that is on this tile
     * @param unitPortrait  the unit's portrait image
     */
    public void setUnitPortrait(Image unitPortrait) {
        this.unitPortrait = unitPortrait;
    }

    /**
     * Set the highlighting for the Tile
     * @see com.tiberiuslabs.BattleChess.Types.Highlight
     * @param highlight the enumerated highlight type
     */
    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }

    /**
     * Gets the list of points that make up the six corners of the polygon
     * @return  an Observable List of the points of the hexagon
     */
    public Vector2f[] getPoints() {
        return hexTile;
    }

    /**
     * Gets the location on the center of the tile
     * @return  a Position object holding the (x,y) center of the Tile
     */
    public Vector2f getLocation() {
        return location;
    }

    /**
     * Get the image to display on the Tile
     * @return  an Image object referencing either the unit that is on the tile, or the default image
     */
    public Image getUnitPortrait() {
        return unitPortrait;
    }

    /**
     * Gets the current highlighting on the Tile
     * @see com.tiberiuslabs.BattleChess.Types.Highlight
     * @return  the enumerated highlight type
     */
    public Highlight getHighlight() {
        return highlight;
    }
}
