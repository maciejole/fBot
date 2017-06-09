/*
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

package pl.hycom.pip.messanger.config.auth;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.hycom.pip.messanger.exception.EmailNotUniqueException;
import pl.hycom.pip.messanger.repository.model.Role;
import pl.hycom.pip.messanger.repository.model.User;
import pl.hycom.pip.messanger.service.RoleService;
import pl.hycom.pip.messanger.service.UserService;

/**
 * Created by Maciek on 2017-05-27.
 */
@Component
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${auth.login:admin@example.com}")
    private String login;

    @Value("${auth.password:admin1}")
    private String password;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("onApplicationEvent method invoked");

        initializeRoleIfNotExists(Role.Name.ADMIN);
        initializeRoleIfNotExists(Role.Name.USER);

        if (userService.findUserByEmail(login) == null) {
            initializeAdminUser();
        }
    }

    private void initializeAdminUser() {
        log.info("initializeAdminUser method invoked");

        Optional<Role> role = roleService.findRoleByName(Role.Name.ADMIN);
        if (!role.isPresent()) {
            log.warn("ApplicationStartup.initializeAdminUser() - Initializing admin failed!!");
            return;
        }

        User user = new User("admin", "admin", login, passwordEncoder.encode(password), "+48923456783");
        user.setRoles(Collections.singleton(role.get()));
        try {
            userService.addUser(user);
        } catch (EmailNotUniqueException e) {
            log.error("Email is not unique");
        }
    }

    private void initializeRoleIfNotExists(final String roleName) {
        log.info("initializeRole method invoked for role " + roleName);

        Optional<Role> role = roleService.findRoleByName(roleName);
        if (!role.isPresent()) {
            roleService.addRole(new Role(roleName));
        }
    }
}
