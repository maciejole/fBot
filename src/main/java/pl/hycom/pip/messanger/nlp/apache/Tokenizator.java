package pl.hycom.pip.messanger.nlp.apache;

/**
 * Created by Piotr on 2017-05-29.
 */

import lombok.extern.log4j.Log4j2;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.InputStream;

@Log4j2
public class Tokenizator {
    
    private final static String FILE_PATH = "src/main/resources/nlp_apache_files/en-token.bin";
    
    public static void main(String[] args) throws Exception {
        
        String sentence = "Witaj w Hycomie. To jest przykładowy tekst , który będzie podzielony na mniejsze części. Właśnie tak.";
        //Loading the Tokenizer model
        try (InputStream inputStream = new FileInputStream(FILE_PATH);) {
            TokenizerModel tokenModel = new TokenizerModel(inputStream);
            //Instantiating the TokenizerME class
            TokenizerME tokenizer = new TokenizerME(tokenModel);
            //Tokenizing the given raw text
            String[] tokens = tokenizer.tokenize(sentence);
            //Printing the tokens
            for (String a : tokens) {
                log.info("Output from tokens " + a);
            }
        } catch (Exception ex) {
            log.error("Error during reading from InputStream " + ex.getMessage(), ex);
        }
        
        
    }
}
