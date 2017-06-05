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

package pl.hycom.pip.messanger.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.KeywordRepository;

/**
 * Created by patry on 18/03/2017.
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class KeywordService {
    @Autowired
    private MapperFacade orikaMapper;

    private final KeywordRepository keywordRepository;

    public Keyword addKeyword(Keyword keyword) {
        log.info("addKeyword method from KeywordService class invoked");
        Keyword keywordByWord = findKeywordByWord(keyword.getWord());
        if (keywordByWord != null) {
            log.info("addKeyword: KeywordDTO already exists !!");
            return keywordByWord;
        }
        return keywordRepository.save(keyword);
    }

    public KeywordDTO findKeywordById(Integer id) {
        log.info("findKeywordById method from KeywordService class invoked");
        return orikaMapper.map(keywordRepository.findOne(id),KeywordDTO.class);
    }

    public List<KeywordDTO> findAllKeywords() {
        log.info("findAllKeywords method from KeywordService class invoked");
        return orikaMapper.mapAsList(StreamSupport.stream(keywordRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()),KeywordDTO.class);
    }

    public boolean deleteKeyword(Integer id) {
        log.info("deleteKeyword method from KeywordService class invoked");
        if(!keywordRepository.exists(id)){
            return Boolean.FALSE;
        }
        keywordRepository.delete(id);
        return Boolean.TRUE;
    }

    public void updateKeyword(Integer id, String newWord) {
        log.info("updateKeyword method from KeywordService class invoked");
        Keyword keyword = keywordRepository.findOne(id);
        if (findKeywordByWord(newWord) == null) {
            keyword.setWord(newWord);
            log.info("keyword update");

            keywordRepository.save(keyword);
        }
    }

    public Keyword updateKeyword(Keyword updatedKeyword) {
        Keyword keyword = keywordRepository.findOne(updatedKeyword.getId());
        if (findKeywordByWord(updatedKeyword.getWord()) == null) {
            keyword.setWord(updatedKeyword.getWord());
            log.info("Updating keyword " + updatedKeyword);

            return keywordRepository.save(keyword);
        }

        log.info("updateKeyword: cannot update " + updatedKeyword + " with given word");
        return keyword;
    }

    public void deleteAllKeywords() {
        keywordRepository.deleteAll();
    }

    public List<String> findKeywordsBySearchTerm(String searchTerm) {
        log.info("findKeywordsBySearchTerm method from KeywordService invoked");
        return keywordRepository.findByWordIgnoreCaseStartingWith(searchTerm).stream().map(Keyword::getWord).collect(Collectors.toList());
    }

    public Keyword findKeywordByWord(String word) {
        log.info("findKeywordsByWord method from KeywordService invoked");
        return keywordRepository.findByWordIgnoreCase(word);
    }

    public boolean isAnyKeywordWithWord(String word){
        log.info("isAnyKeywordWithSpecificWord method from KeywordService invoked");
        return keywordRepository.findByWordIgnoreCase(word)!= null;
    }

    public void addOrUpdateKeyword(KeywordDTO keyword) {
        Keyword entityKeyword=orikaMapper.map(keyword,Keyword.class);
       if (keyword.getId() != null && keyword.getId() != 0) {
            updateKeyword(entityKeyword);
       } else {
            addKeyword(entityKeyword);
       }
    }
}
