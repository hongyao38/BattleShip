package main;

import java.util.*;
import entity.*;

public class TurnHandler {

    // Game Window Parameters
    GamePanel gp;

    // Turn Parameters
    public int textBubbleNum = 17;
    public boolean isPlayerTurn = true;
    public boolean playerWin;
    public boolean playerLose;

    public TurnHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void waitTime(int duration) {
        // Wait at least 1s
        long previousTime = System.currentTimeMillis();
        long interval = 0;

        while (interval < duration) {
            long currentTime = System.currentTimeMillis();
            interval = currentTime - previousTime;
        }
    }

    public void playerAttack(Player player, Enemy enemy) {
        // Get random speach bubble
        Random random = new Random();
        textBubbleNum = random.nextInt(6) + 17;

        char[][] enemyBoard = enemy.board;

        // Prompt for coordinates to attack
        int i = player.cursorI, j = player.cursorJ;
        while (!gp.mouseHandler.leftClicked()) {
            System.out.print("");
            i = player.cursorI;
            j = player.cursorJ;
        }
        gp.mouseHandler.resetLeftClick();

        // If current spot has already been attacked
        while (enemyBoard[i][j] == 'M' || enemyBoard[i][j] == 'H') {
            while (!gp.mouseHandler.leftClicked()) {
                System.out.print("");
                i = player.cursorI;
                j = player.cursorJ;

            }
            gp.mouseHandler.resetLeftClick();
        }

        // If miss
        if (enemyBoard[i][j] == ' ') {
            enemyBoard[i][j] = 'M';
            waitTime(1000);
            return;

        }

        // If hit, get ship that got hit
        int shipIndex = enemyBoard[i][j] - '0';
        Ship shipHit = enemy.fleet.get(shipIndex);

        // Deduct health
        shipHit.health--;

        // If ship's health reaches 0, minus from total ships left
        if (shipHit.health == 0) {
            enemy.shipsLeft--;
            player.showDialogue(String.format("We have sunk one of their ships! (Length %d)", shipHit.length));
        }

        // If 0 ships left, player wins
        if (enemy.shipsLeft == 0) {
            playerWin = true;
        }

        // Mark out grid with an H, and wait 1s
        enemyBoard[i][j] = 'H';  
        waitTime(1000);
    }

    public void enemyAttack(Player player, Enemy enemy) {
        // Get random speach bubble
        Random random = new Random();
        textBubbleNum = random.nextInt(5) + 23;

        char[][] playerBoard = player.board;

        // Get coordinate to attack, choose 5 times to simulate thinking
        enemy.getNextCoordinate();
        for (int k = 0; k < 4; k++) {
            enemy.getNextCoordinate();
            waitTime(300);
        }

        // If chosen grid lands on an attacked grid, rechoose
        while (playerBoard[enemy.cursorI][enemy.cursorJ] == 'M' || playerBoard[enemy.cursorI][enemy.cursorJ] == 'H') {
            enemy.getNextCoordinate();
            waitTime(300);
        }

        // Remove grid from lists of possible moves left
        try {
            enemy.possibleMoves[enemy.cursorI].remove(enemy.possibleMoves[enemy.cursorI].indexOf(enemy.cursorJ));
        } catch (IndexOutOfBoundsException e) {
        }

        // If attack misses, mark with an M
        if (playerBoard[enemy.cursorI][enemy.cursorJ] == ' ') {
            
            playerBoard[enemy.cursorI][enemy.cursorJ] = 'M';

            // If ship is spotted and missed, just remove the coordinate from possible moves
            if (enemy.huntMode) {
                enemy.huntModeMoves.remove(enemy.huntModeMovesIndex);

                // No other possible moves, means ship has sunken
                if (enemy.huntModeMoves.size() == 0) {
                    enemy.huntMode = false;
                    enemy.isOrientationFigured = false;
                }
            }

            waitTime(1000);
            return;
        }

        // If hit, get ship that got hit
        int shipIndex = playerBoard[enemy.cursorI][enemy.cursorJ] - '0';
        Ship shipHit = player.fleet.get(shipIndex);
        playerBoard[enemy.cursorI][enemy.cursorJ] = 'H';

        // If first hit, set shipSpotted to TRUE
        if (!enemy.huntMode) {
            enemy.firstHitI = enemy.cursorI;
            enemy.firstHitJ = enemy.cursorJ;
            enemy.huntMode = true;
        }

        // If ship is spotted and hit
        else if (enemy.huntMode) {

            // Figure out the orientation of the ship
            if (!enemy.isOrientationFigured) {
                if (enemy.cursorI == enemy.firstHitI + 1 || enemy.cursorI == enemy.firstHitI - 1) {
                    enemy.isShipVertical = true;
                } else {
                    enemy.isShipVertical = false;
                }
                enemy.isOrientationFigured = true;
            }
            enemy.huntModeMoves.remove(enemy.huntModeMovesIndex);
            
            // Reset possible moves to locate ship
            enemy.getHuntModeMoves(playerBoard);
        }

        // Deduct health
        shipHit.health--;

        // If ship's health reaches 0, minus from total ships left
        if (shipHit.health == 0) {
            player.shipsLeft--;

            // AI will also get to know if they sunk a ship, thus turning off hunt-mode
            enemy.huntMode = false;
            enemy.isOrientationFigured = false;
            enemy.huntModeMoves.clear();

            // Remove ship length from ships left and re-get possible moves
            enemy.shipSizes.remove(enemy.shipSizes.indexOf(shipHit.length));

            if (player.shipsLeft > 0) {
                enemy.getPossibleMoves();
            }
        }

        // If 0 ships left, player loses
        if (player.shipsLeft == 0) {
            playerLose = true;
        }
        waitTime(1000);
    }
}
