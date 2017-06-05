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
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by szale_000 on 2017-04-06.
 */
@Component
@Log4j2
public class ExtraKeywordProcessor implements PipelineProcessor {

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        log.info("Started finding keyword used in half of Products");

        @SuppressWarnings("unchecked")
        List<Product> products = ctx.get(PRODUCTS, List.class);

        Keyword keywordToBeAsked = findKeyKeyword(products);
        ctx.put(KEYWORD_TO_BE_ASKED, keywordToBeAsked);
        log.info("Added keywordToBeAsked: " + keywordToBeAsked.getWord());
        return 1;
    }

    Keyword findKeyKeyword(List<Product> products) {
        int desiredCount = products == null ? 0 : products.size() / 2;
        Map.Entry<Keyword, Long> keywordCountEntry = Optional.ofNullable(products).orElse(Collections.emptyList()).parallelStream()
                .flatMap(product -> product.getKeywords().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(keywordLongEntry -> {
                    keywordLongEntry.setValue(Math.abs(keywordLongEntry.getValue() - desiredCount));
                    return keywordLongEntry;
                })
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
                .findFirst().orElseThrow(() -> new InvalidParameterException("Prodcuts cannot be null or empty"));

        return keywordCountEntry.getKey();
    }

}
