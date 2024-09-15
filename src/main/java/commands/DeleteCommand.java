package commands;

import exceptions.BrockException;
import storage.Storage;
import task.TaskList;
import utility.CommandUtility;

/**
 * Represents a delete command entered by the user.
 */
public class DeleteCommand extends Command {
    /**
     * Stores the command string associated with delete command.
     *
     * @param command Command string.
     */
    public DeleteCommand(String command) {
        super(command);
    }


    private String[] processCommand() {
        String command = super.getCommand();
        return command.split(" ");
    }

    private void checkLength(String[] commandWords) throws BrockException {
        int commandLength = commandWords.length;
        if (commandLength == 1) {
            throw new BrockException("Missing task number!");
        }
        if (commandLength > 2 || CommandUtility.isNotInteger(commandWords[1])) {
            throw new BrockException("Delete command is in the form delete <task-number>!");
        }
    }

    private void checkTaskNumber(String[] commandWords, TaskList tasks) throws BrockException {
        int taskNumber = Integer.parseInt(commandWords[1]);
        int totalTasks = tasks.numTasks();
        if (taskNumber > totalTasks || taskNumber < 1) {
            throw new BrockException("Task number does not exist!");
        }
    }

    /**
     * Checks if the delete command is valid.
     *
     * @param tasks List of current {@code Task} objects.
     * @throws BrockException If command is missing a task number, has a wrong task number.
     *      Or, it is in the wrong format altogether.
     */
    private void validateDelete(TaskList tasks) throws BrockException {
        String[] commandWords = this.processCommand();
        checkLength(commandWords);
        checkTaskNumber(commandWords, tasks);
    }

    private void updateSaveFile(Storage storage, TaskList tasks) throws BrockException {
        String remainingTasks = tasks.listTasks();
        storage.writeToFile("", false);
        storage.writeToFile(remainingTasks, true);
    }

    private String getResponse(TaskList tasks, String deletedTaskDetails) {
        return "Noted. I've removed this task:\n"
                + "  " + deletedTaskDetails + '\n'
                + tasks.getTasksSummary();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Chatbot checks if delete command is valid.
     * If so, it deletes the corresponding task from {@code tasks}, updates the save file.
     * As well as return a response indicating successful deletion.
     * </p>
     *
     * @throws BrockException If delete command is invalid.
     */
    @Override
    public String execute(Storage storage, TaskList tasks) throws BrockException {
        this.validateDelete(tasks);

        String command = super.getCommand();
        int taskIndex = CommandUtility.getTaskIndex(command);
        String deletedTaskDetails = tasks.getTaskDetails(taskIndex);
        tasks.removeFromList(taskIndex);

        this.updateSaveFile(storage, tasks);
        return this.getResponse(tasks, deletedTaskDetails);
    }
}
