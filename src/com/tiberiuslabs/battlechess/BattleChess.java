package com.tiberiuslabs.battlechess;

import dk.ilios.asciihexgrid.*;
import dk.ilios.asciihexgrid.printers.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class BattleChess {

    private static int boardSize;
    private static Board board;

    public static void updateHexBoard(AsciiBoard ab, List<Position> positions) {
        for (Position pos : positions) {
            char x = (char)((int)'A' + pos.x + boardSize);
            int y = pos.y + boardSize + 1;
            GamePiece p = board.at(pos.x, pos.y);
            String loc = y < 10 ? x + "0" + y : "" + x + y;

            switch(board.tileColor(pos.x, pos.y)) {
                case WHITE:
                    if (p != null)
                        ab.printHex(p.toString(), loc, ' ', pos.x + boardSize, pos.y + boardSize);
                    else
                        ab.printHex("", loc, ' ', pos.x + boardSize, pos.y + boardSize);
                    break;
                case GREY:
                    if (p != null)
                        ab.printHex(p.toString(), loc, '-', pos.x + boardSize, pos.y + boardSize);
                    else
                        ab.printHex("- -", loc, '-', pos.x + boardSize, pos.y + boardSize);
                    break;
                case BLACK:
                    if (p != null)
                        ab.printHex(p.toString(), loc, '#', pos.x + boardSize, pos.y + boardSize);
                    else
                        ab.printHex("# #", loc, '#', pos.x + boardSize, pos.y + boardSize);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        AsciiBoard ab = new AsciiBoard(0, 11, 0, 11, new SmallFlatAsciiHexPrinter());
        board = new Board();
        boardSize = 5;
        List<Position> positions = new ArrayList<Position>();

        for (int q = -boardSize; q <= boardSize; q++) {
            for (int r = -boardSize; r <= boardSize; r++) {
                if (Math.abs(q + r) <= boardSize) {
                    positions.add(new Position(q, r));
                }
            }
        }

        updateHexBoard(ab, positions);

        System.out.println(ab.prettyPrint(true));
    }
}
