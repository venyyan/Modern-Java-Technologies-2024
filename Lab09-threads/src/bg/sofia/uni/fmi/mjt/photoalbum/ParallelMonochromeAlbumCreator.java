package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.photoalbum.thread.ConsumerThread;
import bg.sofia.uni.fmi.mjt.photoalbum.thread.ProducerThread;
import bg.sofia.uni.fmi.mjt.photoalbum.thread.RetroPhotoMaker;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {
    private int imageProcessorsCount;
    private static final String GLOB = "*.{jpg,jpeg,png}";

    List<Thread> producers;
    List<Thread> consumers;
    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
        producers = new ArrayList<>();
        consumers = new ArrayList<>();
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path sourcePath = Path.of(sourceDirectory);
        RetroPhotoMaker retroPhotoMaker = new RetroPhotoMaker(Path.of(outputDirectory));
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath, GLOB)) {

            startConsumers(retroPhotoMaker);
            startProducers(stream, retroPhotoMaker);

            for (Thread producer : producers) {
                producer.join();
            }
            retroPhotoMaker.setAreImagesFinished();

            for (Thread consumer : consumers) {
                consumer.join();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException( e);
        }
    }

    private void startConsumers(RetroPhotoMaker retroPhotoMaker) {
        for (int i = 0; i < imageProcessorsCount; i++) {
            ConsumerThread consumerThread = new ConsumerThread(retroPhotoMaker);
            consumers.add(consumerThread);
            consumerThread.start();
        }
    }

    private void startProducers(DirectoryStream<Path> stream, RetroPhotoMaker retroPhotoMaker) {
        for (Path path : stream) {
            ProducerThread producerThread = new ProducerThread(retroPhotoMaker, path);
            producers.add(producerThread);
            producerThread.start();
        }
    }
}
