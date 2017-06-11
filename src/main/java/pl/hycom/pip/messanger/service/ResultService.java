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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.controller.model.ProductDTO;
import pl.hycom.pip.messanger.controller.model.ResultDTO;
import pl.hycom.pip.messanger.repository.KeywordRepository;
import pl.hycom.pip.messanger.repository.ProductRepository;
import pl.hycom.pip.messanger.repository.ResultRepository;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.repository.model.Product;
import pl.hycom.pip.messanger.repository.model.RKP;
import pl.hycom.pip.messanger.repository.model.Result;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class ResultService {

    @Autowired
    private MapperFacade orikaMapper;

    @Autowired
    private final ResultRepository resultRepository;

    @Autowired
    private final KeywordRepository keywordRepository;

    @Autowired
    private final ProductRepository productRepository;


    public List<ResultDTO> findAllResults() {
        log.info("Searching all results");
        return orikaMapper.mapAsList(StreamSupport.stream(resultRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()), ResultDTO.class);

    }

    public void addResult(Result result) {
        log.info("Adding result" + result);
        resultRepository.save(result);
    }

    public void removeAll() {
        log.info("Deleting all results from database");
        resultRepository.deleteAll();
    }

    public List<RKP> matchKeywords() {
        List<RKP> resultList = new ArrayList<>();
        Keyword tempKeyword = new Keyword();
        for (Result res : resultRepository.findAll()) {
            tempKeyword = keywordRepository.findByWordIgnoreCase(res.getResult());
            if (tempKeyword != null) {
                RKP tempRKP = new RKP(res, tempKeyword);
                resultList.add(tempRKP);
            } else {
                resultList.add(new RKP(res, new Keyword("brak")));
            }
        }
        return resultList;

    }

    public List<RKP> matchProducts() {
        List<RKP> resultKeywordProduct = new ArrayList<>();
        Product tempProduct = new Product();
        for (RKP rkp : matchKeywords()) {
            if ((rkp.getKeyword().getWord()) != "brak") {
                tempProduct = productRepository.findProductsWithKeyword(rkp.getKeyword()).get(0);
                rkp.setProduct(tempProduct);
                resultKeywordProduct.add(rkp);
            }
            else {
                resultKeywordProduct.add(rkp);
            }

        }
        return resultKeywordProduct;
    }


}
