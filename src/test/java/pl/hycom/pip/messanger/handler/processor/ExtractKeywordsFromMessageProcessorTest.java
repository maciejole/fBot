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

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.hycom.pip.messanger.repository.model.Keyword;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by szale_000 on 2017-04-06.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ExtractKeywordsFromMessageProcessorTest {

    ExtractKeywordsFromMessageProcessor sut = new ExtractKeywordsFromMessageProcessor();

    @Test
    public void processMessageShouldRemoveUnwantedSymbols() throws Exception {
        // given
        String message = "te{}[]()!@#$%s^&*~'?\".,/+t";
        // when
        String refactoredMessage = sut.processMessage(message);
        // then
        Assertions.assertThat(refactoredMessage).isEqualTo("test");
    }

    @Test
    public void processMessageShouldMakeLowerCase() throws Exception {
        // given
        String message = "rANdoM TESt";
        // when
        String refactoredMessage = sut.processMessage(message);
        // then
        Assertions.assertThat(refactoredMessage).isEqualTo("random test");
    }

    @Test
    public void processMessageShouldReturnEmptyIfGivenNull() throws Exception {
        // given
        String message = null;
        // when
        String refactoredMessage = sut.processMessage(message);
        // then
        Assertions.assertThat(refactoredMessage).isEqualTo("");
    }

    @Test
    public void processMessageShouldNotRemoveDash() throws Exception {
        // given
        String message = "auto-pilot";
        // when
        String refactoredMessage = sut.processMessage(message);
        // then
        Assertions.assertThat(refactoredMessage).isEqualTo("auto-pilot");
    }

    @Test
    public void processKeywordsShouldRemoveSmallWords() throws Exception {
        // given
        String[] keywords = StringUtils.split("A bb ccc dddd eeeee f ggg", " ");
        // when
        Set<String> processKeywords = sut.processKeywords(keywords);
        // then
        Assertions.assertThat(processKeywords).containsOnly("ccc", "dddd", "eeeee", "ggg");
    }

    @Test
    public void processKeywordsShouldRemoveDuplicates() throws Exception {
        // given
        String[] keywords = StringUtils.split("aaa aaa bbb bbb ccc ccc", " ");
        // when
        Set<String> processKeywords = sut.processKeywords(keywords);
        // then
        Assertions.assertThat(processKeywords).containsOnlyOnce("aaa", "bbb", "ccc");
    }

    @Test
    public void processKeywordsShouldIgnoreMultipleSpaces() throws Exception {
        // given
        String[] keywords = StringUtils.split("aaa     bbb", " ");
        // when
        Set<String> processKeywords = sut.processKeywords(keywords);
        // then
        Assertions.assertThat(processKeywords).containsOnly("aaa", "bbb");
    }

    @Test
    public void extractKeywordsTest() throws Exception {
        // given
        String message = "Ala m4 kota, a kot ma ale(?), nie mam pomysłu na stringa wcale!!";
        // when
        Set<String> processKeywords = sut.extractKeywords(message);
        // then
        Assertions.assertThat(processKeywords).containsOnlyOnce("kota", "pomysłu", "stringa", "wcale");
    }

    @Test
    public void extractKeywordsShouldReturnEmptyListGivenNull() throws Exception {
        // given
        String message = null;
        // when
        Set<String> processKeywords = sut.extractKeywords(message);
        // then
        Assertions.assertThat(processKeywords).isEmpty();
    }

    @Test
    public void testArray() {
        // given
        List<Keyword> arrayList = Arrays.asList(new Keyword("test1"), new Keyword("test2"), new Keyword("test3"));
        String message = arrayList.stream().map(Keyword::getWord).collect(Collectors.toList()).toString();
        // when
        Set<String> processKeywords = sut.extractKeywords(message);
        //then
        Assertions.assertThat(processKeywords).hasSize(3)
                .containsExactlyInAnyOrder("test1", "test2", "test3");
    }

}
