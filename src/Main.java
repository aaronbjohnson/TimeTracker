import java.util.Scanner;

public class Main {
    private final static String MAIN_CREATE_ENTRY = "1";
    private final static String MAIN_ENTER_WORKSPACE = "2";
    private final static String MAIN_SEE_TOTAL = "3";
    private final static String MAIN_QUIT = "4";

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
                    displayProjects(newSheet);

                    // Create a boolean to control a loop for staying inside a project until user exits
                    boolean projectOpen = true;

                    do {
                        // retrieve the project that the user wants
                        Project userProject = getProjectChoice(input, newSheet);
                        //todo: create a displayProjectMenu to add here
                        //todo: make functionality to stop/start timer
                    } while (projectOpen);

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
