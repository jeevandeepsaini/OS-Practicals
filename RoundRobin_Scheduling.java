import java.util.*;

class RRProcess {
    int pid;
    int bt;
    int rt; // Remaining time
    int wt;
    int tat;

    RRProcess(int pid, int bt) {
        this.pid = pid;
        this.bt = bt;
        this.rt = bt;
    }
}

public class RoundRobin_Scheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        RRProcess[] processes = new RRProcess[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for Process " + (i + 1));
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            processes[i] = new RRProcess(i + 1, bt);
        }

        System.out.print("\nEnter Time Quantum: ");
        int timeQuantum = sc.nextInt();

        // Round Robin Scheduling Logic
        int time = 0;
        boolean done;
        do {
            done = true;
            for (RRProcess p : processes) {
                if (p.rt > 0) {
                    done = false;
                    if (p.rt > timeQuantum) {
                        time += timeQuantum;
                        p.rt -= timeQuantum;
                    } else {
                        time += p.rt;
                        p.wt = time - p.bt;
                        p.rt = 0;
                    }
                }
            }
        } while (!done);

        int totalWT = 0, totalTAT = 0;

        for (RRProcess p : processes) {
            p.tat = p.wt + p.bt;
            totalWT += p.wt;
            totalTAT += p.tat;
        }

        // Print aesthetic table
        System.out.println("\n+---------+------------+--------------+----------------+");
        System.out.printf("| %-7s | %-10s | %-12s | %-14s |\n", "Process", "Burst Time", "Waiting Time", "Turnaround Time");
        System.out.println("+---------+------------+--------------+----------------+");

        for (RRProcess p : processes) {
            System.out.printf("| %-7s | %-10d | %-12d | %-14d |\n", "P" + p.pid, p.bt, p.wt, p.tat);
        }

        System.out.println("+---------+------------+--------------+----------------+");

        System.out.printf("\nAverage Waiting Time    : %.2f\n", (float) totalWT / n);
        System.out.printf("Average Turnaround Time : %.2f\n", (float) totalTAT / n);
    }
}

