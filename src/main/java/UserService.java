import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.NoSuchElementException;

public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void create(String name, String email, Integer age) {
        ValidationUtil.validateUser(name, email, age);
        try {
            userDAO.create(new User(name, email, age));
        } catch (ConstraintViolationException ex) {
            String message = "Пользователь с e-mail " + email + " уже существует";
            logger.error(message);
            throw new DuplicateEmailException(message);
        }
        logger.info("Пользователь успешно создан");
    }

    public User read(long id) {
        User user;

        ValidationUtil.validateId(id);
        user = userDAO.read(id);
        if (user == null) {
            String message = "Запись с ID " + id + " не найдена";
            logger.info(message);
            throw new UserNotFoundException(message);
        }
        return user;
    }

    public void update(long id, String name, String email, Integer age) {
        if ((name == null || name.isBlank()) &&
                (email == null || email.isBlank()) &&
                age == null) {
            String message = "Все введенные элементы - null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        try {
            ValidationUtil.validateId(id);
            User user = new User();
            user.setId(id);

            if (name != null && !name.isBlank()) {
                ValidationUtil.validateName(name);
                user.setName(name);
            }
            if (email != null && !email.isBlank()) {
                ValidationUtil.validateEmail(email);
                user.setEmail(email);
            }
            if (age != null) {
                ValidationUtil.validateAge(age);
                user.setAge(age);
            }
            userDAO.update(user);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                String message = "Пользователь с e-mail " + email + " уже существует";
                logger.error(message);
                throw new DuplicateEmailException(message);
            } else if (ex.getCause() instanceof EntityExistsException) {
                String message = "Введенные данные совпадают с уже существующими";
                logger.error(message);
                throw new UnchangedUserDataException(message);
            }
        } catch (NoSuchElementException ex) {
            String message = "Пользователь с ID " + id + " не найден";
            logger.info(message);
            throw new UserNotFoundException(message);
        }
        logger.info("Данные пользователя успешно обновлены");
    }

    public void delete(long id) {
        ValidationUtil.validateId(id);
        try {
            userDAO.delete(id);
        } catch (NoSuchElementException ex) {
            String message = "Пользователь с ID " + id + " не найден";
            logger.info(message);
            throw new UserNotFoundException(message);
        }
        logger.info("Пользователь успешно удален");
    }

    public List<User> getAll() {
        return userDAO.getAll();
    }
}

