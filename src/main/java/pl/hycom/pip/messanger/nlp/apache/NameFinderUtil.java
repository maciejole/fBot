package pl.hycom.pip.messanger.nlp.apache;

import lombok.extern.log4j.Log4j2;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Log4j2


public class NameFinderUtil {

    public static String[] getNames(String tokenizerModel, String nameModel,
                                    String inputFile) throws FileNotFoundException {

        TokenNameFinderModel model;
        try {
             model = new TokenNameFinderModel(new FileInputStream(nameModel));
        } catch (IOException e) {
            log.error("Error during loading inputstream " + e.getMessage());
            return null;
        }

        NameFinderME nameFinder = new NameFinderME(model);

        TokenizerUtil tokenizerUtil = new TokenizerUtil(tokenizerModel);
        String[] tokens = tokenizerUtil
                .tokenizeUsingLearnableTokenizer(FileUtils
                        .getFileDataAsString(inputFile));

        Span[] nameSpans = nameFinder.find(tokens);

        List<String> names = new ArrayList<>();

        for (Span span : nameSpans) {
            int start = span.getStart();
            int end = span.getEnd();

            StringBuilder stringBuilder= new StringBuilder();
            for (int i = start; i < end; i++) {
                stringBuilder.append(nameSpans[i]);
            }


            names.add(stringBuilder.toString());
        }
        String[] temp = new String[names.size()];

        return names.toArray(temp);
    }
}

