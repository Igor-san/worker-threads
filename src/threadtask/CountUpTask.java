package threadtask;

import javafx.concurrent.Task;

/** A worker that counts slowly from 0 to a total count. */
public class CountUpTask extends Task<Integer> {
    private int totalcount;
    private int count;

    public CountUpTask(int maxcount) {
        this.totalcount = maxcount;
        this.count = 0;
    }

    @Override
    public Integer call() {
        updateMessage("Starting");
        updateProgress(0, totalcount);

        while(count < totalcount) {
            count += 1;
            System.out.println(count); // for testing
            // Notification services from the superclass
            updateValue(count);
            updateMessage(Integer.toString(count));
            updateProgress(count, totalcount);
            // wait for 1 millisecond (but not very accurate)
            try { Thread.sleep(1); }
            catch (InterruptedException ex) { break; }
        }
        updateProgress(count,totalcount);
        // Return the result of the task
        return count;
    }
}
