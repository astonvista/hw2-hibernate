import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;

public class UserDAO {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public static void add(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            logger.info("Данные успешно добавлены.");
        } catch (Exception ex) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Не удалось добавить данные.", ex);
        }
    }

    public static User get(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception ex) {
            logger.error("Не удалось получить данные.", ex);
            return null;
        }
    }

    public static void update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User old = session.get(User.class, user.getId());
            if (old != null) {
                old.setName(user.getName());
                old.setAge(user.getAge());
                old.setEmail(user.getEmail());
                transaction.commit();
                logger.info("Данные успешно обновлены.");
            } else {
                logger.error("ID не найден.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Не удалось обновить данные.", ex);
        }
    }

    public static void delete(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User userToDelete = session.get(User.class, id);
            if (userToDelete != null) {
                session.delete(userToDelete);
                transaction.commit();
                logger.info("Данные успешно удалены.");
            } else {
                logger.info("Данные с таким ID не найдены.");
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            logger.error("Не удалось удалить данные.", ex);
        }
    }

    public static List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception ex) {
            logger.error("Не удалось получить список пользователей.", ex);
            return Collections.emptyList();
        }
    }
}
