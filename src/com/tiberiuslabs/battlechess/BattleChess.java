package com.tiberiuslabs.battlechess;

import dk.ilios.asciihexgrid.*;
import dk.ilios.asciihexgrid.printers.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.*;

public class BattleChess {

    private static int boardSize;
    private static Board board;
    private static Types.Color winner;

    public static void updateHexBoard(AsciiBoard ab, List<Position> positions) {
        for (Position pos : positions) {
            char x = (char)((int)'A' + pos.x + boardSize);
            int y = pos.y + boardSize + 1;
            GamePiece p = board.at(pos.x, pos.y);
            String loc = y < 10 ? x + " " + y : "" + x + y;

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
                                    break;
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
                                    break;
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
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }

        return updatePos;
    }

    public static Position convertPosition(char x, int y) {
        return new Position((int)x - (int)'A' - boardSize - 1, y - boardSize - 1);
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

    public static void main(String[] args) {
        AsciiBoard ab = new AsciiBoard(0, 11, 0, 11, new SmallFlatAsciiHexPrinter());
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
            board.update();
            updateHexBoard(ab, positions);
            System.out.println(ab.prettyPrint(true));
            positions.clear();

            Types.Color playerColor = whiteTurn ? Types.Color.WHITE : Types.Color.BLACK;
            showInfo(playerColor);
            positions = useTurn(playerColor);

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
