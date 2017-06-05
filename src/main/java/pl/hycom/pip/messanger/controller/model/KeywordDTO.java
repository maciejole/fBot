package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * Created by Rafal Lebioda on 24.04.2017.
 */
@Data
@NoArgsConstructor
public class KeywordDTO {

    private Integer id;

    @Size(min = 1, max = 100, message = "{keyword.word.size}")
    private String word;

}
