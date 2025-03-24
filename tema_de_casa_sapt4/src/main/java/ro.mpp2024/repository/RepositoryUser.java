package ro.mpp2024.repository;
import ro.mpp2024.domain.User;

import java.util.List;

public interface RepositoryUser extends Repository<Long, User>{
    List<User> findByUsernameAlphabetically(String username);
}
