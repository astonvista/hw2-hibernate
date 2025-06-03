import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static Integer getInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите корректное число");
            }
        }
    }

    public static Integer getOptionalInt(String message) {
        while (true) {
            String input;
            System.out.print(message);
            try {
                input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null;
                } else {
                    return Integer.parseInt(input);
                }
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
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}

