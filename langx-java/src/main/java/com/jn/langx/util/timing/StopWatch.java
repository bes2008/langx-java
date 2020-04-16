package com.jn.langx.util.timing;


import com.jn.langx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;


/**
 * Simple stop watch, allowing for timing of a number of tasks,
 * exposing total running time and running time for each named task.
 * <p>
 * <p>Conceals use of {@code System.currentTimeMillis()}, improving the
 * readability of application code and reducing the likelihood of calculation errors.
 * <p>
 * <p>Note that this object is not designed to be thread-safe and does not
 * use synchronization.
 * <p>
 * <p>This class is normally used to verify performance during proof-of-concepts
 * and in development, rather than as part of production applications.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since May 2, 2001
 */
public class StopWatch {

    /**
     * Identifier of this stop watch.
     * Handy when we have output from multiple stop watches
     * and need to distinguish between them in log or console output.
     */
    private final String id;

    private boolean keepTaskList;

    private final List<TaskInfo> taskList = new LinkedList<TaskInfo>();

    /**
     * Start time of the current task.
     */
    private long startTimeMillis;

    /**
     * Name of the current task.
     */
    @Nullable
    private String currentTaskName;

    @Nullable
    private StopWatch.TaskInfo lastTaskInfo;

    private int taskCount;

    /**
     * Total running time.
     */
    private long totalTimeMillis;


    /**
     * Construct a new stop watch. Does not start any task.
     */
    public StopWatch() {
        this("");
    }

    /**
     * Construct a new stop watch with the given id.
     * Does not start any task.
     *
     * @param id identifier for this stop watch.
     *           Handy when we have output from multiple stop watches
     *           and need to distinguish between them.
     */
    public StopWatch(String id) {
        this(id, true);
    }

    public StopWatch(String id, boolean keepTaskList) {
        this.id = id;
        this.setKeepTaskList(keepTaskList);
    }

    /**
     * Return the id of this stop watch, as specified on construction.
     *
     * @return the id (empty String by default)
     * @see #StopWatch(String)
     * @since 4.2.2
     */
    public String getId() {
        return this.id;
    }

    /**
     * Determine whether the TaskInfo array is built over time. Set this to
     * "false" when using a StopWatch for millions of intervals, or the task
     * info structure will consume excessive memory. Default is "true".
     */
    public void setKeepTaskList(boolean keepTaskList) {
        this.keepTaskList = keepTaskList;
    }


    /**
     * Start an unnamed task. The results are undefined if {@link #stop()}
     * or timing methods are called without invoking this method.
     *
     * @see #stop()
     */
    public void start() throws IllegalStateException {
        start("");
    }

    /**
     * Start a named task. The results are undefined if {@link #stop()}
     * or timing methods are called without invoking this method.
     *
     * @param taskName the name of the task to start
     * @see #stop()
     */
    public void start(String taskName) throws IllegalStateException {
        if (this.currentTaskName != null) {
            throw new IllegalStateException("Can't start StopWatch: it's already running");
        }
        this.currentTaskName = taskName;
        this.startTimeMillis = System.currentTimeMillis();
    }

    /**
     * Stop the current task. The results are undefined if timing
     * methods are called without invoking at least one pair
     * {@code start()} / {@code stop()} methods.
     *
     * @see #start()
     */
    public void stop() throws IllegalStateException {
        if (this.currentTaskName == null) {
            throw new IllegalStateException("Can't stop StopWatch: it's not running");
        }
        long now = System.currentTimeMillis();
        this.lastTaskInfo = new StopWatch.TaskInfo(this.currentTaskName, startTimeMillis, now);
        this.totalTimeMillis += lastTaskInfo.getTimeMillis();
        if (this.keepTaskList) {
            this.taskList.add(this.lastTaskInfo);
        }
        ++this.taskCount;
        this.currentTaskName = null;
    }

    /**
     * Return whether the stop watch is currently running.
     *
     * @see #currentTaskName()
     */
    public boolean isRunning() {
        return (this.currentTaskName != null);
    }

