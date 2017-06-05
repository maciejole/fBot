package pl.hycom.pip.messanger.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.hycom.pip.messanger.model.PasswordResetToken;

/**
 * Created by Piotr on 27.05.2017.
 */
@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Integer>, JpaSpecificationExecutor {
    PasswordResetToken findByToken(String token);
}
