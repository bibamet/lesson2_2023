package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static Players curPlayer = null;
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random(10);


    public static void main(String[] args) {

        System.out.println("Введите размер игрового поля в формете NxN...");
        String size = scanner.nextLine();

        String[] xes = size.split("x");
        List<String> collect = Arrays.stream(xes).filter(x -> Character.isDigit(x.charAt(0))).collect(Collectors.toList());

        if (xes.length != 2 || collect.size() != 2) {
            System.out.println("Вы ввели размер в неправильном формате");
            return;
        }

        int rows = Integer.parseInt(xes[0]);
        int columns = Integer.parseInt(xes[1]);

        char[][] userMap = new char[rows][columns];
        char[][] computerMap = new char[rows][columns];

        fill(userMap);
        fill(computerMap);

        char[][] userShoots = new char[rows][columns];
        char[][] computerShoots = new char[rows][columns];

//        printMap(userMap, rows, columns);

        System.out.println("Первым будет ходить */барабанная дробь/* ...");
        curPlayer = Players.values()[random.nextInt(0, 2)];
        System.out.println(curPlayer.getName());

        playGame(userMap, computerMap, userShoots, computerShoots, rows, columns);

    }

    private static void playGame(char[][] userMap, char[][] computerMap, char[][] userShoots, char[][] computerShoots, int rows, int columns) {

        int i, j;
        StringBuilder val = new StringBuilder();
        boolean gameIsContinue = true;

        while (gameIsContinue) {

            if (curPlayer == Players.USER) {
                System.out.println("Твои корабли:");
                printMap(userMap, rows, columns);
                System.out.println("Твои выстрелы:");
                printMap(userShoots, rows, columns);
                System.out.println("Введите номер ячейки в формате 19, где 1 - номер строки по вертикали, 9 - номер колонки по горизонтали ...");

                while (true) {
                    String s = scanner.nextLine();
                    if ("".equals(s)) {
                        continue;
                    }
                    val.delete(0, val.length()).append(s);
                    if (val.length() != 2 && (!Character.isDigit(val.charAt(0)) || !Character.isDigit(val.charAt(1)))) {
                        System.out.println("Вы неправильно ввели номер ячейки. Попробуйте еще раз.");
                        continue;
                    }
                    break;
                }

               i = Integer.parseInt(String.valueOf(val.charAt(0)));
               j = Integer.parseInt(String.valueOf(val.charAt(1)));

               if (computerMap[i][j] == Cell.ALIVE_SHIP.getValue() || computerMap[i][j] == Cell.DEAD_SHIP.getValue()) {
                   userShoots[i][j] = Cell.DEAD_SHIP.getValue();
                   computerMap[i][j] = Cell.DEAD_SHIP.getValue();
                   System.out.println("Попадание!");
               } else {
                   userShoots[i][j] = Cell.MISS_SHOOT.getValue();
                   curPlayer = Players.COMPUTER;
                   System.out.println("Мимо...");
               }

                gameIsContinue = gameIsContinue(computerMap);
                if (!gameIsContinue) {
                    System.out.println("ЕХЭЭЭЭЙ БАЛЯЯЯ, ТЫ ВЫИГРАЛ");
                }

            } else {

                i = random.nextInt(0, 10);
                j = random.nextInt(0, 10);

                System.out.print(String.format("Компьютер стреляет в ячейку %d %d ............", i, j));

                if (userMap[i][j] == Cell.ALIVE_SHIP.getValue() || userMap[i][j] == Cell.DEAD_SHIP.getValue()) {
                    computerShoots[i][j] = Cell.DEAD_SHIP.getValue();
                    userMap[i][j] = Cell.DEAD_SHIP.getValue();
                    System.out.println(" и попадает!");
                } else {
                    computerShoots[i][j] = Cell.MISS_SHOOT.getValue();
                    userMap[i][j] = Cell.MISS_SHOOT.getValue();
                    curPlayer = Players.USER;
                    System.out.println(" и промахивается!");
                }

                gameIsContinue = gameIsContinue(userMap);

                if (!gameIsContinue) {
                    System.out.println("Эх, к сожалению, выиграл компьютер");
                }
            }

        }

    }

    private static boolean gameIsContinue(char[][] map) {
        for (char[] lowMap : map) {
            for (char c : lowMap) {
                if (c == Cell.ALIVE_SHIP.value) return true;
            }
        }
        return false;
    }


    private static void printMap(char[][] map, int rows, int columns) {
        System.out.print("  ");
        for (int i = 0; i < columns; i++) {
            System.out.print(" " + i);
        }
        System.out.println(" ");

        for (int i = 0; i < rows; i++) {

            if (String.valueOf(i).length() > 1) {
                System.out.print(i);
            } else {
                System.out.print(i + " ");
            }

            for (int j = 0; j < map[i].length; j++) {
                System.out.print(" " + map[i][j]);
            }
            System.out.println(" ");
        }
    }

    public static void fill(char[][] map) {

        fillEmpty(map);

        Ships[] values = Ships.values();

        for (Ships ship : values) {

            for (int i = 0; i < ship.getValue(); i++) {
                fillMap(map, ship.getSize());
            }
        }
    }

    private static void fillEmpty(char[][] map) {
        for (char[] lowMap : map) {
            Arrays.fill(lowMap, Cell.EMPTY.getValue());
        }
    }

    private static void fillMap(char[][] map, int size) {

        boolean filled = false;

        while (!filled) {
            filled = fillMapVertical(map, size);
            if (!filled) {
                filled = fillMapHorizontal(map, size);
            }
        }

    }

    private static boolean fillMapVertical(char[][] map, int size) {

        int row = random.nextInt(10);
        int column = random.nextInt(10);

        if (row - size + 1 >= 0) {

            for (int i = 0; i < size; i++) {
                if (map[row - i][column] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column - 1 >= 0) {
                    if (map[row - i][column - 1] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }

                if (column + 1 <= map[0].length - 1) {
                    if (map[row - i][column + 1] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }
            }

            if (row + 1 <= map.length - 1) {

                if (map[row + 1][column] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column + 1 <= map[0].length - 1 && map[row + 1][column + 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column - 1 >= 0 && map[row + 1][column - 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            if (row - size >= 0) {

                if (map[row - size][column] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column + 1 <= map[0].length - 1 && map[row - size][column + 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column - 1 >= 0 && map[row - size][column - 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            for (int i = 0; i < size; i++) {
                map[row - i][column] = Cell.ALIVE_SHIP.getValue();
            }

            return true;

        }

        if (row + size - 1 <= map.length - 1) {

            for (int i = 0; i < size; i++) {
                if (map[row + i][column] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column - 1 >= 0) {
                    if (map[row + i][column - 1] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }

                if (column + 1 <= map[0].length - 1) {
                    if (map[row + i][column + 1] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }
            }

            if (row + size <= map.length - 1) {

                if (map[row + size][column] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column + 1 <= map[0].length - 1 && map[row + size][column + 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column - 1 >= 0 && map[row + size][column - 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            if (row - 1 >= 0) {

                if (map[row - 1][column] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column + 1 <= map[0].length - 1 && map[row - 1][column + 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (column - 1 >= 0 && map[row - 1][column - 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            for (int i = 0; i < size; i++) {
                map[row + i][column] = Cell.ALIVE_SHIP.getValue();
            }

            return true;

        }

        return false;

    }

    private static boolean fillMapHorizontal(char[][] map, int size) {

        int row = random.nextInt(10);
        int column = random.nextInt(10);

        if (column - size + 1 >= 0) {

            for (int i = 0; i < size; i++) {
                if (map[row][column - i] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row - 1 >= 0) {
                    if (map[row - 1][column - i] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }

                if (row + 1 <= map[0].length - 1) {
                    if (map[row + 1][column - i] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }
            }

            if (column + 1 <= map[0].length - 1) {

                if (map[row][column + 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row + 1 <= map.length - 1 && map[row + 1][column + 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row - 1 >= 0 && map[row - 1][column + 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            if (column - size >= 0) {

                if (map[row][column - size] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row + 1 <= map.length - 1 && map[row + 1][column - size] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row - 1 >= 0 && map[row - 1][column - size] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            for (int i = 0; i < size; i++) {
                map[row][column - i] = Cell.ALIVE_SHIP.getValue();
            }

            return true;

        }

        if (column + size - 1 <= map[0].length - 1) {

            for (int i = 0; i < size; i++) {
                if (map[row][column + i] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row - 1 >= 0) {
                    if (map[row -1][column + i] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }

                if (row + 1 <= map.length - 1) {
                    if (map[row + 1][column + 1] != Cell.EMPTY.getValue()) {
                        return false;
                    }
                }
            }

            if (column + size <= map[0].length - 1) {

                if (map[row][column + size] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row + 1 <= map.length - 1 && map[row + 1][column + size] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row - 1 >= 0 && map[row - 1][column + size] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            if (column - 1 >= 0) {

                if (map[row][column - 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row + 1 <= map.length - 1 && map[row + 1][column - 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

                if (row - 1 >= 0 && map[row - 1][column - 1] != Cell.EMPTY.getValue()) {
                    return false;
                }

            }

            for (int i = 0; i < size; i++) {
                map[row][column + i] = Cell.ALIVE_SHIP.getValue();
            }

            return true;

        }
        return false;
    }

    public enum Cell {
        ALIVE_SHIP('A'),
        DEAD_SHIP('X'),
        MISS_SHOOT('0'),
        EMPTY(' ');

        char value;

        Cell(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum Ships {
        FOUR_SHIP(1, 4),
        THREE_SHIP(2, 3),
        TWO_SHIP(3, 2),
        ONE_SHIP(4, 1);

        int value;
        int size;

        Ships(int value, int size) {
            this.value = value;
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Players {
        USER("ИГРОК"),
        COMPUTER("КОМПЬЮТЕР");

        String name;

        Players(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}


