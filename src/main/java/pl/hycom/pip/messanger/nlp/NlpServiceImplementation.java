package pl.hycom.pip.messanger.nlp;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import pl.hycom.pip.messanger.service.KeywordService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Configuration(value = "nlpService")
public class NlpServiceImplementation implements NlpService {


    KeywordService keywordService;

    private static final String NLPrestURL = "http://ws.clarin-pl.eu/nlprest2/base/";

    // wysyłamy wiadomość do analizy
    public String nlpStringSender(String messageToBeAnalyze) throws IOException {
        log.info("Method to analyzing text was called: '{}' " + messageToBeAnalyze);
        return ClientBuilder.newClient().target(NLPrestURL + "upload").request().post(Entity.entity(messageToBeAnalyze, MediaType.TEXT_PLAIN)).readEntity(String.class);

    }

    public List<Result> inputStreamToResultList(InputStream is) {
        List<Result> resultList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return systemId.contains("ccl.dtd") ? new InputSource(new StringReader("")) : null;
                }
            });
            Document doc = builder.parse(is);
            NodeList orthList = doc.getElementsByTagName("orth");
            NodeList baseList = doc.getElementsByTagName("base");
            for (int i = 0; i < baseList.getLength(); i++) {

                resultList.add(new Result(orthList.item(i).getTextContent(), baseList.item(i).getTextContent()));
            }


        } catch (Exception ex) {
            log.error(ex.getMessage() + " Error during calling inputStreamToNodeList method ");
        }
        return resultList;
    }

    @Override
    public List<Result> matchKeywords(List<Result> list) {
        log.info("Method for matching keywords was called");
        for (Result result : list) {

            if ((keywordService.findKeywordByWord(result.getResult()).getWord()) != null) {
                result.setKeyword(keywordService.findKeywordByWord(result.getResult()).getWord());
            }
            else {
                result.setKeyword("brak keyworda");
            }

        }
        return list;
    }

    public List<Result> nlpGetOutput(String id) throws IOException {
        URL url = new URL(NLPrestURL + "download" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        return inputStreamToResultList(conn.getInputStream());
    }


    public String getRes(Response res) throws IOException {
        if (res.getStatus() != 200)
            throw new IOException("Error in nlprest processing");
        return res.readEntity(String.class);

    }

    public String nlpProcess(String toolName, String id, JSONObject options) throws IOException, InterruptedException, JSONException {
        JSONObject request = new JSONObject();
        Client client = ClientBuilder.newClient();
        request.put("file", id);
        request.put("tool", toolName);
        request.put("options", options);
        String taskid = client.target(NLPrestURL + "startTask").request().
                post(Entity.entity(request.toString(), MediaType.APPLICATION_JSON)).readEntity(String.class);

        String status = "";
        JSONObject jsonres = new JSONObject();
        while (!("DONE").equals(status)) {
            String res = getRes(client.target(NLPrestURL + "getStatus/" + taskid).request().get());
            jsonres = new JSONObject(res);
            status = jsonres.getString("status");
            if (("ERROR").equals(status)) throw new IOException("Error in processing");
            if (("DONE").equals(status))
                Thread.sleep(500);
        }

        return jsonres.getJSONArray("value").getJSONObject(0).getString("fileID");


    }

    public List<Result> analyze(String message) throws IOException, InterruptedException, JSONException {
        String id = nlpStringSender(message);
        JSONObject liner2 = new JSONObject();
        liner2.put("model", "top9");
        id = nlpProcess("liner2", id, liner2);
        return nlpGetOutput(id);

    }


}

