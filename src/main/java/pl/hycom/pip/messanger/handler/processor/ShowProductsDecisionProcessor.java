package pl.hycom.pip.messanger.handler.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Product;

public class ShowProductsDecisionProcessor implements PipelineProcessor {

    private static final int SHOW_PRODUCTS = 1;
    private static final int FIND_KEYWORD_TO_ASK = 2;

    @Value("${messenger.recommendation.products-amount:3}")
    private Integer numberOfProducts;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {

        @SuppressWarnings("unchecked")
        List<Product> products = ctx.get(PRODUCTS, List.class);

        if (products.size() > numberOfProducts) {
            return FIND_KEYWORD_TO_ASK;
        } else {
            return SHOW_PRODUCTS;
        }
    }

}
