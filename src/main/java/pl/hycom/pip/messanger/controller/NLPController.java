package pl.hycom.pip.messanger.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.hycom.pip.messanger.nlp.NlpService;
import pl.hycom.pip.messanger.nlp.NlpServiceImplementation;
import pl.hycom.pip.messanger.nlp.Result;

import javax.ws.rs.GET;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal Lebioda on 25.05.2017.
 */
@Log4j2
@Controller
public class NLPController {

    @Autowired
    private NlpService nlpService;

    public static List<Result> outputList = new ArrayList<>();


    private static final String NLP_VIEW = "nlp";

    @RequestMapping(value = "/admin/nlp", method = RequestMethod.GET)
    public String returnView(Model model) {
        List<Result> temp = outputList;
        if (!outputList.isEmpty()) {
            temp = nlpService.matchKeywords(outputList);
        }
        model.addAttribute("lista",temp);
        return NLP_VIEW;
    }
}







