package bg.sofia.uni.fmi.mjt.photoalbum.thread;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class RetroPhotoMaker {
    private Queue<Image> imageQueue = new LinkedList<>();
    private Path outputPath;

    private boolean areImagesFinished;

    public RetroPhotoMaker(Path outputPath) {
        this.outputPath = outputPath;
        areImagesFinished = false;
    }

    public void addImage(Path imagePath) {
        try {
            BufferedImage imageData = ImageIO.read(imagePath.toFile());
            synchronized (this) {
                this.imageQueue.add(new Image(imagePath.getFileName().toString(), imageData));
                this.notifyAll();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to load image %s", imagePath.toString()), e);
        }
    }

    public void convertImage() {
        Image image;
        synchronized (this) {
            while (imageQueue.isEmpty() && !areImagesFinished) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            image = imageQueue.poll();
            if (image == null) {
                return;
            }
            this.notifyAll();
        }
        BufferedImage processedData = new BufferedImage(image.data.getWidth(), image.data.getHeight(),
            BufferedImage.TYPE_BYTE_GRAY);
        processedData.getGraphics().drawImage(image.data, 0, 0, null);

        try {
            ImageIO.write(processedData, "png", new File(outputPath.toString(), image.name));
            System.out.println("Saved " + image.name + " to " + outputPath.toString());

            convertImage();

        } catch (IOException e) {
            throw new UncheckedIOException(String.format("While saving image %s", image.name), e);
        }
    }

    public void setAreImagesFinished() {
        areImagesFinished = true;
    }
}
