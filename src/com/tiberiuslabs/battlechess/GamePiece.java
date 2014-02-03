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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (playerColor) {
            case BLACK:
                sb.append("B");
                break;
            case WHITE:
                sb.append("W");
                break;
        }

        switch (unitType) {
            case PAWN:
                sb.append("Pa");
                break;
            case ROOK:
                sb.append("Rk");
                break;
            case KNIGHT:
                sb.append("Kn");
                break;
            case BISHOP:
                sb.append("Bi");
                break;
            case KING:
                sb.append("Ki");
                break;
            case QUEEN:
                sb.append("Qu");
                break;
            default:
                sb.append("NA");
                break;
        }

        return sb.toString();
    }
}
