import java.util.*;

class Process {
    int pid;       // Process ID
    int bt;        // Burst Time
    int priority;  // Priority
    int wt;        // Waiting Time
    int tat;       // Turnaround Time

    Process(int pid, int bt, int priority) {
        this.pid = pid;
        this.bt = bt;
        this.priority = priority;
    }
}

public class Priority_Scheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for Process " + (i + 1));
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            System.out.print("Priority (lower value = higher priority): ");
            int priority = sc.nextInt();
            processes[i] = new Process(i + 1, bt, priority);
        }

        // Sort by priority (lower number = higher priority)
        Arrays.sort(processes, Comparator.comparingInt(p -> p.priority));

        // Calculate waiting time and turnaround time
        int totalWT = 0, totalTAT = 0;
        processes[0].wt = 0;
        processes[0].tat = processes[0].bt;

        for (int i = 1; i < n; i++) {
            processes[i].wt = processes[i - 1].wt + processes[i - 1].bt;
            processes[i].tat = processes[i].wt + processes[i].bt;
            totalWT += processes[i].wt;
            totalTAT += processes[i].tat;
        }

        // Print table header
        System.out.println("\n+---------+------------+----------+--------------+----------------+");
        System.out.printf("| %-7s | %-10s | %-8s | %-12s | %-14s |\n", "Process", "Burst Time", "Priority", "Waiting Time", "Turnaround Time");
        System.out.println("+---------+------------+----------+--------------+----------------+");

        // Print process data
        for (Process p : processes) {
            System.out.printf("| %-7s | %-10d | %-8d | %-12d | %-14d |\n", "P" + p.pid, p.bt, p.priority, p.wt, p.tat);
        }

        System.out.println("+---------+------------+----------+--------------+----------------+");

        // Print averages
        System.out.printf("\nAverage Waiting Time    : %.2f\n", (float) totalWT / n);
        System.out.printf("Average Turnaround Time : %.2f\n", (float) (totalTAT + processes[0].tat) / n);
    }
}
