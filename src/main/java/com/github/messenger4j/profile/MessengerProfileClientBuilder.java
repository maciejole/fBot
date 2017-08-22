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

import com.github.messenger4j.common.MessengerHttpClient;
import com.github.messenger4j.internal.PreConditions;

/**
 * Created by Rafal Lebioda on 18.03.2017.
 */
public final class MessengerProfileClientBuilder {

    final String pageAccessToken;
    MessengerHttpClient httpClient;

    public MessengerProfileClientBuilder(String pageAccessToken) {
        PreConditions.notNullOrBlank(pageAccessToken, "pageAccessToken");
        this.pageAccessToken = pageAccessToken;
    }

    public MessengerProfileClientBuilder httpClient(MessengerHttpClient messengerHttpClient) {
        this.httpClient = messengerHttpClient;
        return this;
    }

    public MessengerProfileClient build() {
        return new MessengerProfileClientImpl(this);
    }
}
