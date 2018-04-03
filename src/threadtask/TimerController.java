package threadtask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Control a counter task.
 * This demonstrates the use of worker threads for long-running tasks.
 * 
 * @author jim
 */
public class TimerController {
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;
	/** Field where user types input value (count or time). */
	@FXML
	private TextField inputField;
	/** ProgressBar to show the progress of the task. */
	@FXML
	private ProgressBar progressBar;
	/** Display the result of the task, including partial result. */
	@FXML
	private Label displayField;
	/** Object of inner class that performs counting. */
	private CountUp task;
	
	@FXML
	public void initialize() {
		startButton.setOnAction( this::start );
		stopButton.setOnAction( this::stop );
	}
	
	/** Call this method when Start button is pressed. */
	public void start(ActionEvent event) {
		// this is to avoid 2nd button press while task is still running
		if (task == null || ! task.running) {
			int count = Integer.parseInt(inputField.getText());
			task = new CountUp(count);
			displayCount(task.getCount());
			progressBar.setProgress(0.0);
			task.run();
		}
	}
	
	/** Call this method when Stop button is pressed. */
	public void stop(ActionEvent event) {
		// stop the task
		if (task != null) task.stop();
	}
	
	/** Display the count on the UI displayField. */
	public void displayCount(int count) {
		displayField.setText( Integer.toString(count) );
		System.out.println(count);
	}
	
	/**
	 * A task that counts from 1 to a given total.
	 */
	class CountUp {
		private int totalcount;
		private int count;
		// a boolean flag used to stop the run() method
		private boolean cancelled;
		// a way to test if task is running
		protected boolean running;
		
		public CountUp(int totalCount) {
			this.totalcount = totalCount;
			this.count = 0;
			this.cancelled = false;
		}
		
		private int getCount() {
			return count;
		}

		public void run() {
			running = true;
			// how often to update the progressbar.
			// updating progressbar too often (tiny progress) is a waste of time
			int increment = Math.max(totalcount/100, 2);
			while(count < totalcount && ! cancelled) {
				count += 1;
				displayCount(count); // update the UI
				if (count % increment == 0) updateProgress(count, totalcount);
				// wait for 1 millisecond (this is not accurate)
				try { Thread.sleep(1); } 
				catch (InterruptedException ex) { cancelled = true; }
			}
			running = false;
		}

		public void stop() {
			cancelled = true;
		}
		
		public void updateProgress(int workdone, int totalwork) {
			progressBar.setProgress(((double)workdone)/totalwork);
		}
	}
}
