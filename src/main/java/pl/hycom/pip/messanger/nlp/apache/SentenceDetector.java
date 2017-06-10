package pl.hycom.pip.messanger.nlp.apache;


import lombok.extern.log4j.Log4j2;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.InputStream;
@Log4j2
public class SentenceDetector {

    private final static String FILE_PATH = "src/main/resources/nlp_apache_files/en-sent.bin";

    public static void main(String args[]) throws Exception {

        String sen = "Witaj w Hycomie. To jest przykładowy tekst , który będzie podzielony na mniejsze części. Właśnie tak.";
        //Loading a sentence model
        InputStream inputStream = new FileInputStream(FILE_PATH);
        SentenceModel model = new SentenceModel(inputStream);

        //Instantiating the SentenceDetectorME class
        SentenceDetectorME detector = new SentenceDetectorME(model);

        //Detecting the position of the sentences in the paragraph
        Span[] spans = detector.sentPosDetect(sen);

        //Printing the sentences and their spans of a paragraph
        for (Span span : spans) {
            log.info(sen.substring(span.getStart(), span.getEnd())+" "+ span);
        }

    }





}
