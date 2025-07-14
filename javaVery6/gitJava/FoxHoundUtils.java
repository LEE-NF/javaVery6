/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions to check the state of the game
 * board and validate board coordinates and figure positions.
 */

public class FoxHoundUtils {

    /** Default dimension of the game board in case none is specified. */
    public static final int DEFAULT_DIM = 4;
    /** Minimum possible dimension of the game board. */
    public static final int MIN_DIM = 4;
    /** Maximum possible dimension of the game board. */
    public static final int MAX_DIM = 26;

    /** Symbol to represent a hound figure. */
    public static final char HOUND_FIELD = 'H';
    /** Symbol to represent the fox figure. */
    public static final char FOX_FIELD = 'F';

    // HINT Write your own constants here to improve code readability ...
    public static final String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    public static boolean isValidMove(int dim, String[] players, char figure, String origin, String destination) {
        if (players == null || players.length == 0) {
            throw new IllegalArgumentException("Players array cannot be null or empty");
        }
        if (figure != HOUND_FIELD && figure != FOX_FIELD) {
            throw new IllegalArgumentException("Invalid figure: " + figure);
        }
        if (dim < 4 || dim > 26) {
            throw new IllegalArgumentException("Dimension must be between " + MIN_DIM + "and " + MAX_DIM);
        }
        if (figure == HOUND_FIELD
                && Integer.parseInt(destination.substring(1)) < Integer.parseInt(origin.substring(1))) {
            System.out.println("Hound cannot move backwards");
            return false;
        }
        if (origin == null || destination == null) {
            System.out.println("Both origin and destination must be specified");
            return false;
        }
        if (origin.length() != 3 || destination.length() != 3) {
            System.out.println("Both origin and destination must be in the format A01, B02, C03, etc.");
            return false;
        }
        boolean pass = false;
        int index = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(destination)) {
                System.out.println("destination occupied");
                return false;
            }
            if (players[i].equals(origin)) {
                index = i;
                pass = true;
            }
        }
        if (!pass) {
            System.out.printf("There is no player at %s\n", origin);
            return false;
        }

        if (index == players.length - 1) {
            if (figure == HOUND_FIELD) {
                System.out.println("It is hound turn, fox cannot move");
                return false;
            }
        } else {
            if (figure == FOX_FIELD) {
                System.out.println("It is fox turn, hound cannot move");
                return false;
            }
        }

        if (FoxHoundUI.find(alphabet, destination.charAt(0)) > dim - 1
                || FoxHoundUI.find(alphabet, destination.charAt(0)) == -1) {
            System.out.println("x coordinate out of range");
            return false;
        }

        if (Integer.parseInt(destination.substring(1)) > dim ||
                Integer.parseInt(destination.substring(1)) < 1) {
            System.out.println("y coordinate out of range");
            return false;
        }
        if (Math.abs(origin.charAt(0) - destination.charAt(0)) != 1
                || Math.abs(Integer.parseInt(origin.substring(1)) - Integer.parseInt(destination.substring(1))) != 1) {
            System.out.println("you can move only one step at a time in an diagonal direction");
            return false;
        }
        return true;
    }

    public static String[] initialisePositions(int dimension) {
        if (dimension < MIN_DIM || dimension > MAX_DIM) {
            throw new IllegalArgumentException("Dimension must be between " + MIN_DIM + " and " + MAX_DIM);
        }
        String[] players = new String[dimension / 2 + 1];
        // coordinates of hounds
        for (int i = 0; i < dimension / 2; i++) {
            players[i] = alphabet[(1 + i) * 2 - 1] + "01";
        }
        // coordinates of fox
        String yCoordinate = String.format("%02d", dimension);
        // check if the fox is on an even or odd row
        if (dimension % 2 == 0 && dimension / 2 % 2 == 0) {
            players[dimension / 2] = alphabet[dimension / 2] + yCoordinate;
        } else if (dimension % 2 == 1 && dimension / 2 % 2 == 0) {
            players[dimension / 2] = alphabet[dimension / 2 + 1] + yCoordinate;
        } else if (dimension % 2 == 0 && dimension / 2 % 2 == 1) {
            players[dimension / 2] = alphabet[dimension / 2 - 1] + yCoordinate;
        } else if (dimension % 2 == 1 && dimension / 2 % 2 == 1) {
            players[dimension / 2] = alphabet[dimension / 2] + yCoordinate;
        }
        return players;
    }

    public static boolean cannotMove(String[] players, String position, int dim, char figure) {
        String[] foxNextStep = new String[4];
        boolean result = true;
        foxNextStep[0] = (String.format((char) (position.charAt(0) - 1) + "%02d",
                Integer.parseInt(position.substring(1)) - 1));
        foxNextStep[1] = (String.format((char) (position.charAt(0) - 1) + "%02d",
                Integer.parseInt(position.substring(1)) + 1));
        foxNextStep[2] = (String.format((char) (position.charAt(0) + 1) + "%02d",
                Integer.parseInt(position.substring(1)) - 1));
        foxNextStep[3] = (String.format((char) (position.charAt(0) + 1) + "%02d",
                Integer.parseInt(position.substring(1)) + 1));
        for (int i = 0; i < 4; i++) {
            result = result && (!isValidMove(dim, players, figure, position, foxNextStep[i]));
        }
        return result;

    }

    public static boolean isHoundWin(String[] players, int dim) {
        return cannotMove(players, players[players.length - 1], dim, 'F');
    }

    public static boolean isFoxWin(String[] players, int dim) {
        String foxPosition = players[players.length - 1];
        if (foxPosition.substring(1).equals("01")) {
            return true;
        }
        boolean hCannotMove = true;
        for (int i = 0; i < players.length - 1; i++) {
            hCannotMove = hCannotMove && (cannotMove(players, players[i], dim, 'H'));
        }
        return hCannotMove;
    }
}