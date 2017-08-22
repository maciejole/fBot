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
package pl.hycom.pip.messanger.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;
import pl.hycom.pip.messanger.controller.model.UserDTO;
import pl.hycom.pip.messanger.exception.EmailNotUniqueException;
import pl.hycom.pip.messanger.exception.SecurityException;
import pl.hycom.pip.messanger.repository.PasswordResetTokenRepository;
import pl.hycom.pip.messanger.repository.RoleRepository;
import pl.hycom.pip.messanger.repository.UserRepository;
import pl.hycom.pip.messanger.repository.model.PasswordResetToken;
import pl.hycom.pip.messanger.repository.model.Role;
import pl.hycom.pip.messanger.repository.model.User;
import pl.hycom.pip.messanger.service.EmailService.Message;

/**
 * Created by Monia on 2017-05-20.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository tokenRepository;

    @Value("${messenger.port:8080}")
    private String port;

    @Value("${messenger.host:localhost}")
    private String host;

    @Value("${messenger.protocol:http}")
    private String protocol;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MapperFacade orikaMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> findAllUsers() {
        log.info("Searching all users");

        return orikaMapper.mapAsList(StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()), UserDTO.class);
    }

    public UserDTO findUserById(Integer id) {
        log.info("Searching for user with id[" + id + "]");

        return orikaMapper.map(userRepository.findOne(id), UserDTO.class);
    }

    public UserDTO addOrUpdateUser(UserDTO user) throws EmailNotUniqueException, SecurityException {
        User userToUpdateOrAdd = orikaMapper.map(user, User.class);
        if (user.getId() != null && user.getId() != 0) {
            return orikaMapper.map(updateUser(userToUpdateOrAdd), UserDTO.class);
        } else {
            return orikaMapper.map(addUser(userToUpdateOrAdd), UserDTO.class);
        }
    }

    public User addUser(User user) throws EmailNotUniqueException {
        log.info("Adding user: " + user);
        addDefaultRoleIfNeeded(user);
        return trySaveUser(user, true, false);
    }

    private void addDefaultRoleIfNeeded(User user) {
        log.info("setUserRoleIfNoneGranted method invoked for user: " + user);
        if (CollectionUtils.isEmpty(user.getAuthorities())) {
            roleRepository.findByAuthorityIgnoreCase(Role.Name.USER).ifPresent(role -> user.getRoles().add(role));
        }
    }

    public User updateUser(User user) throws EmailNotUniqueException, SecurityException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser == null || (!currentUser.getRoles().stream().filter(r -> StringUtils.equals(Role.Name.ADMIN, r.getAuthority())).findFirst().isPresent() && !currentUser.getId().equals(user.getId()))) {
            throw new SecurityException("Current user[" + currentUser + "] is not ADMIN and is different than user to update[" + user + "]");
        }

        log.info("Updating user: " + user);
        User userToUpdate = userRepository.findOne(user.getId());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setEmail(user.getEmail().toLowerCase());
        userToUpdate.setProfileImageUrl(user.getProfileImageUrl());
        if (!user.getRoles().isEmpty()) {
            userToUpdate.setRoles(user.getRoles());
        }

        addDefaultRoleIfNeeded(userToUpdate);

        boolean isCurrentAccount = user.getId().equals(currentUser.getId());
        return trySaveUser(userToUpdate, false, isCurrentAccount);
    }

    private User trySaveUser(User user, boolean isNewUser, boolean isCurrentAccount) throws EmailNotUniqueException {
        try {
            User userToSave = userRepository.save(user);

            if (isNewUser) {
                String token = generateToken();
                createPasswordResetTokenForUser(userToSave, token);
                emailService.sendEmail(constructResetTokenEmail(user, token));
            }

            if (isCurrentAccount) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            return userToSave;

        } catch (DataIntegrityViolationException e) {
            throw new EmailNotUniqueException(e);
        }
    }

    public void deleteUser(Integer id) {
        log.info("Deleting user[" + id + "]");
        userRepository.delete(id);
    }

    public void deleteAllUsers() {
        log.info("Deleting all users");
        userRepository.deleteAll();
    }

    @Override
    public User loadUserByUsername(String email) {
        log.info("loadUserByUsername method from UserService invoked");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
    }

    public String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public User findUserByEmail(String email) {
        log.info("findUserByEmail method from UserService invoked");
        return userRepository.findByEmail(email).orElse(null);
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);
    }

    public boolean validatePasswordResetToken(String token, String email) {
        log.info("validatePasswordResetToken method invoke");
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            log.info("Token is invalid");
            return false;
        }

        User user = userRepository.findOne(resetToken.getUser().getId());
        if (!user.getEmail().equals(email)) {
            log.info("Token is invalid");
            return false;
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Token[" + resetToken + "] expired - removing");
            tokenRepository.delete(resetToken);
            return false;
        }

        tokenRepository.delete(resetToken);

        log.info("Token " + token + " is valid");
        log.info("Token " + token + " removed from database");

        return true;
    }

    public void changePassword(User user, String password) {
        User userToUpdate = userRepository.findOne(user.getId());
        userToUpdate.setPassword(passwordEncoder.encode((password)));
        userRepository.save(userToUpdate);
        log.info("User " + user.getUsername() + " changed password for " + password);
    }

    public SimpleMailMessage constructResetTokenEmail(User user, String token) {
        List<String> to = new ArrayList<>();
        to.add(user.getEmail());
        String url = protocol + "://" + host + ":" + port + "/account/password/change/reset/token/" + token;
        Message message = new Message("messenger.recommendations2017@gmail.com", to, "Reset password", url);
        return message.constructEmail();
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000) // every 5 minutes
    public void deleteExpiredTokens() {
        Long numberOfDeletedTokens = tokenRepository.deleteByExpiryDateLessThan(LocalDateTime.now());

        log.info("Deleted[" + numberOfDeletedTokens + "] old tokens");
    }

}
