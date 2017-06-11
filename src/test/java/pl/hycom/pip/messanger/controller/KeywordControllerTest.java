package pl.hycom.pip.messanger.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
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
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.service.KeywordService;

/**
 * Created by Piotr on 17.04.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessengerRecommendationsApplication.class)
@ActiveProfiles({ "dev", "testdb" })
@WebAppConfiguration
@Log4j2
public class KeywordControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private KeywordService keywordService;

    private List<Integer> list = new ArrayList<>();

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        Keyword keyword;
        Keyword keyword1;
        Keyword keyword2;

        keyword = new Keyword();
        keyword.setWord("test");

        keyword1 = new Keyword();
        keyword1.setWord("test1");

        keyword2 = new Keyword();
        keyword2.setWord("test2");

        /*
         * keywordService.addKeyword(keyword);
         * keywordService.addKeyword(keyword1);
         * keywordService.addKeyword(keyword2);
         */
        list.add(keywordService.addKeyword(keyword).getId());
        list.add(keywordService.addKeyword(keyword1).getId());
        list.add(keywordService.addKeyword(keyword2).getId());
    }

    @Test
    public void pageFoundTest() throws Exception {

        mockMvc.perform(get("/admin/keywords"))
                .andExpect(status().isOk());
    }

    @Test
    public void pageNotFoundTest() throws Exception {

        mockMvc.perform(get("/user/keywords"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllKeywords() throws Exception {

        mockMvc.perform(get("/admin/keywords"))
                .andExpect(status().isOk())
                .andExpect(view().name("keywords"))
                .andExpect(model().attribute("keywords", hasSize(3)))
                .andExpect(model().attribute("keywords", hasItem(allOf(
                        hasProperty("id", Is.is(list.get(0))),
                        hasProperty("word", Is.is("test"))))))
                .andExpect(model().attribute("keywords", hasItem(allOf(
                        hasProperty("id", Is.is(list.get(1))),
                        hasProperty("word", Is.is("test1"))))))
                .andExpect(model().attribute("keywords", hasItem(allOf(
                        hasProperty("id", Is.is(list.get(2))),
                        hasProperty("word", Is.is("test2"))))));
    }

    @Test
    public void deleteById() throws Exception {
        int id = list.get(2);
        mockMvc.perform(delete("/admin/keywords/" + id + "/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/keywords"));
    }

    @Test
    public void addOrUpdateTest() throws Exception {
        // Given:
        mockMvc.perform(post("/admin/keywords").param("word", "test1"))
                .andExpect(status().isOk())
                .andExpect(view().name("keywords"));
    }

    @Test
    public void cleanAll() {
        keywordService.deleteAllKeywords();
    }
}
