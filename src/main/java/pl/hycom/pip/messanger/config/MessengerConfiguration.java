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

package pl.hycom.pip.messanger.config;

import com.github.messenger4j.MessengerPlatformWrapper;
import com.github.messenger4j.profile.MessengerProfileClient;
import com.github.messenger4j.receive.MessengerReceiveClient;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.setup.MessengerSetupClient;
import com.github.messenger4j.user.UserProfileClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.hycom.pip.messanger.handler.PipelineMessageHandler;
import pl.hycom.pip.messanger.handler.PipelineQuickReplyHandler;
import pl.hycom.pip.messanger.pipeline.PipelineManager;

/**
 * Created by patry on 07/03/2017.
 */

@Configuration
public class MessengerConfiguration {

    @Value("${messenger.pageAccessToken}")
    private String pageAccessToken;

    @Value("${messenger.appSecret}")
    private String appSecret;

    @Value("${messenger.verifyToken}")
    private String verifyToken;

    @Value("${messenger.pipeline.filepath}")
    private String pipelineFilepath;

    @Bean
    public PipelineManager pipelineManager() {
        return new PipelineManager(pipelineFilepath);
    }

    @Bean
    public PipelineMessageHandler pipelineMessageHandler() {
        return new PipelineMessageHandler(pipelineManager());
    }

    @Bean
    public PipelineQuickReplyHandler pipelineQuickReplyHandler() {
        return new PipelineQuickReplyHandler(pipelineManager());
    }

    @Bean
    public MessengerReceiveClient receiveClient() {
        return MessengerPlatformWrapper.newReceiveClientBuilder(appSecret, verifyToken)
                .onTextMessageEvent(pipelineMessageHandler())
                .onQuickReplyMessageEvent(pipelineQuickReplyHandler())
                .build();
    }

    @Bean
    public MessengerSendClient sendClient() {
        return MessengerPlatformWrapper.newSendClientBuilder(pageAccessToken).build();
    }

    @Bean
    public MessengerSetupClient setupClient() {
        return MessengerPlatformWrapper.newSetupClientBuilder(pageAccessToken).build();
    }

    @Bean
    public MessengerProfileClient profileClient() {
        return MessengerPlatformWrapper.newProfileClientBuilder(pageAccessToken).build();
    }

    @Bean
    public UserProfileClient userProfileClient() {
        return MessengerPlatformWrapper.newUserProfileClientBuilder(pageAccessToken).build();
    }

}
