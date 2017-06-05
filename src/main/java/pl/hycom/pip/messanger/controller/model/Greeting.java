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

package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * Created by rafal on 13.03.2017.
 */
@Data
@NoArgsConstructor
public class Greeting {

    @Size(min = 2, max = 160, message = "{greeting.text.size}")
    private String text;
    private String locale = "";

    public Greeting(com.github.messenger4j.profile.Greeting profileGreeting) {
        setText(profileGreeting.getText());
        setLocale(profileGreeting.getLocale());
    }
}
