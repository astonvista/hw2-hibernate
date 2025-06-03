import java.util.List;

public interface DAO {
    void create(User user);
    User read(long id);
    List<User> getAll();
    void update(User user);
    void delete(long id);
}