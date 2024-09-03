package commands;

import exceptions.BrockException;
import storage.Storage;
import task.TaskList;
import ui.Ui;
import utility.Utility;

/**
 * Represents an unmark command entered by the user.
 */
public class UnmarkCommand extends Command {
    /**
     * Stores the command string associated with unmark command.
     *
     * @param command Command string.
     */
    public UnmarkCommand(String command) {
        super(command);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Chatbot checks if unmark command is valid.
     * If so, it unmarks the associated task in {@code tasks}.
     * Displays a response indicating it has successfully unmarked the task.
     * </p>
     *
     * @throws BrockException If unmark command is invalid.
     */
    @Override
    public void execute(Ui ui, Storage storage, TaskList tasks) throws BrockException {
        String command = super.getCommand();
        Utility.validateStatus(command, Utility.Action.UNMARK, tasks);

        int taskIndex = Utility.getTaskIndex(command);
        tasks.unmarkTask(taskIndex);
        ui.displayResponse("OK, I've marked this task as not done yet:\n"
                + "  "
                + tasks.getTaskDetails(taskIndex));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
