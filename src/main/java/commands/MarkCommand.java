package commands;

import exceptions.BrockException;
import storage.TaskStorage.TaskStorage;
import task.TaskList;
import utility.CommandUtility;

/**
 * Represents a mark command entered by the user.
 */
public class MarkCommand extends Command {
    /**
     * Stores the command string associated with mark command.
     *
     * @param command Command string.
     */
    public MarkCommand(String command) {
        super(command);
    }

    private void updateSaveFile(TaskStorage taskStorage, TaskList tasks) throws BrockException {
        String tasksString = tasks.listTasks();
        taskStorage.writeToFile("", false);
        taskStorage.writeToFile(tasksString, true);
    }

    private String getResponse(TaskList tasks, int taskIndex, boolean isSuccessful) {
        if (!isSuccessful) {
            return "Task has been marked already!";
        }
        return "Nice! I've marked this task as done:\n"
                + "  " + tasks.getTaskDetails(taskIndex);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Chatbot checks if mark command is valid.
     * If so, it marks the associated task in {@code tasks} and updates the save file.
     * Returns a response indicating it has successfully marked the task.
     * </p>
     *
     * @throws BrockException If mark command is invalid.
     */
    @Override
    public String execute(TaskStorage taskStorage, TaskList tasks) throws BrockException {
        String command = super.getCommand();
        CommandUtility.validateStatus(command, CommandUtility.Action.MARK, tasks);
        int taskIndex = CommandUtility.getTaskIndex(command);
        boolean isSuccessful = tasks.markTask(taskIndex);

        this.updateSaveFile(taskStorage, tasks);
        return this.getResponse(tasks, taskIndex, isSuccessful);
    }
}
