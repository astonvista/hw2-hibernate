import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUtil {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    public static void validateUser(String name, String email, Integer age) {
        validateName(name);
        validateEmail(email);
        validateAge(age);
    }

    public static void validateName(String name) {
        String message;

        if (name == null || name.isBlank()) {
            message = "Поле \"Имя\" не может быть пустым";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        for (char ch : name.toCharArray()) {
            if (Character.isDigit(ch)) {
                message = "Имя не может содержать цифр";
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static void validateEmail(String email) {
        String message;

        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            message = "Введите корректный e-mail";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateAge(Integer age) {
        String message;

        if (age == null || (age < 18 || age > 100)) {
            message = "Возраст не может быть меньше 18 и больше 100 лет";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateId(long id) {
        String message;

        if (id <= 0) {
            message = "ID должен быть больше 0";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
