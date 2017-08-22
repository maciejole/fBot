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

import java.util.List;

/**
 * Created by Rafal Lebioda on 18.03.2017.
 */
@Data
final class ProfilePayload {

    private final String[] fields = new String[]{"greeting"}; // This is needed for deleting greetings. Do not remove unless u handle greetings
    private Greeting[] greeting;

    private ProfilePayload(ProfilePayload.Builder builder) {
        greeting = new Greeting[]{builder.greeting};
    }

    public ProfilePayload(List<Greeting> greetings) {
        this.greeting = greetings.toArray(new Greeting[]{});
    }

    static ProfilePayload.Builder newBuilder() {
        return new ProfilePayload.Builder();
    }

    static final class Builder {
        private Greeting greeting;

        public ProfilePayload.Builder greeting(Greeting greeting) {
            this.greeting = greeting;
            return this;
        }

        public ProfilePayload build() {
            return new ProfilePayload(this);
        }

    }

}
