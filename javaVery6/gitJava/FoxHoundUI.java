import java.util.Scanner;
import java.util.Objects;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all user interface related
 * functionality such as printing menus and displaying the game board.
 */
public class FoxHoundUI {

    /** Number of main menu entries. */
    private static final int MENU_ENTRIES = 4;
    /** Main menu display string. */
    private static final String MAIN_MENU = "\n1. Move\n2. Save Game\n3. Load Game\n4. Exit\n\nEnter 1 - 4:";

    /** Menu entry to select a move action. */
    public static final int MENU_MOVE = 1;
    public static final int MENU_SAVE = 2;
    public static final int MENU_LOAD = 3;
    public static final int MENU_EXIT = 4;
    static final String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };

    public static int find(String[] arr, char str) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].charAt(0) == str) {
                return i;
            }
        }
        return -1;
    }

    // floor looks like this:|===|===|===|===|
    public static String[] floorGenerator(int dimension) {
        String[] floor = new String[dimension + 2];
        floor[0] = "   ";
        floor[floor.length - 1] = "|   ";
        for (int i = 1; i < dimension + 1; i++) {
            floor[i] = "|===";
        }
        return floor;
    }

    // scale looks like this:A B C D E F G H I J
    public static String[] scaleGenerator(int dimension) {
        String[] scale = new String[dimension + 2];
        scale[0] = "   ";
        scale[scale.length - 1] = "    ";
        for (int i = 1; i < dimension + 1; i++) {
            scale[i] = "  " + alphabet[i - 1] + " ";
        }
        return scale;
    }

    // board is the original board without any players on it, also without floor and
    // scale,
    // but includes the numbers on both sides of the board.
    public static String[][] boardGenerator(int dimension) {
        String[][] board = new String[dimension][dimension + 2];
        for (int i = 0; i < dimension; i++) {
            String sideNum = String.format("%02d", i + 1);
            board[i][0] = sideNum + " ";
            board[i][dimension + 1] = "| " + sideNum;
            for (int j = 1; j < dimension + 1; j++) {
                board[i][j] = "|   ";
            }
        }
        return board;
    }

    public static void addPlayerToBoard(String[] players, String[][] board) {
        // add hounds
        for (int i = 0; i < players.length - 1; i++) {
            int index1 = Integer.parseInt(String.valueOf(players[i].charAt(1)) + players[i].charAt(2)) - 1;
            int index2 = find(alphabet, players[i].charAt(0)) + 1;
            board[index1][index2] = "| H ";
        }
        // add fox
        int index1 = Integer.parseInt(
                String.valueOf(players[players.length - 1].charAt(1)) + players[players.length - 1].charAt(2)) - 1;
        int index2 = find(alphabet, players[players.length - 1].charAt(0)) + 1;
        board[index1][index2] = "| F ";
    }

    // print an one dimensional array in a single line, then move to the next line
    public static void printArray(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    public static void displayBoard(String[] players, int dimension) {
        String[] floor = floorGenerator(dimension);
        String[] scale = scaleGenerator(dimension);
        String[][] board = boardGenerator(dimension);
        addPlayerToBoard(players, board);
        printArray(scale);
        printArray(floor);
        for (int i = 0; i < dimension; i++) {
            printArray(board[i]);
            printArray(floor);
        }
        printArray(scale);
    }

    /**
     * Print the main menu and query the user for an entry selection.
     * 
     * @param figureToMove the figure type that has the next move
     * @param stdin        a Scanner object to read user input from
     * @return a number representing the menu entry selected by the user
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException     if the given Scanner is null
     */
    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD
                && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure = figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    public static void getInputAndMove(int dim, String[] players, char figure, Scanner stdin) {
        System.out.println("Provide origin and destination coordinates.");
        System.out.printf("Example: A01 to %s%02d\n", alphabet[dim - 1], dim);
        String coordinates = stdin.nextLine().toUpperCase();
        String[] coordinatesArray = coordinates.split(" ");
        if (!FoxHoundUtils.isValidMove(dim, players, figure, coordinatesArray[0],
                coordinatesArray[1])) {
            System.out.println(" ERROR: Please enter valid coordinate pair separated by space.");
            getInputAndMove(dim, players, figure, stdin);
        } else {
            for (int i = 0; i < players.length; i++) {
                if (players[i].equals(coordinatesArray[0])) {
                    players[i] = coordinatesArray[1];
                    System.out.println(players[i] + " has moved.");
                }
            }
        }
        displayBoard(players, dim);
        System.out.println("Move successful.");

    }

    public static boolean checkWin(char figure, String[] players, int dim) {
        if (figure == FoxHoundUtils.FOX_FIELD) {
            return FoxHoundUtils.isFoxWin(players, dim);
        } else {
            return FoxHoundUtils.isHoundWin(players, dim);
        }
    }

}
