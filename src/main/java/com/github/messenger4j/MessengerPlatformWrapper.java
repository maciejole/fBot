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
package com.github.messenger4j;

import com.github.messenger4j.profile.MessengerProfileClientBuilder;
import com.github.messenger4j.receive.MessengerReceiveClientBuilder;
import com.github.messenger4j.send.MessengerSendClientBuilder;
import com.github.messenger4j.setup.MessengerSetupClientBuilder;
import com.github.messenger4j.user.UserProfileClientBuilder;

/**
 * Wrapper powstal w celu umozliwienia rozszerzalnosci klasy MessengerPlatform, ktora jest finalna.
 *
 * @author Hubert Pruszynski
 *
 */
public class MessengerPlatformWrapper {

    private MessengerPlatformWrapper() {
    }

    public static MessengerReceiveClientBuilder newReceiveClientBuilder(String appSecret, String verifyToken) {
        return MessengerPlatform.newReceiveClientBuilder(appSecret, verifyToken);
    }

    public static MessengerSendClientBuilder newSendClientBuilder(String pageAccessToken) {
        return MessengerPlatform.newSendClientBuilder(pageAccessToken);
    }

    public static MessengerSetupClientBuilder newSetupClientBuilder(String pageAccessToken) {
        return MessengerPlatform.newSetupClientBuilder(pageAccessToken);
    }

    public static UserProfileClientBuilder newUserProfileClientBuilder(String pageAccessToken) {
        return MessengerPlatform.newUserProfileClientBuilder(pageAccessToken);
    }

    public static MessengerProfileClientBuilder newProfileClientBuilder(String pageAccessToken) {
        return new MessengerProfileClientBuilder(pageAccessToken);
    }
}
