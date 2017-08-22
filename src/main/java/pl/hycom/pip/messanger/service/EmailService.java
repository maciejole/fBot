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
package pl.hycom.pip.messanger.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Piotr on 21.05.2017.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
            log.info("Email sent.");
        } catch (Exception e) {
            log.error("Failed to send email.", e);
        }
    }

    /**
     * Created by Piotr on 31.05.2017.
     */
    @Data
    @AllArgsConstructor
    public static class Message {

        private String from;

        private List<String> to;

        private String subject;

        private String content;

        public SimpleMailMessage constructEmail() {
            String[] array = new String[to.size()];
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to.toArray(array));
            message.setSubject(subject);
            message.setText(content);
            return message;
        }

    }
}
