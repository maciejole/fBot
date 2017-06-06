package pl.hycom.pip.messanger.handler;

import com.github.messenger4j.receive.events.QuickReplyMessageEvent;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.h2.util.StringUtils;
import pl.hycom.pip.messanger.handler.model.Payload;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineManager;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by patry on 04/06/17.
 */

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PipelineQuickReplyHandler implements QuickReplyMessageEventHandler {

    private static final String PIPELINECHAIN_NAME = "processMessage";
    private final PipelineManager pipelineManager;

    private static final String YES_ANSWER = "tak";
    private static final String NO_ANSWER = "nie";

    @Override
    public void handle(QuickReplyMessageEvent event) {
        log.info("Received quick reply event");

        Map<String, Object> params = new HashMap<>();

        Gson gson = new Gson();
        String payloadString = event.getQuickReply().getPayload();
        Payload payload = gson.fromJson(payloadString, Payload.class);

        List<Keyword> keywords = payload.getKeywords();
        List<Keyword> keywordsExcluded = payload.getExcludedKeywords();
        Keyword keywordAsked = payload.getKeywordToBeAsked();

        String answer = event.getText();
        if (YES_ANSWER.equals(answer)) {
            keywords.add(keywordAsked);
        } else if (NO_ANSWER.equals(answer)) {
            keywordsExcluded.add(keywordAsked);
        }

        params.put(PipelineProcessor.SENDER_ID, event.getSender().getId());
        params.put(PipelineProcessor.MESSAGE, keywords.stream().map(Keyword::getWord).collect(Collectors.toList()).toString());
        params.put(PipelineProcessor.KEYWORDS_EXCLUDED, keywordsExcluded);

        try {
            pipelineManager.runProcess(PIPELINECHAIN_NAME, params);
        } catch (PipelineException e) {
            log.error(e);
        }
    }
}
