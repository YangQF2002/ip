package commands;

import exceptions.BrockException;
import storage.Storage;
import task.TaskList;
import ui.Ui;
import utility.Utility;

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

    /**
     * {@inheritDoc}
     *
     * <p>
     * Chatbot checks if mark command is valid.
     * If so, it marks the associated task in {@code tasks} and updates the save file.
     * Displays a response indicating it has successfully marked the task.
     * </p>
     *
     * @throws BrockException If mark command is invalid.
     */
    @Override
    public void execute(Ui ui, Storage storage, TaskList tasks) throws BrockException {
        String command = super.getCommand();
        Utility.validateStatus(command, Utility.Action.MARK, tasks);

        int taskIndex = Utility.getTaskIndex(command);
        boolean isSuccessful = tasks.markTask(taskIndex);
        if (!isSuccessful) {
            ui.displayResponse("Task has been marked already!");
            return;
        }

        ui.displayResponse("Nice! I've marked this task as done:\n"
                + "  "
                + tasks.getTaskDetails(taskIndex));

        // Update the save file
        String tasksString = tasks.listTasks();
        storage.writeToFile("", false);
        storage.writeToFile(tasksString, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
