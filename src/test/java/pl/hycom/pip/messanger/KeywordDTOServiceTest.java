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

package pl.hycom.pip.messanger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.KeywordRepository;
import pl.hycom.pip.messanger.service.KeywordService;

/**
 * Created by Piotr on 20.03.2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
@ActiveProfiles({ "dev", "testdb" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class KeywordDTOServiceTest {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private KeywordRepository repository;

    private Keyword keyword;
    private Keyword keyword1;
    private Keyword keyword2;

    /**
     * Preparation before each test.
     * Creates 2 keywords which are added to repository
     * and keyword which is used is some tests
     */
    @Before
    public void setUp() {

        keyword = new Keyword();
        keyword.setWord("test");

        keyword1 = new Keyword(); // always in repository before test
        keyword1.setWord("test1");

        keyword2 = new Keyword(); // always in repository before test
        keyword2.setWord("test2");

        repository.save(keyword1);
        repository.save(keyword2);
    }

    /**
     * Adds unique keyword to repository
     * 
     * @result KeywordDTO will be added without any error,
     *         number of keywords in repository is expected to be 3
     */
    @Test
    public void addUniqueKeywordTest() {

        // act
        keywordService.addKeyword(keyword);

        // assert
        assertEquals("number of keywords in repository is expected to be 3", 3, keywordService.findAllKeywords().size());
    }

    /**
     * Adds non-unique keyword to repository
     * 
     * @result KeywordDTO that has given word
     */
    @Test
    public void addNonUniqueKeywordTest() {
        // preparation
        Keyword keyword = new Keyword();
        keyword.setWord("test1");

        // act
        Keyword keywordResult = keywordService.addKeyword(keyword);

        // assert
        assertEquals(keyword.getWord(), keywordResult.getWord());
    }

    /**
     * Finds keyword using its specific id
     * 
     * @result Null will not be returned,
     *         returned will be "test"
     */
    @Test
    public void findKeywordByIdTest() {
        // act
        repository.save(keyword);

        // assert
        assertNotNull("Null will not be returned", keywordService.findKeywordById(keyword.getId()));
        assertEquals("Returned keyword will be 'test'", "test", keywordService.findKeywordById(keyword.getId()).getWord());
    }

    /**
     * Returns all keywords from repository
     * 
     * @result Size of returned list will be the same as
     *         the size of prepared list of keywords
     */
    @Test
    public void findAllKeyWordsTest() {

        // preparation
        List<KeywordDTO> keywords = new ArrayList<>();
        keywords.add(new KeywordDTO());
        keywords.add(new KeywordDTO());
        List<KeywordDTO> keywordsFromRepository;

        // act
        keywordsFromRepository = keywordService.findAllKeywords();

        // assert
        assertEquals("Size of returned list will be the same as the size of prepared list of keywords", keywords.size(), keywordsFromRepository.size());

    }

    /**
     * Updates existing keyword in repository
     * 
     * @result KeywordDTO specified by id will be updated by "newWord"
     */
    @Test
    public void updateKeywordTest() {

        // preparation
        String newWord = "newWord";

        // act
        keywordService.updateKeyword(keyword1.getId(), newWord);

        // assert
        assertEquals("KeywordDTO specified by id will be updated by \"newWord\"", newWord, repository.findOne(keyword1.getId()).getWord());
    }

    /**
     * Deletes keyword from repository
     * 
     * @result KeywordDTO added to repository will be deleted without error,
     *         size of repository keywords before adding keyword will be equal to 2,
     *         size of repository keywords after adding keyword will be equal to 3,
     *         size of repository keywords after deleting will be equal to
     *         to the size of repository keywords before adding keyword
     */
    @Test
    public void deleteKeywordTest() {

        // preparation
        long numberOfKeywordsBeforeAddingKeyword = repository.count();
        repository.save(keyword);
        long numberOfKeywordsAfterAddingKeyword = repository.count();

        // act
        keywordService.deleteKeyword(keyword.getId());
        long numberOfKeywordsAfterDeletingKeyword = repository.count();

        // assert
        assertEquals("size of repository keywords before adding keyword will be equal to 2", 2, numberOfKeywordsBeforeAddingKeyword);
        assertEquals("size of repository keywords after adding keyword will be equal to 3", 3, numberOfKeywordsAfterAddingKeyword);
        assertEquals("size of repository keywords after deleting will be equal to\n" +
                " to the size of repository keywords before adding keyword", numberOfKeywordsBeforeAddingKeyword, numberOfKeywordsAfterDeletingKeyword);
    }

    /**
     * Adds to repository keyword with Polish diacritic characters
     * 
     * @result Added keyword will keep Polish diacritic characters in repository
     */
    @Test
    public void checkIfCharsNoChange() {

        // preparation
        Keyword keyword = new Keyword();
        keyword.setWord("zażółć gęślą jaźń");

        // act
        repository.save(keyword);

        // assert
        assertThat("Added keyword will keep Polish diacritic characters in repository", repository.findOne(keyword.getId()).getWord(), is("zażółć gęślą jaźń"));
    }

    /**
     * Deletes all data from repository after each test
     */
    @After
    public void cleanUp() {
        repository.deleteAll();
    }

}
