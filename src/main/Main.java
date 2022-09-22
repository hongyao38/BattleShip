package main;

import javax.swing.JFrame;
import java.awt.image.*;
import java.awt.*;

public class Main {
    
    public static void main(String[] args) {

        // Create JFrame parameters
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("BattleShips");

        // Create a Game Panel
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        // Set window position on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Set blank cursor
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        window.getContentPane().setCursor(customCursor);

        gamePanel.startGameThread();
    }

}
