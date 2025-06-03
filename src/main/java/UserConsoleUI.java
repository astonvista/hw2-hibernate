import java.util.List;

public class UserConsoleUI {
    private final UserService userService;
    private boolean quitFlag = false;
    private boolean isSessionFactoryStarted = false;

    public UserConsoleUI(UserService userService) {
        this.userService = userService;
    }

    public void start() {
        while (!quitFlag) {
            System.out.print("""
                    1 - Добавить данные
                    2 - Получить данные
                    3 - Обновить данные
                    4 - Удалить данные
                    5 - Показать таблицу
                    0 - Выход
                    """);

            int command = InputUtil.getInt("Введите команду: ");
            if (command >= 1 && command <= 5) {
                isSessionFactoryStarted = true;
            }

            switch (command) {
                case 1 -> create();
                case 2 -> read();
                case 3 -> update();
                case 4 -> delete();
                case 5 -> printUserList();
                case 0 -> {
                    System.out.println("Выход...");
                    quitFlag = true;
                }
                default -> {
                    System.out.println("Введите корректную команду");
                    menuPrompt();
                }
            }
        }
        if (isSessionFactoryStarted) {
            HibernateUtil.shutdown();
        }
    }

    private void menuPrompt() {
        while (true) {
            Integer choice = InputUtil.getInt("Введите 1 для возврата в меню, 0 для выхода: ");
            switch (choice) {
                case 0 -> {
                    quitFlag = true;
                    System.out.println("Выход...");
                    return;
                }
                case 1 -> {
                    return;
                }
                default -> System.out.println("Введите корректную команду");
            }
        }
    }

    private void create() {
        String name = InputUtil.getString("Введите имя: ");
        String email = InputUtil.getString("Введите e-mail: ");
        Integer age = InputUtil.getInt("Введите возраст: ");

        try {
            userService.create(name, email, age);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            menuPrompt();
        }
    }

    private void read() {
        long id = InputUtil.getLong("Введите ID запрашиваемого объекта: ");
        User user;

        try {
            user = userService.read(id);
            System.out.println(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            menuPrompt();
        }
    }

    private void update() {
        long id = InputUtil.getLong("Введите ID: ");
        String name = InputUtil.getString("Введите новое имя (не вводите ничего, если " +
                "имя не изменилось): ");
        String email = InputUtil.getString("Введите новый e-mail (не вводите ничего, если " +
                "e-mail не изменился): ");
        Integer age = InputUtil.getOptionalInt("Введите новый возраст (не вводите ничего, если " +
                "возраст не изменился): ");

        try {
            userService.update(id, name, email, age);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            menuPrompt();
        }
    }

    private void delete() {
        long id = InputUtil.getLong("Введите ID: ");

        try {
            userService.delete(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            menuPrompt();
        }
    }

    public void printUserList() {
        List<User> users = userService.getAll();

        if (!users.isEmpty()) {
            System.out.printf("%-5s %-20s %-30s %-5s %-25s%n", "ID", "Имя", "Email", "Возраст",
                    "Дата создания");
            System.out.println("=".repeat(90));

            for (User user : users) {
                System.out.printf("%-5d %-20s %-30s %-5d %-25s%n", user.getId(), user.getName(),
                        user.getEmail(), user.getAge(), user.getCreatedAt());
            }
        } else {
            System.out.println("Список пользователей пуст");
        }
        menuPrompt();
    }
}
