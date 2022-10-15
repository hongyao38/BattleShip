package entity;

import java.util.*;
import main.*;

@SuppressWarnings("unchecked")
public class Enemy extends Player {

    // Game Parameters
    GamePanel gp;
    Random random = new Random();

    // Possible moves
    public int firstHitI, firstHitJ;
    public ArrayList<Integer>[] possibleMoves = new ArrayList[10];
    public ArrayList<int[]> huntModeMoves = new ArrayList<>();
    public int huntModeMovesIndex;

    // Fire mode parameters
    public boolean huntMode = false;
    public boolean isShipVertical = false;
    public boolean isOrientationFigured = false;

    public Enemy(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // Initialise possible moves
        getPossibleMoves();
        
        // Place ships randomly
        placeShips();
    }

    public void getPossibleMoves() {
        // Initialise possible moves (Variable parity based on smallest ship length alive)
        int shortestShipLen = shipSizes.get(shipSizes.size() - 1);

        // For each row
        for (int i = 0; i < 10; i++) {
            possibleMoves[i] = new ArrayList<Integer>();

            // In each row, go by parity of length of smallest ship left alive
            for (int j = i % shortestShipLen; j < 10; j += shortestShipLen) {

                // If between this grid and the prev grid, there are already misses, no need to add
                if (isEmptyCell(i, j) && isValidGap(shortestShipLen, i, j)) {
                    possibleMoves[i].add(j);
                }
            }
        }
    }

    public boolean isEmptyCell(int i, int j) {
        return gp.player.board[i][j] != 'H' && gp.player.board[i][j] != 'M';
    }

    public boolean isValidGap(int shortestShipLen, int i, int j) {
        for (int k = 1; k < shortestShipLen - 1; k++) {
            if (j - k > 0 && gp.player.board[i][j - k] == 'M') {
                return false;
            }
        }
        return true;
    }

    public void placeShips() {
        // Fill board with empty spaces
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }

        // Place ships on board
        for (int k = 0; k < shipSizes.size(); k++) {
            int len = shipSizes.get(k);

            // Initialise coordinates and orientation of ship
            cursorI = -1;
            cursorJ = -1;
            isVertical = false;

            // Create new ship object
            Ship newShip = new Ship(len, isVertical, cursorI, cursorJ);

            // Position ship till it is of a valid position
            while (!newShip.isValidPosition(board)) {
                cursorI = random.nextInt(10);
                cursorJ = random.nextInt(10);
                isVertical = random.nextInt(2) == 1 ? true : false;

                newShip.move(cursorI, cursorJ, isVertical);
            }

            // Place ship at valid position
            newShip.placeOnBoard(board, k);
            fleet.add(newShip);
        }
    }

    public void getNextCoordinate() {
        if (huntMode) {
            fireHuntMode();
            return;
        }
        fireRandomMode();
    }

    public void fireRandomMode() {
        // Choose a row with remaining possible moves
        cursorI = random.nextInt(10);
        while (possibleMoves[cursorI].size() == 0) {
            cursorI = random.nextInt(10);
        }
        
        // Choose a column from the possible moves
        cursorJ = random.nextInt(possibleMoves[cursorI].size());
        cursorJ = possibleMoves[cursorI].get(cursorJ);
    }

    public void fireHuntMode() {
        getHuntModeMoves(gp.player.board);
        huntModeMovesIndex = random.nextInt(huntModeMoves.size());
        int[] coord = huntModeMoves.get(huntModeMovesIndex);
        cursorI = coord[0];
        cursorJ = coord[1];
    }

    public void getHuntModeMoves(char[][] playerBoard) {

        // If not yet in hunt mode, check all 4 directions
        if (!huntMode) {
            checkSurroundings(firstHitI, firstHitJ);
            return;
        }

        // If already in hunt mode, reset possible moves and add new ones
        huntModeMoves.clear();
        int i1 = firstHitI, i2 = firstHitI;
        int j1 = firstHitJ, j2 = firstHitJ;
        
        // Start from hitI and hitJ, initial hit grid
        if (isShipVertical) {
            // Check up and down till a non-hit grid is found
            checkUp(i1, playerBoard);
            checkDown(i2, playerBoard);

        } else {
            // Check left and right till a non-hit grid is found
            checkLeft(j1, playerBoard);
            checkRight(j2, playerBoard);
        }

        if (!isOrientationFigured) {
            checkUp(i1, playerBoard);
            checkDown(i2, playerBoard);
        }

        // If no other possible moves in hunt mode, but ship not yet sunken
        // if (huntModeMoves.size() == 0) {
        //     checkSurroundings(firstHitI, firstHitJ);
        //     isShipVertical = isShipVertical ? false : true;
        // }
    }

    private void checkSurroundings(int i, int j) {
        int[] move1 = {i - 1, j};
        int[] move2 = {i, j - 1};
        int[] move3 = {i, j + 1};
        int[] move4 = {i + 1, j};
        
        if (isValidCoord(move1)) huntModeMoves.add(move1);
        if (isValidCoord(move2)) huntModeMoves.add(move2);
        if (isValidCoord(move3)) huntModeMoves.add(move3);
        if (isValidCoord(move4)) huntModeMoves.add(move4);
    }

    private void checkUp(int i, char[][] playerBoard) {
        while (i > 0 && playerBoard[i][firstHitJ] == 'H') {
            i--;
        }
        // Add only if it is an empty grid, do not add if it is a MISS
        if (isEmptyCell(i, firstHitJ)) {
            int[] newMove = {i, firstHitJ};
            if (isValidCoord(newMove)) huntModeMoves.add(newMove);
        }
    }

    private void checkDown(int i, char[][] playerBoard) {
        while (i < 9 && playerBoard[i][firstHitJ] == 'H') {
            i++;
        }
        // Add only if it is an empty grid, do not add if it is a MISS
        if (isEmptyCell(i, firstHitJ)) {
            int[] newMove = {i, firstHitJ};
            if (isValidCoord(newMove)) huntModeMoves.add(newMove);
        }
    }

    private void checkLeft(int j, char[][] playerBoard) {
        while (j > 0 && playerBoard[firstHitI][j] == 'H') {
            j--;
        }
        // Add only if it is an empty grid, do not add if it is a MISS
        if (isEmptyCell(firstHitI, j)) {
            int[] newMove = {firstHitI, j};
            if (isValidCoord(newMove)) huntModeMoves.add(newMove);
        }
    }

    private void checkRight(int j, char[][] playerBoard) {
        while (j < 9 && playerBoard[firstHitI][j] == 'H') {
            j++;
        }
        // Add only if it is an empty grid, do not add if it is a MISS
        if (isEmptyCell(firstHitI, j)) {
            int[] newMove = {firstHitI, j};
            if (isValidCoord(newMove)) huntModeMoves.add(newMove);
        }
    }

    public boolean isValidCoord(int[] coord) {
        int i = coord[0];
        int j = coord[1];
        return !(i < 0 || j < 0 || j > 9 || i > 9);
    }

}
