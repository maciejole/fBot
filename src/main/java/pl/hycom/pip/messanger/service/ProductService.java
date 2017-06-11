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

package pl.hycom.pip.messanger.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.controller.model.ProductDTO;
import pl.hycom.pip.messanger.repository.KeywordRepository;
import pl.hycom.pip.messanger.repository.ProductRepository;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class ProductService {

    @Autowired
    private MapperFacade orikaMapper;

    private final ProductRepository productRepository;
    private final KeywordRepository keywordRepository;

    public Product addProduct(Product product) {
        log.info("Adding product: " + product);

        return productRepository.save(product);
    }

    public Product findProductById(Integer id) {
        log.info("Searching for product with id[" + id + "]");

        return productRepository.findOne(id);
    }

    public List<ProductDTO> findAllProducts() {
        log.info("Searching all products");
        return orikaMapper.mapAsList(StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()), ProductDTO.class);

    }

    public List<String> findProductKeywords(Integer id) {
        log.info("Searching keywords for product with id[" + id + "]");
        return productRepository.findOne(id).getKeywords().stream().map(Keyword::getWord).collect(Collectors.toList());
    }

    public boolean deleteProduct(Integer id) {
        log.info("Deleting product[" + id + "]");
        if (!productRepository.exists(id)) {
            return Boolean.FALSE;
        }
        productRepository.delete(id);
        return Boolean.TRUE;
    }

    public Product updateProduct(Product product) {
        log.info("Updating product: " + product);

        Product updatedProduct = productRepository.findOne(product.getId());
        updatedProduct.setName(product.getName());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setImageUrl(product.getImageUrl());
        updatedProduct.setKeywords(product.getKeywords());
        return productRepository.save(updatedProduct);
    }

    public ProductDTO addOrUpdateProduct(ProductDTO product) {
        String[] keywordsStr = StringUtils.split(product.getKeywordsHolder(), ',');
        final Set<Keyword> keywords = new HashSet<>();
        if (keywordsStr != null) {
            Arrays.stream(keywordsStr)
                    .map(StringUtils::lowerCase)
                    .distinct()
                    .forEach(k -> {
                        Keyword keyword = keywordRepository.findByWord(k);
                        if (keyword == null) {
                            keyword = new Keyword(k);
                        }
                        keywords.add(keyword);
                    });
        }
        Product entityProduct = orikaMapper.map(product, Product.class);
        entityProduct.setKeywords(keywords);

        return orikaMapper.map(addOrUpdateProduct(entityProduct), ProductDTO.class);
    }

    public Product addOrUpdateProduct(Product product) {
        if (product.getId() != null && product.getId() != 0) {
            Product updatedProduct = updateProduct(product);
            log.info("ProductDTO updated !!!");
            return updatedProduct;
        }
        Product addedProduct = addProduct(product);
        log.info("ProductDTO added !!!");
        return addedProduct;
    }

    public List<Product> getRandomProducts(int howManyProducts) {
        List<Product> products = new ArrayList<>(howManyProducts);

        int quantity = (int) productRepository.count();
        log.info("Searching for [" + howManyProducts + "] random products from quantity[" + quantity + "]");

        if (quantity == 0 || howManyProducts > quantity) {
            products.addAll(StreamSupport.stream(productRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList()));

            log.info("Returning all products");
            return products;
        }

        for (int i = 0; i < howManyProducts; i++) {
            PageRequest pr = new PageRequest(new Random().nextInt(quantity - products.size()), 1);
            products.addAll(i == 0 ? productRepository.findSomeProducts(pr) : productRepository.findSomeProducts(products, pr));
        }

        log.info("Found [" + products.size() + "] random products");
        return products;
    }

    public List<Product> findAllProductsContainingAtLeastOneKeyword(List<Keyword> keywords) {
        return keywords.stream().filter(Objects::nonNull)
                .flatMap(k -> productRepository.findProductsWithKeyword(k).stream()).filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
    }

    public boolean isAnyProductContainingAtLeastOneKeyword(List<KeywordDTO> keywords) {
        List<Keyword> keywordsList = orikaMapper.mapAsList(keywords, Keyword.class);
        return !findAllProductsContainingAtLeastOneKeyword(keywordsList).isEmpty();
    }

    public Product addKeywordsToProduct(Integer id, Keyword... keywords) {
        Product product = findProductById(id);
        product.getKeywords().addAll(Arrays.asList(keywords));
        return productRepository.save(product);
    }

    public Product removeKeywordsFromProduct(Integer id, Keyword... keywords) {
        Product product = findProductById(id);
        product.getKeywords().removeAll(Arrays.asList(keywords));
        return productRepository.save(product);
    }

    public void deleteAllProducts() {
        log.info("Deleting all products");

        productRepository.deleteAll();
    }

    public int count() {
        return findAllProducts().size();
    }
}
