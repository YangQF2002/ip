package Tasks;

/**
 * Class representing a todo task.
 */
public class ToDos extends Task {
    /**
     * Stores the todo task description.
     * Sets the todo task status to be uncompleted.
     *
     * @param description Todo task description.
     */
    public ToDos(String description) {
        super(description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskType() {
        return "T";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtraInfo() {
        return "";
    }
}
