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

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.profile.MessengerProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pl.hycom.pip.messanger.controller.model.Greeting;
import pl.hycom.pip.messanger.controller.model.GreetingListWrapper;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class GreetingService implements InitializingBean {


    private static final String DEFAULT_LOCALE = "default";

    @Autowired
    private MessengerProfileClient profileClient;


    private Map<String, String> availableLocale = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try (Reader in = new InputStreamReader(getClass().getResourceAsStream("/messenger-locale.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {
                availableLocale.put(record.get(0), record.get(1));
            }
        }

        log.info("Locale loaded: " + availableLocale);
    }

    public Map<String, String> getAvailableLocale(List<com.github.messenger4j.profile.Greeting> greetings) {
        Map<String, String> locale = new TreeMap<>(availableLocale);

        greetings.stream().forEach(g -> locale.remove(g.getLocale()));

        return locale;
    }

    public boolean isValidLocale(String locale) {
        return availableLocale.containsKey(locale);
    }


    public List<com.github.messenger4j.profile.Greeting> getGreetingsWithDefaultLocale() {
        List<com.github.messenger4j.profile.Greeting> greetings = getGreetings();
        if (!greetings.isEmpty()) {
            if (!containsLocale(greetings, DEFAULT_LOCALE)) {
                greetings.add(new com.github.messenger4j.profile.Greeting(StringUtils.EMPTY, DEFAULT_LOCALE));
            }
        }

        return greetings;
    }

    private List<com.github.messenger4j.profile.Greeting> getGreetings() {
        try {
            return new ArrayList<>(profileClient.getWelcomeMessage().getGreetings());
        } catch (MessengerApiException | MessengerIOException e) {
            log.error("Error during getting greeting text from facebook", e);
            return Collections.emptyList();
        }
    }


    public void addGreetings(@Valid GreetingListWrapper greetingListWrapper) {
        try {
            profileClient.setupWelcomeMessages(greetingListWrapper.extractGreetings());
            log.info("Greeting text correctly updated");
        } catch (MessengerApiException | MessengerIOException e) {
            log.error("Error during changing greeting message", e);
        }

    }


    public void sortByLocale(List<com.github.messenger4j.profile.Greeting> greetings) {
        greetings.sort((g1, g2) -> StringUtils.compare(g1.getLocale(), g2.getLocale()));
    }

    private boolean containsLocale(List<com.github.messenger4j.profile.Greeting> greetings, String locale) {
        return greetings.stream().anyMatch(g -> StringUtils.equals(g.getLocale(), locale));
    }


    public void addGreeting(@Valid Greeting greeting) throws MessengerApiException, MessengerIOException {

        List<com.github.messenger4j.profile.Greeting> greetings = getGreetingsWithDefaultLocale();
        com.github.messenger4j.profile.Greeting profileGreeting = new com.github.messenger4j.profile.Greeting(greeting.getText(), greeting.getLocale());
        greetings.add(profileGreeting);
        profileClient.setupWelcomeMessages(greetings);
        log.info("Greeting text correctly updated");
    }


    public void removeGreeting(@PathVariable String locale) throws MessengerIOException, MessengerApiException {

        List<com.github.messenger4j.profile.Greeting> greetings = getGreetingsWithDefaultLocale();
        greetings.removeIf(g -> StringUtils.equals(g.getLocale(), locale));
        profileClient.removeWelcomeMessage();
        profileClient.setupWelcomeMessages(greetings);

    }
}

