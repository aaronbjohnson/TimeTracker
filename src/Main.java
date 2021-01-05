import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private final static String MAIN_CREATE_ENTRY = "1";
    private final static String MAIN_ENTER_WORKSPACE = "2";
    private final static String DELETE_PROJECT = "3";
    private final static String MERGE_PROJECTS = "4";
    private final static String MAIN_QUIT = "5";

    // project level global constants
    private final static String START_TIMER_CHOICE = "1";
    private final static String EDIT_PROJECT_CHOICE = "2";
    private final static String CHOOSE_ANOTHER_PROJECT = "3";
    private final static String MAIN_MENU_RETURN_CHOICE = "4";

    // global constant for ending a timer
    private final static String END_TIMER = "end";

    // global constant for number of minutes in an hour
    private final static double MINUTES_IN_HOUR = 60.0;

    // global constant for yes options
    private final static String YES_OPTION = "y";
    private final static String NO_OPTION = "n";

    // gc for editing a project
    private final static String CHANGE_NAME = "1";
    private final static String CHANGE_TIME = "2";
    private final static String RETURN_TO_PROJECT_MENU= "3";
    private final static String RETURN_TO_MAIN_MENU="4";

    public static void main(String[] args) throws IOException {



        // Create a time sheet
        TimeSheet newSheet = new TimeSheet(0.0);

        // Create a Scanner object to be used for program
        Scanner input = new Scanner(System.in);

        // Create a boolean to control main loop of program
        boolean programRunning = true;

        do {
            String mainMenuChoice = getMainChoice(input, newSheet);

            switch (mainMenuChoice) {
                case MAIN_CREATE_ENTRY:
                    // Create a new project with a user-given name and add that Project to the list of projects for this
                    // timesheet
                    newSheet.addProject(createProject(input));
                    System.out.println("you selected to create an entry..");
                    break;
                case MAIN_ENTER_WORKSPACE:
                    // Create a boolean to control a loop for staying inside a project until user exits
                    boolean projectSelectionOpen = true;
                    do {

                        System.out.println("\nWhich project would you like to enter?");
                        displayProjects(newSheet);
                        // retrieve the project that the user wants
                        Project userProject = getProjectChoice(input, newSheet);

                        // create another boolean to control staying within a chosen project
                        boolean projectOpen = true;

                        do {

                            String projectActionChoice = getProjectActionChoice(input, userProject);

                            switch (projectActionChoice) {
                                case START_TIMER_CHOICE:
                                    System.out.println("you chose to start a timer...");
                                    //todo: make functionality to stop/start timer
                                    long start = System.currentTimeMillis();
                                    // Display info to the user that the timer has started
                                    System.out.println("The timer is running...");
                                    System.out.println("Type 'end' to STOP the timer.");
                                    long end = getStopTime(input);
                                    System.out.println("Timer stopped.");
                                    double interval = getInterval(start, end);
                                    System.out.println("The interval was " + interval + " hours.");


                                    // First we'll make a boolean to control whether the user saves time interval to project
                                    boolean commitTime = true;

                                    do {
                                        System.out.println("Do you want to add this time to the project? (y/n)");
                                        String commitTimeChoice = input.next();

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
                                case EDIT_PROJECT_CHOICE:
                                    System.out.println("you chose to edit a project..");
                                    boolean editingProject = true;
                                    do {
                                        String userEditChoice = getEditMenuChoice(input, userProject);

                                        switch (userEditChoice) {
                                            case CHANGE_NAME:
                                                updateName(input, userProject);
                                                break;
                                            case CHANGE_TIME:
                                                updateTime(input, userProject);
                                                break;
                                            case RETURN_TO_PROJECT_MENU:
                                                editingProject = false;
                                                break;
                                            case RETURN_TO_MAIN_MENU:
                                                editingProject = false;
                                                projectOpen = false;
                                                projectSelectionOpen = false;
                                                break;
                                        }


                                    } while (editingProject);
                                    break;
                                case CHOOSE_ANOTHER_PROJECT:
                                    // set to false so we go back a level to choose another project
                                    projectOpen = false;
                                    break;
                                case MAIN_MENU_RETURN_CHOICE:
                                    System.out.println("you chose to return to the main menu...");
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
                    int projectKey = getProjectKey(input);
                    newSheet.deleteProject(projectKey);
                    break;
                case MERGE_PROJECTS:
                    System.out.println("Which projects would you like to merge?");
                    System.out.println("\nEnter the projects separated by space (1 3 5) and press Enter.");
                    displayProjects(newSheet);

                   /* // testing getInputString
                    ArrayList<String> inputStrings = getInputString(input);
                    System.out.println("\nthe input string:");
                    System.out.println(inputStrings);*/

                    int[] projectsToMerge = inputGetter();

                    System.out.println("now calling the get name function...");
                    String newProjectName = getNewName();

                    mergeProjects(projectsToMerge, newProjectName, newSheet);



                    //ArrayList<Integer> projectsSelection = getProjectSelections(input);

                   /* System.out.println("\nthis thing...here's the entry:");
                    System.out.println(projectsSelection);*/

          /*          int[] toMerge = getProjectsToMerge(input);
                    for (int k : toMerge
                         ) {
                        System.out.println(k);
                    }

                    String newProjectName = getNewName(input);
                    mergeProjects(toMerge, newProjectName, newSheet);*/
                    break;
                case MAIN_QUIT:
                    System.out.println("Bye");
                    programRunning = false;
                    break;
            }
        } while (programRunning);


        //todo: here we can save the info to a file in case we accidentally quit. We can save daily logs this way too.
    }


    public static int[] inputGetter() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] arr = reader.readLine().split(" ");
        int[] intarr = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            intarr[i] = Integer.parseInt(arr[i]);
        }
        reader.close();

        return intarr;

    }

    /*public static ArrayList<Integer> getProjectSelections(Scanner console) {
        *//*System.out.println("getProjectsSelections is being run...");
        console.useDelimiter(" ");
        String projects = "";
        projects = console.nextLine();// todo: 1/5/2021. figure this out...

        System.out.println("returning: " + projects);*//*
        ArrayList<Integer> projects = new ArrayList<>();


        Integer enteredProjects = console.nextInt();



        *//*while (enteredProjects!=null) {
            projects.add(console.nextInt());

            if (enteredProjects.isEmpty()) {
                return projects;
            }
        }*//*

        return projects;
    }*/


//todo: 1/5/2020 @ 12:57. Next thing to do is maybe implement what is in the coderanch forum: Make a separate Inputs utility class.
    public static String getNewName() {
        System.out.println("\nEntering getName function...");
        String name = "";

        try (
                Scanner input = new Scanner(System.in)
        ) {
            while (input.hasNext()) {
                name = input.next();
            }
        }

        return name;
    }

    public static void mergeProjects(int[] projects, String name, TimeSheet sheet) {
        double totalTime = 0;

        for (int key : projects) {
            // Access each project's time in the timesheet and add to total time above.
            totalTime += sheet.getProjectMap().get(key).getTotalTime();
        }

        // Create a new project with the new name and hours
        new Project(name, totalTime);
    }

    public static int[] getProjectsToMerge(Scanner console) {
        System.out.println("\nEnter the projects separated by space (1 3 5) and press Enter.");

        String enteredKeys = console.nextLine();

        System.out.println("\nthe entered keys are: ");
        System.out.println(enteredKeys);

        String[] tokens = enteredKeys.split(" ");

        System.out.println("\nafter splitting, the array looks like: ");

        //char[] separatedKeys = enteredKeys.toCharArray();
        System.out.println(Arrays.toString(tokens));

        return convertStringsToInts(tokens);
    }

    // todo: may can delete now that using BufferReader
    public static int[] convertStringsToInts(String[] separatedKeys) {

        int[] keys = new int[separatedKeys.length - 1];

        for (int i = 0; i < separatedKeys.length - 1; i++) {
            keys[i] = Integer.parseInt(separatedKeys[i]);
        }
        return keys;
    }

    public static void updateTime(Scanner console, Project project) {
        System.out.println("Enter the new total time: ");

        while(!console.hasNextDouble()) {
            console.next();
        }

        double newTime = console.nextDouble();
        project.setTotalTime(newTime);

        System.out.println("The project's time has been changed.");
    }

    public static void updateName(Scanner console, Project project) {
        System.out.println("Enter new name: ");
        String newName = console.next();

        // Update the name
        project.setName(newName);
        System.out.println("The project's name has been changed.");
    }

    public static String getEditMenuChoice(Scanner console, Project project) {
        String answer;
        do {
            displayEditMenu(project);
            answer = console.next();
        } while (!answer.equals(CHANGE_NAME) &&
                !answer.equals(CHANGE_TIME) &&
                !answer.equals(RETURN_TO_PROJECT_MENU) &&
                !answer.equals(RETURN_TO_MAIN_MENU));

        return answer;
    }

    public static void displayEditMenu(Project project) {
        System.out.println("Project: " + project.getName());
        System.out.println("Hours: " + project.getTotalTime() + "\n");
        System.out.println("1) Rename");
        System.out.println("2) Change time");
        System.out.println("3) Return to project menu");
        System.out.println("4) Return to main menu");
    }

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


    public static long getStopTime(Scanner console) {
        String answer;
        do {
            answer = console.next();
        } while (!answer.equals(END_TIMER));

        return System.currentTimeMillis();
    }



    public static String getProjectActionChoice(Scanner console, Project project) {
        String answer;
        do {
            displayProjectInfo(project);
            answer = console.next();
        } while (!answer.equals(START_TIMER_CHOICE) &&
                !answer.equals(EDIT_PROJECT_CHOICE) &&
                !answer.equals(CHOOSE_ANOTHER_PROJECT) &&
                !answer.equals(MAIN_MENU_RETURN_CHOICE));

        return answer;
    }

    public static void displayProjectInfo(Project project) {
        System.out.println("Project: " + project.getName());
        System.out.println("Hours: " + project.getTotalTime() + "\n");
        System.out.println("1) Start a timer");
        System.out.println("2) Edit project");
        System.out.println("3) Choose another project");
        System.out.println("4) Return to main menu");
    }

    public static int getProjectKey(Scanner console) {

        while(!console.hasNextInt()) {
            console.next();
        }

        return console.nextInt();
    }

    public static Project getProjectChoice(Scanner console, TimeSheet sheet) {
        int answer = 0;
        do {
            //displayProjects(sheet);  don't think we need this here as we might want to display menu again?
            // have to convert the answer to an int to compare
            answer = Integer.parseInt(console.next());
        } while (!sheet.getProjectMap().containsKey(answer));

        return sheet.getProjectMap().get(answer);
    }

    public static void displayProjects(TimeSheet sheet) {
        System.out.println();
        sheet.getProjectMap().forEach((k, v) -> {
            System.out.format("%d) %s", k, v.getName());
            System.out.println();
        });
    }

    public static Project createProject(Scanner console) {
        System.out.print("Enter the project name: ");
        String projectName = console.next();

        return new Project(projectName);
    }

    public static String getMainChoice(Scanner console, TimeSheet sheet) {
        String answer;
        int numProjects = sheet.getNumberProjects();
        double totalHours = sheet.getGrandTotal();
        do {
            displayWelcome(numProjects, totalHours);
            answer = console.next();
        } while (!answer.equals(MAIN_CREATE_ENTRY) &&
                !answer.equals(MAIN_ENTER_WORKSPACE) &&
                !answer.equals(DELETE_PROJECT) &&
                !answer.equals(MERGE_PROJECTS) &&
                !answer.equals(MAIN_QUIT));

        return answer;
    }

    public static void displayWelcome(int numProjects, double numHours) {
        System.out.println();
        System.out.println("Welcome.");
        System.out.println("Number of projects so far: " + numProjects);
        System.out.println("Number of hours logged so far: " + numHours);
        System.out.println();

        System.out.println("What would you like to do?");
        System.out.println();
        System.out.println("1) Create new entry");
        System.out.println("2) Enter project workspace");
        System.out.println("3) Delete project");
        System.out.println("4) Merge projects");
        System.out.println("5) Quit");
    }
}
