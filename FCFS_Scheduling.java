import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class FCFS_Scheduling {

    private static class Process {
        int id, burstTime, arrivalTime, waitingTime, turnaroundTime, completionTime;

        Process(int id, int burstTime, int arrivalTime) {
            this.id = id;
            this.burstTime = burstTime;
            this.arrivalTime = arrivalTime;
        }
    }

    // Function to compute waiting time and turnaround time
    public static void calculateTimes(Process[] processes) {
        int currentTime = 0;

        for (Process process : processes) {
            // If CPU is idle, jump to process arrival time
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            process.completionTime = currentTime + process.burstTime;
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;

            currentTime = process.completionTime;
        }
    }

    // Function to display results with average TAT and WT
    public static void displayResults(Process[] processes) {
        int totalTAT = 0, totalWT = 0;

        System.out.println("\n--------------------------------------------------");
        System.out.printf("%-10s%-10s%-10s%-10s%-10s%-10s\n", "Process", "AT", "BT", "CT", "TAT", "WT");
        System.out.println("--------------------------------------------------");

        for (Process p : processes) {
            totalTAT += p.turnaroundTime;
            totalWT += p.waitingTime;
            System.out.printf("%-10s%-10d%-10d%-10d%-10d%-10d\n",
                    "P" + p.id, p.arrivalTime, p.burstTime, p.completionTime, p.turnaroundTime, p.waitingTime);
        }

        double avgTAT = (double) totalTAT / processes.length;
        double avgWT = (double) totalWT / processes.length;

        System.out.println("--------------------------------------------------");
        System.out.printf("Average Turnaround Time: %.2f\n", avgTAT);
        System.out.printf("Average Waiting Time   : %.2f\n", avgWT);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        Process[] processes = new Process[n];

        System.out.println("Enter Burst Time for each process:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + " Burst Time: ");
            int bt = scanner.nextInt();
            int at = random.nextInt(10); // Random Arrival Time (0-9) for Case 1
            processes[i] = new Process(i + 1, bt, at);
        }

        // Sort processes by Arrival Time
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        // Calculate and display FCFS with arrival time
        System.out.println("\n--- FCFS Scheduling WITH Arrival Time ---");
        calculateTimes(processes);
        displayResults(processes);

        // Reset arrival time for case 2 (all AT = 0)
        for (Process p : processes) {
            p.arrivalTime = 0;
        }

        // Sort processes by original ID
        Arrays.sort(processes, Comparator.comparingInt(p -> p.id));

        // Calculate and display FCFS without arrival time
        System.out.println("\n--- FCFS Scheduling WITHOUT Arrival Time (AT = 0) ---");
        calculateTimes(processes);
        displayResults(processes);

        scanner.close();
    }
}
