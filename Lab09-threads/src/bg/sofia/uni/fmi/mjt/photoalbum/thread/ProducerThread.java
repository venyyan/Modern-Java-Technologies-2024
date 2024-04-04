package bg.sofia.uni.fmi.mjt.photoalbum.thread;

import java.nio.file.Path;

public class ProducerThread extends Thread {
    private RetroPhotoMaker photoMaker;
    private Path imagePath;
    public ProducerThread(RetroPhotoMaker photoMaker, Path imagePath) {
        this.photoMaker = photoMaker;
        this.imagePath = imagePath;
    }

    @Override
    public void run() {
        photoMaker.addImage(imagePath);
    }
}
