package commands;

import exceptions.BrockException;
import storage.Storage;
import task.Deadline;
import task.Task;
import task.TaskList;
import ui.Ui;
import utility.Utility;

/**
 * Represents a deadline command entered by the user.
 */
public class DeadlineCommand extends Command {
    /**
     * Stores the command string associated with deadline command.
     *
     * @param command Command string.
     */
    public DeadlineCommand(String command) {
        super(command);
    }

    /**
     * Creates a {@code Deadline} object encapsulating details about the deadline task.
     *
     * @return {@code Deadline} object.
     * @throws BrockException If deadline missing description or due date.
     */
    private Task createDeadline() throws BrockException {
        String command = super.getCommand();
        String[] commandWords = command.split(" ");
        int commandLength = commandWords.length;

        StringBuilder description = new StringBuilder();
        for (int i = 1; i < commandLength; i++) {
            if (commandWords[i].equalsIgnoreCase("/by")) {
                break;
            }
            description.append(commandWords[i])
                    .append(" ");
        }
        description.deleteCharAt(description.length() - 1);

        StringBuilder dateTime = new StringBuilder();
        boolean isSeeingDateTime = false;
        int dateTimeWords = 0;
        for (String word : commandWords) {
            if (isSeeingDateTime) {
                dateTimeWords += 1;
                dateTime.append(word)
                        .append(" ");
            }
            if (word.equalsIgnoreCase("/by")) {
                isSeeingDateTime = true;
            }
        }

        if (description.isEmpty()) {
            throw new BrockException("Description is missing!");
        }
        if (dateTime.isEmpty()) {
            throw new BrockException("Missing due date! Remember it is specified after /by!");
        }

        String[] dateTimeValues = Utility.validateDateTime(dateTime.toString(),
                dateTimeWords, Utility.Context.DUE);
        if (dateTimeWords == 1) {
            return new Deadline(description.toString(),
                    dateTimeValues[0]);
        } else {
            return new Deadline(description.toString(),
                    dateTimeValues[0],
                    dateTimeValues[1]);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Chatbot checks if deadline command is valid.
     * If so, it creates a {@code Deadlines} object.
     * Adds it to {@code tasks}, writes it to save file.
     * Displays a response indicating it has added the deadline task.
     * </p>
     *
     * @throws BrockException If deadline command is invalid.
     */
    @Override
    public void execute(Ui ui, Storage storage, TaskList tasks) throws BrockException {
        Task deadlineTask = createDeadline();
        tasks.addToList(deadlineTask);
        ui.displayResponse("Got it. I've added this task:\n"
                + "  "
                + tasks.getTaskDetails(deadlineTask)
                + '\n'
                + tasks.getTasksSummary());

        // Update the save file
        storage.writeToFile(tasks.numTasks()
                        + ". "
                        + tasks.getTaskDetails(deadlineTask)
                        + '\n',
                true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
