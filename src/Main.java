//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        long now = java.time.Instant.now().getEpochSecond();
        TaskScheduler sceduler = new TaskScheduler();
        sceduler.scheduleTask("t1", now + 2, "Backup database");
        sceduler.scheduleTask("t2", now + 2, "Send report email");
        sceduler.scheduleTask("late", now - 5, "Run missed task");

// simple loop
        for (int i = 0; i < 6; i++) {
            sceduler.executeDueTasks();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        }
}
