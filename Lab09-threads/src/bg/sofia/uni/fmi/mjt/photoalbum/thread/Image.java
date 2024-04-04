package bg.sofia.uni.fmi.mjt.photoalbum.thread;

import java.awt.image.BufferedImage;

public class Image {
    String name;
    BufferedImage data;

    public Image(String name, BufferedImage data) {
        this.name = name;
        this.data = data;
    }
}
