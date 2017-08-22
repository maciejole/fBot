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

import org.springframework.stereotype.Component;

import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;

@Component
public class UserResponseDecisionProcessor implements PipelineProcessor {

    public static final int SEND_KEYWORD_QUESTION = 1;
    public static final int SEND_PRODUCTS_MESSAGE = 2;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        return ctx.get(KEYWORD_TO_BE_ASKED, Keyword.class) == null ? SEND_PRODUCTS_MESSAGE : SEND_KEYWORD_QUESTION;
    }

}
