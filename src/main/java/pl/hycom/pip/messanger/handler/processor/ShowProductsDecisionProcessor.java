/**
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

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Product;

@Component
public class ShowProductsDecisionProcessor implements PipelineProcessor {

    private static final int SHOW_PRODUCTS = 1;
    private static final int FIND_KEYWORD_TO_ASK = 2;

    @Value("${messenger.recommendation.products-amount:3}")
    private Integer numberOfProducts;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {

        @SuppressWarnings("unchecked")
        List<Product> products = ctx.get(PRODUCTS, List.class);

        if (products.size() > numberOfProducts) {
            return FIND_KEYWORD_TO_ASK;
        } else {
            return SHOW_PRODUCTS;
        }
    }

}
