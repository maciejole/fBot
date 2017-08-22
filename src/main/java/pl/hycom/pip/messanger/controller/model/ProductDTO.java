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
package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rafal Lebioda on 24.04.2017.
 */

@Data
@NoArgsConstructor
public class ProductDTO {
    private Integer id;

    @Size(min = 1, max = 80)
    private String name;

    @Size(min = 1, max = 80)
    private String description;

    private String imageUrl;

    private Set<KeywordDTO> keywords = new HashSet<>();

    private  String keywordsHolder;

}
