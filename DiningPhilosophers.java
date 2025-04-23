import java.util.concurrent.*;
import java.util.logging.*;

public class DiningPhilosophers {

    private static final int NUM_PHILOSOPHERS = 5;
    private static final Semaphore[] forks = new Semaphore[NUM_PHILOSOPHERS];  // Semaphores representing forks
    private static final Logger LOGGER = Logger.getLogger(DiningPhilosophers.class.getName());

    static {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Semaphore(1);  // Initially, each fork is available
        }
    }

    // Philosopher thread
    static class Philosopher implements Runnable {
        private final int id;

        public Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    think();
                    pickUpForks();
                    eat();
                    putDownForks();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Simulate thinking
        private void think() throws InterruptedException {
            LOGGER.info("Philosopher " + id + " is thinking.");
            Thread.sleep((long) (Math.random() * 1000)); // Thinking time
        }

        // Pick up forks
        private void pickUpForks() throws InterruptedException {
            // Philosopher 0 picks the right fork first; others pick the left fork first.
            if (id == 0) {
                // Philosopher 0 picks up right fork first (chaining).
                forks[(id + 1) % NUM_PHILOSOPHERS].acquire();  // Pick up right fork
                LOGGER.info("Philosopher " + id + " picked up right fork.");
                forks[id].acquire();  // Pick up left fork
                LOGGER.info("Philosopher " + id + " picked up left fork.");
            } else {
                // Other philosophers pick up left fork first
                forks[id].acquire();  // Pick up left fork
                LOGGER.info("Philosopher " + id + " picked up left fork.");
                forks[(id + 1) % NUM_PHILOSOPHERS].acquire();  // Pick up right fork
                LOGGER.info("Philosopher " + id + " picked up right fork.");
            }
        }

        // Simulate eating
        private void eat() throws InterruptedException {
            LOGGER.info("Philosopher " + id + " is eating.");
            Thread.sleep((long) (Math.random() * 1000)); // Eating time
        }

        // Put down forks
        private void putDownForks() {
            // Philosopher 0 puts down left fork first, others put down right fork first
            if (id == 0) {
                forks[id].release();  // Put down left fork
                LOGGER.info("Philosopher " + id + " put down left fork.");
                forks[(id + 1) % NUM_PHILOSOPHERS].release();  // Put down right fork
                LOGGER.info("Philosopher " + id + " put down right fork.");
            } else {
                forks[(id + 1) % NUM_PHILOSOPHERS].release();  // Put down right fork
                LOGGER.info("Philosopher " + id + " put down right fork.");
                forks[id].release();  // Put down left fork
                LOGGER.info("Philosopher " + id + " put down left fork.");
            }
        }
    }

    public static void main(String[] args) {
        // Executor service to manage philosopher threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_PHILOSOPHERS);

        // Create philosopher threads and submit them to the executor
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            executor.submit(new Philosopher(i));
        }

        // Initiate orderly shutdown after some time (to allow philosophers to eat and think)
        try {
            Thread.sleep(10000);  // Let philosophers eat for a while (10 seconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdownNow();  // Stop all philosophers after the time limit
    }
}