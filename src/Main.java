import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private final static String MAIN_CREATE_ENTRY = "1";
    private final static String MAIN_ENTER_WORKSPACE = "2";
    private final static String DELETE_PROJECT = "3";
    private final static String MERGE_PROJECTS = "4";
    private final static String VIEW_TIMESHEET = "5";
    private final static String MAIN_QUIT = "6";

    // project level global constants
    private final static String START_TIMER_CHOICE = "1";
    //private final static String EDIT_PROJECT_CHOICE = "2";
    private final static String CHANGE_NAME = "2";
    private final static String CHANGE_TIME = "3";
    private final static String CHOOSE_ANOTHER_PROJECT = "4";
    private final static String MAIN_MENU_RETURN_CHOICE = "5";

    // global constant for ending a timer
    private final static String END_TIMER = "end";

    // global constant for number of minutes in an hour
    private final static double MINUTES_IN_HOUR = 60.0;

    // global constant for yes options
    private final static String YES_OPTION = "y";
    private final static String NO_OPTION = "n";

    // Create constant for the path to the user's desktop where a saved log file is saved by default when requested
    private final static String DESKTOP_PATH = System.getProperty("user.home") + "/Desktop/";

    // global constant for time sheet log file name
    private final static String OUTFILE_NAME = DESKTOP_PATH + "timesheet_log.txt";


    public static void main(String[] args) throws IOException {


        // Create a time sheet
        TimeSheet newSheet = new TimeSheet(0.0);

        // Create a boolean to control main loop of program
        boolean programRunning = true;

        do {

            String mainMenuChoice = getMainChoice(newSheet);

            switch (mainMenuChoice) {
                case MAIN_CREATE_ENTRY:
                    // Create a new project with a user-given name and add that Project to the list of projects for this
                    // timesheet
                    newSheet.addProject(createProject());
                    System.out.println("you selected to create an entry..");
                    break;
                case MAIN_ENTER_WORKSPACE:
                    // Create a boolean to control a loop for staying inside a project until user exits
                    boolean projectSelectionOpen = true;
                    do {

                        System.out.println("\nWhich project would you like to enter?");
                        displayProjects(newSheet);
                        // retrieve the project that the user wants
                        Project userProject = getProjectChoice(newSheet);

                        // create another boolean to control staying within a chosen project
                        boolean projectOpen = true;

                        do {

                            String projectActionChoice = getProjectActionChoice(userProject);

                            switch (projectActionChoice) {
                                case START_TIMER_CHOICE:
                                    System.out.println("you chose to start a timer...");
                                    //todo: make functionality to stop/start timer
                                    long start = System.currentTimeMillis();
                                    // Display info to the user that the timer has started
                                    System.out.println("The timer is running...");
                                    System.out.println("Type 'end' to STOP the timer.");
                                    long end = getStopTime();
                                    System.out.println("Timer stopped.");
                                    double interval = getInterval(start, end);
                                    System.out.println("The interval was " + interval + " hours.");


                                    // First we'll make a boolean to control whether the user saves time interval to project
                                    boolean commitTime = true;

                                    do {
                                        System.out.println("Do you want to add this time to the project? (y/n)");
                                        String commitTimeChoice = InputUtility.getLine();

                                        if (commitTimeChoice.toLowerCase().equals(YES_OPTION)) {
                                            // commit the time to the project
                                            updateProjectTime(userProject, interval);

                                            // get out of the loop
                                            commitTime = false;

                                        } else if (commitTimeChoice.toLowerCase().equals(NO_OPTION)) {
                                            commitTime = false;
                                        }
                                    } while (commitTime);
                                    break;
                                case CHANGE_NAME:
                                    updateName(userProject);
                                    break;
                                case CHANGE_TIME:
                                    updateTime(userProject);
                                    break;
                                case CHOOSE_ANOTHER_PROJECT:
                                    // set to false so we go back a level to choose another project
                                    projectOpen = false;
                                    break;
                                case MAIN_MENU_RETURN_CHOICE:
                                    // set this to false so we skip selecting another project and instead go straight to main menu
                                    projectOpen = false;
                                    projectSelectionOpen = false;
                                    break;
                            }
                        } while (projectOpen);
                    } while (projectSelectionOpen);


                    System.out.println();
                    break;
                case DELETE_PROJECT:
                    System.out.println("What project would you like to delete?");
                    // display the project like in project selection to enter:
                    displayProjects(newSheet);
                    int projectKey = getProjectKey();
                    newSheet.deleteProject(projectKey);
                    break;
                case MERGE_PROJECTS:
                    System.out.println("Which projects would you like to merge?");
                    System.out.println("\nEnter the projects separated by space (1 3 5) and press Enter.");
                    displayProjects(newSheet);

                    int[] projectsToMerge = inputGetter();

                    String newProjectName = "";

                    try {
                        newProjectName = getNewName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Add the new project to the timesheet instance
                    newSheet.addProject(mergeProjects(projectsToMerge, newProjectName, newSheet));

                    break;
                case VIEW_TIMESHEET:
                    //displayTimeSheet(newSheet);
                    System.out.println(assembleTimesheet(newSheet));

                    System.out.println("\nWould you like to save this to a file? (y/n)");
                    System.out.println("WARNING: This will overwrite any previously saved log.");

                    String saveTimesheet = InputUtility.getNextInput();

                    if (saveTimesheet.toLowerCase().equals(YES_OPTION)) {
                        // save the timesheet output to a text file
                        createTimesheetFile(newSheet);
                    }


                    break;
                case MAIN_QUIT:
                    System.out.println("Bye");
                    programRunning = false;
                    break;
            }
        } while (programRunning);
    }

    /**
     * This method is used to save a timesheet as a text file. The file's destination is specified in the global
     * constants section above.
     * @param sheet A TimeSheet containing the information the user wants to save to a text file.
     * @throws FileNotFoundException If the file is not found.
     */
    public static void createTimesheetFile(TimeSheet sheet) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(OUTFILE_NAME);
        output.println(assembleTimesheet(sheet));
        output.close();
    }


    public static int[] inputGetter() throws IOException {
        String[] arr = InputUtility.getLine().split(" ");

        int[] intarr = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            intarr[i] = Integer.parseInt(arr[i]);
        }

        return intarr;

    }

    public static String getNewName() throws IOException {
        System.out.print("\nEnter a name for the merged project: ");

        return InputUtility.input.readLine();
    }

    public static Project mergeProjects(int[] projects, String name, TimeSheet sheet) {
        double totalTime = 0;

        for (int key : projects) {
            // Access each project's time in the timesheet and add to total time above.
            totalTime += sheet.getProjectMap().get(key).getTotalTime();
            // After getting the project's time, delete that project's record
            sheet.deleteProject(key);
        }

        // Create a new project with the new name and hours
        return new Project(name, totalTime);
    }

    //todo: delete...
