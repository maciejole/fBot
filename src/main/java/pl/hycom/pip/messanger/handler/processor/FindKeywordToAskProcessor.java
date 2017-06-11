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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;

/**
 * Created by szale_000 on 2017-04-06.
 */
@Component
@Log4j2
public class FindKeywordToAskProcessor implements PipelineProcessor {

    public static final int SEND_KEYWORD_QUESTION = 1;
    public static final int SEND_PRODUCTS_MESSAGE = 2;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        log.info("Started finding keyword used in half of Products");

        @SuppressWarnings("unchecked")
        List<Product> products = ctx.get(PRODUCTS, List.class);
        @SuppressWarnings("unchecked")
        List<Keyword> keywords = ctx.get(KEYWORDS, List.class);

        Optional<Keyword> keywordToBeAsked = findKeywordToAsk(products, keywords);
        if (keywordToBeAsked.isPresent()) {
            ctx.put(KEYWORD_TO_BE_ASKED, keywordToBeAsked.get());
            log.info("Added keywordToBeAsked: " + keywordToBeAsked.get().getWord());
            return SEND_KEYWORD_QUESTION;
        } else {
            log.info("No keyword to be asked could be found");
            return SEND_PRODUCTS_MESSAGE;
        }
    }

    /**
     * @param products
     * @param wantedKeywords
     *            Keywordy, które zostały wyciągnięte z zapytań
     * @return null if no keyword found
     */
    Optional<Keyword> findKeywordToAsk(List<Product> products, List<Keyword> wantedKeywords) {
        if (CollectionUtils.isEmpty(products)) {
            throw new InvalidParameterException("Products cannot be null or empty");
        }

        int desiredCount = products.size() / 2;

        Optional<Map.Entry<Keyword, Long>> keywordCountEntry = products.parallelStream()
                .flatMap(product -> product.getKeywords().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(keywordLongEntry -> !wantedKeywords.contains(keywordLongEntry.getKey()))
                .map(keywordLongEntry -> {
                    keywordLongEntry.setValue(Math.abs(keywordLongEntry.getValue() - desiredCount));
                    return keywordLongEntry;
                })
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
                .findFirst();

        return keywordCountEntry.isPresent() ? Optional.of(keywordCountEntry.get().getKey()) : Optional.empty();
    }

}
