/**
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.hycom.pip.messanger.repository;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.hycom.pip.messanger.repository.model.PasswordResetToken;

/**
 * Created by Piotr on 27.05.2017.
 */
@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);

    @Transactional
    Long deleteByExpiryDateLessThan(LocalDateTime date);

}
