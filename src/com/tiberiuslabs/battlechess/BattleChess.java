package com.tiberiuslabs.battlechess;

import dk.ilios.asciihexgrid.*;
import dk.ilios.asciihexgrid.printers.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;
import java.text.ParseException;
import java.util.*;

public class BattleChess {

    private static int boardSize;
    private static Board board;
    private static Types.Color winner;
    private static char white = ' ';
    private static char grey = '.';
    private static char black = ':';

    public static void updateHexBoard(AsciiBoard ab, List<Position> positions) {
        for (Position pos : positions) {
            char x = (char)((int)'A' + pos.x + boardSize);
            int y = pos.y + boardSize + 1;
            GamePiece p = board.at(pos.x, pos.y);
            String loc = y < 10 ? x + " " + y : "" + x + y;

            switch(board.tileColor(pos.x, pos.y)) {
                case WHITE:
                    if (p != null)
                        ab.printHex(p.toString(), loc, white, pos.x + boardSize, pos.y + boardSize);
                    else
                        ab.printHex("", loc, white, pos.x + boardSize, pos.y + boardSize);
                    break;
                case GREY:
                    if (p != null)
                        ab.printHex(p.toString(), loc, grey, pos.x + boardSize, pos.y + boardSize);
                    else
                        ab.printHex("", loc, grey, pos.x + boardSize, pos.y + boardSize);
                    break;
                default:
                    if (p != null)
                        ab.printHex(p.toString(), loc, black, pos.x + boardSize, pos.y + boardSize);
                    else
                        ab.printHex("", loc, black, pos.x + boardSize, pos.y + boardSize);
                    break;
            }
        }
    }

    public static void showInfo(Types.Color playerColor) {
        List<String> availableRecruits = board.availableRecruits(playerColor);
        System.out.println("Cities you control: " + board.citiesHeld(playerColor));
        System.out.print("Available recruits: ");

        StringBuilder sb = new StringBuilder();
        boolean comma = false;
        for (String s : availableRecruits) {
            if (comma) {
                sb.append(", ");
            }
            else {
                comma = true;
            }
            sb.append(s);
        }

        System.out.println(sb.toString());
    }

