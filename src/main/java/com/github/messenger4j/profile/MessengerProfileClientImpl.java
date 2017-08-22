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

import com.github.messenger4j.common.MessengerHttpClient.HttpMethod;
import com.github.messenger4j.common.MessengerRestClientAbstract;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static com.github.messenger4j.common.MessengerHttpClient.HttpMethod.*;

/**
 * Created by rafal on 18.03.2017.
 */
@Log4j2
final class MessengerProfileClientImpl extends MessengerRestClientAbstract<ProfilePayload, ProfileResponse> implements MessengerProfileClient {

    private static final String URL = "https://graph.facebook.com/v2.6/me/messenger_profile";
    private static final String GET_URL_PARAMETERS = "?fields=greeting&access_token=%s";
    private static final String POST_AND_DELETE_URL_PARAMETERS = "?access_token=%s";

    private final String pageAccessToken;

    private final String requestUrl;

    MessengerProfileClientImpl(MessengerProfileClientBuilder builder) {
        super(builder.httpClient);
        pageAccessToken = builder.pageAccessToken;
        this.requestUrl = URL;
        log.debug("{} initialized successfully.", MessengerProfileClientImpl.class.getSimpleName());
    }

    @Override
    public ProfileResponse setupWelcomeMessage(Greeting greeting) throws MessengerApiException, MessengerIOException {
        final ProfilePayload payload = ProfilePayload.newBuilder()
                .greeting(greeting)
                .build();
        String url = requestUrl + String.format(POST_AND_DELETE_URL_PARAMETERS, pageAccessToken);

        return sendPayload(POST, payload, url);
    }

    @Override
    public ProfileResponse setupWelcomeMessages(List<Greeting> greetingList) throws MessengerApiException, MessengerIOException {
        final ProfilePayload payload = new ProfilePayload(greetingList);
        String url = requestUrl + String.format(POST_AND_DELETE_URL_PARAMETERS, pageAccessToken);
        return sendPayload(POST, payload, url);
    }

    @Override
    public ProfileResponse removeWelcomeMessage() throws MessengerApiException, MessengerIOException {
        final ProfilePayload payload = ProfilePayload.newBuilder()
                .build();
        String url = requestUrl + String.format(POST_AND_DELETE_URL_PARAMETERS, pageAccessToken);

        return sendPayload(DELETE, payload, url);
    }

    @Override
    public GreetingsProfileResponse getWelcomeMessage() throws MessengerApiException, MessengerIOException {
        final ProfilePayload payload = ProfilePayload.newBuilder()
                .build();
        String url = requestUrl + String.format(GET_URL_PARAMETERS, pageAccessToken);

        return (GreetingsProfileResponse) sendPayload(GET, payload, url);
    }

    @Override
    protected ProfileResponse responseFromJson(JsonObject responseJsonObject) {

        try {
            if (responseJsonObject.has("data")) {
                return GreetingsProfileResponse.fromJson(responseJsonObject);
            } else {
                return ProfileResponse.fromJson(responseJsonObject);
            }
        } catch (MessengerIOException e) {
            log.error(e);
            return null;
        }
    }

    private ProfileResponse sendPayload(HttpMethod httpMethod, ProfilePayload payload, String url) throws MessengerApiException, MessengerIOException {
        return doRequest(httpMethod, url, payload);
    }
}
