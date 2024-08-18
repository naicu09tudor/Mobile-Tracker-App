package edu.tucn.scd.serverapp.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Naicu Tudor
 */
public interface UserRepository extends CrudRepository<User, String> {

    @Query(nativeQuery = true, value = "SELECT * FROM user WHERE username=?1")
    User findByUser(String username);

    void deleteByUsername(String username);

}
