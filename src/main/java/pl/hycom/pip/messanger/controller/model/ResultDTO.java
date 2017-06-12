package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Monia on 2017-05-27.
 */
@Data
@NoArgsConstructor
public class ResultDTO {
    private Integer id;
    private String base;
    private String result;


    public ResultDTO(String base, String result) {
        this.base = base;
        this.result = result;
    }


}
