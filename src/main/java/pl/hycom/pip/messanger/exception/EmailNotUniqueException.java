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
package pl.hycom.pip.messanger.exception;

/**
 * Created by Monia on 2017-05-27.
 */
public class EmailNotUniqueException extends Exception {

    private static final long serialVersionUID = 8779583852449852654L;

    public EmailNotUniqueException(Throwable cause) {
        super(cause);
    }

    public EmailNotUniqueException() {
        super();
    }

    public EmailNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

}