    /**
     * Return the name of the currently running task, if any.
     *
     * @see #isRunning()
     * @since 4.2.2
     */
    @Nullable
    public String currentTaskName() {
        return this.currentTaskName;
    }


    /**
     * Return the time taken by the last task.
     */
    public long getLastTaskTimeMillis() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task interval");
        }
        return this.lastTaskInfo.getDurationTimeMillis();
    }

    /**
     * Return the name of the last task.
     */
    public String getLastTaskName() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task name");
        }
        return this.lastTaskInfo.getTaskName();
    }

    /**
     * Return the last task as a TaskInfo object.
     */
    public StopWatch.TaskInfo getLastTaskInfo() throws IllegalStateException {
        if (this.lastTaskInfo == null) {
            throw new IllegalStateException("No tasks run: can't get last task info");
        }
        return this.lastTaskInfo;
    }


    /**
     * Return the total time in milliseconds for all tasks.
     */
    public long getTotalTimeMillis() {
        return this.totalTimeMillis;
    }

    /**
     * Return the total time in seconds for all tasks.
     */
    public double getTotalTimeSeconds() {
        return this.totalTimeMillis / 1000.0;
    }

    /**
     * Return the number of tasks timed.
     */
    public int getTaskCount() {
        return this.taskCount;
    }

    /**
     * Return an array of the data for tasks performed.
     */
    public StopWatch.TaskInfo[] getTaskInfo() {
        if (!this.keepTaskList) {
            throw new UnsupportedOperationException("Task info is not being kept!");
        }
        return this.taskList.toArray(new StopWatch.TaskInfo[0]);
    }


    /**
     * Return a short description of the total running time.
     */
    public String shortSummary() {
        return "StopWatch '" + getId() + "': running time (millis) = " + getTotalTimeMillis();
    }

    /**
     * Return a string with a table describing all tasks performed.
     * For custom reporting, call getTaskInfo() and use the task info directly.
     */
    public String prettyPrint() {
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append('\n');
        if (!this.keepTaskList) {
            sb.append("No task info kept");
        } else {
            sb.append("-----------------------------------------\n");
            sb.append("ms     %     Task name\n");
            sb.append("-----------------------------------------\n");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(5);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);
            for (StopWatch.TaskInfo task : getTaskInfo()) {
                sb.append(nf.format(task.getDurationTimeMillis())).append("  ");
                sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Return an informative string describing all tasks performed
     * For custom reporting, call {@code getTaskInfo()} and use the task info directly.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(shortSummary());
        if (this.keepTaskList) {
            for (StopWatch.TaskInfo task : getTaskInfo()) {
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getDurationTimeMillis());
                long percent = Math.round((100.0 * task.getTimeSeconds()) / getTotalTimeSeconds());
                sb.append(" = ").append(percent).append("%");
            }
        } else {
            sb.append("; no task info kept");
        }
        return sb.toString();
    }


    /**
     * Inner class to hold data about one task executed within the stop watch.
     */
    public static final class TaskInfo {

        private final String taskName;

        private final long startTimeMills;

        private final long endTimeMills;

        private final long durationTimeMillis;

        TaskInfo(String taskName, long startTimeMills) {
            this(taskName, startTimeMills, System.currentTimeMillis());
        }

        TaskInfo(String taskName, long startTimeMills, long endTimeMillis) {
            this.taskName = taskName;
            this.startTimeMills = startTimeMills;
            this.endTimeMills = endTimeMillis;
            this.durationTimeMillis = endTimeMillis - startTimeMills;
        }

        /**
         * Return the name of this task.
         */
        public String getTaskName() {
            return this.taskName;
        }

        /**
         * Return the time in milliseconds this task took.
         */
        public long getTimeMillis() {
            return this.durationTimeMillis;
        }


        public long getStartTimeMills() {
            return startTimeMills;
        }

        public long getEndTimeMills() {
            return endTimeMills;
        }

        public long getDurationTimeMillis() {
            return durationTimeMillis;
        }

        /**
         * Return the time in seconds this task took.
         */
        public double getTimeSeconds() {
            return (this.durationTimeMillis / 1000.0);
        }
    }

}
