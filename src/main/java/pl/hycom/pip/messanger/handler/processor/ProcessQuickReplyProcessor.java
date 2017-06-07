package pl.hycom.pip.messanger.handler.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.handler.model.EventType;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;

/**
 * Created by patry on 07/06/17.
 */
@Component
@Log4j2
public class ProcessQuickReplyProcessor implements PipelineProcessor{

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        EventType eventType = ctx.get(EVENT_TYPE, EventType.class);
        if (eventType != EventType.quickReply) {
            return 1;
        }


        if ()

        return 0;
    }
}
