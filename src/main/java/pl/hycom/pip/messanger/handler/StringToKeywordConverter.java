package pl.hycom.pip.messanger.handler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.service.KeywordService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by szale_000 on 2017-05-28.
 */
@Component
@Log4j2
public class StringToKeywordConverter {

    @Autowired
    KeywordService keywordService;

    public List<Keyword> convertStringsToKeywords(Set<String> keywords) {
        if (CollectionUtils.isEmpty(keywords)) {
            return Collections.emptyList();
        }

        // This stream maps set of strings into list of keywords
        return keywords.stream()
                .map(s -> keywordService.findKeywordByWord(s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
