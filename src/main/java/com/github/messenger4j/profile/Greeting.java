/**
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
package com.github.messenger4j.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Rafal Lebioda on 18.03.2017.
 * Supported locales: https://developers.facebook.com/docs/messenger-platform/messenger-profile/supported-locales
 */
@Data
@NoArgsConstructor
public class Greeting {

    private String text;
    private String locale;

    public Greeting(String text) {
        locale = "default";
        this.text = text;
    }

    public Greeting(String text, String locale) {
        this.text = text;
        this.locale = locale;
    }
}
