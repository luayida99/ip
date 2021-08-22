import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {

    private Parser parser;

    public Storage() {
        this.parser = new Parser();
    }

    public void save(ArrayList<Task> savedList, String directory) throws IOException {
        String filePath = directory + "/Side.txt";
        File file = new File(filePath);
        File dataDirectory = new File(directory);

        if (!Files.exists(Path.of(directory))) {
            boolean isDirectory = dataDirectory.mkdir();
        }
        if (!Files.exists(Path.of(filePath))) {
            boolean isFile = file.createNewFile();
        }

        FileWriter writer = new FileWriter(filePath);
        StringBuilder taskLine = new StringBuilder();

        for (Task t : savedList) {
            if (t instanceof Deadline) {
                String taskDetails = "D | " + ((Deadline) t).datetime.saveDatetime();
                taskLine.append(taskDetails);
            } else if (t instanceof Event) {
                String taskDetails = "E | " + ((Event) t).startDatetime.saveDatetime()
                        + "/to" + ((Event) t).endDatetime.saveDatetime();
                taskLine.append(taskDetails);
            } else {
                taskLine.append("T | No time");
            }
            String generalTaskDetails = " | " + t.description + " | " + (t.isDone ? "T" : "F") + "\n";
            taskLine.append(generalTaskDetails);
        }

        writer.write(taskLine.toString());
        writer.close();
    }

    public ArrayList<Task> retrieve(String directory) throws FileNotFoundException {
        String filePath = directory + "/Side.txt";
        File file = new File(filePath);
        ArrayList<Task> savedList = new ArrayList<>();

        if (Files.exists(Path.of(filePath))) {
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String[] lineData = fileScanner.nextLine().split("\\|");
                String taskType = lineData[0];
                String taskTime = lineData[1].trim();
                String taskDescription = lineData[2];
                boolean isTaskDone = lineData[3].equals(" T");

                switch (taskType) {
                case "D ":
                    savedList.add(new Deadline(taskDescription, taskTime, isTaskDone));
                    break;
                case "E ":
                    if (this.parser.findEventDatetime(taskTime) != null) {
                        savedList.add(new Event(taskDescription, this.parser.findEventDatetime(taskTime), isTaskDone));
                    }
                    break;
                case "T ":
                    savedList.add(new Task(taskDescription, isTaskDone));
                    break;
                }
            }
        } else {
            throw new FileNotFoundException();
        }
        return savedList;
    }
}
