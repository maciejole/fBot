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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcinus on 25.03.17.
 * This wrapper is needed for ThymeLeaf's foreach
 * see greetings.html file
 */
@Data
@NoArgsConstructor
public class GreetingListWrapper {
    private List<Greeting> greetings = new ArrayList<>();

    public GreetingListWrapper(List<com.github.messenger4j.profile.Greeting> profileGreetingsList) {
        for (com.github.messenger4j.profile.Greeting greeting : profileGreetingsList) {
            greetings.add(new Greeting(greeting));
        }
    }

    public List<com.github.messenger4j.profile.Greeting> extractGreetings() {
        List<com.github.messenger4j.profile.Greeting> out = new ArrayList<>();
        for (Greeting greeting : greetings) {
            out.add(new com.github.messenger4j.profile.Greeting(greeting.getText(), greeting.getLocale()));
        }
        return out;
    }
}
