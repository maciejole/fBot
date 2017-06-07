package pl.hycom.pip.messanger.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.MessengerRecommendationsApplication;

/**
 * Created by piotr on 15.05.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessengerRecommendationsApplication.class)
@WebAppConfiguration
@ActiveProfiles({ "dev", "testdb" })
@Log4j2
public class GreetingControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void removeGreetingTest() throws Exception {
        String locale = "pl_PL";

        this.mockMvc.perform(get("/admin/deleteGreeting/" + locale))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/greetings"));
    }

}
