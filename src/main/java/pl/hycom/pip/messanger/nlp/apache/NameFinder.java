package pl.hycom.pip.messanger.nlp.apache;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
/**
 * Created by Piotr on 2017-05-29.
 */
public class NameFinder {
    private static final String TOKEN_MODE_FILE = "src/main/resources/nlp_apache_files/en-token.bin";
    private static final String MODE_FILE = "src/main/resources/nlp_apache_files/en-ner-person.bin";
    private static final String INPUT_FILE = "src/main/resources/nlp_apache_files/names_en.txt";

    public static void main(String args[]) throws IOException {

        String[] result = NameFinderUtil.getNames(TOKEN_MODE_FILE, MODE_FILE, INPUT_FILE);

        for (String s : result) {
            log.info(s);
        }
    }
}

