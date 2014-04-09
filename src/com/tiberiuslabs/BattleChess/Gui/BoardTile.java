package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Highlight;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

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
public class BoardTile {
    private final Position position;
    private final Color backgroundColor;
    private final Polygon polygon;
    private final Text text;
    private final Group group;
    private Highlight highlight;
    private Unit unit;

    /**
     * Instantiate the BoardTile with the given size and position
     *
     * @param position        the x,y position to draw the tile on the screen
     * @param unit            the unit at this position, can be null
     * @param size            the length of the hexagon's edges
     * @param backgroundColor the background image of the tile
     */
    public BoardTile(Position position, Unit unit, int size, Color backgroundColor) {
        this.position = position;
        this.backgroundColor = backgroundColor;
        this.unit = unit;
        double x = position.x() * size * 1.5 + 500;
        double y = position.y() * size * sqrt(3) + position.x() * size * .9 + 300;

        highlight = Highlight.NONE;

        double[] verts = new double[12];
        double theta = PI / 6;

        for (int i = 0; i < 12; i += 2) {
            verts[i] = size * cos(i * theta) + x;
            verts[i + 1] = size * sin(i * theta) + y;
        }

        this.polygon = new Polygon(verts);
        this.text = new Text();
        text.setLayoutX(x);
        text.setLayoutY(y);
        group = new Group(polygon, text);
    }

    /**
     * Sets the listener callback for when selections are registered
     *
     * @param selectionListener the listener callback for when this BoardTile is selected
     */
    public void addSelectionListener(SelectionListener selectionListener) {
        group.setOnMouseClicked(event -> selectionListener.select(position));
    }

    /**
     * Repaint the tile onto the screen, highlights the tile as necessary and displays the unit on the tile
     */
    public void repaint() {
        polygon.setStrokeWidth(2);
        polygon.setStroke(Color.BLACK);
        switch (highlight) {
            case SELD:
                polygon.setFill(Color.BLUE);
                break;
            case THRT:
                polygon.setFill(Color.RED);
                break;
            case MOVE:
                polygon.setFill(Color.GREEN);
                break;
            case NONE:
                polygon.setFill(backgroundColor);
                break;
        }

        // TODO: display the unit portrait if it isn't null
        if (unit != null) {
            text.setFill(unit.color == com.tiberiuslabs.BattleChess.Types.Color.BLACK ? Color.CHOCOLATE : Color.LIGHTGRAY);
            switch (unit.unitType) {
                case PAWN:
                    text.setText("Pa");
                    break;
                case KNIGHT:
                    text.setText("Kn");
                    break;
                case ROOK:
                    text.setText("Ro");
                    break;
                case BISHOP:
                    text.setText("Bi");
                    break;
                case QUEEN:
                    text.setText("Qu");
                    break;
                case KING:
                    text.setText("Ki");
                    break;
            }
        } else {
            text.setText("");
        }
    }

    /**
     * Set the highlighting for the BoardTile
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
     * @return a Position object holding the (x,y) board coordinate of the BoardTile
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the Unit on this tile (may be null)
     *
     * @return the Unit currently on this tile (may be null)
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Set the Unit that is currently one this tile
     *
     * @param unit the unit at this position, can be null
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Node getGroup() {
        return group;
    }

    /**
     * Interface for the SelectionListener callback
     */
    interface SelectionListener {
        void select(Position position);
    }
}
