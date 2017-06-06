package pl.hycom.pip.messanger.handler.model;

import lombok.Data;
import pl.hycom.pip.messanger.repository.model.Keyword;

import java.util.List;

/**
 * Created by patry on 05/06/17.
 */
@Data
public class Payload {

    private List<Keyword> keywords;
    private List<Keyword> excludedKeywords;
    private Keyword keywordToBeAsked;

    public Payload(List<Keyword> keywords, List<Keyword> excludedKeywords, Keyword keywordToBeAsked) {
        this.keywords = keywords;
        this.excludedKeywords = excludedKeywords;
        this.keywordToBeAsked = keywordToBeAsked;
    }
}
