import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Crud {
    private static boolean quitFlag = false;
    private static Logger logger = LoggerFactory.getLogger(Crud.class);

    public static void start() {
        while (!quitFlag) {
            System.out.print("""
                    1 - Добавить данные
                    2 - Получить данные
                    3 - Обновить данные
                    4 - Удалить данные
                    5 - Показать таблицу
                    0 - Выход
                    """);

            switch (Input.getInt("Введите команду: ")) {
                case 1 -> create();
                case 2 -> read();
                case 3 -> update();
                case 4 -> delete();
                case 5 -> {
                    printUserList(UserDAO.getAll());
                    menuPrompt();
                }
                case 0 -> {
                    System.out.println("Выход");
                    quitFlag = true;
                }
                default -> {
                    System.out.println("Введите корректную команду");
                    menuPrompt();
                }
            }
        }
    }

    private static void menuPrompt() {
        while (true) {
            int choice = Input.getInt("Введите 1 для возврата в меню, 0 для выхода: ");
            switch (choice) {
                case 0 -> {
                    quitFlag = true;
                    System.out.println("Выход");
                    return;
                }
                case 1 -> {
                    return;
                }
                default -> System.out.println("Введите корректную команду");
            }
        }
    }

    private static void create() {
        User user = new User();
        user.setName(Input.getString("Введите имя: "));
        user.setEmail(Input.getString("Введите e-mail: "));
        user.setAge(Input.getInt("Введите возраст: "));
        UserDAO.add(user);
        menuPrompt();
    }

    private static void read() {
        User user = UserDAO.get(Input.getInt("Введите ID запрашиваемого объекта: "));
        if (user != null) {
            System.out.println(user);
        } else {
            logger.info("Запись с таким ID не найдена");
        }
        menuPrompt();
    }

    private static void update() {
        User user = new User();
        user.setId(Input.getLong("Введите ID: "));
        user.setName(Input.getString("Введите имя: "));
        user.setEmail(Input.getString("Введите e-mail: "));
        user.setAge(Input.getInt("Введите возраст: "));
        UserDAO.update(user);
        menuPrompt();
    }

    private static void delete() {
        UserDAO.delete(Input.getLong("Введите ID: "));
        menuPrompt();
    }

    public static void printUserList(List<User> users) {
        if (users.isEmpty()) {
            logger.info("Пользователи не найдены");
            return;
        }

        System.out.printf("%-5s %-20s %-30s %-5s %-25s%n", "ID", "Имя",
                "Email", "Возраст", "Дата создания");
        System.out.println("=".repeat(90));

        for (User user : users) {
            System.out.printf("%-5d %-20s %-30s %-5d %-25s%n", user.getId(),
                    user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
        }
    }
}
