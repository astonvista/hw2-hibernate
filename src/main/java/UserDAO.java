import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class UserDAO implements DAO {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public UserDAO() {
    }

    public void create(User user) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (ConstraintViolationException ex) {
            safeRollback(transaction);
            throw ex;
        } catch (HibernateException ex) {
            safeRollback(transaction);
            logger.error("Ошибка работы с Hibernate", ex);
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User read(long id) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            return session.get(User.class, id);
        } catch (HibernateException ex) {
            logger.error("Ошибка работы с Hibernate", ex);
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void update(User user) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            User old = session.get(User.class, user.getId());

            if (old != null) {
                if (!old.equals(user)) {
                    if (user.getName() != null && !user.getName().equals(old.getName())) {
                        old.setName(user.getName());
                    }
                    if (user.getEmail() != null && !user.getEmail().equals(old.getEmail())) {
                        old.setEmail(user.getEmail());
                    }
                    if (user.getAge() != null && !user.getAge().equals(old.getAge())) {
                        old.setAge(user.getAge());
                    }
                    transaction.commit();
                } else {
                    throw new EntityExistsException();
                }
            } else {
                throw new NoSuchElementException();
            }
        } catch (ConstraintViolationException | EntityExistsException | NoSuchElementException ex) {
            safeRollback(transaction);
            throw ex;
        } catch (HibernateException ex) {
            safeRollback(transaction);
            logger.error("Ошибка работы с Hibernate", ex);
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void delete(long id) {
        Transaction transaction = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            User userToDelete = session.get(User.class, id);

            if (userToDelete != null) {
                session.delete(userToDelete);
                transaction.commit();
            } else {
                throw new NoSuchElementException();
            }
        } catch (HibernateException ex) {
            safeRollback(transaction);
            logger.error("Ошибка работы с Hibernate", ex);
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<User> getAll() {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            List<User> list = session.createQuery("from User", User.class).list();
            if (!list.isEmpty()) {
                return list;
            } else {
                return Collections.emptyList();
            }
        } catch (HibernateException ex) {
            logger.error("Ошибка работы с Hibernate", ex);
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void safeRollback(Transaction tx) {
        if (tx != null && tx.getStatus().canRollback()) {
            tx.rollback();
        }
    }
}
