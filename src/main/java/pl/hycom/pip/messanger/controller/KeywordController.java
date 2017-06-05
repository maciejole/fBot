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

package pl.hycom.pip.messanger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.repository.model.Keyword;
import pl.hycom.pip.messanger.service.KeywordService;
import pl.hycom.pip.messanger.service.ProductService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class KeywordController {

    private static final String KEYWORDS_VIEW = "keywords";

    private final KeywordService keywordService;
    private final ProductService productService;
    private MessageSource messageSource;

    @GetMapping("/admin/keywords")
    public String showKeywords(Model model) {
        prepareModel(model, new KeywordDTO());
        return KEYWORDS_VIEW;
    }

    @PostMapping("/admin/keywords")
    public String addOrUpdateKeyword(@Valid KeywordDTO keyword, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            prepareModel(model, keyword);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            log.info("Validation keyword error");

            return KEYWORDS_VIEW;
        }

        if (keywordService.isAnyKeywordWithWord(keyword.getWord())) {
            prepareModel(model, keyword);
            model.addAttribute("error", new ObjectError("keywordExists", "Słowo kluczowe już istnieje."));

            return KEYWORDS_VIEW;
        }

        keywordService.addOrUpdateKeyword(keyword);

        return "redirect:/admin/keywords";
    }

    @DeleteMapping("/admin/keywords/{keywordId}/delete")
    public String deleteKeyword(@PathVariable("keywordId") final Integer id, Model model) {

        KeywordDTO keywordToDelete = keywordService.findKeywordById(id);
        if (!productService.isAnyProductContainingAtLeastOneKeyword(Arrays.asList(keywordToDelete))) {
            keywordService.deleteKeyword(id);
            log.info("KeywordDTO[" + id + "] deleted !!!");
        } else {
            prepareModel(model, new KeywordDTO());
            ObjectError error = new ObjectError("keywordInUsage", "Słowo kluczowe jest przypisane do produktu.");
            model.addAttribute("error", error);
            log.info("cannot delete keyword = " + keywordToDelete);
            return KEYWORDS_VIEW;
        }

        return "redirect:/admin/keywords";
    }

    private void prepareModel(Model model, KeywordDTO keyword) {
        List<KeywordDTO> allKeywords = keywordService.findAllKeywords();
        model.addAttribute("keywords", allKeywords);
        model.addAttribute("keywordForm", keyword);
    }

}
