import java.util.*;

public class SJF_Scheduling {

    private static class Process {
        int id, burstTime, arrivalTime, waitingTime, turnaroundTime, completionTime, remainingTime;

        Process(int id, int burstTime, int arrivalTime) {
            this.id = id;
            this.burstTime = burstTime;
            this.arrivalTime = arrivalTime;
            this.remainingTime = burstTime;
        }

        // Deep copy constructor
        Process(Process p) {
            this(p.id, p.burstTime, p.arrivalTime);
        }
    }

    // Generate process array
    public static Process[] generateProcesses(int n, boolean randomArrival, Scanner scanner) {
        Random random = new Random();
        Process[] processes = new Process[n];
        System.out.println("Enter Burst Time for each process:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + " Burst Time: ");
            int bt = scanner.nextInt();
            int at = randomArrival ? random.nextInt(10) : 0;
            processes[i] = new Process(i + 1, bt, at);
        }
        return processes;
    }

    // SJF (Non-Preemptive)
    public static void sjfScheduling(Process[] original, String variant) {
        Process[] processes = Arrays.stream(original).map(Process::new).toArray(Process[]::new);
        int n = processes.length;
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        boolean[] completed = new boolean[n];
        int completedCount = 0, currentTime = 0;

        while (completedCount < n) {
            int minIndex = -1;
            int minBurst = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!completed[i] && processes[i].arrivalTime <= currentTime && processes[i].burstTime < minBurst) {
                    minBurst = processes[i].burstTime;
                    minIndex = i;
                }
            }

            if (minIndex == -1) {
                currentTime++;
            } else {
                Process p = processes[minIndex];
                p.completionTime = currentTime + p.burstTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;

                completed[minIndex] = true;
                completedCount++;
                currentTime = p.completionTime;
            }
        }

        displayResults("SJF (Non-Preemptive) - " + variant, processes);
    }

    // SRTF (Preemptive)
    public static void srtfScheduling(Process[] original, String variant) {
        Process[] processes = Arrays.stream(original).map(Process::new).toArray(Process[]::new);
        int n = processes.length;
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0, completedCount = 0;

        while (completedCount < n) {
            int minIndex = -1;
            int minRemaining = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime && processes[i].remainingTime > 0 &&
                        processes[i].remainingTime < minRemaining) {
                    minRemaining = processes[i].remainingTime;
                    minIndex = i;
                }
            }

            if (minIndex == -1) {
                currentTime++;
            } else {
                Process p = processes[minIndex];
                p.remainingTime--;
                currentTime++;

                if (p.remainingTime == 0) {
                    p.completionTime = currentTime;
                    p.turnaroundTime = p.completionTime - p.arrivalTime;
                    p.waitingTime = p.turnaroundTime - p.burstTime;
                    completedCount++;
                }
            }
        }

        displayResults("SRTF (Preemptive SJF) - " + variant, processes);
    }

    // Display formatted results and averages
    public static void displayResults(String algorithm, Process[] processes) {
        double totalWT = 0, totalTAT = 0;
        System.out.println("\n--- " + algorithm + " ---");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-10s%-10s%-10s%-10s%-10s%-10s\n", "Process", "AT", "BT", "CT", "TAT", "WT");
        System.out.println("------------------------------------------------------------");

        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
            System.out.printf("%-10s%-10d%-10d%-10d%-10d%-10d\n",
                    "P" + p.id, p.arrivalTime, p.burstTime, p.completionTime, p.turnaroundTime, p.waitingTime);
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("Average TAT = %.2f\n", totalTAT / processes.length);
        System.out.printf("Average WT  = %.2f\n", totalWT / processes.length);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();

        // Generate processes with and without arrival time
        Process[] processesWithArrival = generateProcesses(n, true, scanner);
        Process[] processesZeroArrival = Arrays.stream(processesWithArrival)
                .map(p -> new Process(p.id, p.burstTime, 0))
                .toArray(Process[]::new);

        // Scheduling variants
        sjfScheduling(processesWithArrival, "Arrival Time (Random)");
        sjfScheduling(processesZeroArrival, "Arrival Time = 0");
        srtfScheduling(processesWithArrival, "Arrival Time (Random)");
        srtfScheduling(processesZeroArrival, "Arrival Time = 0");

        scanner.close();
    }
}
