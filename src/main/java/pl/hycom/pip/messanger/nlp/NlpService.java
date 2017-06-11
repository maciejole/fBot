package pl.hycom.pip.messanger.nlp;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr on 2017-06-10.
 */
public interface NlpService {


    public  String nlpStringSender(String messageToBeAnalyze) throws IOException;
    public List<Result> nlpGetOutput(String id) throws IOException;
    public  String getRes(Response res) throws IOException;
    public  String nlpProcess(String toolName, String id, JSONObject options) throws IOException, InterruptedException, JSONException;
    public  List<Result> analyze(String message) throws IOException, InterruptedException, JSONException;
    public  List<Result> inputStreamToResultList(InputStream is) ;
    public List<Result> matchKeywords(List<Result> list);


}

