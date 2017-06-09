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

package pl.hycom.pip.messanger.handler.processor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.service.KeywordService;

/**
 * Created by szale_000 on 2017-04-06.
 */
@Component
@Log4j2
public class ExtractKeywordsFromMessageProcessor implements PipelineProcessor {

    private static final String CHARS_TO_REMOVE_REGEX = "[{}\\[\\]()!@#$%^&*~'?\".,/+]";

    @Autowired
    KeywordService keywordService;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {
        log.info("Started keyword generating");

        String message = ctx.get(MESSAGE, String.class);
        Set<String> keywordsStrings = extractKeywords(message);
        log.info("Keywords extracted from message [{}]: {}", message, keywordsStrings);

        List<Keyword> keywords = convertStringsToKeywords(keywordsStrings);
        ctx.put(KEYWORDS, keywords);
        return 1;
    }

    Set<String> extractKeywords(String message) {
        return processKeywords(StringUtils.split(processMessage(message), " "));
    }

    protected String processMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return StringUtils.EMPTY;
        }

        String out = message;
        out = StringUtils.replaceAll(out, CHARS_TO_REMOVE_REGEX, StringUtils.EMPTY);
        out = StringUtils.lowerCase(out);

        return out;
    }

    protected Set<String> processKeywords(@NonNull String[] keywords) {
        return Arrays.stream(keywords)
                .filter(s -> StringUtils.length(s) > 2)
                .collect(Collectors.toSet());
    }

    private List<Keyword> convertStringsToKeywords(Set<String> keywords) {
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
