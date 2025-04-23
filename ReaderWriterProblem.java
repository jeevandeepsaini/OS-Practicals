import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderWriterProblem {
    private static final Logger LOGGER = Logger.getLogger(ReaderWriterProblem.class.getName());
    private static final Semaphore mutex = new Semaphore(1);  // Protect readCount
    private static final Semaphore writeSemaphore = new Semaphore(1);  // Protect write access
    private static final AtomicInteger readCount = new AtomicInteger(0);  // Track active readers

    // Reader thread
    static class Reader implements Runnable {
        private final int readerId;

        public Reader(int id) {
            this.readerId = id;
        }

        @Override
        public void run() {
            try {
                mutex.acquire();  // Acquire to modify readCount
                if (readCount.incrementAndGet() == 1) {
                    writeSemaphore.acquire();  // Block writers if First Reader arrives
                }
                mutex.release();  // Release readCount lock

                // Simulate reading
                LOGGER.info(() -> "Reader " + readerId + " is reading.");
                TimeUnit.SECONDS.sleep(1);

                mutex.acquire();  // Acquire to modify readCount
                if (readCount.decrementAndGet() == 0) {
                    writeSemaphore.release();  // Release writeSemaphore if Last Reader leaves
                }
                mutex.release();  // Release readCount lock
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, "Reader " + readerId + " interrupted", e);
            }
        }
    }

    // Writer thread
    static class Writer implements Runnable {
        private final int writerId;

        public Writer(int id) {
            this.writerId = id;
        }

        @Override
        public void run() {
            try {
                writeSemaphore.acquire();  // Acquire write access

                // Simulate writing
                LOGGER.info(() -> "Writer " + writerId + " is writing.");
                TimeUnit.SECONDS.sleep(2);

                writeSemaphore.release();  // Release write access after writing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, "Writer " + writerId + " interrupted", e);
            }
        }
    }

    public static void main(String[] args) {
        int numReaders = 5;
        int numWriters = 3;

        // Executor service with a fixed thread pool size
        ExecutorService executor = Executors.newFixedThreadPool(numReaders + numWriters);

        try {
            // Submit reader tasks
            for (int i = 0; i < numReaders; i++) {
                executor.submit(new Reader(i + 1));
            }

            // Submit writer tasks
            for (int i = 0; i < numWriters; i++) {
                executor.submit(new Writer(i + 1));
            }

            // Initiate orderly shutdown
            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();  // Force shutdown If timeout
            }

            LOGGER.info("All threads finished execution.");
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Main thread interrupted", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}