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
package pl.hycom.pip.messanger.handler;

import com.github.messenger4j.receive.events.Event;
import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.events.TextMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.handler.model.EventType;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineManager;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public abstract class PipelineMessageHandler {

    private static final String PIPELINECHAIN_NAME = "processMessage";
    private final PipelineManager pipelineManager;

    protected void handleEvent(Event msg) {
        log.info("Received message - starting prepare answer");
        Map<String, Object> params = new HashMap<>();
        params.put(PipelineProcessor.SENDER_ID, msg.getSender().getId());
        params.put(PipelineProcessor.KEYWORDS_EXCLUDED, new LinkedList<Keyword>());
        if (msg instanceof TextMessageEvent) {
            params.put(PipelineProcessor.MESSAGE, ((TextMessageEvent) msg).getText());
            params.put(PipelineProcessor.EVENT_TYPE, EventType.message);
        } else if (msg instanceof QuickReplyMessageEvent) {
            params.put(PipelineProcessor.ANSWER, ((QuickReplyMessageEvent)msg).getText());
            params.put(PipelineProcessor.EVENT_TYPE, EventType.quickReply);
            params.put(PipelineProcessor.PAYLOAD, ((QuickReplyMessageEvent)msg).getQuickReply().getPayload());
        }
        try {
            pipelineManager.runProcess(PIPELINECHAIN_NAME, params);
        } catch (PipelineException e) {
            log.error(e);
        }
    }
    public static class PipelineTextMessageEventHandler extends PipelineMessageHandler implements TextMessageEventHandler {
        public PipelineTextMessageEventHandler(PipelineManager pipelineManager) {
            super(pipelineManager);
        }
        @Override
        public void handle(TextMessageEvent e) {
            handleEvent(e);
        }
    }
    public static class PipelineQuickReplyMessageEventHandler extends PipelineMessageHandler implements QuickReplyMessageEventHandler {
        public PipelineQuickReplyMessageEventHandler(PipelineManager pipelineManager) {
            super(pipelineManager);
        }
        @Override
        public void handle(QuickReplyMessageEvent e) {
            handleEvent(e);
        }
    }
}