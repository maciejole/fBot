package pl.hycom.pip.messanger.controller;

import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import pl.hycom.pip.messanger.MessengerRecommendationsApplication;
import pl.hycom.pip.messanger.repository.ProductRepository;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;
import pl.hycom.pip.messanger.service.ProductService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by piotr on 03.04.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessengerRecommendationsApplication.class)
@ActiveProfiles({ "dev", "testdb" })
@WebAppConfiguration
@Log4j2
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private List<Integer> list = new ArrayList<>();

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();


        Product product2;
        Product product1;
        product1 = new Product();
        product1.setName("name1");
        product1.setDescription("desc1");
        product1.setImageUrl("url1");
        Keyword keyword = new Keyword();
        keyword.setWord("testKeyword");
        product1.addKeyword(keyword);
        product2 = new Product();
        product2.setName("name2");
        product2.setDescription("desc2");
        product2.setImageUrl("url2");

        //productService.addProduct(product1);
        //productService.addProduct(product2);
        list.add(productService.addProduct(product1).getId());
        list.add(productService.addProduct(product2).getId());
        //productRepository.save(product1);
        //productRepository.save(product2);
        log.error("Before");

    }

    @Test
    public void pageNotFoundTest() throws Exception {

         String user = "tester";

         mockMvc.perform(get("/"+user+"/products/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void pageFoundTest() throws Exception {

        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllProducts() throws Exception {

        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attribute("products",hasSize(2)))
                .andExpect(model().attribute("products",hasItem(allOf(
                        hasProperty("id", is(list.get(0))),
                        hasProperty("name", is("name1")),
                        hasProperty("description", is("desc1")),
                        hasProperty("imageUrl", is("url1"))
                ))))
                .andExpect(model().attribute("products",hasItem(allOf(
                        hasProperty("id", is(list.get(1))),
                hasProperty("name", is("name2")),
                hasProperty("description", is("desc2")),
                hasProperty("imageUrl", is("url2"))
        ))));
    }

    @Test
    public void deleteById() throws Exception {

        int id = list.get(0);
        mockMvc.perform(delete("/admin/products/" + id + "/delete"))
                .andExpect(status().isOk());
    }

    @Test
    public void addOrUpdateTest() throws Exception {
        final MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.put("id", Collections.singletonList("0"));
        params.put("name", Collections.singletonList("test product"));
        params.put("name", Collections.singletonList("test product"));
        params.put("description", Collections.singletonList("test product desc"));
        params.put("imageUrl", Collections.singletonList("http://localhost/test.image.jpg"));
        params.put("keywords.id", Collections.singletonList("1"));
        params.put("keywords.word", Collections.singletonList("testKeyword"));

        mockMvc.perform(post("/admin/products").params(params))
               .andExpect(status().isFound())
               .andExpect(view().name("redirect:/admin/products"));
    }

    @After
    public void cleanAll() {
        productService.deleteAllProducts();
    }

}
