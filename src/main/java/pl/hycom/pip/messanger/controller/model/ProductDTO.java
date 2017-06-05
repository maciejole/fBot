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
