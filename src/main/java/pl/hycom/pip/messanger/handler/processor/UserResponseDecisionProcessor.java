package pl.hycom.pip.messanger.handler.processor;

import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;

public class UserResponseDecisionProcessor implements PipelineProcessor {

    public static final int SEND_KEYWORD_QUESTION = 1;
    public static final int SEND_PRODUCTS_MESSAGE = 2;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        return ctx.get(KEYWORD_TO_BE_ASKED, Keyword.class) == null ? SEND_PRODUCTS_MESSAGE : SEND_KEYWORD_QUESTION;
    }

}
