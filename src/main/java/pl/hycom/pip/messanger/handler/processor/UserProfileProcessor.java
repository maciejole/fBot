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

package pl.hycom.pip.messanger.handler.processor;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.user.UserProfile;
import com.github.messenger4j.user.UserProfileClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.hycom.pip.messanger.pipeline.PipelineContext;
import pl.hycom.pip.messanger.pipeline.PipelineException;
import pl.hycom.pip.messanger.pipeline.PipelineProcessor;

@Component
@Log4j2
public class UserProfileProcessor implements PipelineProcessor {

    public static final String LOCALE = "user.locale";
    public static final String GENDER = "user.gender";
    public static final String FIRST_NAME = "user.firstName";
    public static final String LAST_NAME = "user.lastName";

    @Autowired
    private UserProfileClient userProfileClient;

    @Override
    public int runProcess(PipelineContext ctx) throws PipelineException {

        log.info("Started process of UserProfileProcessor");

        try {
            UserProfile userProfile = userProfileClient.queryUserProfile(ctx.get(SENDER_ID, String.class));

            ctx.put(LOCALE, userProfile.getLocale());
            ctx.put(GENDER, userProfile.getGender());
            ctx.put(FIRST_NAME, userProfile.getFirstName());
            ctx.put(LAST_NAME, userProfile.getLastName());

        } catch (MessengerApiException | MessengerIOException e) {
            log.error(e);
        }

        return 1;
    }

}