/*    public static int[] getProjectsToMerge(Scanner console) {
        System.out.println("\nEnter the projects separated by space (1 3 5) and press Enter.");

        String enteredKeys = console.nextLine();

        System.out.println("\nthe entered keys are: ");
        System.out.println(enteredKeys);

        String[] tokens = enteredKeys.split(" ");

        System.out.println("\nafter splitting, the array looks like: ");

        //char[] separatedKeys = enteredKeys.toCharArray();
        System.out.println(Arrays.toString(tokens));

        return convertStringsToInts(tokens);
    }*/

    // todo: may can delete now that using BufferReader
/*    public static int[] convertStringsToInts(String[] separatedKeys) {

        int[] keys = new int[separatedKeys.length - 1];

        for (int i = 0; i < separatedKeys.length - 1; i++) {
            keys[i] = Integer.parseInt(separatedKeys[i]);
        }
        return keys;
    }*/

    public static void updateTime(Project project) {
        System.out.println("Enter the new total time: ");

        while(!InputUtility.hasAnotherDouble()) {
            InputUtility.getNextInput();
        }

        double newTime = InputUtility.getDouble();
        project.setTotalTime(newTime);

        System.out.println("The project's time has been changed.");
    }

    public static void updateName(Project project) {
        System.out.println("Enter new name: ");
        String newName = InputUtility.getString();

        // Update the name
        project.setName(newName);
        System.out.println("The project's name has been changed.");
    }

