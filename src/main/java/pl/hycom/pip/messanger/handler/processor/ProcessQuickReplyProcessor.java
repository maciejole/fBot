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

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.handler.model.EventType;
import pl.hycom.pip.messanger.handler.model.Payload;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;

import java.util.List;

/**
 * Created by patry on 07/06/17.
 */
@Component
@Log4j2
public class ProcessQuickReplyProcessor implements PipelineProcessor{

    private static final String YES_ANSWER = "tak";
    private static final String NO_ANSWER = "nie";

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        EventType eventType = ctx.get(EVENT_TYPE, EventType.class);
        if (eventType != EventType.QUICK_REPLY) {
            return 1;
        }

        log.info("Started process of ProcessQuickReplyProcessor");

        String answer = ctx.get(ANSWER, String.class);
        String payloadString = ctx.get(PAYLOAD, String.class);
        Payload payload = getPayloadFromString(payloadString);
        List<Keyword> keywords = payload.getKeywords();
        List<Keyword> excludedKeywords = payload.getExcludedKeywords();
        Keyword keywordAsked = payload.getKeywordToBeAsked();

        String debugMsg = "keywords: " + keywords + "\nexcludedKeywords: " + excludedKeywords
                + "\nkeywordAsked: " + keywordAsked;
        log.debug(debugMsg);

        if (YES_ANSWER.equals(answer)) {
            keywords.add(keywordAsked);
        } else {
            excludedKeywords.add(keywordAsked);
        }
        ctx.put(KEYWORDS, keywords);
        ctx.put(KEYWORDS_EXCLUDED, excludedKeywords);

        return 1;
    }

    private Payload getPayloadFromString(String payloadString) {
        Gson gson = new Gson();
        return gson.fromJson(payloadString, Payload.class);
    }
}
