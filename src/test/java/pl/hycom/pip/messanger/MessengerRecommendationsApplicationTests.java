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
package pl.hycom.pip.messanger;

import com.github.messenger4j.common.MessengerHttpClient;
import com.github.messenger4j.receive.MessengerReceiveClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.hycom.pip.messanger.config.MessengerConfigurationTest;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"dev"})
@ContextConfiguration(classes = { MessengerConfigurationTest.class })
public class MessengerRecommendationsApplicationTests {

    @Autowired
    private MessengerReceiveClient messengerReceiveClient;
    @Autowired
    private MessengerHttpClient mockHttpClient;

    @Test
    public void shouldHandleHelloWorldMessageEvent() throws Exception {
        // given
        final String payload = "{\n" +
                "    \"object\": \"page\",\n" +
                "    \"entry\": [{\n" +
                "        \"id\": \"PAGE_ID\",\n" +
                "        \"time\": 1458692752478,\n" +
                "        \"messaging\": [{\n" +
                "            \"sender\": {\n" +
                "                \"id\": \"USER_ID\"\n" +
                "            },\n" +
                "            \"recipient\": {\n" +
                "                \"id\": \"PAGE_ID\"\n" +
                "            },\n" +
                "            \"timestamp\": 1458692752478,\n" +
                "            \"message\": {\n" +
                "                \"mid\": \"mid.1457764197618:41d102a3e1ae206a38\",\n" +
                "                \"text\": \"hello, text message world!\"\n" +
                "            }\n" +
                "        }]\n" +
                "    }]\n" +
                "}";

        messengerReceiveClient.processCallbackPayload(payload);

        // then
        // TODO: dopisac test jak bedzie dzialac juz pobieranie produktow na podstawie tekstu uzytkownika
        // final String expectedJsonBody = "{\"recipient\":{\"id\":\"USER_ID\"}," + "\"message\":{\"text\":\"Hello World\"}}";
        // verify(mockHttpClient).execute(eq(POST), endsWith("X"), eq(expectedJsonBody));

    }

}
