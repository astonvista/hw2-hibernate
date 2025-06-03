public class Main {

    public static void main(String[] args) {
        UserConsoleUI userConsoleUI = new UserConsoleUI(new UserService(new UserDAO()));
        userConsoleUI.start();
//        UserService userService = new UserService(new UserDAO());
//        userService.update(1, null, null, null);
    }
}
