package pl.hycom.pip.messanger.handler.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.hycom.pip.messanger.repository.model.Keyword;

/**
 * Created by patry on 05/06/17.
 */
@Data
@AllArgsConstructor
public class Payload {

    private List<Keyword> keywords;
    private List<Keyword> excludedKeywords;
    private Keyword keywordToBeAsked;

}
