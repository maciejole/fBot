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

import static com.github.messenger4j.internal.JsonHelper.getPropertyAsString;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_RESULT;

import com.github.messenger4j.exceptions.MessengerIOException;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by Rafal Lebioda on 18.03.2017.
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ProfileResponse {

    private final String result;

    public static ProfileResponse fromJson(JsonObject jsonObject) throws MessengerIOException {
        final String result = getPropertyAsString(jsonObject, PROP_RESULT);
        return new ProfileResponse(result);
    }

}