/*    public static void displayEditMenu(Project project) {
        System.out.println("Project: " + project.getName());
        System.out.println("Hours: " + project.getTotalTime() + "\n");
        System.out.println("1) Rename");
        System.out.println("2) Change time");
        System.out.println("3) Return to project menu");
        System.out.println("4) Return to main menu");
    }*/

    public static void updateProjectTime(Project project, double time) {
        // Get the project's current time and add new time
        double updatedTime = project.getTotalTime() + time;
        // Update the project's time
        project.setTotalTime(updatedTime);
    }

    public static double getInterval(long start, long end) {
        // todo: create global constants for all the literals below
        long diffInMilliseconds = end - start;
        long diffInSeconds = diffInMilliseconds / 1000;
        int seconds = (int) (diffInSeconds % 60);
        diffInSeconds /= 60;
        int minutes = (int) (diffInSeconds % 60);
        diffInSeconds /= 60;
        int hours = (int) (diffInSeconds % 24);

        // Convert minutes to decimal by dividing by 60
        double rawTime = hours + (minutes / MINUTES_IN_HOUR);

        // Make a BigDecimal version of the raw time
        BigDecimal finalTime = new BigDecimal(rawTime);

        // Round to the nearest 10th
        finalTime = finalTime.setScale(1, RoundingMode.UP);

        return finalTime.doubleValue();
    }


    public static long getStopTime() {
        String answer;
        do {
            answer = InputUtility.getString();
        } while (!answer.toLowerCase().equals(END_TIMER));

        return System.currentTimeMillis();
    }


    /**
     * This method is used to get the user's choice for the project menu. This method calls the displayProjectInfo,
     * which displays the menu of choices.
     * @param project The Project that the user is currently operating within.
     * @return A String representing the user's menu choice.
     */
    public static String getProjectActionChoice(Project project) {
        String answer;
        do {
            displayProjectInfo(project);
            answer = InputUtility.getString();
        } while (!answer.equals(START_TIMER_CHOICE) &&
                !answer.equals(CHANGE_NAME) &&
                !answer.equals(CHANGE_TIME) &&
                !answer.equals(CHOOSE_ANOTHER_PROJECT) &&
                !answer.equals(MAIN_MENU_RETURN_CHOICE));

        return answer;
    }

    /**
     * This method displays the operations a user can perform within a given Project.
     * @param project The Project that the user is performing operations on.
     */
    public static void displayProjectInfo(Project project) {
        System.out.println("Project: " + project.getName());
        System.out.println("Hours: " + project.getTotalTime() + "\n");
        System.out.println("1) Start a timer");
        System.out.println("2) Rename project");
        System.out.println("3) Change time");
        System.out.println("4) Choose another project");
        System.out.println("5) Return to main menu");
    }

    /**
     * This method is used to get a valid project key from the user. This method is used to match the user's choice to
     * the actual ID of a project. This method ensures the user enters an int.
     * @return An int representing the user's choice AND the desired project's ID.
     */
    public static int getProjectKey( ) {

        while (!InputUtility.hasAnotherInt()) {
            System.out.println("\nPlease enter a valid number option.\n");
            InputUtility.getNextInput();
        }

        return InputUtility.getNumber();
    }


    /**
     * This method is used to get the user's choice for which project they'd like to select given a list of projects.
     * @param sheet The TimeSheet being used for the current session.
     * @return A Project that matches the user's selection.
     */
    public static Project getProjectChoice(TimeSheet sheet) {
        // Use get project key method to ensure user enters an int before proceeding.
        int answer;

        do {
            answer = getProjectKey();

        } while (!sheet.getProjectMap().containsKey(answer));

        return sheet.getProjectMap().get(answer);
    }

    /**
     * This method is used to display a list of all projects stored withing a given TimeSheet.
     * @param sheet The TimeSheet being used for the current session.
     */
    public static void displayProjects(TimeSheet sheet) {
        System.out.println();
        sheet.getProjectMap().forEach((k, v) -> {
            System.out.format("%d) %s", k, v.getName());
            System.out.println();
        });
    }

    /**
     * This method is used to create a new Project with a user-provided name.
     * @return A Project created with the name provided by the user.
     */
    public static Project createProject() {
        System.out.print("Enter the project name: ");
        String projectName = InputUtility.getString();

        return new Project(projectName);
    }

    /**
     * This method is used to get the user's choice for the main menu. This method calls the displayWelcome -- which
     * displays the menu of choices.
     * @param sheet The TimeSheet being used for the current session.
     * @return A String representing the user's menu choice.
     */
    public static String getMainChoice(TimeSheet sheet) {
        String answer;
        do {
            displayWelcome(sheet);
            answer = InputUtility.getString();
        } while (!answer.equals(MAIN_CREATE_ENTRY) &&
                !answer.equals(MAIN_ENTER_WORKSPACE) &&
                !answer.equals(DELETE_PROJECT) &&
                !answer.equals(MERGE_PROJECTS) &&
                !answer.equals(VIEW_TIMESHEET) &&
                !answer.equals(MAIN_QUIT));

        return answer;
    }


    /**
     * This method is used to construct a simple table showing the projects for a given TimeSheet and the total logged
     * time for each project. The table is made up of a StringBuilder object, which can either be displayed or used as
     * the content for a log .txt file (see createTimesheetFile)
     * @param sheet The TimeSheet that contains the projects to be displayed.
     * @return A StringBuilder object containing the formatted projects and their logged hours.
     */
    public static StringBuilder assembleTimesheet(TimeSheet sheet) {
        StringBuilder content = new StringBuilder("Project\t\t\t| Total Hours\n---------------------------------------------\n");

        for (Map.Entry<Integer, Project> entry : sheet.getProjectMap().entrySet()) {
            String name = entry.getValue().getName();
            double time = entry.getValue().getTotalTime();
            content.append(name).append("\t\t\t| ").append(time).append("\n");
        }

        // Add grand total to time sheet display
        content.append("\nGrand Total\t\t| ").append(sheet.getGrandTotal()).append("\n");

        return content;

    }

    /**
     * Used to display the main root menu of the program.
     * @param sheet A TimeSheet containing projects and their associated logged time.
     */
    public static void displayWelcome(TimeSheet sheet) {
        System.out.println();
        System.out.println("Welcome.");
        System.out.println("Number of projects so far: " + sheet.getNumberProjects());
        System.out.println("Number of hours logged so far: " + sheet.getGrandTotal());

        System.out.println("What would you like to do?");
        System.out.println();
        System.out.println("1) Create new entry");
        System.out.println("2) Enter project workspace");
        System.out.println("3) Delete project");
        System.out.println("4) Merge projects");
        System.out.println("5) View time sheet");
        System.out.println("6) Quit");
    }
}
