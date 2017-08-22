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
package pl.hycom.pip.messanger.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.MessengerRecommendationsApplication;
import pl.hycom.pip.messanger.exception.EmailNotUniqueException;
import pl.hycom.pip.messanger.repository.model.User;
import pl.hycom.pip.messanger.service.UserService;

/**
 * Created by Piotr on 10.06.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessengerRecommendationsApplication.class)
@ActiveProfiles({ "dev", "testdb" })
@WebAppConfiguration
@Log4j2
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    private User user;
    private User user1;
    private User user2;

    @Before
    public void setUp() throws EmailNotUniqueException {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setFirstName("Name");
        user.setLastName("Lastname");
        user.setEmail("mail1@example.com");
        user.setPhoneNumber("+48789999999");

        user1 = new User();
        user1.setFirstName("Name");
        user1.setLastName("Lastnamee");
        user1.setEmail("mail2@example.com");
        user1.setPhoneNumber("+48789499999");

        user2 = new User();
        user2.setFirstName("Name");
        user2.setLastName("Lastnameee");
        user2.setEmail("mail3@example.com");
        user2.setPhoneNumber("+48789929999");

        user = userService.addUser(user);
        user1 = userService.addUser(user1);
        user2 = userService.addUser(user2);
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void pageFoundTest() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void pageNotFoundTest() throws Exception {

        mockMvc.perform(get("/user/users"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void getAllKeywords() throws Exception {

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("users", hasSize(3)))
                .andExpect(model().attribute("users", hasItem(allOf(
                        hasProperty("id", Is.is(user.getId())),
                        hasProperty("lastName", Is.is(user.getLastName())),
                        hasProperty("email", Is.is(user.getEmail())),
                        hasProperty("firstName", Is.is(user.getFirstName()))))))
               .andExpect(model().attribute("users", hasItem(allOf(
                       hasProperty("id", Is.is(user1.getId())),
                       hasProperty("lastName", Is.is(user1.getLastName())),
                       hasProperty("email", Is.is(user1.getEmail())),
                       hasProperty("firstName", Is.is(user1.getFirstName()))))))
               .andExpect(model().attribute("users", hasItem(allOf(
                       hasProperty("id", Is.is(user2.getId())),
                       hasProperty("lastName", Is.is(user2.getLastName())),
                       hasProperty("email", Is.is(user2.getEmail())),
                       hasProperty("firstName", Is.is(user2.getFirstName()))))));
    }

    @After
    public void cleanAll() {
        userService.deleteAllUsers();
    }
}
