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

package pl.hycom.pip.messanger.handler.processor;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;
import pl.hycom.pip.messanger.service.ProductService;

import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
@Log4j2
public class LoadBestMatchingProductsProcessorTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private LoadBestMatchingProductsProcessor processor;

    private List<Keyword> keywords;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        keywords = Arrays.asList(new Keyword("K1"), new Keyword("K2"), new Keyword("K3"), new Keyword("K4"), new Keyword("K5"), new Keyword("K6"));
    }

    @Test
    public void findBestFittingProductsTest() {
        //given
        List<Product> givenProducts = Arrays.asList(
                createProduct(1, Arrays.asList(keywords.get(0), keywords.get(1), keywords.get(2))),
                createProduct(2, Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3))),
                createProduct(3, Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3), keywords.get(4))),
                createProduct(4, Arrays.asList(keywords.get(0), keywords.get(5), keywords.get(4))),
                createProduct(5, Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3), keywords.get(5))),
                createProduct(6, Arrays.asList(keywords.get(0), keywords.get(1)))
        );
        Mockito.when(productService.findAllProductsContainingAtLeastOneKeyword(any()))
                .thenReturn(givenProducts);

        List<Keyword> excludedKeywords = Collections.emptyList();
        List<Keyword> keywordsFromRequest = Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3));
        /*
        Product 0 contains 2 wanted keywords (0,2)
        Product 1 contains 3 wanted keywords (0,2,3)
        Product 2 contains 3 wanted keywords (0,2,3)
        Product 3 contains 1 wanted keyword  (0)
        Product 4 contains 3 wanted keywords (0,2,3)
        Product 5 contains 1 wanted keyword  (1)
         */
        //when
        List<Product> bestFittingProducts = processor.tryFindBestMatchingProducts(keywordsFromRequest, excludedKeywords);

        // then
        Assertions.assertThat(bestFittingProducts).hasSize(3)
                .containsOnly(givenProducts.get(1), givenProducts.get(2), givenProducts.get(4));
    }

    @Test
    public void findBestFittingProductsTestWithAllExcluded() {
        //given
        List<Product> givenProducts = Arrays.asList(
                createProduct(1, Arrays.asList(keywords.get(0), keywords.get(1), keywords.get(2))),
                createProduct(2, Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3)))
        );
        Mockito.when(productService.findAllProductsContainingAtLeastOneKeyword(any()))
                .thenReturn(givenProducts);

        List<Keyword> excludedKeywords = Collections.singletonList(keywords.get(0));
        List<Keyword> keywordsFromRequest = Arrays.asList(keywords.get(2), keywords.get(3));
        /*
        Product 0 contains 1 wanted keywords (2)   but contains excluded keyword (0)
        Product 1 contains 2 wanted keywords (2,3) but contains excluded keyword (0)
         */

        //when
        List<Product> bestFittingProducts = processor.tryFindBestMatchingProducts(keywordsFromRequest, excludedKeywords);

        // then
        Assertions.assertThat(bestFittingProducts).isEmpty();
    }

    @Test
    public void findBestFittingProductsTestWithExcludedKeywords() {
        //given
        List<Product> givenProducts = Arrays.asList(
                createProduct(1, Arrays.asList(keywords.get(0), keywords.get(1), keywords.get(2))),
                createProduct(2, Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3))),
                createProduct(3, Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3), keywords.get(4))),
                createProduct(4, Arrays.asList(keywords.get(0), keywords.get(5), keywords.get(4))),
                createProduct(5, Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3), keywords.get(5))),
                createProduct(6, Arrays.asList(keywords.get(0), keywords.get(1)))
        );
        Mockito.when(productService.findAllProductsContainingAtLeastOneKeyword(any()))
                .thenReturn(givenProducts);

        List<Keyword> excludedKeywords = Collections.singletonList(keywords.get(4));
        List<Keyword> keywordsFromRequest = Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3));
        /*
        Product 0 contains 2 wanted keywords (0,2)
        Product 1 contains 3 wanted keywords (0,2,3)
        Product 2 contains 3 wanted keywords (0,2,3) but contains excluded keyword (4)
        Product 3 contains 1 wanted keyword  (0)     but contains excluded keyword (4)
        Product 4 contains 3 wanted keywords (0,2,3)
        Product 5 contains 1 wanted keyword  (1)
         */

        //when
        List<Product> bestFittingProducts = processor.tryFindBestMatchingProducts(keywordsFromRequest, excludedKeywords);

        // then
        Assertions.assertThat(bestFittingProducts).hasSize(2)
                .containsOnly(givenProducts.get(1), givenProducts.get(4));
    }

    @Test
    public void findBestFittingProductsTestWithKeywordThatIsAlsoExcluded() {
        //given
        List<Product> givenProducts = Collections.singletonList(
                createProduct(1, Arrays.asList(keywords.get(0), keywords.get(1), keywords.get(2)))
        );
        Mockito.when(productService.findAllProductsContainingAtLeastOneKeyword(any()))
                .thenReturn(givenProducts);

        List<Keyword> excludedKeywords = Collections.singletonList(keywords.get(0));
        List<Keyword> keywordsFromRequest = Arrays.asList(keywords.get(0), keywords.get(2), keywords.get(3));
        /*
        Keyword excluded is also keyword from request. Such operation cannot occur
         */

        //when
        Throwable thrown = Assertions.catchThrowable(() -> processor.tryFindBestMatchingProducts(keywordsFromRequest, excludedKeywords));

        // then
        Assertions.assertThat(thrown).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    public void findBestFittingProductsTestWithHundredProducts() {
        //given
        List<Product> givenProducts = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Product product = createProduct(i, Collections.singletonList(keywords.get(0)));
            if (i % 2 == 0) {
                product.addKeyword(keywords.get(1));
            }
            givenProducts.add(product);
        }
        Mockito.when(productService.findAllProductsContainingAtLeastOneKeyword(any()))
                .thenReturn(givenProducts);

        List<Keyword> excludedKeywords = Collections.singletonList(keywords.get(1));
        List<Keyword> keywordsFromRequest = Collections.singletonList(keywords.get(0));
        /*
        There are 100 products, all of them contain keyword 0 and every second of them contains excluded keyword
         */
        //when
        List<Product> products = processor.tryFindBestMatchingProducts(keywordsFromRequest, excludedKeywords);

        // then
        Assertions.assertThat(products).hasSize(50)
                .are(new Condition<Product>() {
                    @Override
                    public boolean matches(Product product) {
                        return product.getKeywords().contains(keywords.get(0))
                                && !product.getKeywords().contains(keywords.get(1));
                    }
                });
    }

    @Test
    public void findBestFittingProductsTestNoKeywords() {
        List<Product> bestFittingProducts = processor.tryFindBestMatchingProducts(Collections.emptyList(), Collections.emptyList());

        // assertion
        assertEquals("List should contain no products", 0, bestFittingProducts.size());
    }

    private Product createProduct(int id, List<Keyword> keywords) {
        final Product product = new Product();
        product.setId(id);
        product.setName("Foo");
        product.setDescription("Foo");
        product.setImageUrl("Foo");
        product.setKeywords(new HashSet<>(keywords));
        return product;
    }
}
