package pl.hycom.pip.messanger.nlp.apache;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
@Log4j2
public class TokenizerUtil {
    TokenizerModel model = null;
    Tokenizer learnableTokenizer = null;

    public TokenizerUtil(String modelFile) {
        initTokenizerModel(modelFile);
        learnableTokenizer = new TokenizerME(model);
    }

    private void initTokenizerModel(String modelFile) {
        Objects.nonNull(modelFile);

        InputStream modelIn;
        try {
            modelIn = new FileInputStream(modelFile);
            model = new TokenizerModel(modelIn);
        } catch (IOException e) {
            log.error("File not found or error involved with InputStream" + e.getMessage() ,e );

        }
    }
    public String[] tokenizeFileUsingLearnableTokenizer(String file) {
        String data = FileUtils.getFileDataAsString(file);
        return learnableTokenizer.tokenize(data);
    }

    public String[] tokenizeUsingLearnableTokenizer(String data) {
        return learnableTokenizer.tokenize(data);
    }

    public String[] tokenizeFileUsingWhiteSpaceTokenizer(String file) {
        String data = FileUtils.getFileDataAsString(file);
        return WhitespaceTokenizer.INSTANCE.tokenize(data);
    }

    public String[] tokenizeUsingWhiteSpaceTokenizer(String data) {
        return WhitespaceTokenizer.INSTANCE.tokenize(data);
    }
}