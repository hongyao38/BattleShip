package main;

import java.awt.*;
import javax.swing.*;

import entity.*;
import sound.Sound;
import ui.*;
import ui.screens.MainMenu;

public class GamePanel extends JPanel implements Runnable {
    
    // TILE Settings
    public final int tileSize = (int)(64 * 1);

    // SCREEN Settings
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;  // 1024
    public final int screenHeight = maxScreenRow * tileSize; // 768
    int FPS = 60;
    Thread gameThread;

    // Application Paremeters
    public MouseHandler mouseHandler = new MouseHandler(this);
    public UIHandler uiHandler       = new UIHandler(this);
    public Sound soundHandler        = new Sound();

    // Game Parameters
    public TurnHandler turnHandler   = new TurnHandler(this);
    public Player player             = new Player(this);
    public Enemy enemy               = new Enemy(this);

    // Screens
    public MainMenu mainMenu         = new MainMenu(this);
    public boolean inMenu            = true;


    public GamePanel() {
        // Create game screen
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);

        // Add a mouse listener
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

        while (true) {
            while (inMenu)
                mainMenu.startMenu();
        }
    }

    public void newGame() {
        playMusic(1);
        soundHandler.setVolume(0.25f);

        // Show tutorial to draw board, then set up board
        player.showDialogue("Position your ships to prepare for battle!");
        player.showDialogue("LMB to place a ship, RMB to rotate");
        player.initBoard();

        // Show tutorial to attack enemy
        player.showDialogue("The enemy has also gotten their fleet ready for battle.");
        player.inSetUpPhase = false;
        player.showDialogue("You are now staring at the enemy fleet's location");
        player.showDialogue("Press on a grid square to attack that area");
        player.showDialogue("Misses will be marked with a white X");
        player.showDialogue("Hits will be marked with a red crosshair");
        player.showDialogue("Eliminate all enemy ships to win!");

        // Battle Phase
        while (!turnHandler.playerWin && !turnHandler.playerLose) {

            if (turnHandler.isPlayerTurn) {
                turnHandler.playerAttack(player, enemy);
                turnHandler.isPlayerTurn = false;
                
            } else {
                turnHandler.enemyAttack(player, enemy);
                turnHandler.isPlayerTurn = true;
            }
        }

        // Display Win/Lose
        if (turnHandler.playerWin) {
            playSFX(4);
            player.showDialogue("Well done! You have sunk all enemy ships!");
            
        } else if (turnHandler.playerLose) {
            playSFX(5);
            player.showDialogue("Ugh! They have sunk all of our ship!");
        }
        player.inPlayPhase = false;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // 1 UPDATE: update information such as object positions
                update();
                // 2 DRAW: draw the screen with the updated information
                repaint();

                delta--;
            }
        }
    }

    public void update() {
        player.updateBoardCoordinates();
        mainMenu.animateMenu();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        if (inMenu) {
            mainMenu.drawMenu(g2);

        } else {
            // Draw the board
            uiHandler.drawGrid(g2);
            uiHandler.drawTurn(g2);

            // Reveal enemy ships once game over
            if (turnHandler.playerWin || turnHandler.playerLose) {
                uiHandler.drawHitsMisses(g2, enemy.board);
                uiHandler.drawPlacedShip(g2, enemy.fleet);
            }

            // Draw set-up phase
            if (player.inSetUpPhase) {
                uiHandler.drawPlacedShip(g2, player.fleet);
            }

            // Draw board hover cursor
            if (player.inPlayPhase) {

                if (turnHandler.isPlayerTurn) {
                    uiHandler.drawSunkShips(g2, enemy.fleet);
                    uiHandler.drawHitsMisses(g2, enemy.board);
                    uiHandler.drawHoverCursor(g2, player.cursorI, player.cursorJ);
                }

                if (!turnHandler.isPlayerTurn) {
                    uiHandler.drawPlacedShip(g2, player.fleet);
                    uiHandler.drawHitsMisses(g2, player.board);
                    uiHandler.showEnemyCursor(g2, enemy.cursorI, enemy.cursorJ);
                }
            }

            // Draw dialogue box
            if (player.inDialoguePhase) {
                uiHandler.drawDialogueWindow(g2, player.dialogue);
            }
        }

        // Draw cursor (FOREVER)
        uiHandler.drawMainCursor(g2, mouseHandler.x, mouseHandler.y);
    }

    public void playMusic(int i) {
        soundHandler.setFile(i);
        soundHandler.play();
        soundHandler.loop();
    }

    public void stopMusic() {
        soundHandler.stop();
    }

    public void playSFX(int i) {
        // SFX do not need to loop
        soundHandler.setFile(i);
        soundHandler.play();
    }
}
