import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /*
    Читал, что сейчас SessionFactory принято создавать с помощью ServiceRegistry,
    но в учебных целях решил пока сделать через configuration.buildSessionFactory().
     */
    private static SessionFactory buildSessionFactory() {
        logger.info("Создание SessionFactory...");
        try {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(User.class);
            SessionFactory sf = configuration.buildSessionFactory();
            logger.info("SessionFactory успешно создана");
            return sf;
        } catch (Exception ex) {
            logger.error("Не удалось создать экземпляр SessionFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Выключение SessionFactory...");
            sessionFactory.close();
        }
    }
}
