package ui;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import main.*;
import entity.*;

public class UIHandler {

    GamePanel gp;
    int numTile = 0;
    public Tile[] tiles;    

    public UIHandler(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[35];

        getTileImage();
    }

    public void addNewTile(String tileFilePath) throws IOException {
        tiles[numTile] = new Tile();
        tiles[numTile].image = ImageIO.read(new FileInputStream(tileFilePath));
        numTile++;
    }

    public void getTileImage() {
        try {
            addNewTile("app/res/tiles/Water.png");                 // 0
            addNewTile("app/res/ships/CarrierHorizontal.png");     // 1
            addNewTile("app/res/ships/BattleshipHorizontal.png");  // 2
            addNewTile("app/res/ships/CruiserHorizontal.png");     // 3
            addNewTile("app/res/ships/DestroyerHorizontal.png");   // 4
            addNewTile("app/res/ships/CarrierVertical.png");       // 5
            addNewTile("app/res/ships/BattleshipVertical.png");    // 6
            addNewTile("app/res/ships/CruiserVertical.png");       // 7
            addNewTile("app/res/ships/DestroyerVertical.png");     // 8
            addNewTile("app/res/tiles/Cursor.png");                // 9
            addNewTile("app/res/tiles/Background.png");            // 10
            addNewTile("app/res/tiles/AttackCursor.png");          // 11
            addNewTile("app/res/tiles/Hit.png");                   // 12
            addNewTile("app/res/tiles/Miss.png");                  // 13
            addNewTile("app/res/tiles/EnemyCursor.png");           // 14
            addNewTile("app/res/tiles/PlayerTurn.png");            // 15
            addNewTile("app/res/tiles/EnemyTurn.png");             // 16
            addNewTile("app/res/tiles/TextBubble1.png");           // 17
            addNewTile("app/res/tiles/TextBubble2.png");           // 18
            addNewTile("app/res/tiles/TextBubble3.png");           // 19
            addNewTile("app/res/tiles/TextBubble4.png");           // 20
            addNewTile("app/res/tiles/TextBubble5.png");           // 21
            addNewTile("app/res/tiles/TextBubble6.png");           // 22
            addNewTile("app/res/tiles/TextBubble7.png");           // 23
            addNewTile("app/res/tiles/TextBubble8.png");           // 24
            addNewTile("app/res/tiles/TextBubble9.png");           // 25
            addNewTile("app/res/tiles/TextBubble10.png");          // 26
            addNewTile("app/res/tiles/TextBubble11.png");          // 27
            addNewTile("app/res/tiles/PlayerAvatar.png");          // 28
            addNewTile("app/res/tiles/EnemyAvatar.png");           // 29
            addNewTile("app/res/tiles/Win.png");                   // 30
            addNewTile("app/res/tiles/Lose.png");                  // 31

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tile getShipTile(int length, boolean isVertical) {
        if (length == 5) return !isVertical ? tiles[1] : tiles[5];
        if (length == 4) return !isVertical ? tiles[2] : tiles[6];
        if (length == 3) return !isVertical ? tiles[3] : tiles[7];
        if (length == 2) return !isVertical ? tiles[4] : tiles[8];
        return tiles[11];
    }


    // Draw Board Methods

    public void drawGrid(Graphics2D g2) {
        // Draw background
        g2.drawImage(tiles[10].image, 0, 0, gp.screenWidth, gp.screenHeight, null);

        // Draw board
        for (int i = 0; i < 10; i++) {
            int y = gp.tileSize * (i + 1);

            for (int j = 0; j < 10; j++) {
                int x = gp.tileSize * (j + 1);
                g2.drawImage(tiles[0].image, x, y, gp.tileSize, gp.tileSize, null);
            }
        }
    }

    public void drawPlacedShip(Graphics2D g2, List<Ship> fleet) {
        for (Ship s : fleet) {
            int len = s.length;
            boolean isVertical = s.isVertical;

            int x = gp.tileSize * (s.start[1] + 1);
            int y = gp.tileSize * (s.start[0] + 1);
            int width = s.isVertical ? gp.tileSize : gp.tileSize * s.length;
            int height = s.isVertical ? gp.tileSize * s.length : gp.tileSize;

            g2.drawImage(getShipTile(len, isVertical).image, x, y, width, height, null);
        }
    }

    public void drawHitsMisses(Graphics2D g2, char[][] board) {
        for (int i = 0; i < board.length; i++) {
            int y = gp.tileSize * (i + 1);

            for (int j = 0; j < board[i].length; j++) {
                int x = gp.tileSize * (j + 1);

                if (board[i][j] == 'H') {
                    g2.drawImage(tiles[12].image, x, y, gp.tileSize, gp.tileSize, null);
                } else if (board[i][j] == 'M') {
                    g2.drawImage(tiles[13].image, x, y, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }


    // Cursor Drawing Methods

    public void drawHoverCursor(Graphics2D g2, int cursorI, int cursorJ) {
        // If cursor exceeds top left corner
        if (cursorI < 0 || cursorJ < 0) {
            return;
        }
        
        // If cursor exceeds right side of board
        if (gp.player.isVertical) {
            if (cursorI > 10 - gp.player.currentShipLength) {
                cursorI = 10 - gp.player.currentShipLength;
            }
            if (cursorJ > 9) {
                cursorJ = 9;
                g2.drawImage(gp.uiHandler.getShipTile(gp.player.currentShipLength, gp.player.isVertical).image, 
                                gp.tileSize * 10, gp.tileSize * (cursorI + 1), null);
                return;
            }
            
        } else {
            if (cursorJ > 10 - gp.player.currentShipLength) {
                cursorJ = 10 - gp.player.currentShipLength;
            }
            if (cursorI > 9) {
                cursorI = 9;
                g2.drawImage(gp.uiHandler.getShipTile(gp.player.currentShipLength, gp.player.isVertical).image, 
                                gp.tileSize * (cursorJ + 1), gp.tileSize * 10, null);
                return;
            }
        }

        g2.drawImage(gp.uiHandler.getShipTile(gp.player.currentShipLength, gp.player.isVertical).image, 
                                gp.tileSize * (cursorJ + 1), gp.tileSize * (cursorI + 1), null);
    }

    public void drawMainCursor(Graphics2D g2, int x, int y) {
        int width = gp.tileSize;
        int height = gp.tileSize;
        g2.drawImage(gp.uiHandler.tiles[9].image, x, y, width, height, null);
    }

    // Draw Enemy UI Methods

    public void showEnemyCursor(Graphics2D g2, int i, int j) {
        int x = gp.tileSize * (j + 1);
        int y = gp.tileSize * (i + 1);
        int width = gp.tileSize;
        int height = gp.tileSize;
        g2.drawImage(gp.uiHandler.tiles[14].image, x, y, width, height, null);
    }

    public void drawSunkShips(Graphics2D g2, List<Ship> fleet) {
        for (Ship ship : fleet) {
            if (ship.health == 0) {
                BufferedImage shipImage = gp.uiHandler.getShipTile(ship.length, ship.isVertical).image;
                int x = (ship.start[1] + 1) * gp.tileSize;
                int y = (ship.start[0] + 1) * gp.tileSize;
                int width = ship.isVertical ? gp.tileSize : gp.tileSize * ship.length;
                int height = ship.isVertical ? gp.tileSize * ship.length : gp.tileSize;
                g2.drawImage(shipImage, x, y, width, height, null);
            }
        }
    }


    // Draw Turns Methods

    public void drawTurn(Graphics2D g2) {
        if (gp.turnHandler.isPlayerTurn) {
            g2.drawImage(tiles[15].image, gp.tileSize * 12, gp.tileSize * 1, null);
            g2.drawImage(tiles[28].image, gp.tileSize * 12, gp.tileSize * 5, null);
            g2.drawImage(tiles[gp.turnHandler.textBubbleNum].image, 12 * gp.tileSize, 6 * gp.tileSize, null);
        }
        if (!gp.turnHandler.isPlayerTurn) {
            g2.drawImage(tiles[16].image, gp.tileSize * 12, gp.tileSize * 1, null);
            g2.drawImage(tiles[29].image, gp.tileSize * 12, gp.tileSize * 5, null);
            g2.drawImage(tiles[gp.turnHandler.textBubbleNum].image, 12 * gp.tileSize, 6 * gp.tileSize, null);
        }
    }


    // Draw Dialogue Windows Method

    public void drawDialogueWindow(Graphics2D g2, Dialogue dialogue) {
        int x = gp.tileSize * 4;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 10;
        int height = gp.tileSize * 2;

        // Darken background
        Color c = new Color(50, 50, 50, 150);
        g2.setColor(c);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Draw win/lose state if necessary
        if (gp.turnHandler.playerWin) {
            g2.drawImage(gp.uiHandler.tiles[30].image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        else if (gp.turnHandler.playerLose) {
            g2.drawImage(gp.uiHandler.tiles[31].image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }

        // Draw Speaker
        g2.drawImage(dialogue.speaker.image, 0, gp.tileSize * 7, gp.tileSize * 5, gp.tileSize * 5,null);

        // Draw dialogue window (WHITE)
        c = new Color(255, 255, 255, 220);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        // Draw dialogue window stroke (BLACK)
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

        // Write speaker name
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        g2.drawString(dialogue.name, x + 20, y - 10);

        // Write message in dialogue window
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.drawString(dialogue.message, x + 30, y + 45);
    }
}
