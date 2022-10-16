package ui.screens;

import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import ui.Tile;

public class MainMenu {

    private GamePanel gp;
    private int startBtnNum = 32;
    private int quitBtnNum = 34;
    private Tile tiles[] = new Tile[15];
    private int numTile;

    private double animationSeq = 0;
    
    public MainMenu(GamePanel gp) {
        this.gp = gp;

        try {
            addNewTile("app/res/menu/menu0.png");
            addNewTile("app/res/menu/menu1.png");
            addNewTile("app/res/menu/menu2.png");
            addNewTile("app/res/menu/menu3.png");
            addNewTile("app/res/menu/menu4.png");
            addNewTile("app/res/menu/menu5.png");
            addNewTile("app/res/menu/menu6.png");
            addNewTile("app/res/menu/menu7.png");
            addNewTile("app/res/menu/menu8.png");
            addNewTile("app/res/menu/menu9.png");
            addNewTile("app/res/menu/menu10.png");
            addNewTile("app/res/menu/menu11.png");
            addNewTile("app/res/menu/menu12.png");
            addNewTile("app/res/menu/menu13.png");
            addNewTile("app/res/menu/menu14.png");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewTile(String tileFilePath) throws IOException {
        tiles[numTile] = new Tile();
        tiles[numTile].image = ImageIO.read(new FileInputStream(tileFilePath));
        numTile++;
    }

    public void startMenu() {
        gp.stopMusic();
        gp.playMusic(0);
        gp.soundHandler.setVolume(0.25f);
        int x = -1;
        int y = -1;

        while (!gp.mouseHandler.leftClicked()) {
            System.out.print("");
            x = gp.mouseHandler.x;
            y = gp.mouseHandler.y;

            // Set button hover states
            if (startBtnNum != 32) startBtnNum = 32;
            if (quitBtnNum != 34) quitBtnNum = 34;
            if (mouseInStartButton(x, y)) startBtnNum++;
            if (mouseInQuitButton(x, y)) quitBtnNum++;
        }

        // If mouse coordinates within START button
        if (mouseInStartButton(x, y)) {
            gp.stopMusic();
            gp.playSFX(10);

            // Blinking animation
            startBtnNum--;
            gp.turnHandler.waitTime(200);
            startBtnNum++;
            gp.turnHandler.waitTime(200);
            startBtnNum--;
            gp.turnHandler.waitTime(200);
            startBtnNum++;
            gp.turnHandler.waitTime(200);
            startBtnNum--;
            gp.turnHandler.waitTime(200);
            startBtnNum++;
            gp.turnHandler.waitTime(200);
            startBtnNum--;

            // Exit menu and start new game
            gp.inMenu = false;
            gp.player.inSetUpPhase = true;
            gp.player.inPlayPhase = true;
            gp.newGame();
        }

        // If mouse coordinates within QUIT button
        else if (mouseInQuitButton(x, y)) {
            gp.playSFX(5);
            gp.turnHandler.waitTime(1000);
            System.exit(0);
        }
    }


    private boolean mouseInStartButton(int x, int y) {
        return (x > 6 * gp.tileSize && x < 10 * gp.tileSize &&
                y > 8 * gp.tileSize && y < 9 * gp.tileSize);
    }

    private boolean mouseInQuitButton(int x, int y) {
        return (x > 6 * gp.tileSize && x < 10 * gp.tileSize &&
                y > 10 * gp.tileSize && y < 11 * gp.tileSize);
    }

    
    public void animateMenu() {
        animationSeq += 0.1;
        if (animationSeq > 14) {
            animationSeq = 0;
        }
    }


    public void drawMenu(Graphics2D g2) {
        // Background
        g2.drawImage(tiles[(int)animationSeq].image, 0, 0, gp.screenWidth, gp.screenHeight, null);

        // START button
        g2.drawImage(gp.uiHandler.tiles[startBtnNum].image, 
                    6 * gp.tileSize, 8 * gp.tileSize, 4 * gp.tileSize, gp.tileSize, null);

        // QUIT button
        g2.drawImage(gp.uiHandler.tiles[quitBtnNum].image,
                    6 * gp.tileSize, 10 * gp.tileSize, 4 * gp.tileSize, gp.tileSize, null);
    }
}
