import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Input {
    private static final Logger log = LoggerFactory.getLogger(Input.class);
    private static Scanner scanner = new Scanner(System.in);

    public static int getInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите корректное число");
            }
        }
    }

    public static long getLong(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите корректное число");
            }
        }
    }

    public static String getString(String message) {
        while (true) {
            System.out.print(message);
            try {
                return scanner.nextLine().trim();
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
