package entity;

import java.util.*;
import main.*;

public class Player {

    // Game Parameters
    GamePanel gp;

    // Board Parameters
    public List<Integer> shipSizes = new ArrayList<>();
    public char[][] board = new char[10][10];
    public int shipsLeft = 5;
    public List<Ship> fleet = new ArrayList<>();

    // Game states
    public boolean inSetUpPhase = true;
    public boolean inPlayPhase = false;
    public boolean inDialoguePhase = false;
    public Dialogue dialogue = new Dialogue("app/res/tiles/Admiral.png", "ADMIRAL MACARTHUR");

    // Cursor Parameters
    public int cursorI;
    public int cursorJ;
    public int currentShipLength;
    public boolean isVertical;

    public Player(GamePanel gp) {
        // Link to GamePanel
        this.gp = gp;

        // Initialise ship sizes
        shipSizes.add(5);
        shipSizes.add(4);
        shipSizes.add(3);
        shipSizes.add(3);
        shipSizes.add(2);
    }

    public void initBoard() {
        // Fill board with spaces
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }

        // Places ships to initialize board
        for (int i = 0; i < shipSizes.size(); i++) {
            currentShipLength = shipSizes.get(i);
            addShip(currentShipLength, i);
        }

        currentShipLength = 1;
    }

    public void showDialogue(String message) {
        inDialoguePhase = true;
        dialogue.setMessage(message);
        dialogueAwaitClick();
    }

    public void dialogueAwaitClick() {
        // Wait for a LMB input
        gp.mouseHandler.resetLeftClick();
        while (!gp.mouseHandler.leftClicked()) {
            System.out.print("");
        }

        // Go back to play phase
        gp.mouseHandler.resetLeftClick();
        inDialoguePhase = false;
        inPlayPhase = true;
    }

    public void addShip(int len, int order) {

        // Get ship coordinates
        cursorI = -1;
        cursorJ = -1;
        isVertical = false;

        // Create new ship object
        Ship newShip = new Ship(len, isVertical, cursorI, cursorJ);

        while (!newShip.isValidPosition(board)) {

            // Get mouse input for coordinate of ship
            while (!gp.mouseHandler.leftClicked()) {

                System.out.print(""); // Why do I need this for it to work?

                cursorI = gp.mouseHandler.getI();
                cursorJ = gp.mouseHandler.getJ();

                // If ship exceeds right side of board
                if (isVertical && cursorI > 10 - currentShipLength) {
                    cursorI = 10 - currentShipLength;

                } else if (!isVertical && cursorJ > 10 - currentShipLength) {
                    cursorJ = 10 - currentShipLength;
                }

                // If right click detected, rotate ship
                if (gp.mouseHandler.rightClicked()) {
                    isVertical = isVertical ? false : true;
                    gp.mouseHandler.resetRightClick();
                }
            }
            newShip.move(cursorI, cursorJ, isVertical);
        }

        // Place ship onto board and add to fleet array
        newShip.placeOnBoard(board, order);
        fleet.add(newShip);
    }

    public void updateBoardCoordinates() {
        cursorI = gp.mouseHandler.getI();
        cursorI = cursorI > 9 ? 9 : cursorI;

        cursorJ = gp.mouseHandler.getJ();
        cursorJ = cursorJ > 9 ? 9 : cursorJ;
    }

}
