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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.service.KeywordService;
import pl.hycom.pip.messanger.service.ProductService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Log4j2
public class LoadBestMatchingProductsProcessor implements PipelineProcessor {

    public static final String PRODUCTS = "products";
    public static final String KEYWORDS_FOUND = "keywordsFound";

    @Autowired
    private ProductService productService;

    @Autowired
    private KeywordService keywordService;

    @Value("${messenger.recommendation.products-amount}")
    private Integer numberOfProducts;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        log.info("Started process of LoadBestMatchingProductsProcessor");

        @SuppressWarnings("unchecked")
        Set<String> keywordsStr = ctx.get(ExtractKeywordsFromMessageProcessor.KEYWORDS, Set.class);

        List<Keyword> keywords = convertStringsToKeywords(keywordsStr);

        List<Product> products = findBestMatchingProducts(numberOfProducts, keywords);
        ctx.put(PRODUCTS, products);

        List<Keyword> keywordsToBeSaved = getKeywordsThatWereInAnyProduct(products, keywords);
        ctx.put(KEYWORDS_FOUND, keywordsToBeSaved);

        return 1;
    }

    public List<Keyword> convertStringsToKeywords(Set<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return Collections.emptyList();
        }

        // This stream maps Array of strings into list of keywords
        return keywords.stream()
                .map(s -> keywordService.findKeywordByWord(s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Product> findBestMatchingProducts(int numberOfProducts, List<Keyword> keywordsList) {
        if (keywordsList == null || keywordsList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Product> productsWithKeywords = productService.findAllProductsContainingAtLeastOneKeyword(keywordsList);

        // This is priority queue, works like a stack, you can only access top element, but always has highest
        // element on top. The priority of elements is decided by comparator passed in constructor
        PriorityQueue<Map.Entry<Product, Long>> productsQueue = new PriorityQueue<>((o1, o2) -> Math.toIntExact(o2.getValue() - o1.getValue()));

        // This stream maps each product into an entry to priorityQueue with product as a key and number of keywords
        // it has from list as value
        productsWithKeywords.stream()
                .filter(Objects::nonNull)
                .map(product -> new HashMap.SimpleEntry<>(product, keywordsList.stream().filter(Objects::nonNull).distinct().filter(product::containsKeyword).count()))
                .forEach(productsQueue::add);

        // This stream takes x first products from queue end puts them into list
        return IntStream.iterate(0, i -> i + 1).limit(numberOfProducts)
                .filter(i -> !productsQueue.isEmpty())
                .mapToObj(i -> productsQueue.poll().getKey())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Keyword> getKeywordsThatWereInAnyProduct(List<Product> products, List<Keyword> keywords) {
        if (keywords == null || keywords.isEmpty() || products == null || products.isEmpty()) {
            return Collections.emptyList();
        }

        // This stream takes only these keywords from list that appear in at least one product
        return keywords.stream()
                .distinct()
                .filter(keyword -> products.stream().anyMatch(product -> product.containsKeyword(keyword)))
                .collect(Collectors.toList());

    }
}
