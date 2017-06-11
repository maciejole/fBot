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

package pl.hycom.pip.messanger.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;

import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.controller.model.Greeting;
import pl.hycom.pip.messanger.controller.model.GreetingListWrapper;
import pl.hycom.pip.messanger.service.GreetingService;

/**
 * Created by piotr on 12.03.2017.
 */

@Log4j2
@Controller
public class GreetingController {

    protected static final String VIEW_GREETINGS = "greetings";
    private static final String ADMIN_GREETINGS = "/admin/greetings";
    protected static final String REDIRECT_ADMIN_GREETINGS = "redirect:" + ADMIN_GREETINGS;

    private static final String DEFAULT_LOCALE = "default";

    @Autowired
    private GreetingService greetingService;

    @Autowired
    private MessageSource bundleMessageSource;

    @GetMapping("/admin/greetings")
    public String getGreetings(Model model) {
        prepareModel(model);
        return VIEW_GREETINGS;
    }

    @PostMapping("/admin/greetings")
    public String addGreetings(@Valid GreetingListWrapper greetingListWrapper) {
        greetingService.addGreetings(greetingListWrapper);
        return REDIRECT_ADMIN_GREETINGS;
    }

    @PostMapping("/admin/greeting")
    public String addGreeting(@Valid Greeting greeting, BindingResult bindingResult, Model model) {
        boolean isAdditionSuccessfull = true;

        if (!greetingService.isValidLocale(greeting.getLocale())) {
            log.error("Not supported locale[" + greeting.getLocale() + "]");
            addError(bindingResult, "greeting.locale.empty");
        }

        if (bindingResult.hasErrors()) {
            prepareModel(model, greeting);
            log.error("Greeting validation errors: " + bindingResult.getAllErrors());
            return VIEW_GREETINGS;

        }
        try {
            greetingService.addGreeting(greeting);
            return "redirect:" + ADMIN_GREETINGS + "?success=" + isAdditionSuccessfull;

        } catch (MessengerApiException | MessengerIOException e) {
            isAdditionSuccessfull = false;
            log.error("Error during changing greeting message", e);
            addError(bindingResult, "unexpectedError");
            prepareModel(model, greeting);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "redirect:" + ADMIN_GREETINGS + "?success=" + isAdditionSuccessfull;
        }

    }

    @DeleteMapping("/admin/deleteGreeting/{locale}")
    public String removeGreeting(@PathVariable String locale, Model model) {
        if (StringUtils.equals(locale, DEFAULT_LOCALE)) {
            prepareModel(model);
            String message = getMessage("greetings.invalidOperation");
            model.addAttribute("errors", Collections.singletonList(message));
            return VIEW_GREETINGS;
        }

        try {
            greetingService.removeGreeting(locale);
            log.info("Deleting greeting succeeded");
        } catch (MessengerApiException | MessengerIOException e) {
            log.info("Deleting greeting failed", e);
        }

        return VIEW_GREETINGS;
    }

    private String getMessage(String messageCode, Object... args) {
        return bundleMessageSource.getMessage(messageCode, args, "Unsupported operation", LocaleContextHolder.getLocale());
    }

    public void prepareModel(Model model) {
        prepareModel(model, new Greeting());
    }

    private void prepareModel(Model model, Greeting greeting) {
        List<com.github.messenger4j.profile.Greeting> greetings = greetingService.getGreetingsWithDefaultLocale();
        greetingService.sortByLocale(greetings);
        GreetingListWrapper greetingListWrapper = new GreetingListWrapper(greetings);

        model.addAttribute("greetingListWrapper", greetingListWrapper);
        model.addAttribute("availableLocale", greetingService.getAvailableLocale(greetings));
        model.addAttribute("greeting", greeting);
    }

    private void addError(BindingResult bindingResult, String messageCode, Object... args) {
        addError(bindingResult, "all", messageCode, args);
    }

    private void addError(BindingResult bindingResult, String objectName, String messageCode, Object... args) {
        bindingResult.addError(new ObjectError(objectName, getMessage(messageCode, args)));
    }

}
