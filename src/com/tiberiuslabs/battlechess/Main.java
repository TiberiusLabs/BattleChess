package com.tiberiuslabs.battlechess;

import dk.ilios.asciihexgrid.*;
import dk.ilios.asciihexgrid.printers.*;
import java.lang.Math;

public class Main {

    public static void main(String[] args) {
        AsciiBoard ab = new AsciiBoard(0, 11, 0, 11, new SmallFlatAsciiHexPrinter());

        int boardSize = 5;
        int color = 1;

        for (int q = -boardSize; q <= boardSize; q++) {
            for (int r = -boardSize; r <= boardSize; r++) {
                if (Math.abs(q + r) <= boardSize) {
                    char x = (char)((int)'A' + q + boardSize);
                    int y = r + boardSize + 1;
                    switch(color) {
                        case 0:
                            ab.printHex("", ""+ x + y, ' ', q + boardSize, r + boardSize);
                            break;
                        case 1:
                            ab.printHex("", "" + x + y, '-', q + boardSize, r + boardSize);
                            break;
                        case 2:
                            ab.printHex("", "" + x + y, '#', q + boardSize, r + boardSize);
                            break;
                    }
                }

                color = ++color % 3;
            }
        }

        System.out.println(ab.prettyPrint(true));
    }
}