    public static List<Position> useTurn(Types.Color playerColor) {
        List<Position> updatePos = new ArrayList<Position>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print(colorString(playerColor) + "'s turn: ");
            String pre_input;

            try {
                pre_input = bufferedReader.readLine();
                String input = pre_input.toUpperCase();

                try {
                    String[] inputArray = input.split(" ");
                    if (inputArray.length > 0) {
                        if (inputArray[0].charAt(0) == 'Q') {
                            winner = playerColor == Types.Color.BLACK ? Types.Color.WHITE : Types.Color.BLACK;
                            return updatePos;
                        }
                        else if (inputArray[0].charAt(0) == 'H') {
                            printRules(false);
                        }
                        else if (inputArray[0].charAt(0) == 'M') {
                            if (inputArray.length == 5) {
                                char x1 = inputArray[1].charAt(0);
                                char x2 = inputArray[3].charAt(0);
                                int y1 = Integer.parseInt(inputArray[2]);
                                int y2 = Integer.parseInt(inputArray[4]);

                                Position pos1 = convertPosition(x1, y1);
                                Position pos2 = convertPosition(x2, y2);

                                if (board.move(playerColor, pos1.x, pos1.y, pos2.x, pos2.y)) {
                                    updatePos.add(pos1);
                                    updatePos.add(pos2);
                                    return updatePos;
                                }
                                else {
                                    System.out.println("Invalid Move");
                                }
                            }
                            else {
                                System.out.println("Invalid Move");
                            }
                        }
                        else if (inputArray[0].charAt(0) == 'R') {
                            if (inputArray.length == 4) {
                                Types.UnitType unitType = convertUnit(inputArray[1]);

                                if (unitType == null) {
                                    System.out.println("Unknown Unit Type");
                                    continue;
                                }

                                Position pos = convertPosition(inputArray[2].charAt(0), Integer.parseInt(inputArray[3]));

                                if (board.recruit(unitType, playerColor, pos.x, pos.y)) {
                                    updatePos.add(pos);
                                    return updatePos;
                                }
                                else {
                                    System.out.println("Invalid Recruitment");
                                }
                            }
                        }
                        else {
                            System.out.println("Unknown Input");
                        }
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid Move");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public static Position convertPosition(char x, int y) {
        return new Position((int)x - (int)'A' - boardSize, y - boardSize - 1);
    }

    public static Types.UnitType convertUnit(String unitName) {
        int i = unitName.length() <= 2 ? 0 : 1;
        Types.UnitType unitType = null;

        switch (unitName.charAt(i)) {
            case 'P':
                unitType = Types.UnitType.PAWN;
                break;
            case 'R':
                unitType = Types.UnitType.ROOK;
                break;
            case 'B':
                unitType = Types.UnitType.BISHOP;
                break;
            case 'Q':
                unitType = Types.UnitType.QUEEN;
                break;
            case 'K':
                switch (unitName.charAt(i + 1)) {
                    case 'N':
                        unitType = Types.UnitType.KNIGHT;
                        break;
                    case 'I':
                        unitType = Types.UnitType.KING;
                        break;
                }
                break;
        }

        return unitType;
    }

    public static String colorString(Types.Color colorType) {
        String color = "";
        switch (colorType) {
            case BLACK:
                color = "Black";
                break;
            case WHITE:
                color = "White";
                break;
            case GREY:
                color = "Grey";
                break;
            case NEUTRAL:
                color = "Neutral";
                break;
        }

        return color;
    }

    public static void printRules(boolean printWelcome) {
        if (printWelcome) {
            System.out.println("WELCOME TO BATTLE CHESS!\n");
        }
        System.out.println("The Goal: remove all your opponent's units from the board\n" +
                           "      or: be the first to capture both Capitols (starting corners)\n\n" +
                           "Basic rules: units move according to Glinsky's Hexagonal Chess rules\n" +
                           "        * Pawns: can move one tile up (if White) or down (if Black)\n" +
                           "                 can move two tiles if they have not already moved\n" +
                           "                 can only attack pieces that are diagonally forward and one tile away\n" +
                           "        * Rooks: can move and attack along any of the 6 diagonals from their current position\n" +
                           "        * Bishop: can move and attack any of the 6 edges radiating from their current position\n" +
                           "        * Knight: can move and attack by moving two tiles diagonally from the current position,\n" +
                           "                 followed by a single diagonal move outward from that position\n" +
                           "        * Queen: can move like either the Rook or the Bishop\n" +
                           "        * King:  can move like the Queen, but only one tile away\n\n" +
                           "        * For further explanation of the movement rules: http://en.wikipedia.org/wiki/Hexagonal_chess\n\n" +
                           "        * Recruitment: if you control three or more Villages (any corner), then you have access to the recruits\n" +
                           "                * you must control your Capitol in order to recruit new units\n" +
                           "                * your King must be on one of the Villages or your Capitol\n" +
                           "                * you may only recruit units that have been removed from your Army\n" +
                           "                * recruits must enter the Battle adjacent to one of the Villages or Capitols under your control\n\n" +
                           "Controls:\n" +
                           "        * Moving a unit: [m]ove current[a] current[#] final[a] final[#]\n" +
                           "        * Recruiting a unit: [r]ecruit [unit] final[a] final[#]\n" +
                           "        * Print rules: [h]elp\n" +
                           "        * Forfeit match: [q]uit\n");
    }

    public static void main(String[] args) {
        printRules(true);
        System.out.print("[Press enter to continue]");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.exit(-1);
        }


        // AsciiBoard ab = new AsciiBoard(0, 11, 0, 11, new SmallFlatAsciiHexPrinter());
        board = new Board();
        boardSize = 5;
        winner = Types.Color.NEUTRAL;

        List<Position> positions = new ArrayList<Position>();

        for (int q = -boardSize; q <= boardSize; q++) {
            for (int r = -boardSize; r <= boardSize; r++) {
                if (Math.abs(q + r) <= boardSize) {
                    positions.add(new Position(q, r));
                }
            }
        }

        boolean whiteTurn = true;
        while (winner == Types.Color.NEUTRAL) {
            AsciiBoard ab = new AsciiBoard(0, 11, 0, 11, new SmallFlatAsciiHexPrinter());
            board.update();
            updateHexBoard(ab, positions);
            System.out.println(ab.prettyPrint(true));
            // positions.clear();

            Types.Color playerColor = whiteTurn ? Types.Color.WHITE : Types.Color.BLACK;
            showInfo(playerColor);
            // positions.addAll(
            useTurn(playerColor);

            if (winner == Types.Color.NEUTRAL) {
                winner = board.winner();
            }
            whiteTurn = !whiteTurn;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(winner == Types.Color.BLACK ? "Black" : "White").append(" Wins!");
        System.out.println(stringBuilder.toString());
    }
}
