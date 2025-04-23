import java.util.*;

public class PageReplacement {

    // LRU (Least Recently Used)
    public static void LRU(int[] pages, int frameCount) {
        Set<Integer> frames = new HashSet<>();
        LinkedList<Integer> order = new LinkedList<>();
        int pageFaults = 0;

        for (int page : pages) {
            if (!frames.contains(page)) {
                if (frames.size() == frameCount) {
                    frames.remove(order.poll());
                }
                frames.add(page);
                pageFaults++;
            }
            order.remove((Integer) page);
            order.add(page);
        }
        System.out.println("LRU Page Faults: " + pageFaults);
    }

    // FIFO (First In First Out)
    public static void FIFO(int[] pages, int frameCount) {
        Queue<Integer> frames = new LinkedList<>();
        Set<Integer> frameSet = new HashSet<>();
        int pageFaults = 0;

        for (int page : pages) {
            if (!frameSet.contains(page)) {
                if (frames.size() == frameCount) {
                    frameSet.remove(frames.poll());
                }
                frames.offer(page);
                frameSet.add(page);
                pageFaults++;
            }
        }
        System.out.println("FIFO Page Faults: " + pageFaults);
    }

    // Optimal Page Replacement
    public static void Optimal(int[] pages, int frameCount) {
        int pageFaults = 0;
        int[] frames = new int[frameCount];
        Arrays.fill(frames, -1);

        for (int i = 0; i < pages.length; i++) {
            if (indexOf(frames, pages[i]) == -1) {
                int emptyIndex = indexOf(frames, -1);
                if (emptyIndex != -1) frames[emptyIndex] = pages[i];
                else {
                    int farthest = farthestUse(frames, pages, i);
                    frames[farthest] = pages[i];
                }
                pageFaults++;
            }
        }
        System.out.println("Optimal Page Faults: " + pageFaults);
    }

    private static int indexOf(int[] frames, int page) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == page) return i;
        }
        return -1;
    }

    private static int farthestUse(int[] frames, int[] pages, int currentIndex) {
        int farthest = -1, maxDistance = -1;
        for (int i = 0; i < frames.length; i++) {
            int dist = nextUse(pages, currentIndex, frames[i]);
            if (dist == -1) return i;
            if (dist > maxDistance) {
                maxDistance = dist;
                farthest = i;
            }
        }
        return farthest;
    }

    private static int nextUse(int[] pages, int currentIndex, int page) {
        for (int i = currentIndex + 1; i < pages.length; i++) {
            if (pages[i] == page) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // User input for page reference string
        System.out.print("Enter page reference string (space-separated): ");
        String input = sc.nextLine();
        String[] pageStrings = input.split(" ");
        int[] pages = new int[pageStrings.length];

        for (int i = 0; i < pageStrings.length; i++) {
            pages[i] = Integer.parseInt(pageStrings[i]);
        }

        // User input for Number of frames
        System.out.print("Enter the number of frames: ");
        int frameCount = sc.nextInt();

        // Call page replacement algorithms
        FIFO(pages, frameCount);
        LRU(pages, frameCount);
        Optimal(pages, frameCount);

        sc.close();
    }
}