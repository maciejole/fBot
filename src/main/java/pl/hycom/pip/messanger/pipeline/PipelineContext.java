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
package pl.hycom.pip.messanger.pipeline;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PipelineContext extends HashMap<String, Object> {

    private static final long serialVersionUID = 4022714046624372103L;

    public PipelineContext(Map<? extends String, ? extends Object> m) {
        super(m);
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            Object o = get(key);
            if (o != null) {
                return clazz.cast(o);
            }

        } catch (ClassCastException e) {
            log.error(e);
        }

        return null;
    }

}
