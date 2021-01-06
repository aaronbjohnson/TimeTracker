import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InputUtility {
    static Scanner userInput = new Scanner(System.in);

    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private InputUtility() {

    }

    public static String getLine() throws IOException {
        return input.readLine();
    }

    public static String getNextInput() {
        return userInput.next();
    }

    public static String getString() {
        System.out.print(">: ");
        return userInput.nextLine();
    }

    public static int getNumber() {
        System.out.print(">: ");
        int userNumber = userInput.nextInt();
        userInput.nextLine();
        return userNumber;
    }

    public static double getDouble() {
        System.out.print(">: ");
        double userDouble = userInput.nextDouble();
        userInput.nextLine();
        return userDouble;
    }

    public static boolean hasAnotherDouble() {
        return userInput.hasNextDouble();
    }

    public static boolean hasAnotherInt() {
        return userInput.hasNextInt();
    }

    public static boolean hasAnotherString() {
        return userInput.hasNextLine();
    }
}
