import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {
    private final static String MAIN_CREATE_ENTRY = "1";
    private final static String MAIN_ENTER_WORKSPACE = "2";
    private final static String MAIN_SEE_TOTAL = "3";
    private final static String MAIN_QUIT = "4";

    // project level global constants
    private final static String START_TIMER_CHOICE = "1";
    private final static String EDIT_PROJECT_CHOICE = "2";
    private final static String DELETE_PROJECT_CHOICE = "3";
    private final static String CHOOSE_ANOTHER_PROJECT = "4";
    private final static String MAIN_MENU_RETURN_CHOICE = "5";

    // global constant for ending a timer
    private final static String END_TIMER = "end";

    // global constant for number of minutes in an hour
    private final static double MINUTES_IN_HOUR = 60.0;

    public static void main(String[] args) {



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
                                    System.out.println("Do you want to add this time to the project?");
                                    // todo: next make a switch statement to process user's choice to add time to project...
                                    break;
                                case EDIT_PROJECT_CHOICE:
                                    System.out.println("you chose to edit a project..");
                                    break;
                                case DELETE_PROJECT_CHOICE:
                                    System.out.println("you chose to delete a projet...");
                                    break;
                                case CHOOSE_ANOTHER_PROJECT:
                                    System.out.println("you chose choose another project...");
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
                case MAIN_SEE_TOTAL:
                    System.out.println("see the total of all proejcts you wanted...");
                    break;
                case MAIN_QUIT:
                    System.out.println("Bye");
                    programRunning = false;
                    break;
            }
        } while (programRunning);


        //todo: here we can save the info to a file in case we accidentally quit. We can save daily logs this way too.
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
                !answer.equals(DELETE_PROJECT_CHOICE) &&
                !answer.equals(CHOOSE_ANOTHER_PROJECT) &&
                !answer.equals(MAIN_MENU_RETURN_CHOICE));

        return answer;
    }

    public static void displayProjectInfo(Project project) {
        System.out.println("Project: " + project.getName());
        System.out.println("Hours: " + project.getTotalTime() + "\n");
        System.out.println("1) Start a timer");
        System.out.println("2) Edit project");
        System.out.println("3) Delete project");
        System.out.println("4) Choose another project");
        System.out.println("5) Return to main menu");
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
        System.out.println("Which project would you like to enter:");
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
        do {
            displayWelcome(numProjects);
            answer = console.next();
        } while (!answer.equals(MAIN_CREATE_ENTRY) &&
                !answer.equals(MAIN_ENTER_WORKSPACE) &&
                !answer.equals(MAIN_QUIT));

        return answer;
    }

    public static void displayWelcome(int numProjects) {
        System.out.println("Welcome.");
        System.out.println("Number of projects so far: " + numProjects);

        System.out.println("What would you like to do?");
        System.out.println("1) Create new entry");
        System.out.println("2) Enter project workspace");
        System.out.println("3) See total worked time");
        System.out.println("4) Quit");
    }
}
