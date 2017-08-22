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

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.QuickReply;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.handler.model.Payload;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;

import java.util.List;

/**
 * Created by szale_000 on 2017-05-28.
 */
@Component
@Log4j2
public class SendKeywordQuestionProcessor implements PipelineProcessor {

    private static final Gson GSON = new Gson();

    @Autowired
    private MessengerSendClient sendClient;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        log.info("Started process of SendKeywordQuestionProcessor");

        Keyword keywordToBeAsked = ctx.get(KEYWORD_TO_BE_ASKED, Keyword.class);
        List<Keyword> keywords = ctx.get(KEYWORDS, List.class);
        List<Keyword> excludedKeywords = ctx.get(KEYWORDS_EXCLUDED, List.class);
        String payload = getPayload(keywords, excludedKeywords, keywordToBeAsked);

        String message = "Znaleziono za dużo wyników, czy jesteś zainteresowany produktem który jest " + keywordToBeAsked.getWord() + "?";

        sendQuickReply(ctx.get(SENDER_ID, String.class), message, getQuickReplies(payload));

        return 1;
    }

    private void sendQuickReply(String id, String message, List<QuickReply> quickReplies) {
        try {
            sendClient.sendTextMessage(id, message, quickReplies);
            logPayload(quickReplies);

        } catch (MessengerApiException | MessengerIOException e) {
            log.error("Error sending quick reply", e);
        }
    }

    private void logPayload(List<QuickReply> quickReplies) {
        quickReplies.forEach(quickReply -> log.info(quickReply.getTitle() + ":" + quickReply.getPayload()));
    }

    private List<QuickReply> getQuickReplies(String payload) {
        return QuickReply.newListBuilder()
                .addTextQuickReply("tak", payload).toList()
                .addTextQuickReply("nie", payload).toList()
                .build();
    }

    private String getPayload(List<Keyword> keywords, List<Keyword> excludedKeywords, Keyword keywordToBeAsked) {
        return GSON.toJson(new Payload(keywords, excludedKeywords, keywordToBeAsked));
    }
}
