package pl.hycom.pip.messanger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.hycom.pip.messanger.MessengerRecommendationsApplication;
import pl.hycom.pip.messanger.exception.EmailNotUniqueException;
import pl.hycom.pip.messanger.repository.UserRepository;
import pl.hycom.pip.messanger.repository.model.User;
import pl.hycom.pip.messanger.service.UserService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Piotr on 10.06.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessengerRecommendationsApplication.class)
@ActiveProfiles({"dev", "testdb"})
@WebAppConfiguration
@Log4j2
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    private List<Integer> IdList = new ArrayList<>();

    private User user;
    private User user1;
    private User user2;

    @Before
    public void setUp() throws EmailNotUniqueException {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setFirstName("Name");
        user.setLastName("Lastname");
        user.setEmail("mail@example.com");
        user.setPhoneNumber("+48789999999");

        user1 = new User();
        user1.setFirstName("Name");
        user1.setLastName("Lastnamee");
        user1.setEmail("mail1@example.com");
        user.setPhoneNumber("+48789499999");

        user2 = new User();
        user2.setFirstName("Name");
        user2.setLastName("Lastnameee");
        user2.setEmail("mail2@example.com");
        user.setPhoneNumber("+48789929999");

        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);
    }

    @Ignore
    @Test
    public void pageFoundTest() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Ignore
    @Test
    public void pageNotFoundTest() throws Exception {

        mockMvc.perform(get("/user/users"))
                .andExpect(status().isNotFound());
    }

    @Ignore
    @Test
    public void getAllKeywords() throws Exception {

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("users", hasSize(3)))
                .andExpect(model().attribute("users", hasItem(allOf(
                        hasProperty("id", Is.is(IdList.get(0))),
                        hasProperty("lastname", Is.is("Lastname")),
                        hasProperty("email", Is.is("mail@example.com")),
                        hasProperty("name", Is.is("Name"))))))
                .andExpect(model().attribute("users", hasItem(allOf(
                        hasProperty("id", Is.is(IdList.get(0))),
                        hasProperty("lastname", Is.is("Lastnamee")),
                        hasProperty("email", Is.is("mail1@example.com")),
                        hasProperty("name", Is.is("Name1"))))))
                .andExpect(model().attribute("users", hasItem(allOf(
                        hasProperty("id", Is.is(IdList.get(0))),
                        hasProperty("lastname", Is.is("Lastnameee")),
                        hasProperty("email", Is.is("mail2@example.com")),
                        hasProperty("name", Is.is("Name2"))))));
    }

    @After
    public void cleanAll() {
        userService.deleteUser(user.getId());
        userService.deleteUser(user1.getId());
        userService.deleteUser(user2.getId());
    }
}
