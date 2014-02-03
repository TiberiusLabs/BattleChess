package com.tiberiuslabs.battlechess;

import dk.ilios.asciihexgrid.*;
import dk.ilios.asciihexgrid.printers.*;
import java.lang.Math;

public class Main {

    public static void main(String[] args) {
        AsciiBoard ab = new AsciiBoard(0, 11, 0, 11, new SmallFlatAsciiHexPrinter());
        Board board = new Board();
        int boardSize = 5;

        for (int q = -boardSize; q <= boardSize; q++) {
            for (int r = -boardSize; r <= boardSize; r++) {
                if (Math.abs(q + r) <= boardSize) {
                    char x = (char)((int)'A' + q + boardSize);
                    int y = r + boardSize + 1;
                    GamePiece p = board.at(q, r);
                    String loc = y < 10 ? new String(x + "0" + y) : new String("" + x + y);
                    switch(board.tile(q, r)) {
                        case WHITE:
                            if (p != null)
                                ab.printHex(p.toString(), loc, ' ', q + boardSize, r + boardSize);
                            else
                                ab.printHex("", loc, ' ', q + boardSize, r + boardSize);
                            break;
                        case GREY:
                            if (p != null)
                                ab.printHex(p.toString(), loc, '-', q + boardSize, r + boardSize);
                            else
                                ab.printHex("- -", loc, '-', q + boardSize, r + boardSize);
                            break;
                        case BLACK:
                            if (p != null)
                                ab.printHex(p.toString(), loc, '#', q + boardSize, r + boardSize);
                            else
                                ab.printHex("# #", loc, '#', q + boardSize, r + boardSize);
                            break;
                    }
                }
            }
        }

        System.out.println(ab.prettyPrint(true));
    }
}
