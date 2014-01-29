package com.tiberiuslabs.battlechess;

public class GamePiece {
    public int x;
    public int y;
    public final Types.UnitType unitType;
    public final Types.Color playerColor;
    public final Types.Color startColor;
    public boolean hasMoved;
    
    public GamePiece(Types.UnitType unitType, Types.Color playerColor, int x, int y, Types.Color startColor) {
        this.x = x;
        this.y = y;
        this.unitType = unitType;
        this.playerColor = playerColor;
        this.startColor = startColor;
        this.hasMoved = false;
    }
}
