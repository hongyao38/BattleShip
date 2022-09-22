package entity;

import java.io.*;
import javax.imageio.ImageIO;

import ui.*;

public class Dialogue {
    public Tile speaker;
    public String name;
    public String message;

    public Dialogue(String speakerFileDirectory, String name) {
        this.name = name;
        speaker = new Tile();
        try {
            speaker.image = ImageIO.read(new FileInputStream(speakerFileDirectory));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
