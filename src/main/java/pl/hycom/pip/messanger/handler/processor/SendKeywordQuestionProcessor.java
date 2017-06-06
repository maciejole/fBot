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

    @Autowired
    private MessengerSendClient sendClient;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        log.info("Started process of SendKeywordQuestionProcessor");
        StringBuilder messageBuilder = new StringBuilder();

        String id = ctx.get(SENDER_ID, String.class);
        Keyword keywordToBeAsked = ctx.get(KEYWORD_TO_BE_ASKED, Keyword.class);
        List<Keyword> keywords = ctx.get(KEYWORDS, List.class);
        List<Keyword> excludedKeywords = ctx.get(KEYWORDS_EXCLUDED, List.class);
        String message = messageBuilder.append("Znaleziono za duzo wynikow, czy jestes zainteresowany produktem ktory jest ").append(keywordToBeAsked.getWord()).toString();
        String payload = getPayload(keywords, excludedKeywords, keywordToBeAsked);
        List<QuickReply> quickReplies = getQuickReplies(payload);

        sendQuickReply(id, message, quickReplies);

        return 1;
    }

    private void sendQuickReply(String id, String message, List<QuickReply> quickReplies) {
        try {
            sendClient.sendTextMessage(id, message, quickReplies);
            logPayload(quickReplies);
        } catch (MessengerApiException | MessengerIOException e) {
            log.error(e.getMessage());
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
        Gson gson = new Gson();
        Payload payload = new Payload(keywords, excludedKeywords, keywordToBeAsked);
        return gson.toJson(payload);
    }
}
