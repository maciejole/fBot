package pl.hycom.pip.messanger.repository.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Piotr on 2017-06-05.
 */
@Data

public class RKP   {


    private Result result;
    private Keyword keyword;
    private  Product product;

    public RKP(Result result, Keyword keyword) {
        this.result = result;
        this.keyword = keyword;
        this.product = new Product();
    }




}
