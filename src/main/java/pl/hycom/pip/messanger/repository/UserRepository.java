package pl.hycom.pip.messanger.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.hycom.pip.messanger.repository.model.User;

/**
 * Created by Monia on 2017-05-20.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
