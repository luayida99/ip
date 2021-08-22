package side.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import side.util.Storage;
import side.tasks.Task;
import side.tasks.Event;
import side.tasks.Deadline;

/**
 * CS2103T Individual Project AY 21/22 Sem 1
 * Project Duke: Incrementally building a Chatbot.
 *
 * The TaskList class provides a wrapper around an ArrayList of Tasks to encapsulate the list of tasks given to
 * Side by its user. Supports add, delete and done operations on tasks in it.
 *
 * @author Lua Yi Da
 */

public class TaskList {
    private ArrayList<Task> tasks;
    private int taskLabel;
    private Storage storage;

    public TaskList() {
        this.tasks = new ArrayList<>();
        this.taskLabel = 0;
        this.storage = new Storage();
    }

    public String retrieve()  {
        try {
            String filepath = "./data";
            this.tasks = this.storage.retrieve(filepath);
            this.taskLabel = this.tasks.size();
            return "Here's your history...\n" + this.listToString();
        } catch (FileNotFoundException e) {
            return "You have no history...";
        }
    }

    public void save() {
        try {
            String filepath = "./data";
            this.storage.save(this.tasks, filepath);
        } catch (IOException e) {
            System.out.println("Problem");
        }
    }

    /**
     * Calculates the number of tasks in the TaskList.
     *
     * @return Number of tasks in TaskList.
     */
    public int length() {
        return this.tasks.size();
    }

    /**
     * Adds a task to the TaskList.
     *
     * @param description String representation of task information.
     */
    public void addTask(String description) {
        Task task = new Task(description);
        this.tasks.add(task);
        taskLabel++;
    }

    /**
     * Adds an event to the TaskList.
     *
     * @param description String representation of event information.
     * @param startDatetime Time at which the event starts.
     * @param endDatetime Time at which event ends.
     */
    public void addEvent(String description, String startDatetime, String endDatetime) {
        Event event = new Event(description, startDatetime, endDatetime);
        this.tasks.add(event);
        taskLabel++;
    }

    /**
     * Adds a deadline to the TaskList.
     *
     * @param description String representation of deadline information.
     * @param time Time at which the deadline elapses.
     */
    public void addDeadline(String description, String time) {
        Deadline event = new Deadline(description, time);
        this.tasks.add(event);
        taskLabel++;
    }

    /**
     * Marks a task as done.
     *
     * @param index Index of task to be marked as done.
     * @return String response of Side in response to marking task as done.
     */
    public String markTaskDone(int index) {
        return tasks.get(index).markAsDone();
    }

    /**
     * Deletes a task from the TaskList.
     *
     * @param index Index of task to be removed.
     * @return String response of Side in response to deleting task.
     */
    public String delete(int index) {
        Task taskToDelete = this.tasks.get(index);
        this.tasks.remove(index);
        taskLabel--;
        String taskQuantifier = this.tasks.size() == 1 ? "task..." : "tasks...";
        String taskCount = "\nYou now have " + tasks.size() + " " + taskQuantifier;
        return "Fine, I'll delete: " + taskToDelete.toString() + taskCount;
    }

    public String listToString() {
        StringBuilder tasksList = new StringBuilder();

        if (this.taskLabel == 0) {
            return "Nothing to see here...";
        }

        for (int i = 0; i < this.taskLabel; i++) {
            String fullTaskLine = (i + 1) + ". " + this.tasks.get(i).toString() + "\n";
            tasksList.append(fullTaskLine);
        }

        return tasksList.toString();
    }

    @Override
    public String toString() {
        if (this.taskLabel == 0) {
            return "No tasks yet, stop checking...";
        }

        return "Fine, here are your tasks: \n" + this.listToString();
    }
}
