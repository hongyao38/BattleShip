package entity;

import main.GamePanel;

public class Ship {

    private GamePanel gp;

    public int health;
    public int length;
    public boolean isVertical;
    public int[] start = new int[2];

    public Ship(int length, boolean isVertical, int i, int j, GamePanel gp) {
        this.gp = gp;
        this.length = length;
        this.health = length;
        this.isVertical = isVertical;
        this.start[0] = i;
        this.start[1] = j;
    }

    public boolean isValidPosition(char[][] board) {
        int i = start[0], j = start[1];

        // Out of bounds
        if (i < 0 || j < 0 || i >= 10 || j >= 10 || board[i][j] != ' ') {
            return false;
        }

        // Not enough room for length of ship (VERTICAL)
        if (isVertical) {
            for (int k = i; k < i + length; k++) {
                if (k < 10 && board[k][j] != ' ') {
                    return false;
                }
            }
            return i + length - 1 < 10;
        }

        // Not enough room for length of ship (HORIZONTAL)
        for (int k = j; k < j + length; k++) {
            if (k < 10 && board[i][k] != ' ') {
                return false;
            }
        }
        return j + length - 1 < 10;
    }

    public void move(int i, int j, boolean isVertical) {
        start[0] = i;
        start[1] = j;
        this.isVertical = isVertical;
    }

    public void placeOnBoard(char[][] board, int order) {
        int i = start[0];
        int j = start[1];
        for (int k = 0; k < length; k++) {
            if (isVertical) {
                board[i + k][j] = (char)('0' + order);
            } else {
                board[i][j + k] = (char)('0' + order);
            }
        }
    }

    public void getHit() {
        health--;
        gp.playSFX(2);
    }
}
