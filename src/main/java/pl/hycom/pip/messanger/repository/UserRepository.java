package pl.hycom.pip.messanger.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hycom.pip.messanger.repository.model.User;

import java.util.Optional;

/**
 * Created by Monia on 2017-05-20.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer>, JpaSpecificationExecutor {
    Optional<User> findByEmail(String email);
}

