package bg.sofia.uni.fmi.mjt.photoalbum.thread;

public class ConsumerThread extends Thread {
    private RetroPhotoMaker photoMaker;
    public ConsumerThread(RetroPhotoMaker photoMaker) {
        this.photoMaker = photoMaker;
    }

    @Override
    public void run() {
        photoMaker.convertImage();
    }
}
