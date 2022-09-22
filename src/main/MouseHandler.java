package main;

import java.awt.event.*;

import javax.swing.SwingUtilities;
import javax.swing.event.*;

public class MouseHandler implements MouseInputListener {

    GamePanel gp;
    boolean leftClicked;
    boolean rightClicked;
    public int x, y;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    public int getI() {
        return (y - gp.tileSize) / gp.tileSize;
    }

    public int getJ() {
        return (x - gp.tileSize) / gp.tileSize;
    }

    public boolean leftClicked() {
        return leftClicked;
    }

    public boolean rightClicked() {
        return rightClicked;
    }

    public void resetLeftClick() {
        leftClicked = false;
    }

    public void resetRightClick() {
        rightClicked = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (SwingUtilities.isRightMouseButton(e)) {
            rightClicked = true;
            return;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (SwingUtilities.isLeftMouseButton(e)) {
            leftClicked = true;
            return;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (SwingUtilities.isLeftMouseButton(e)) {
            leftClicked = false;
            return;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    
}
