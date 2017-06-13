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

package pl.hycom.pip.messanger.handler.processor;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;
import pl.hycom.pip.messanger.service.ProductService;

@Component
@Log4j2
public class LoadBestMatchingProductsProcessor implements PipelineProcessor {

    @Autowired
    private ProductService productService;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        log.info("Started process of LoadBestMatchingProductsProcessor");

        @SuppressWarnings("unchecked")
        List<Keyword> keywords = ctx.get(KEYWORDS, List.class);

        @SuppressWarnings("unchecked")
        List<Keyword> excludedKeywords = ctx.get(KEYWORDS_EXCLUDED, List.class);

        List<Product> products = tryFindBestMatchingProducts(keywords, CollectionUtils.isEmpty(excludedKeywords) ? Collections.emptyList() : excludedKeywords);
        ctx.put(PRODUCTS, products);

        ctx.put(KEYWORDS_FOUND, getKeywordsThatWereInAnyProduct(products, keywords));

        return 1;
    }

    List<Product> tryFindBestMatchingProducts(List<Keyword> keywordsList, List<Keyword> excludedKeywords) {
        if (CollectionUtils.isEmpty(keywordsList)) {
            return Collections.emptyList();
        }
        List<Keyword> commonKeywords = new ArrayList<>(keywordsList);
        commonKeywords.retainAll(excludedKeywords);
        if (!commonKeywords.isEmpty()) {
            throw new InvalidParameterException("Excluded keyword cant be keyword to find");
        }
        return findBestMatchingProducts(keywordsList, excludedKeywords);
    }

    /**
     * Call only from wrapper method
     *
     * @see #tryFindBestMatchingProducts
     */
    private List<Product> findBestMatchingProducts(List<Keyword> keywordsList, List<Keyword> excludedKeywords) {
        List<Map.Entry<Product, Integer>> productsWithKeywords = productService
                .findAllProductsContainingAtLeastOneKeyword(keywordsList)
                .stream()
                .filter(product -> product.getKeywords().stream().noneMatch(excludedKeywords::contains))
                .collect(Collectors.toMap(Function.identity(), product -> {
                    List<Keyword> commonKeywords = new ArrayList<>(product.getKeywords());
                    commonKeywords.retainAll(keywordsList);
                    return commonKeywords.size();
                }))
                .entrySet().stream()
                .sorted(Comparator.<Map.Entry<Product, Integer>>comparingInt(Map.Entry::getValue).reversed())
                .collect(Collectors.toList());

        if (!productsWithKeywords.isEmpty()) {
            int maxMatchingKeywords = productsWithKeywords.get(0).getValue();
            return productsWithKeywords.stream()
                    .filter(entry -> entry.getValue().equals(maxMatchingKeywords))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private List<Keyword> getKeywordsThatWereInAnyProduct(List<Product> products, List<Keyword> keywords) {
        if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(keywords)) {
            return Collections.emptyList();
        }

        // This stream takes only these keywords from list that appear in at least one product
        return keywords.stream()
                .distinct()
                .filter(keyword -> products.stream().anyMatch(product -> product.containsKeyword(keyword)))
                .collect(Collectors.toList());

    }
}
