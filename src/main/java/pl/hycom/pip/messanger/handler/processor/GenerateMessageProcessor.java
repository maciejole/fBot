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

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.github.messenger4j.send.templates.Template;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Product;

import java.util.List;

@Component
@Log4j2
public class GenerateMessageProcessor implements PipelineProcessor {

    @Autowired
    private MessengerSendClient sendClient;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {

        log.info("Started process of GenerateMessageProcessor");

        @SuppressWarnings("unchecked")
        List<Product> products = ctx.get(PRODUCTS, List.class);
        String senderId = ctx.get(SENDER_ID, String.class);

        if (CollectionUtils.isEmpty(products)) {
            sendTextMessage(senderId, "No products found.");
            return 1;
        }

        sendStructuredMessage(senderId, getStructuredMessage(products));
        return 1;
    }

    private void sendStructuredMessage(String id, Template template) {
        try {
            sendClient.sendTemplate(id, template);
        } catch (MessengerApiException | MessengerIOException e) {
            log.error("Error during sending structured answer", e);
        }
    }

    private void sendTextMessage(String id, String message) {
        try {
            sendClient.sendTextMessage(id, message);
        } catch (MessengerApiException | MessengerIOException e) {
            log.error("Error during sending answer", e);
        }
    }

    private GenericTemplate getStructuredMessage(List<Product> products) {
        GenericTemplate.Element.ListBuilder listBuilder = GenericTemplate.newBuilder().addElements();
        for (Product product : products) {
            listBuilder
                    .addElement(product.getName())
                    .subtitle(product.getDescription())
                    .imageUrl(product.getImageUrl())
                    .toList();
        }
        return listBuilder.done().build();
    }
}
