package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Monia on 2017-05-27.
 */
@Data
@NoArgsConstructor
public class ResultDTO {
    private Integer id;
    private String base;
    private String result;
    private String keyword;

    public ResultDTO(String base, String result) {
        this.base = base;
        this.result = result;
    }


}
