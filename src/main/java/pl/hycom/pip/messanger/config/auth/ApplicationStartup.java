/*
 *   Copyright 2012-2014 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package pl.hycom.pip.messanger.config.auth;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
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

    private final String ROLE_ADMIN = Role.RoleName.ROLE_ADMIN.name();
    private final String ROLE_USER = Role.RoleName.ROLE_USER.name();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("onApplicationEvent method invoked");

        initializeRole(ROLE_ADMIN);
        initializeRole(ROLE_USER);

        if (userService.findAllUsers().isEmpty()) {
            initializeAdminUser();
        }
    }

    private void initializeAdminUser() {
        log.info("initializeAdminUser method invoked");

        User user = new User("admin", "admin", login, password, "+48923456783");
        Optional<Role> role = roleService.findRoleByName(ROLE_ADMIN);
        if (role.isPresent()) {
            user.setRoles(Collections.singleton(role.get()));
//            try {
//                userService.addUser(user, RequestHelper.getURLBase(request));
//            } catch (EmailNotUniqueException e) {
//                e.printStackTrace();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
        } else {
            log.warn("ApplicationStartup.initializeAdminUser() - Initializing admin failed!!");
        }
    }

    private void initializeRole(final String roleName) {
        log.info("initializeRole method invoked for role " + roleName);

        Optional<Role> role = roleService.findRoleByName(roleName);
        if (!role.isPresent()) {
            Role newRole = new Role(roleName);
            roleService.addRole(newRole);
        }
    }
}
