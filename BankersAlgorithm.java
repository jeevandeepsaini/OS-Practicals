import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankersAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(BankersAlgorithm.class.getName());
    private final int numProcesses, numResources;
    private final int[][] allocation, max, need;
    private final int[] available;

    public BankersAlgorithm(int numProcesses, int numResources, int[][] allocation, int[][] max, int[] available) {
        if (numProcesses <= 0 || numResources <= 0 || allocation == null || max == null || available == null)
            throw new IllegalArgumentException("Invalid input");

        this.numProcesses = numProcesses;
        this.numResources = numResources;
        this.allocation = deepCopy(allocation);
        this.max = deepCopy(max);
        this.available = Arrays.copyOf(available, available.length);
        this.need = new int[numProcesses][numResources];
        for (int i = 0; i < numProcesses; i++)
            for (int j = 0; j < numResources; j++)
                need[i][j] = max[i][j] - allocation[i][j];
    }

    private int[][] deepCopy(int[][] matrix) {
        return Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
    }

    private boolean isSafe() {
        boolean[] finish = new boolean[numProcesses];
        int[] work = Arrays.copyOf(available, numResources);
        int[] safeSequence = new int[numProcesses];
        int count = 0;

        while (count < numProcesses) {
            boolean found = false;
            for (int i = 0; i < numProcesses; i++) {
                if (!finish[i] && canAllocate(need[i], work)) {
                    for (int j = 0; j < numResources; j++)
                        work[j] += allocation[i][j];
                    safeSequence[count++] = i;
                    finish[i] = true;
                    found = true;
                }
            }
            if (!found) {
                LOGGER.warning("System is not in safe state");
                return false;
            }
        }
        LOGGER.info("Safe sequence: " + Arrays.toString(safeSequence));
        return true;
    }

    private boolean canAllocate(int[] need, int[] work) {
        for (int i = 0; i < numResources; i++) if (need[i] > work[i]) return false;
        return true;
    }

    public synchronized boolean requestResources(int processId, int[] request) {
        validateRequest(processId, request);
        for (int i = 0; i < numResources; i++) {
            available[i] -= request[i];
            allocation[processId][i] += request[i];
            need[processId][i] -= request[i];
        }
        if (isSafe()) {
            LOGGER.info("Resources allocated to process " + processId);
            return true;
        }
        rollbackAllocation(processId, request);
        return false;
    }

    private void validateRequest(int processId, int[] request) {
        if (processId < 0 || processId >= numProcesses || request.length != numResources)
            throw new IllegalArgumentException("Invalid process ID or request vector");
        for (int i = 0; i < numResources; i++) {
            if (request[i] < 0 || request[i] > need[processId][i] || request[i] > available[i])
                throw new IllegalArgumentException("Invalid request");
        }
    }

    private void rollbackAllocation(int processId, int[] request) {
        for (int i = 0; i < numResources; i++) {
            available[i] += request[i];
            allocation[processId][i] -= request[i];
            need[processId][i] += request[i];
        }
        LOGGER.warning("Request denied for process " + processId);
    }

    public synchronized void releaseResources(int processId, int[] release) {
        validateRelease(processId, release);
        for (int i = 0; i < numResources; i++) {
            allocation[processId][i] -= release[i];
            need[processId][i] += release[i];
            available[i] += release[i];
        }
        LOGGER.info("Process " + processId + " released resources: " + Arrays.toString(release));
    }

    private void validateRelease(int processId, int[] release) {
        if (processId < 0 || processId >= numProcesses || release.length != numResources)
            throw new IllegalArgumentException("Invalid process ID or release vector");
        for (int i = 0; i < numResources; i++) {
            if (release[i] < 0 || release[i] > allocation[processId][i])
                throw new IllegalArgumentException("Invalid release");
        }
    }

    public static void main(String[] args) {
        try {
            int numProcesses = 5, numResources = 3;
            int[][] allocation = { {0, 1, 0}, {2, 0, 0}, {3, 0, 2}, {2, 1, 1}, {0, 0, 2} };
            int[][] max = { {7, 5, 3}, {3, 2, 2}, {9, 0, 2}, {2, 2, 2}, {4, 3, 3} };
            int[] available = {3, 3, 2};
            BankersAlgorithm banker = new BankersAlgorithm(numProcesses, numResources, allocation, max, available);

            banker.requestResources(1, new int[] {1, 0, 2});
            banker.requestResources(3, new int[] {0, 1, 0});
            banker.releaseResources(0, new int[] {0, 1, 0});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in Banker's Algorithm", e);
        }
    }
}
