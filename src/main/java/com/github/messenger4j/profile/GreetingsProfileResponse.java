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

import com.github.messenger4j.exceptions.MessengerIOException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rafal Lebioda on 18.03.2017.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class GreetingsProfileResponse extends ProfileResponse {

    private final List<Greeting> greetings;

    public GreetingsProfileResponse(String result) {
        super(result);
        greetings = Collections.emptyList();
    }

    public GreetingsProfileResponse(String result, Greeting[] greetingsArray) {
        super(result);
        greetings = Arrays.asList(greetingsArray);
    }

    public static GreetingsProfileResponse fromJson(JsonObject jsonObject) throws MessengerIOException {
        JsonArray dataArray = getArray(jsonObject, "data");
        if (isJsonArrayEmpty(dataArray)) {
            return new GreetingsProfileResponse(jsonObject.toString());
        }

        JsonArray greetingsJsonArray = getArray(dataArray.get(0).getAsJsonObject(), "greeting");
        if (isJsonArrayEmpty(greetingsJsonArray)) {
            return new GreetingsProfileResponse(jsonObject.toString());
        }

        return new GreetingsProfileResponse(jsonObject.toString(), new Gson().fromJson(greetingsJsonArray, Greeting[].class));
    }

    private static JsonArray getArray(JsonObject jsonObject, String label) {
        return jsonObject.getAsJsonArray(label);
    }

    private static boolean isJsonArrayEmpty(JsonArray jsonArray) {
        return jsonArray == null || jsonArray.size() == 0;
    }

}
