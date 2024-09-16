package storage.TaskStorage.TaskManager;

import exceptions.BrockException;
import task.Event;
import task.Task;
import utility.StorageUtility;

public class EventManager extends TaskManager {
    private String[] processEventBody(String eventBody) throws BrockException {
        String[] parts = eventBody.split("\\(from: ", 2);
        if (parts.length < 2) {
            throw new BrockException("Invalid event entry - missing start date!");
        }
        return parts;
    }

    private String[] processStartDateTime(String startDateTime) throws BrockException {
        String[] startDateTimeParts = startDateTime.split(", ");
        String startDateString = StorageUtility.parseDate(startDateTimeParts[0]);
        String startTimeString = startDateTimeParts.length == 1
                ? ""
                : startDateTimeParts[1].replace(":", "");

        return new String[]{startDateString, startTimeString};
    }

    private String[] processEndDateTime(String endDateTime) throws BrockException {
        String[] endDateTimeParts = endDateTime.substring(4)
                .split(", ");
        String endDateString = endDateTimeParts.length == 1
                ? StorageUtility.parseDate(StorageUtility.removeCloseBracket(endDateTimeParts[0]))
                : StorageUtility.parseDate(endDateTimeParts[0]);
        String endTimeString = endDateTimeParts.length == 1
                ? ""
                : StorageUtility.removeCloseBracket(endDateTimeParts[1])
                .replace(":", "");

        return new String[]{endDateString, endTimeString};
    }

    /**
     * Creates an {code Event} object corresponding to an event task.
     *
     * @param eventBody   String storing event description, as well as start and end datetime.
     * @param eventStatus Character representing event status.
     * @return {@code Event} object created.
     * @throws BrockException If date and time are invalid when constructing object.
     *                        (They should already be validated)
     */
    @Override
    public Task convertToTaskObject(String eventBody, char eventStatus) throws BrockException {
        String[] parts = this.processEventBody(eventBody);

        String description = parts[0];
        String dateTime = parts[1];
        String[] dateTimeParts = dateTime.split(" \\| ", 2);
        if (dateTimeParts.length < 2) {
           throw new BrockException("Invalid event entry - missing start end date separator!");
        }

        String[] startValues = this.processStartDateTime(dateTimeParts[0]);
        String[] endValues = this.processEndDateTime(dateTimeParts[1]);

        Task eventTask;
        if (startValues[1].isEmpty()) {
            // Start time is empty
            // Create event with no time
            eventTask = new Event(description, startValues[0], endValues[0]);
        } else {
            // Start time is not empty
            // Create event with time
            eventTask = new Event(description, startValues[0], startValues[1],
                    endValues[0], endValues[1]);
        }

        assert eventStatus == 'X' | eventStatus == ' ' : "Invalid event status extracted.";
        if (eventStatus == 'X') {
            eventTask.markAsDone();
        }
        return eventTask;
    }
}
