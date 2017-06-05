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

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.hycom.pip.messanger.controller.model.Greeting;
import pl.hycom.pip.messanger.service.GreetingService;

/**
 * Created by marcinus on 04.04.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
@ActiveProfiles({"dev", "testdb"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    public void isValid_pl_PL() throws Exception {
        Greeting greeting = new Greeting();
        greeting.setLocale("pl_PL");

        boolean valid = greetingService.isValidLocale(greeting.getLocale());

        Assertions.assertThat(valid).isTrue();
    }

    @Test
    public void isInvalid_pl() throws Exception {
        Greeting greeting = new Greeting();
        greeting.setLocale("pl");

        boolean valid = greetingService.isValidLocale(greeting.getLocale());

        Assertions.assertThat(valid).isFalse();
    }

    @Test
    public void isValid_en_US() throws Exception {
        Greeting greeting = new Greeting();
        greeting.setLocale("en_US");

        boolean valid = greetingService.isValidLocale(greeting.getLocale());

        Assertions.assertThat(valid).isTrue();
    }

    @Test
    public void isInvalid_random() throws Exception {
        Greeting greeting = new Greeting();
        greeting.setLocale("adadads");

        boolean valid = greetingService.isValidLocale(greeting.getLocale());

        Assertions.assertThat(valid).isFalse();
    }

    @Test
    public void isInvalid_random_random() throws Exception {
        Greeting greeting = new Greeting();
        greeting.setLocale("sdvkj_skdksdsf");

        boolean valid = greetingService.isValidLocale(greeting.getLocale());

        Assertions.assertThat(valid).isFalse();
    }

    @Test
    public void isInvalid_pl_random() throws Exception {
        Greeting greeting = new Greeting();
        greeting.setLocale("pl_skdksdsf");

        boolean valid = greetingService.isValidLocale(greeting.getLocale());

        Assertions.assertThat(valid).isFalse();
    }

    @Test
    public void isInvalid_random_PL() throws Exception {
        Greeting greeting = new Greeting();
        greeting.setLocale("sdvkj_PL");

        boolean valid = greetingService.isValidLocale(greeting.getLocale());

        Assertions.assertThat(valid).isFalse();
    }

//    @Test
//    public void removeDefaultLocale() throws Exception {
//        Greeting greeting = new Greeting();
//        greeting.setLocale("default");
//        GreetingService gs  = new GreetingService();
//
//        gs.removeGreeting(greeting.getLocale(), {
//        });
//        Assertions.assertThat(greeting.getLocale().isEmpty()==true);
//
//
//    }

}
