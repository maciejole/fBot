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
import ma.glasnost.orika.MapperFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;
import pl.hycom.pip.messanger.service.KeywordService;
import pl.hycom.pip.messanger.service.ProductService;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles({"dev", "testdb"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class ProductDTOServiceTest {

    @Autowired
    private MapperFacade orikaMapper;

    @Value("${messenger.recommendation.products-amount:3}")
    Integer expectedNumberOfProducts;
    @Autowired
    private ProductService productService;
    @Autowired
    private KeywordService keywordService;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;
    private Keyword keyword;
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

        keyword = new Keyword();
        keyword.setWord("test_keyword");

        keyword1 = new Keyword();
        keyword2 = new Keyword();
        keyword3 = new Keyword();
        keyword4 = new Keyword();

        keyword1.setWord("test1");
        keyword2.setWord("test2");
        keyword3.setWord("test3");
        keyword4.setWord("test4");
    }

    private void addKeywordsToProduct1() {
        LinkedHashSet<Keyword> keywords1 = new LinkedHashSet<>();
        keywords1.add(keyword1);
        keywords1.add(keyword2);
        product1.setKeywords(keywords1);
    }

    private void addKeywordsToDB() {
        keywordService.addKeyword(keyword1);
        keywordService.addKeyword(keyword2);
        keywordService.addKeyword(keyword3);
        keywordService.addKeyword(keyword4);
    }

    private void addKeywordsToProducts() {
        addKeywordsToDB();

        product1.addKeyword(keyword1);
        product1.addKeyword(keyword2);
        product1.addKeyword(keyword3);
        product2.addKeyword(keyword2);
        product3.addKeyword(keyword3);
    }

    @Test
    public void addProductWithKeywordsTest() {
        log.info("Test of addProduct method from ProductService class");

        // preparation
        long productsCount = productService.count();
        addKeywordsToProduct1();

        // action
        productService.addProduct(product1);

        // assertion
        assertEquals("size of returned productsList should be greater by one than productsCount", productsCount + 1, productService.findAllProducts().size());
        Product checkedProduct = productService.findProductById(product1.getId());
        assertEquals("size of returned keywordsList should be equal to 2", 2, checkedProduct.getKeywords().size());
    }

    @Test
    public void findProductByIdTest() {
        log.info("Test of findProductById method from ProductService class");

        // preparation
        productService.addProduct(product1);
        Product product = productService.findProductById(product1.getId());

        // assertion
        assertNotNull("added product shouldn't be null", product);
        assertEquals("name of product1 should be equal to 'name1'", "name1", product.getName());
        assertEquals("description of product1 should be equal to 'desc1'", "desc1", product.getDescription());
        assertEquals("url of product1 should be equal to 'url1'", "url1", product.getImageUrl());
    }

    @Test
    public void deleteProductByIdTest() {
        log.info("Test of deleteProduct method from ProductService class");

        // preparation
        long count = productService.count();
        productService.addProduct(product1);

        // assertion
        assertEquals("after adding product: size of returned productList should be greater by one than count", count + 1, productService.count());
        productService.deleteProduct(product1.getId());
        assertEquals("after deleting product: size of returned productList should be equal to count", count, productService.count());
    }

    @Test
    public void updateProductNameTest() {
        log.info("Test of updateProductName method from ProductService class");

        // preparation
        Product addedProduct = productService.addProduct(product1);
        String newName = "zażółć gęślą jaźń";
        addedProduct.setName(newName);

        // action
        productService.updateProduct(addedProduct);

        // assertion
        assertEquals("name of product1 should be updated and equal to " + newName, newName, productService.findProductById(addedProduct.getId()).getName());
    }

    @Test
    public void getRandomElements() {
        log.info("Test of getRandomElements method from ProductService class");

        // preparation
        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);

        // assertion
        assertEquals("size of returned list should be equal to the value of getRandomProducts parameter - 3", 3, productService.getRandomProducts(3).size());
        assertEquals("size of returned list should be equal to the value of getRandomProducts parameter - 2", 2, productService.getRandomProducts(2).size());
        assertEquals("size of returned list should be equal to the value of getRandomProducts parameter - 4", 3, productService.getRandomProducts(4).size());

        productService.addProduct(product4);
        assertEquals("size of returned list should be equal to the value of getRandomProducts parameter - 3", 3, productService.getRandomProducts(3).size());
        assertEquals("size of returned list should be equal to the value of getRandomProducts parameter - 2", 2, productService.getRandomProducts(2).size());
        assertEquals("size of returned list should be equal to the value of getRandomProducts parameter - 4", 4, productService.getRandomProducts(4).size());
    }

    @Test
    @Transactional
    public void addKeywordToProductTest() {
        // preparation
        addKeywordsToProduct1();
        long initialKeywordCount = product1.getKeywords().size();
        productService.addProduct(product1);
        keywordService.addKeyword(keyword);
        keyword = orikaMapper.map(keywordService.findKeywordById(keyword.getId()),Keyword.class);

        // action
        product1 = productService.addKeywordsToProduct(product1.getId(), keyword);

        // assertion
        assertEquals("product1 should have one more keyword than at the beginning of the test", initialKeywordCount + 1, product1.getKeywords().size());
        assertTrue("product1 should contain keyword", product1.getKeywords().contains(keyword));
    }

    @Test
    public void removeKeywordFromProductTest() {
        // preparation
        addKeywordsToProduct1();
        long initialKeywordCount = product1.getKeywords().size();
        productService.addProduct(product1);

        // action
        product1 = productService.removeKeywordsFromProduct(product1.getId(), keyword1);

        // assertion
        assertEquals("product1 should have one less keyword than at the beginning of the test", initialKeywordCount - 1, product1.getKeywords().size());
        assertFalse("product1 should not contain keyword", product1.getKeywords().contains(keyword));
    }

    @Test
    @Transactional
    public void findAllProductsContainingAtLeastOneKeywordTest() {
        // preparation
        addKeywordsToProducts();

        // action
        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);
        List<Product> productsWithKeywords = productService.findAllProductsContainingAtLeastOneKeyword(Arrays.asList(keyword1, keyword2, keyword3));

        // assertion
        assertEquals("list should contain all 3 products", 3, productsWithKeywords.size());
    }

    @After
    public void cleanAll() {
        productService.deleteAllProducts();
        keywordService.deleteAllKeywords();
    }
}
