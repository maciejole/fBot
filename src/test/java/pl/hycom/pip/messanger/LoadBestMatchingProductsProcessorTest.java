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

package pl.hycom.pip.messanger;

import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.hycom.pip.messanger.handler.processor.LoadBestMatchingProductsProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;
import pl.hycom.pip.messanger.service.KeywordService;
import pl.hycom.pip.messanger.service.ProductService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles({"dev", "testdb"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class LoadBestMatchingProductsProcessorTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private LoadBestMatchingProductsProcessor processor;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;
    private Keyword keyword1;
    private Keyword keyword2;
    private Keyword keyword3;
    private Keyword keyword4;

    @Before
    public void setUp() {
        product1 = new Product();
        product1.setName("name1");
        product1.setDescription("desc1");
        product1.setImageUrl("url1");

        product2 = new Product();
        product2.setName("name2");
        product2.setDescription("desc2");
        product2.setImageUrl("url2");

        product3 = new Product();
        product3.setName("name3");
        product3.setDescription("desc3");
        product3.setImageUrl("url3");

        product4 = new Product();
        product4.setName("name4");
        product4.setDescription("desc4");
        product4.setImageUrl("url4");

        product5 = new Product();
        product5.setName("name5");
        product5.setDescription("desc5");
        product5.setImageUrl("url5");

        product6 = new Product();
        product6.setName("name6");
        product6.setDescription("desc6");
        product6.setImageUrl("url6");

        keyword1 = new Keyword();
        keyword2 = new Keyword();
        keyword3 = new Keyword();
        keyword4 = new Keyword();

        keyword1.setWord("test1");
        keyword2.setWord("test2");
        keyword3.setWord("test3");
        keyword4.setWord("test4");

        keyword1 = keywordService.addKeyword(keyword1);
        keyword2 = keywordService.addKeyword(keyword2);
        keyword3 = keywordService.addKeyword(keyword3);
        keyword4 = keywordService.addKeyword(keyword4);

        product1.addKeyword(keyword1);
        product1.addKeyword(keyword2);
        product1.addKeyword(keyword3);
        product1.addKeyword(keyword4);

        product2.addKeyword(keyword1);
        product2.addKeyword(keyword2);

        product3.addKeyword(keyword1);
        product3.addKeyword(keyword2);
        product3.addKeyword(keyword4);

        product4.addKeyword(keyword1);
        product4.addKeyword(keyword2);
        product4.addKeyword(keyword4);

        product5.addKeyword(keyword3);
        product5.addKeyword(keyword4);

        product6.addKeyword(keyword1);

        product1 = productService.addProduct(product1);
        product2 = productService.addProduct(product2);
        product3 = productService.addProduct(product3);
        product4 = productService.addProduct(product4);
        product5 = productService.addProduct(product5);
        product6 = productService.addProduct(product6);
    }

    @Test
    @Transactional
    public void findBestFittingProductsTest() {
        List<Keyword> keywords = processor.convertStringsToKeywords(Stream.of("test1", "test2", "test3", "test4", "test5").collect(Collectors.toSet()));

        List<Product> bestFittingProducts = processor.findBestMatchingProducts(3, keywords);

        // assertion
        assertEquals("List should contain 3 products", 3, bestFittingProducts.size());
        assertTrue("List should contain product1", bestFittingProducts.contains(product1));
        assertTrue("List should contain product3", bestFittingProducts.contains(product3));
        assertTrue("List should contain product4", bestFittingProducts.contains(product4));
    }

    @Test
    @Transactional
    public void findBestFittingProductsTestNoKeywords() {
        List<Product> bestFittingProducts = processor.findBestMatchingProducts(3, Collections.emptyList());

        // assertion
        assertEquals("List should contain no products", 0, bestFittingProducts.size());
    }

    @After
    public void cleanAll() {
        productService.deleteAllProducts();
        keywordService.deleteAllKeywords();
    }
}
