import java.time.Instant;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class TaskScheduler {

    // creating a atomicLong sequence variable
    private final AtomicLong seq = new AtomicLong(0);

    // creating a static class to represnt a Task
    private static final class Task {
        // Making every attribute of class to final so that other process cannot make a change in it
        final String id;
        final long epochSeconds;
        final String description;
        final long seq;

        // Parameterized constructor for Task Class
        Task(String id, long epochSeconds, String description, long seq) {
            // Assigning the value passed to constructor to current object
            this.id = id;
            this.epochSeconds = epochSeconds;
            this.description = description;
            this.seq = seq;
        }
    }


    // Creating a new PriorityQueue based on rule given
    private final PriorityQueue<Task> pq = new PriorityQueue<>(
            // Lambda expression to compare the sequence
            (a, b) -> {
                // Checking their epoch time is equal or not
                if (a.epochSeconds != b.epochSeconds) {
                    // if those are not equal we simply compare the return them numerically
                    return Long.compare(a.epochSeconds, b.epochSeconds);
                }
                // Otherwise compare their sequence on return on the basis of sequence
                return Long.compare(a.seq, b.seq);
            }
    );


    // Creating the reentrant lock to make the call syn
    private final ReentrantLock lock = new ReentrantLock();

    // Method to schedule the task
    public void scheduleTask(String id, long epochSeconds, String description) {
        // Lock to avoid deadlock situations
        lock.lock();
        try {
            // Try to insert new task to priority queue
            pq.offer(new Task(id, epochSeconds, description, seq.getAndIncrement()));
        } finally {
            // Unlock to avoid deadlock situations
            lock.unlock();
        }
    }

    public void executeDueTasks() {
        // Get current time
        long now = Instant.now().getEpochSecond();
        while (true) {

            Task next;
            // Lock to avoid deadlock situations
            lock.lock();
            try{
                next = pq.peek();
                // Check if there is nothing in the priority queue or next's epoch time is greater than
                if (next == null || next.epochSeconds > now) {
                    return; // nothing due
                }
                // Simply remove the task out of the priority queue
                pq.poll();
            } finally {
                // Unlock to avoid deadlock situations
                lock.unlock();
            }
            System.out.println("Executing task: " + next.description);
        }
    }// execute & remove all tasks with time <= now
}



