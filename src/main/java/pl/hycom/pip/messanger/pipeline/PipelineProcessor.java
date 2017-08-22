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
package pl.hycom.pip.messanger.pipeline;

import lombok.NonNull;

public interface PipelineProcessor {

    String SENDER_ID = "senderId";
    String MESSAGE = "message";
    String PRODUCTS = "products";
    String KEYWORDS = "keywords";
    String KEYWORDS_FOUND = "keywordsFound";
    String KEYWORD_TO_BE_ASKED = "keywordToBeAsked";
    String KEYWORDS_EXCLUDED = "keywordsExcluded";
    String ANSWER = "answer";
    String EVENT_TYPE = "eventType";
    String PAYLOAD = "payload";

    int runProcess(@NonNull PipelineContext ctx) throws PipelineException;
}
