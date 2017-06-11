package pl.hycom.pip.messanger.nlp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Piotr on 2017-06-05.
 */
@Data
@NoArgsConstructor
public class Result {

    private String base;
    private  String result;
    private String keyword;

    public Result(String base, String result) {
       this.base = base;
       this.result = result;
       this.keyword = "brak keyworda";


   }
}
